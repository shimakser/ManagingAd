package by.shimakser.office.service.impl.csv;

import by.shimakser.office.exception.ExceptionOfficeText;
import by.shimakser.office.model.*;
import by.shimakser.office.repository.OfficeRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("officeCustomService")
public class OfficeCustomCsvService extends BaseCsvService<Office> {

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeCustomCsvService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, FileNotFoundException.class})
    public Long importFromFile(ExportRequest exportRequest) throws FileNotFoundException {
        String path = exportRequest.getPathToFile();
        File file = new File(path);
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionOfficeText.FILE_NOT_FOUND.getExceptionDescription());
        }

        ID_OF_OPERATION.incrementAndGet();
        Runnable importTask = () -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                statusOfExport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.UPLOADED, path));

                while ((line = reader.readLine()) != null) {
                    String officeLine = line.substring(7, line.length() - 1);
                    String[] arrayOfOffices = officeLine.replace("\"", "").split(",", 5);

                    String strContacts = arrayOfOffices[4].substring(arrayOfOffices[4].indexOf("["), arrayOfOffices[4].indexOf("]") + 1);

                    int indexOfContactListEnd = arrayOfOffices[4].indexOf("]");
                    String strDescriptions = arrayOfOffices[4].substring(indexOfContactListEnd + 3);

                    Office office = new Office(Long.parseLong(arrayOfOffices[0]), arrayOfOffices[1],
                            arrayOfOffices[2], Double.parseDouble(arrayOfOffices[3]),
                            contactConverterForImport(strContacts), jsonConvertForImport(strDescriptions).toString());
                    officeRepository.save(office);

                    statusOfExport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.UPLOADED, path));
                }
                log.info("Import {} office data from db into csv file.", ID_OF_OPERATION.get());
            } catch (IOException ex) {
                statusOfExport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.NOT_LOADED, path));
                log.error("IOException from importing data from csv:{} into db.", path, ex);
            }
        };
        Thread importThread = new Thread(importTask);
        importThread.start();
        return ID_OF_OPERATION.get();
    }

    @Override
    @Transactional(rollbackFor = IOException.class)
    public byte[] exportToFile(ExportRequest exportRequest) throws IOException {

        ID_OF_OPERATION.incrementAndGet();
        File file = Files.createTempFile(null, null).toFile();
        try (FileWriter writer = new FileWriter(file)) {
            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.IN_PROCESS, file.toPath().toString()));

            List<Office> offices = getDataToExport();
            for (Office office : offices) {
                writer.write(office.toString());
                writer.write("\n");
            }
            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.UPLOADED, file.toPath().toString()));
            log.info("Export {} office data from db into csv.", ID_OF_OPERATION.get());
        } catch (IOException e) {
            statusOfImport.put(ID_OF_OPERATION.get(), new ExportOperationInfo(Status.NOT_LOADED, file.toPath().toString()));
            log.error("IOException from exporting data from db into csv.", e);
        }
        return Files.readAllBytes(Path.of(file.getPath()));
    }

    @Override
    public List<Office> getDataToExport() {
        return officeRepository.findAll();
    }

    protected List<Contact> contactConverterForImport(String strContacts) {
        if (strContacts.equals("[]")) {
            return Collections.emptyList();
        }

        String strListOfContacts = strContacts.substring(8, strContacts.length() - 1);

        String[] arrWithContacts;
        if (strListOfContacts.contains(", Contact")) {
            arrWithContacts = strListOfContacts.split(", Contact");
        } else arrWithContacts = new String[]{strListOfContacts};

        return Arrays.stream(arrWithContacts)
                .map(contacts -> contacts.substring(1, contacts.length() - 1))
                .map(contact -> contact.split(","))
                .map(fields -> new Contact(Long.parseLong(fields[0]), fields[1].trim(), fields[2].trim(), fields[3].trim()))
                .collect(Collectors.toList());
    }

    protected JSONObject jsonConvertForImport(String json) {
        if (json.equals("null")) {
            return new JSONObject();
        }

        return (json.equals("") || json.equals("{}"))
                ? new JSONObject()
                : new JSONObject(json);
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