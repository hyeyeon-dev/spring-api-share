package demo.api.server.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class NullUtils {
    public static boolean isObjectEquals(Object obj1, Object obj2) {
        boolean result = true;

        if (obj1 == null || obj2 == null) {
            if (obj1 != obj2) result = false;
        } else {
            if (!obj1.equals(obj2)) result = false;
        }

        return result;
    }

    public static Object ifNullDefault(Object obj, Object defaultValue) {
        if (obj == null) return defaultValue;
        else return obj;
    }

    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
