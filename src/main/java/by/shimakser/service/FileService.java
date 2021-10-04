package by.shimakser.service;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.FileRequest;
import by.shimakser.model.Office;
import by.shimakser.model.Status;
import by.shimakser.repository.OfficeRepository;
import javassist.NotFoundException;
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

    private final Map<Long, Status> statusOfImport = new HashMap<>();
    private final Map<Long, Status> statusOfExport = new HashMap<>();

    private static Long idOfOperation = 0L;

    @Transactional(rollbackFor = {FileNotFoundException.class})
    public Long exportFromFile(FileRequest fileRequest) throws FileNotFoundException {
        String path = fileRequest.getPathToFile();
        File file = new File(path);
        if (!file.isFile()) {
            throw new FileNotFoundException(ExceptionText.FileNotFound.getExceptionText());
        }

        idOfOperation++;
        Runnable exportTask = () -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                Status.In_Process.setPathForFile(path);
                statusOfExport.put(idOfOperation, Status.In_Process);

                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");

                    Office office = new Office();
                    office.setId(Long.parseLong(fields[0]));
                    office.setOfficeTitle(fields[1]);
                    office.setOfficeAddress(fields[2]);
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
        return idOfOperation;
    }

    @Transactional
    public Long importToFile(FileRequest fileRequest) {
        String path = fileRequest.getPathToFile();
        idOfOperation++;
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

        return idOfOperation;
    }

    @Transactional
    public Status getStatusOfImportById(Long id) throws NotFoundException {
        Status status = statusOfImport.get(id);
        if (status == null) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return status;
    }

    @Transactional
    public Status getStatusOfExportById(Long id) throws NotFoundException {
        Status status = statusOfExport.get(id);
        if (status == null) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return status;
    }

    @Transactional
    public String getImportedFileById(Long id) throws NotFoundException {
        Status status = statusOfImport.get(id);
        if (status == null || !status.equals(Status.Uploaded)) {
            throw new NotFoundException(ExceptionText.NotFound.getExceptionText());
        }
        return status.getPathForFile();
    }
}
