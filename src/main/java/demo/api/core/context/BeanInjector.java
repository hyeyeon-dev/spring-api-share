package demo.api.core.context;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class BeanInjector {
    public static Object getBean(String beanName) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return  applicationContext.getBean(clazz);
    }

    public static Environment getEnvironment() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return  applicationContext.getEnvironment();
    }
}
