package com.github.vjgorla.refdata.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * 
 * @author Vijaya Gorla
 */
@Entity
@Table(name = "CODE_TYPES")
@Immutable
@NamedQuery(name = "RefDataTypeEntity.findByCode", query="SELECT obj FROM RefDataTypeEntity obj WHERE obj.typeCode = :typeCode")
public class RefDataTypeEntity {
    
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    
    @Column(name = "TYPE_CODE", nullable = false)
    private String typeCode;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="TYPE_ID")
    private Set<RefDataValueEntity> values;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Set<RefDataValueEntity> getValues() {
        return values;
    }

    public void setValues(Set<RefDataValueEntity> values) {
        this.values = values;
    }
}
