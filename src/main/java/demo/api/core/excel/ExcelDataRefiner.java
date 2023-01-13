package demo.api.core.excel;

import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ExcelDataRefiner {

    public Object getDataRefiner(Object cellValue, Class<?> clazz) {

        if (isLocalDataType(clazz)) {
            LocalDateTime dateTime = (LocalDateTime) cellValue;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return ObjectUtils.isNotEmpty(dateTime)? dateTime.format(formatter) : "";
        }

        return cellValue;
    }

    private boolean isLocalDataType(Class<?> clazz) {
        List<Class<?>> localDataTypes = Arrays.asList(
                LocalDateTime.class
        );
        return localDataTypes.contains(clazz);
    }
}
