package by.shimakser.cache;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

public class MongoCacheBuilder {

    private static final long DEFAULT_TTL = TimeUnit.DAYS.toSeconds(7);

    private String collectionName;
    private MongoTemplate mongoTemplate;
    private long ttl;

    public MongoCacheBuilder(String collectionName, MongoTemplate mongoTemplate) {
        this.collectionName = collectionName;
        this.mongoTemplate = mongoTemplate;
        this.ttl = DEFAULT_TTL;
    }

    public static MongoCacheBuilder newInstance(String collectionName, MongoTemplate mongoTemplate) {
        return new MongoCacheBuilder(collectionName, mongoTemplate);
    }

    public MongoCache build(String cacheName) {
        return new MongoCache(collectionName, cacheName, mongoTemplate, ttl);
    }
}
