package by.shimakser.cache;

import by.shimakser.feign.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.concurrent.Callable;

public class MongoCache implements Cache {

    private static final String INDEX_KEY_NAME = "creationDate";
    private static final String INDEX_NAME = "_expire";

    private final String collectionName;
    private final String cacheName;
    private final MongoTemplate mongoTemplate;
    private final long ttl;

    @Autowired
    public MongoCache(String collectionName, String cacheName, MongoTemplate mongoTemplate, long ttl) {
        this.collectionName = collectionName;
        this.cacheName = cacheName;
        this.mongoTemplate = mongoTemplate;
        this.ttl = ttl;

        initialize();
    }

    private Index initialize() {
        mongoTemplate.getCollection(collectionName);

        final Index index = new Index();
        index.named(INDEX_NAME);
        index.on(INDEX_KEY_NAME, Sort.Direction.ASC);
        index.expire(ttl);

        return index;
    }

    @Override
    public String getName() {
        return cacheName;
    }

    @Override
    public Object getNativeCache() {
        return mongoTemplate;
    }

    @Override
    public ValueWrapper get(Object key) {
        final Currency value = getFromCache(key);
        return new SimpleValueWrapper(value);
    }

    private Currency getFromCache(Object key) {
        final String id = key.toString();
        final Currency cache = mongoTemplate.findById(id, Currency.class, collectionName);
        return cache;
    }

    private String serialize(Object value) throws IOException {
        try (final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
             final ObjectOutputStream output = new ObjectOutputStream(buffer)) {

            output.writeObject(value);

            final byte[] data = buffer.toByteArray();

            final Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(data);
        }
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        try {
            final Object value = getFromCache(key);
            if (value == null) {
                return null;
            }
            return type.cast(value);
        } catch (ClassCastException e) {
            throw new IllegalStateException("Unable to cast the object.", e);
        }
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object cached = getFromCache(key);
        if (cached != null) {
            return (T) cached;
        }

        final Object dynamicLock = ((String) key).intern();

        synchronized (dynamicLock) {
            cached = getFromCache(key);
            if (cached != null) {
                return (T) cached;
            }

            T value;
            try {
                value = valueLoader.call();
            } catch (Throwable ex) {
                throw new ValueRetrievalException(key, valueLoader, ex);
            }

            ValueWrapper newCachedValue = putIfAbsent(key, value);
            if (newCachedValue != null) {
                return (T) newCachedValue.get();
            } else {
                return value;
            }
        }
    }

    @Override
    public void put(Object key, Object value) {
        try {
            final String id = (String) key;
            String code = null;
            if (value != null) {
                code = serialize(value);
            }

            final Currency cache = new Currency(id, code);
            mongoTemplate.save(cache, collectionName);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can not serialize the value: %s", key), e);
        }
    }

    @Override
    public void evict(Object key) {
        final String id = (String) key;
        final CriteriaDefinition criteria = Criteria.where("_id").is(id);
        final Query query = Query.query(criteria);

        mongoTemplate.remove(query, collectionName);
    }

    @Override
    public void clear() {
        mongoTemplate.remove(new Query(), Currency.class, collectionName);
    }
}
