package com.github.vjgorla.refdata.entity;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import com.github.vjgorla.refdata.util.StringUtils;

/**
 * 
 * @author Vijaya Gorla
 */
@Entity
@Table(name = "CODE_VALUES")
@Immutable
public class RefDataValueEntity {
    
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    
    @Column(name = "CODE", nullable = false)
    private String code;
    
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    
    @Column(name = "ATTRIBUTES")
    private String attributes;
    
    @Column(name = "EFFECTIVE_FROM")
    private Date effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private Date effectiveTo;

    @Transient
    private Map<String, String> attributeMap;
    
    public RefDataValueEntity() {
    }
    
    public RefDataValueEntity(long id, String code, String description, 
                              String attributes, Date effectiveFrom, Date effectiveTo) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.attributes = attributes;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
    }

    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Date getEffectiveFrom() {
        return effectiveFrom;
    }

    public Date getEffectiveTo() {
        return effectiveTo;
    }

    public String getAttribute(String key) {
        if (attributeMap == null) {
            attributeMap = StringUtils.convertToMap(attributes, ";", "=");
        }
        return attributeMap.get(key);
    }
}
