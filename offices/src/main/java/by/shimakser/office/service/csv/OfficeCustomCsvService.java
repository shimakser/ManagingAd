package by.shimakser.office.service.csv;

import by.shimakser.office.model.OfficeRequest;
import by.shimakser.office.exception.ExceptionOfficeText;
import by.shimakser.office.model.Contact;
import by.shimakser.office.model.Office;
import by.shimakser.office.model.OfficeOperationInfo;
import by.shimakser.office.model.Status;
import by.shimakser.office.repository.OfficeRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("officeCustomService")
public class OfficeCustomCsvService extends BaseOfficeCsvService {

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeCustomCsvService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, FileNotFoundException.class})
    public Long exportFromFile(OfficeRequest officeRequest) throws FileNotFoundException {
        String path = officeRequest.getPathToFile();
        File file = new File(path);
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionOfficeText.FILE_NOT_FOUND.getExceptionDescription());
        }

        ID_OF_OPERATION.incrementAndGet();
        Runnable exportTask = () -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                statusOfExport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.UPLOADED, path));

                while ((line = reader.readLine()) != null) {
                    String[] arrayOfOffices = line.replace("\"", "").split(",", 5);

                    int indexOfClosSqBracket = arrayOfOffices[4].indexOf("]");
                    String strListOfContacts = arrayOfOffices[4].substring(8, indexOfClosSqBracket);

                    String descriptions = arrayOfOffices[4].substring(indexOfClosSqBracket + 2);

                    Office office = new Office(Long.parseLong(arrayOfOffices[0]), arrayOfOffices[1],
                            arrayOfOffices[2], Double.parseDouble(arrayOfOffices[3]),
                            contactConverterForExport(strListOfContacts), jsonConvertForExport(descriptions).toString());
                    officeRepository.save(office);

                    statusOfExport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.UPLOADED, path));
                }
            } catch (IOException ex) {
                statusOfExport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.NOT_LOADED, path));
                ex.printStackTrace();
            }
        };
        Thread exportThread = new Thread(exportTask);
        exportThread.start();
        return ID_OF_OPERATION.get();
    }

    @Override
    @Transactional(rollbackFor = IOException.class)
    public Long importToFile(OfficeRequest officeRequest) {
        String importFileName = "/offices_import_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HH_mm_ss")) + ".csv";
        String path = officeRequest.getPathToFile() + importFileName;

        ID_OF_OPERATION.incrementAndGet();
        Runnable importTask = () -> {
            statusOfImport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.IN_PROCESS, path));
            try (FileWriter writer = new FileWriter(path, false)) {
                List<Office> offices = officeRepository.findAll();
                for (Office office : offices) {
                    writer.write(office.toString());
                    writer.write("\n");
                }

                statusOfImport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.UPLOADED, path));
            } catch (IOException e) {
                statusOfImport.put(ID_OF_OPERATION.get(), new OfficeOperationInfo(Status.NOT_LOADED, path));
                e.printStackTrace();
            }
        };
        Thread importThread = new Thread(importTask);
        importThread.start();

        return ID_OF_OPERATION.get();
    }

    private List<Contact> contactConverterForExport(String strContacts) {
        String[] arrWithContacts;
        if (strContacts.contains(", Contact")) {
            arrWithContacts = strContacts.split(", Contact");
        } else arrWithContacts = new String[]{strContacts};

        return Arrays.stream(arrWithContacts)
                .map(contacts -> contacts.substring(1, contacts.length() - 1))
                .map(contact -> contact.split(","))
                .map(fields -> new Contact(Long.parseLong(fields[0]), fields[1].trim(), fields[2].trim(), fields[3].trim()))
                .collect(Collectors.toList());
    }

    protected JSONObject jsonConvertForExport(String json) {
        return (json.equals("") || json.equals("{}"))
                ? new JSONObject()
                : new JSONObject(json);
    }
}