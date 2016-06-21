package com.github.vjgorla.refdata.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.github.vjgorla.refdata.config.Config;

/**
 * 
 * @author Vijaya Gorla
 */
public class TestUtils {

    public static void resetRefDataConfig(Config config) {
        reflectionSet(Config.class, "INSTANCE", config);
    }
    
    private static void reflectionSet(Class<?> clazz, String fieldName, Object value) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(fieldName) && !field.isSynthetic() && Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    field.set(clazz, value);
                    return;
                }
            }
            throw new IllegalArgumentException("Can't find field [" + fieldName + "] in object [" + clazz.getName() + "]");
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Could not set field [" + fieldName + "] in class [" + clazz.getName() + "]", ex);
        }
    }
}
