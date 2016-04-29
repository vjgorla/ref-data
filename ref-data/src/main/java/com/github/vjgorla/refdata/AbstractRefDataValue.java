package com.github.vjgorla.refdata;

import java.io.Serializable;
import java.util.Date;

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
        this.type.ensureExists(this.code);
    }
    
    public RefDataType<?> getType() {
        return type;
    }

    public String getCode() {
        return this.code;
    }
    
    public String getDescription() {
        return type.getDescription(code);
    }

    public Date getEffectiveFrom() {
        return type.getEffectiveFrom(code);
    }

    public Date getEffectiveTo() {
        return type.getEffectiveTo(code);
    }
    
    public boolean isEffective() {
        return DateUtils.isBetween(new Date(), 
                                   getEffectiveFrom(), 
                                   getEffectiveTo());
    }

    protected String getAttribute(String key) {
        return type.getAttribute(code, key);
    }
    
    protected String[] getAttributeAsList(String key) {
        String attributeStr = getAttribute(key);
        if (StringUtils.isEmpty(attributeStr)) {
            return new String[0];
        }
        return attributeStr.split(",");
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
