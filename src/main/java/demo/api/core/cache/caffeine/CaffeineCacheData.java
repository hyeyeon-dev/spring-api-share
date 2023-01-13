package demo.api.core.cache.caffeine;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CaffeineCacheData {
    private String name;
    private String data;

    @Builder
    public CaffeineCacheData(String name, String data) {
        this.name = name;
        this.data = data;
    }
}
