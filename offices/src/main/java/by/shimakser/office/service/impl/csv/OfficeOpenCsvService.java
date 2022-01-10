package by.shimakser.office.service.impl.csv;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.*;
import by.shimakser.office.exception.ExceptionOfficeText;
import by.shimakser.office.repository.ContactRepository;
import by.shimakser.office.repository.OfficeRepository;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service("officeOpenCsvService")
public class OfficeOpenCsvService extends BaseCsvService<Office> {

    private final OfficeRepository officeRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public OfficeOpenCsvService(OfficeRepository officeRepository, ContactRepository contactRepository) {
        this.officeRepository = officeRepository;
        this.contactRepository = contactRepository;
    }

    private final String[] OFFICES_FIELDS = new String[]{"id", "officeTitle", "officeAddress", "officePrice", "officeContacts", "officeDescription"};

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public Long importFromFile(ExportRequest exportRequest) throws FileNotFoundException {
        String path = exportRequest.getPathToFile();
        File file = new File(path);
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionOfficeText.FILE_NOT_FOUND.getExceptionDescription());
        }

        ID_OF_OPERATION.incrementAndGet();
        statusOfExport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.IN_PROCESS, path));
        try (Reader reader = new BufferedReader(new FileReader(path))) {
            CsvToBean<Office> csvToBean = new CsvToBeanBuilder<Office>(reader)
                    .withType(Office.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Office> list = csvToBean.parse();

            list.forEach(office -> contactRepository.saveAll(office.getOfficeContacts()));
            officeRepository.saveAll(list);

            statusOfExport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.UPLOADED, path));
        } catch (IOException e) {
            statusOfExport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.NOT_LOADED, path));
            e.printStackTrace();
        }

        return ID_OF_OPERATION.get();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, CsvRequiredFieldEmptyException.class, CsvDataTypeMismatchException.class})
    public byte[] exportToFile(ExportRequest exportRequest) throws IOException {
        ID_OF_OPERATION.incrementAndGet();
        File file = null;
        try {
            file = Files.createTempFile(null, null).toFile();
            FileWriter writer = new FileWriter(file);
            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.IN_PROCESS, file.toPath().toString()));

            ColumnPositionMappingStrategy<Office> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(Office.class);
            mappingStrategy.setColumnMapping(OFFICES_FIELDS);

            StatefulBeanToCsvBuilder<Office> builder = new StatefulBeanToCsvBuilder<>(writer);
            StatefulBeanToCsv<Office> beanWriter = builder.withMappingStrategy(mappingStrategy).build();

            beanWriter.write(getDataToExport());

            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.UPLOADED, file.toPath().toString()));
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.NOT_LOADED, file.toPath().toString()));
            e.printStackTrace();
        }

        return Files.readAllBytes(Path.of(file.getPath()));
    }

    @Override
    public FileType getType() {
        return FileType.CSV;
    }

    @Override
    public EntityType getEntity() {
        return EntityType.OFFICE;
    }
}
