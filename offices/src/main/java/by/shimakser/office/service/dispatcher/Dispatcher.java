package by.shimakser.office.service.dispatcher;

import by.shimakser.office.service.ExportService;
import org.springframework.stereotype.Component;


public interface Dispatcher<K, V> {

  ExportService<?> getByEntityAndExportType(K k, V v);
}
