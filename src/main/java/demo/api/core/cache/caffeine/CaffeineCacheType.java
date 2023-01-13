package demo.api.core.cache.caffeine;

import lombok.Getter;

@Getter
public enum CaffeineCacheType {
    DATA("caffeineCache", 5*60, 10000);

    CaffeineCacheType(String cacheName, int expireAfterWrite, int maximumSize) {
        this.cacheName = cacheName;
        this.expireAfterWrite = expireAfterWrite;
        this.maximumSize = maximumSize;
    }

    private final String cacheName;
    private final int expireAfterWrite;
    private final int maximumSize;
}
