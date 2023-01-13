package demo.api.core.excel;

import org.apache.poi.ss.usermodel.DataFormat;

import java.util.Arrays;
import java.util.List;

public class ExcelDataFormatter {
    private static final String CURRENT_FORMAT = "#,##0";
    private static final String FLOAT_FORMAT_2_DECIMAL_PLACES = "#,##0.00";
    private static final String DEFAULT_FORMAT = "";

    public short getDataFormat(DataFormat dataFormat, Class<?> clazz) {
        if (isFloatType(clazz)) {
            return dataFormat.getFormat(FLOAT_FORMAT_2_DECIMAL_PLACES);
        }

        if (isIntegerType(clazz)) {
            return dataFormat.getFormat(CURRENT_FORMAT);
        }

        return dataFormat.getFormat(DEFAULT_FORMAT);
    }

    private boolean isFloatType(Class<?> clazz) {
        List<Class<?>> floatTypes = Arrays.asList(
                Float.class, float.class,
                Double.class, double.class
        );
        return floatTypes.contains(clazz);
    }

    private boolean isIntegerType(Class<?> clazz) {
        List<Class<?>> integerTypes = Arrays.asList(
                Byte.class, byte.class,
                Short.class, short.class,
                Integer.class, int.class,
                Long.class, long.class
        );
        return integerTypes.contains(clazz);
    }
}
