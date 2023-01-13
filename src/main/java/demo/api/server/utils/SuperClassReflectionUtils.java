package demo.api.server.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class SuperClassReflectionUtils {
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClass(clazz, true)) {
            fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
        }
        return fields;
    }

    public static Annotation getAnnotation(Class<?> clazz, Class<? extends Annotation> targetAnnotation) {
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClass(clazz, false)) {
            if (clazzInClasses.isAnnotationPresent(targetAnnotation)) {
                return clazzInClasses.getAnnotation(targetAnnotation);
            }
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String name) throws Exception {
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClass(clazz, false)) {
            for (Field field : clazzInClasses.getDeclaredFields()) {
                if (field.getName().equals(name)) {
                    return clazzInClasses.getDeclaredField(name);
                }
            }
        }
        throw new NoSuchFieldException();
    }

    public static Method getGetterMethod(Class<?> clazz, String name) throws Exception {
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClass(clazz, false)) {
            for (Method method : clazzInClasses.getMethods()) {
                String methodName = method.getName();

                if ((methodName.startsWith("get") && (methodName.length() == name.length()+3))
                        || (methodName.startsWith("is") && methodName.length() == name.length()+2)) {

                    if (methodName.toLowerCase(Locale.ROOT).endsWith(name.toLowerCase(Locale.ROOT))) {
                        return method;
                    }

                }

            }
        }
        throw new NoSuchFieldException();
    }

    private static List<Class<?>> getAllClassesIncludingSuperClass(Class<?> clazz, boolean fromSuper) {
        List<Class<?>> classes = new ArrayList<>();
        while (clazz != null) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
        if (fromSuper) {
            Collections.reverse(classes);
        }
        return classes;
    }
}
