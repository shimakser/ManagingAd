package by.shimakser.office.service.dispatcher;

import by.shimakser.office.model.FileType;
import by.shimakser.office.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Service
public class OfficeExportDispatcher implements Dispatcher<FileType, OfficeService> {

    private final Map<FileType, OfficeService> map;

    @Autowired
    public OfficeExportDispatcher(List<OfficeService> services) {
        this.map = services.stream()
                .collect(
                        toMap(
                                OfficeService::name,
                                v -> v
                        )
                );
    }

    @Override
    public OfficeService getByName(FileType name) {
        return Optional
                .ofNullable(name)
                .map(map::get)
                .orElseThrow(
                        () -> new NullPointerException(
                                "Cannot find " + OfficeService.class + " for name = " + name));
    }
}
