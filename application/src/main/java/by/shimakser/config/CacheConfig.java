package by.shimakser.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
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

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("currency");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    Caffeine< Object, Object > caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .weakKeys()
                .recordStats();
    }
}
