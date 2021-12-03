package by.shimakser.service.office;

import by.shimakser.model.office.CSVRequest;
import by.shimakser.model.office.Office;
import by.shimakser.model.office.Status;
import by.shimakser.repository.office.ContactRepository;
import by.shimakser.repository.office.OfficeRepository;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OfficeOpenCsvService {

    private final OfficeRepository officeRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public OfficeOpenCsvService(OfficeRepository officeRepository, ContactRepository contactRepository) {
        this.officeRepository = officeRepository;
        this.contactRepository = contactRepository;
    }

    private final Map<AtomicLong, Status> statusOfExport = new HashMap<>();
    private final Map<AtomicLong, Status> statusOfImport = new HashMap<>();

    private final AtomicLong idOfOperation = new AtomicLong(0);

    @Transactional(rollbackFor = {IOException.class})
    public Long exportFromFile(CSVRequest csvRequest) {
        String path = csvRequest.getPathToFile();

        Map<String, String> mapping = new HashMap<>();
        mapping.put("id", "id");
        mapping.put("office_title", "officeTitle");
        mapping.put("office_address", "officeAddress");
        mapping.put("office_price", "officePrice");

        HeaderColumnNameTranslateMappingStrategy<Office> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
        strategy.setType(Office.class);
        strategy.setColumnMapping(mapping);

        idOfOperation.set(idOfOperation.get() + 1);
        Runnable exportTask = () -> {
            try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
                CsvToBean<Office> csvToBean = new CsvToBean<>();
                csvToBean.setCsvReader(csvReader);
                csvToBean.setMappingStrategy(strategy);

                List<Office> list = csvToBean.parse();

                officeRepository.saveAll(list);
            } catch (IOException e) {
                Status.Not_Loaded.setPathForFile(path);
                statusOfExport.put(idOfOperation, Status.Not_Loaded);
                e.printStackTrace();
            }
        };
        Thread exportThread = new Thread(exportTask);
        exportThread.start();

        return idOfOperation.get();
    }

    @Transactional
    public Long importToFile(CSVRequest csvRequest) {
        String path = csvRequest.getPathToFile();
        idOfOperation.set(idOfOperation.get() + 1);
        Runnable importTask = () -> {
            Status.In_Process.setPathForFile(path);
            try (FileWriter writer = new FileWriter(path);) {

                ColumnPositionMappingStrategy<Office> mappingStrategy = new ColumnPositionMappingStrategy<>();
                mappingStrategy.setType(Office.class);

                String[] columns = new String[]{"id", "officeTitle", "officeAddress", "officePrice", "officeContacts", "officeDescription"};
                mappingStrategy.setColumnMapping(columns);

                StatefulBeanToCsvBuilder<Office> builder = new StatefulBeanToCsvBuilder<>(writer);
                StatefulBeanToCsv<Office> beanWriter = builder.withMappingStrategy(mappingStrategy).build();

                beanWriter.write(officeRepository.findAll());

                Status.Uploaded.setPathForFile(path);
                statusOfImport.put(idOfOperation, Status.Uploaded);
            } catch (IOException e) {
                Status.Not_Loaded.setPathForFile(path);
                statusOfImport.put(idOfOperation, Status.Not_Loaded);
                e.printStackTrace();
            } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                Status.Not_Loaded.setPathForFile(path);
                statusOfImport.put(idOfOperation, Status.Not_Loaded);
                e.printStackTrace();
            }
        };
        Thread importThread = new Thread(importTask);
        importThread.start();

        return idOfOperation.get();
    }


}
