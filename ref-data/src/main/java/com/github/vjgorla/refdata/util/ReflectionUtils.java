package com.github.vjgorla.refdata.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;

/**
 * 
 * @author Vijaya Gorla
 */
public class ReflectionUtils {

    private ReflectionUtils() {
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends AbstractRefDataValue> RefDataType<T> getType(Class<T> valueClass) {
        RefDataType<T> type = null;
        try {
            for (Field field : valueClass.getDeclaredFields()) {
                if (!field.isSynthetic() && Modifier.isStatic(field.getModifiers()) 
                        && RefDataType.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    type = (RefDataType<T>)field.get(valueClass);
                    break;
                }
            }
            if (type == null) {
                throw new IllegalArgumentException("RefDataType static field not found in class [" + valueClass.getName() + "]");
            }
            return type;
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Could not read RefDataType field in class [" + valueClass.getName() + "]", ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        try {
            Class<T> implClass = (Class<T>)Class.forName(className);
            return implClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("Error instantiating class [" + className + "]", ex);
        }
    }
    
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Can't get constructor from class [" + clazz.getName() + "]");
        }
    }
    
    public static <T> T newInstance(Constructor<T> constructor, Object ... initargs) {
        try {
            return constructor.newInstance(initargs);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalArgumentException("Can't instantiate class [" + constructor.getDeclaringClass().getName() + "]", ex);
        }
    }
}
