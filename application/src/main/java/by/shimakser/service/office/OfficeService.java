package by.shimakser.service.office;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.office.Contact;
import by.shimakser.model.office.CSVRequest;
import by.shimakser.model.office.Office;
import by.shimakser.model.office.Status;
import by.shimakser.repository.office.ContactRepository;
import by.shimakser.repository.office.OfficeRepository;
import javassist.NotFoundException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public OfficeService(OfficeRepository officeRepository, ContactRepository contactRepository) {
        this.officeRepository = officeRepository;
        this.contactRepository = contactRepository;
    }

    private final Map<AtomicLong, Status> statusOfImport = new HashMap<>();
    private final Map<AtomicLong, Status> statusOfExport = new HashMap<>();

    private final AtomicLong idOfOperation = new AtomicLong(0);

    @Transactional(rollbackFor = {FileNotFoundException.class})
    public Long exportFromFile(CSVRequest csvRequest) throws FileNotFoundException {
        String path = csvRequest.getPathToFile();
        File file = new File(path);
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionText.FileNotFound.getExceptionText());
        }

        idOfOperation.set(idOfOperation.get() + 1);
        Runnable exportTask = () -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                Status.In_Process.setPathForFile(path);
                statusOfExport.put(idOfOperation, Status.In_Process);

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

                    Status.Uploaded.setPathForFile(path);
                    statusOfExport.put(idOfOperation, Status.Uploaded);
                }
            } catch (IOException ex) {
                Status.Not_Loaded.setPathForFile(path);
                statusOfExport.put(idOfOperation, Status.Not_Loaded);
                ex.printStackTrace();
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
            statusOfImport.put(idOfOperation, Status.In_Process);
            try (FileWriter writer = new FileWriter(path, false)) {
                List<Office> offices = officeRepository.findAll();
                for (Office office : offices) {
                    writer.write(office.toString());
                    writer.write("\n");
                }
                Status.Uploaded.setPathForFile(path);
                statusOfImport.put(idOfOperation, Status.Uploaded);
            } catch (IOException e) {
                Status.Not_Loaded.setPathForFile(path);
                statusOfImport.put(idOfOperation, Status.Not_Loaded);
                e.printStackTrace();
            }
        };

        Thread importThread = new Thread(importTask);
        importThread.start();

        return idOfOperation.get();
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public Status getStatusOfImportById(Long id) throws NotFoundException {
        Status status = statusOfImport.get(new AtomicLong(id));
        if (status == null) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return status;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public Status getStatusOfExportById(Long id) throws NotFoundException {
        Status status = statusOfExport.get(new AtomicLong(id));
        if (status == null) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return status;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public String getImportedFileById(Long id) throws NotFoundException {
        Status status = statusOfImport.get(new AtomicLong(id));
        if (status == null || !status.equals(Status.Uploaded)) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return status.getPathForFile();
    }
}