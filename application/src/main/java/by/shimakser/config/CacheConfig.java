package by.shimakser.config;

import by.shimakser.cache.MongoCacheBuilder;
import by.shimakser.cache.MongoCacheManager;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@ConditionalOnClass(MongoTemplate.class)
@ConditionalOnMissingBean(CacheManager.class)
public class CacheConfig {

    /*
    Есть некий удаленный сервис, который долго отвечает, нужно настроить кеширование таким образом,
    чтобы сначала подтягивался локальный кэш, если там запрашиваемого айтема нет, то подтягивается из монги,
    и если уже в монге нет, то тянуть айтем из удаленного сервиса.

    Реализовать таким образом, чтобы было достаточно для кешируемого метода только навесить на него @Cacheable анноташку,
    сам алгоритм многоуровневого кеширования настроить через конфиги.
    Внутри аннотируемого метода не должно быть сторонней логики, кроме вызова некого удаленного ресурса
    (или другого вызова, не участвующего в алгоритме кеширования)
    */


    private final MongoTemplate mongoTemplate;

    @Value(value = "${spring.cache.collection-name}")
    private String mongoCollectionName;

    @Value(value = "${spring.cache.cache-name}")
    private String cacheName;

    @Autowired
    public CacheConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    @Primary
    public CompositeCacheManager compositeCacheManager() {
        return new CompositeCacheManager(caffeineCacheManager(), mongoCacheManager());
    }

    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(cacheName);
        caffeineCacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(500)
                        .expireAfterAccess(5, TimeUnit.SECONDS)
                        .weakKeys()
                        .recordStats()
        );
        return caffeineCacheManager;
    }

    @Bean
    public CacheManager mongoCacheManager() {
        MongoCacheBuilder cache = MongoCacheBuilder.newInstance(mongoCollectionName, mongoTemplate);

        List<MongoCacheBuilder> caches = new ArrayList<>();
        caches.add(cache);

        return new MongoCacheManager(caches, cacheName);
    }
}
