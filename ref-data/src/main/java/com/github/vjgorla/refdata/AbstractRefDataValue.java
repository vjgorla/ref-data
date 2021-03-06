package com.github.vjgorla.refdata;

import java.io.Serializable;
import java.util.Date;

import com.github.vjgorla.refdata.cache.RefDataCache;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.util.DateUtils;
import com.github.vjgorla.refdata.util.StringUtils;
import com.google.common.base.Objects;

/**
 * 
 * @author Vijaya Gorla
 */
@SuppressWarnings("serial")
public abstract class AbstractRefDataValue implements Serializable {
    
    private final RefDataType<?> type;
    private final String code;

    protected AbstractRefDataValue(RefDataType<?> type, String code) {
        this.type = type;
        this.code = code;
        // Ensure entity exists
        getValueEntity();
    }
    
    public RefDataType<?> getType() {
        return type;
    }

    public String getCode() {
        return this.code;
    }
    
    public String getDescription() {
        return getValueEntity().getDescription();
    }

    public Date getEffectiveFrom() {
        return getValueEntity().getEffectiveFrom();
    }

    public Date getEffectiveTo() {
        return getValueEntity().getEffectiveTo();
    }
    
    public boolean isEffective() {
        return DateUtils.isBetween(new Date(), 
                                   getEffectiveFrom(), 
                                   getEffectiveTo());
    }

    protected String getAttribute(String key) {
        return getValueEntity().getAttribute(key);
    }
    
    protected String[] getAttributeAsList(String key) {
        String attributeStr = getAttribute(key);
        if (StringUtils.isEmpty(attributeStr)) {
            return new String[0];
        }
        return attributeStr.split(",");
    }
    
    private RefDataValueEntity getValueEntity() {
        return RefDataCache.getCacheImpl().get(type, code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, code);
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
        AbstractRefDataValue other = (AbstractRefDataValue) obj;
        return Objects.equal(this.type, other.type) 
                && Objects.equal(this.code, other.code);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + code + ", " + getDescription() + "]";
    }
}
