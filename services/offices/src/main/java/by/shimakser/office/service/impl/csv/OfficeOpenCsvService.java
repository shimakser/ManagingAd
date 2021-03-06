package by.shimakser.office.service.impl.csv;

import by.shimakser.office.model.EntityType;
import by.shimakser.office.model.*;
import by.shimakser.office.exception.ExceptionOfficeText;
import by.shimakser.office.repository.ContactRepository;
import by.shimakser.office.repository.OfficeRepository;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service("officeOpenCsvService")
public class OfficeOpenCsvService extends BaseCsvService<Office> {

    private final OfficeRepository officeRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public OfficeOpenCsvService(OfficeRepository officeRepository, ContactRepository contactRepository) {
        this.officeRepository = officeRepository;
        this.contactRepository = contactRepository;
    }

    private static final String[] OFFICES_FIELDS = new String[]{"id", "officeTitle", "officeAddress", "officePrice", "officeContacts", "officeDescription"};

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
            log.info("Import {} office data from csv into db.", ID_OF_OPERATION.get());
        } catch (IOException e) {
            statusOfExport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.NOT_LOADED, path));
            log.error("IOException from importing data from csv:{} into db.", path, e);
        }

        return ID_OF_OPERATION.get();
    }

    @Override
    public List<Office> getDataToExport() {
        return officeRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, CsvRequiredFieldEmptyException.class, CsvDataTypeMismatchException.class})
    public byte[] exportToFile(ExportRequest exportRequest) throws IOException {
        ID_OF_OPERATION.incrementAndGet();
        File file = Files.createTempFile(null, null).toFile();
        try (FileWriter writer = new FileWriter(file)) {
            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.IN_PROCESS, file.toPath().toString()));

            ColumnPositionMappingStrategy<Office> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(Office.class);
            mappingStrategy.setColumnMapping(OFFICES_FIELDS);

            StatefulBeanToCsvBuilder<Office> builder = new StatefulBeanToCsvBuilder<>(writer);
            StatefulBeanToCsv<Office> beanWriter = builder.withMappingStrategy(mappingStrategy).build();

            beanWriter.write(getDataToExport());

            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.UPLOADED, file.toPath().toString()));
            log.info("Export {} office data from db into csv.", ID_OF_OPERATION.get());
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.NOT_LOADED, file.toPath().toString()));
            log.error("IOException from exporting data from db into csv.", e);
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
