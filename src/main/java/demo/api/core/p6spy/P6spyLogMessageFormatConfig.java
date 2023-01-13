package demo.api.core.p6spy;

import com.p6spy.engine.spy.P6SpyOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class P6spyLogMessageFormatConfig {

    @PostConstruct
    public void P6spyLogMessageFormatConfig() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6SpySqlFormatConfig.class.getName());
    }
}
