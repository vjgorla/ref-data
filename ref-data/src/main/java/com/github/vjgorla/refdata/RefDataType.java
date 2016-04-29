package com.github.vjgorla.refdata;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.github.vjgorla.refdata.cache.RefDataCache;
import com.github.vjgorla.refdata.util.NamingStrategy;
import com.github.vjgorla.refdata.util.ReflectionUtils;
import com.google.common.base.Objects;

/**
 * 
 * @author Vijaya Gorla
 * @param <V>
 */
@SuppressWarnings("serial")
public class RefDataType<V extends AbstractRefDataValue> implements Serializable {

    private String typeCode;
    private Class<V> valueClass;
    private Constructor<V> valueConstructor;
    
    public RefDataType(Class<V> valueClass) {
        this(valueClass, NamingStrategy.generateTypeCode(valueClass));
    }
    
    public RefDataType(Class<V> valueClass, String typeCode) {
        this.typeCode = typeCode;
        this.valueClass = valueClass;
        this.valueConstructor = ReflectionUtils.getConstructor(valueClass, String.class);
    }
    
    public String getTypeCode() {
        return this.typeCode;
    }
    
    public Class<V> getValueClass() {
        return valueClass;
    }
    
    void ensureExists(String code) {
        RefDataCache.get(this, code);
    }
    
    String getDescription(String code) {
        return RefDataCache.get(this, code).getDescription();
    }
    
    Date getEffectiveFrom(String code) {
        return RefDataCache.get(this, code).getEffectiveFrom();
    }

    Date getEffectiveTo(String code) {
        return RefDataCache.get(this, code).getEffectiveTo();
    }

    String getAttribute(String code, String key) {
        return RefDataCache.get(this, code).getAttribute(key);
    }
    
    public V decode(String code) {
        return ReflectionUtils.newInstance(valueConstructor, code);
    }
    
    public List<V> values(boolean effectiveOnly) {
        List<V> values = new ArrayList<>();
        for (String code : RefDataCache.getCodes(this)) {
            V value = decode(code);
            if (!effectiveOnly || value.isEffective()) {
                values.add(value);
            }
        }
        return values;
    }

    public List<V> values(boolean effectiveOnly, Comparator<V> comparator) {
        List<V> valueList = values(effectiveOnly);
        Collections.sort(valueList, comparator);
        return valueList;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(typeCode, valueClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RefDataType<?> other = (RefDataType<?>) obj;
        return Objects.equal(this.typeCode, other.typeCode) 
                && Objects.equal(this.valueClass, other.valueClass); 
    }

    @Override
    public String toString() {
        return "RefDataType [typeCode=" + typeCode + "]";
    }
}
