package by.shimakser.office.service.csv;

import by.shimakser.office.model.OfficeRequest;
import by.shimakser.office.exception.ExceptionOfficeText;
import by.shimakser.office.model.Office;
import by.shimakser.office.model.OfficeOperationInfo;
import by.shimakser.office.model.Status;
import by.shimakser.office.repository.ContactRepository;
import by.shimakser.office.repository.OfficeRepository;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("officeOpenCsvService")
public class OfficeOpenCsvService extends BaseOfficeCsvService {

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
    public Long exportFromFile(OfficeRequest officeRequest) throws FileNotFoundException {
        String path = officeRequest.getPathToFile();
        File file = new File(path);
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionOfficeText.FILE_NOT_FOUND.getExceptionDescription());
        }

        ID_OF_OPERATION.incrementAndGet();
        statusOfExport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.IN_PROCESS, path));
        try (Reader reader = new BufferedReader(new FileReader(path))) {
            CsvToBean<Office> csvToBean = new CsvToBeanBuilder<Office>(reader)
                    .withType(Office.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Office> list = csvToBean.parse();

            list.forEach(office -> contactRepository.saveAll(office.getOfficeContacts()));
            officeRepository.saveAll(list);

            statusOfExport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.UPLOADED, path));
        } catch (IOException e) {
            statusOfExport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.NOT_LOADED, path));
            e.printStackTrace();
        }

        return ID_OF_OPERATION.get();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, CsvRequiredFieldEmptyException.class, CsvDataTypeMismatchException.class})
    public Long importToFile(OfficeRequest officeRequest) {
        String importFileName = "/offices_import_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HH_mm_ss")) + ".csv";
        String path = officeRequest.getPathToFile() + importFileName;

        ID_OF_OPERATION.incrementAndGet();
        statusOfImport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.IN_PROCESS, path));
        try (FileWriter writer = new FileWriter(path)) {
            ColumnPositionMappingStrategy<Office> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(Office.class);
            mappingStrategy.setColumnMapping(OFFICES_FIELDS);

            StatefulBeanToCsvBuilder<Office> builder = new StatefulBeanToCsvBuilder<>(writer);
            StatefulBeanToCsv<Office> beanWriter = builder.withMappingStrategy(mappingStrategy).build();

            beanWriter.write(officeRepository.findAll());

            statusOfImport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.UPLOADED, path));
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            statusOfImport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.NOT_LOADED, path));
            e.printStackTrace();
        }

        return ID_OF_OPERATION.get();
    }
}
