package by.shimakser.office.service.dispatcher;

public interface Dispatcher<K, V> {

  V getByName(K name);
}
