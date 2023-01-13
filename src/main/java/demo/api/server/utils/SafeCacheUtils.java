package demo.api.server.utils;

import java.util.concurrent.ConcurrentHashMap;

public class SafeCacheUtils {

    private ConcurrentHashMap cacheDataMap;

    private SafeCacheUtils() {
        this.cacheDataMap = new ConcurrentHashMap();
    }

    private static class lazyHolder {
        public static final SafeCacheUtils instance = new SafeCacheUtils();
    }

    private static SafeCacheUtils getInstance() {
        return lazyHolder.instance;
    }

    public static ConcurrentHashMap getCashDataMap() {
        return getInstance().cacheDataMap;
    }

    public static void put(String cacheType, Object data) {
        getInstance().cacheDataMap.put(cacheType, data);
    }

    public static <T> T get(String cacheType) {
        return (T) getInstance().cacheDataMap.get(cacheType);
    }

}
