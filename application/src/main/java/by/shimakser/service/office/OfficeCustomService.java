package by.shimakser.service.office;

import by.shimakser.dto.CSVRequest;
import by.shimakser.exception.ExceptionText;
import by.shimakser.model.office.Contact;
import by.shimakser.model.office.Office;
import by.shimakser.model.office.OfficeOperationInfo;
import by.shimakser.model.office.Status;
import by.shimakser.repository.office.ContactRepository;
import by.shimakser.repository.office.OfficeRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("officeCustomService")
public class OfficeCustomService extends BaseOfficeService {

    private final OfficeRepository officeRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public OfficeCustomService(OfficeRepository officeRepository, ContactRepository contactRepository) {
        this.officeRepository = officeRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, FileNotFoundException.class})
    public Long exportFromFile(CSVRequest csvRequest) throws FileNotFoundException {
        String path = csvRequest.getPathToFile();
        File file = new File(path);
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionText.FILE_NOT_FOUND.getExceptionText());
        }

        ID_OF_OPERATION.set(ID_OF_OPERATION.get() + 1);
        Runnable exportTask = () -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                statusOfExport.put(ID_OF_OPERATION, new OfficeOperationInfo(Status.IN_PROCESS, path));

                while ((line = reader.readLine()) != null) {
                    String[] arrayOfOffices = line.split(",", 5);
                    List<Contact> listOfContacts = new ArrayList<>();

                    int indexOfClosSqBracket = arrayOfOffices[4].indexOf("]");

                    String[] splitContacts = arrayOfOffices[4].substring(1, indexOfClosSqBracket).split(", ");
                    Arrays.stream(splitContacts)
                            .filter(str -> str.length() > 2)
                            .forEach(str -> {
                                String[] contactsField = str.split(",");

                                Contact contact = new Contact(Long.parseLong(contactsField[0]), contactsField[1],
                                        contactsField[2], contactsField[3]);
                                contactRepository.save(contact);

                                listOfContacts.add(contact);
                            });

                    JSONObject jsonObject = null;
                    String descriptions = arrayOfOffices[4].substring(indexOfClosSqBracket + 2);
                    if (descriptions.length() > 4) {
                        jsonObject = new org.json.JSONObject(descriptions);
                    }

                    Office office = new Office(Long.parseLong(arrayOfOffices[0]), arrayOfOffices[1],
                            arrayOfOffices[2], Double.parseDouble(arrayOfOffices[3]), listOfContacts, jsonObject);
                    officeRepository.save(office);

                    statusOfExport.put(ID_OF_OPERATION, new OfficeOperationInfo(Status.UPLOADED, path));
                }
            } catch (IOException ex) {
                statusOfExport.put(ID_OF_OPERATION, new OfficeOperationInfo(Status.NOT_LOADED, path));
                ex.printStackTrace();
            }
        };
        Thread exportThread = new Thread(exportTask);
        exportThread.start();
        return ID_OF_OPERATION.get();
    }

    @Override
    @Transactional(rollbackFor = IOException.class)
    public Long importToFile(CSVRequest csvRequest) {
        String importFileName = "/offices_import_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMYYYY_HH_mm_ss"));
        String path = csvRequest.getPathToFile() + importFileName;

        ID_OF_OPERATION.set(ID_OF_OPERATION.get() + 1);
        Runnable importTask = () -> {
            statusOfExport.put(ID_OF_OPERATION, new OfficeOperationInfo(Status.IN_PROCESS, path));
            try (FileWriter writer = new FileWriter(path, false)) {
                List<Office> offices = officeRepository.findAll();
                for (Office office : offices) {
                    writer.write(office.toString());
                    writer.write("\n");
                }

                statusOfExport.put(ID_OF_OPERATION, new OfficeOperationInfo(Status.UPLOADED, path));
            } catch (IOException e) {
                statusOfExport.put(ID_OF_OPERATION, new OfficeOperationInfo(Status.NOT_LOADED, path));
                e.printStackTrace();
            }
        };
        Thread importThread = new Thread(importTask);
        importThread.start();

        return ID_OF_OPERATION.get();
    }
}