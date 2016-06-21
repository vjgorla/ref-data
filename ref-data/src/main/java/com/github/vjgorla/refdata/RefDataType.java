package com.github.vjgorla.refdata;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, V> valueMap = new HashMap<>();
    
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
    
    public synchronized V decode(String code) {
        V value = valueMap.get(code);
        if (value != null) {
            boolean hasEntity = RefDataCache.getCacheImpl().hasEntity(this, code);
            if (!hasEntity) {
                valueMap.remove(code);
                return null;
            }
        } else {
            value = ReflectionUtils.newInstance(valueConstructor, code);
            valueMap.put(code, value);
        }
        return value;
    }
    
    public List<V> values(boolean effectiveOnly) {
        List<V> values = new ArrayList<>();
        for (String code : RefDataCache.getCacheImpl().getCodes(this)) {
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
