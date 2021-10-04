package by.shimakser.service;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.FileRequest;
import by.shimakser.model.Office;
import by.shimakser.model.Status;
import by.shimakser.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    private final OfficeRepository officeRepository;

    @Autowired
    public FileService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    private final Map<Long, Status> statusMap = new HashMap<>();

    private static Long idOfOperation = 1L;

    @Transactional(rollbackFor = {FileNotFoundException.class})
    public Long exportFromFile(FileRequest fileRequest) throws FileNotFoundException {
        File file = new File(fileRequest.getPathToFile());
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionText.FileNotFound.getExceptionText());
        }

        Runnable exportTask = () -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileRequest.getPathToFile()))) {
                String line;
                idOfOperation++;
                statusMap.put(idOfOperation, Status.In_Process);

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);

                    String[] fields = line.split(",");

                    Office office = new Office();
                    office.setId(Long.parseLong(fields[0]));
                    office.setOfficeTitle(fields[1]);
                    office.setOfficeAddress(fields[2]);
                    officeRepository.save(office);
                    statusMap.put(idOfOperation, Status.Uploaded);
                }
            } catch (IOException ex) {
                statusMap.put(idOfOperation, Status.Not_Loaded);
                ex.printStackTrace();
            }
        };
        Thread exportThread = new Thread(exportTask);
        exportThread.start();
        return idOfOperation;
    }

    @Transactional
    public Long importToFile(FileRequest fileRequest) {
        Runnable importTask = () -> {
            idOfOperation++;
            statusMap.put(idOfOperation, Status.In_Process);
            try (FileWriter writer = new FileWriter(fileRequest.getPathToFile(), false)) {
                List<Office> offices = officeRepository.findAll();
                for (Office office : offices) {
                    writer.write(office.toString());
                    writer.write("\n");
                }
                statusMap.put(idOfOperation, Status.Uploaded);
            } catch (IOException e) {
                statusMap.put(idOfOperation, Status.Not_Loaded);
                e.printStackTrace();
            }
        };

        Thread importThread = new Thread(importTask);
        importThread.start();

        return idOfOperation;
    }
}
