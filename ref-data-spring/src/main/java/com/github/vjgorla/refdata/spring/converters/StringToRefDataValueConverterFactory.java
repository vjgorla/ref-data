package com.github.vjgorla.refdata.spring.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.util.ReflectionUtils;

public class StringToRefDataValueConverterFactory implements ConverterFactory<String, AbstractRefDataValue> {

    public <T extends AbstractRefDataValue> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToRefDataValueConverter<T>(targetType);
    }
    
    private static final class StringToRefDataValueConverter<T extends AbstractRefDataValue> implements Converter<String, T> {

        private RefDataType<T> type;

        public StringToRefDataValueConverter(Class<T> valueClass) {
            type = ReflectionUtils.getType(valueClass);
        }

        public T convert(String code) {
            return type.decode(code);
        }
    }
}