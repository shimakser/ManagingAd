package by.shimakser.office.service.dispatcher;

import by.shimakser.dto.EntityType;
import by.shimakser.office.model.ExportServiceStructure;
import by.shimakser.office.model.FileType;
import by.shimakser.office.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ExportDispatcher implements Dispatcher<EntityType, FileType> {

    private final List<ExportServiceStructure> list = new ArrayList<>();

    @Autowired
    public ExportDispatcher(List<ExportService<?>> services) {

        Map<EntityType, ExportService<?>> map;
        for (ExportService<?> service: services) {
            map = new HashMap<>();
            map.put(service.getEntity(), service);

            list.add(new ExportServiceStructure(service.getType(), map));
        }
    }

    @Override
    public ExportService<?> getByEntityAndExportType(EntityType entityType, FileType fileType) {
        return list.stream()
                .filter(service -> service.getFileType().equals(fileType))
                .map(ExportServiceStructure::getServiceMap)
                .map(map -> map.get(entityType))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("No such service by " + entityType + " and " + fileType));
    }
}
