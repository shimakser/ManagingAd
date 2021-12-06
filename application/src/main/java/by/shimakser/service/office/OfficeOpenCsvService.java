package by.shimakser.service.office;

import by.shimakser.dto.CSVRequest;
import by.shimakser.exception.ExceptionText;
import by.shimakser.model.office.Office;
import by.shimakser.model.office.OfficeOperationInfo;
import by.shimakser.model.office.Status;
import by.shimakser.repository.office.ContactRepository;
import by.shimakser.repository.office.OfficeRepository;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service("officeOpenCsvService")
public class OfficeOpenCsvService extends BaseOfficeService {

    private final OfficeRepository officeRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public OfficeOpenCsvService(OfficeRepository officeRepository, ContactRepository contactRepository) {
        this.officeRepository = officeRepository;
        this.contactRepository = contactRepository;
    }

    private final AtomicLong idOfOperation = new AtomicLong(0);

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public Long exportFromFile(CSVRequest csvRequest) throws FileNotFoundException {
        String path = csvRequest.getPathToFile();
        File file = new File(path);
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionText.FILE_NOT_FOUND.getExceptionText());
        }

        idOfOperation.set(idOfOperation.get() + 1);
        Runnable exportTask = () -> {
            try (Reader reader = new BufferedReader(new FileReader(path))) {
                statusOfExport.put(idOfOperation, new OfficeOperationInfo(Status.In_Process, path));

                CsvToBean<Office> csvToBean = new CsvToBeanBuilder<Office>(reader)
                        .withType(Office.class)
                        .withSeparator(',')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                List<Office> list = csvToBean.parse();

                list.forEach(office -> contactRepository.saveAll(office.getOfficeContacts()));
                officeRepository.saveAll(list);
                statusOfExport.put(idOfOperation, new OfficeOperationInfo(Status.Uploaded, path));
            } catch (IOException e) {
                statusOfExport.put(idOfOperation, new OfficeOperationInfo(Status.Not_Loaded, path));
                e.printStackTrace();
            }
        };
        Thread exportThread = new Thread(exportTask);
        exportThread.start();

        return idOfOperation.get();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, CsvRequiredFieldEmptyException.class, CsvDataTypeMismatchException.class})
    public Long importToFile(CSVRequest csvRequest) {
        String path = csvRequest.getPathToFile();

        idOfOperation.set(idOfOperation.get() + 1);
        Runnable importTask = () -> {
            try (FileWriter writer = new FileWriter(path)) {
                statusOfExport.put(idOfOperation, new OfficeOperationInfo(Status.In_Process, path));

                ColumnPositionMappingStrategy<Office> mappingStrategy = new ColumnPositionMappingStrategy<>();
                mappingStrategy.setType(Office.class);

                final String[] columns = new String[]{"id", "officeTitle", "officeAddress", "officePrice", "officeContacts", "officeDescription"};
                mappingStrategy.setColumnMapping(columns);

                StatefulBeanToCsvBuilder<Office> builder = new StatefulBeanToCsvBuilder<>(writer);
                StatefulBeanToCsv<Office> beanWriter = builder.withMappingStrategy(mappingStrategy).build();

                beanWriter.write(officeRepository.findAll());

                statusOfExport.put(idOfOperation, new OfficeOperationInfo(Status.Uploaded, path));
            } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                statusOfExport.put(idOfOperation, new OfficeOperationInfo(Status.Not_Loaded, path));
                e.printStackTrace();
            }
        };
        Thread importThread = new Thread(importTask);
        importThread.start();

        return idOfOperation.get();
    }
}
