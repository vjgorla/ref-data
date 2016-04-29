package com.github.vjgorla.refdata.spring.formatters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.expression.ParseException;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.util.ReflectionUtils;

public class RefDataValueAnnotationFormatterFactory implements AnnotationFormatterFactory<RefDataValueFormat> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { AbstractRefDataValue.class }));
    }

    @Override
    public Printer<AbstractRefDataValue> getPrinter(RefDataValueFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    @Override
    public Parser<AbstractRefDataValue> getParser(RefDataValueFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Formatter<AbstractRefDataValue> configureFormatterFrom(RefDataValueFormat annotation, Class<?> fieldType) {
        return new RefDataValueFormatter((Class<AbstractRefDataValue>)fieldType);
    }

    private static class RefDataValueFormatter<T extends AbstractRefDataValue> implements Formatter<T> {

        private RefDataType<T> type;
        
        public RefDataValueFormatter(Class<T> valueClass) {
            type = ReflectionUtils.getType(valueClass);
        }
        
        @Override
        public String print(T value, Locale locale) {
            if (value == null) {
                return "";
            }
            return value.getCode();
        }

        @Override
        public T parse(String formatted, Locale locale) throws ParseException {
            if (formatted == null || formatted.length() == 0) {
                return null;
            }
            return type.decode(formatted);
        }
    }
}