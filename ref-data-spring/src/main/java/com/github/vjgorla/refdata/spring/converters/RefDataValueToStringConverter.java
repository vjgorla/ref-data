package com.github.vjgorla.refdata.spring.converters;

import org.springframework.core.convert.converter.Converter;

import com.github.vjgorla.refdata.AbstractRefDataValue;

public class RefDataValueToStringConverter<T extends AbstractRefDataValue> implements Converter<T, String> {

    public String convert(T value) {
        return value.getCode();
    }
}