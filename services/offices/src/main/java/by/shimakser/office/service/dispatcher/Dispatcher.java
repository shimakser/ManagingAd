package by.shimakser.office.service.dispatcher;

import org.springframework.stereotype.Component;

@Component
public interface Dispatcher<K1, K2, V> {

  V getByEntityAndExportType(K1 k1, K2 k2);
}
