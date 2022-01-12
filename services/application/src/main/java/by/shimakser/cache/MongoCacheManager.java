package by.shimakser.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.LinkedHashSet;

public class MongoCacheManager extends AbstractCacheManager {

    private final Collection<MongoCacheBuilder> initCache;

    private final String cacheName;

    public MongoCacheManager(Collection<MongoCacheBuilder> initCache, String cacheName) {
        this.initCache = initCache;
        this.cacheName = cacheName;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<Cache> caches = new LinkedHashSet<>(initCache.size());

        for (MongoCacheBuilder cacheBuilder : initCache) {
            MongoCache cache = cacheBuilder.build(cacheName);
            caches.add(cache);
        }

        return caches;
    }
}
