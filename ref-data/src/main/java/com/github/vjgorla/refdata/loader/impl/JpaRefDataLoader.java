package com.github.vjgorla.refdata.loader.impl;

import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.hibernate.ejb.Ejb3Configuration;

import com.github.vjgorla.refdata.config.Config;
import com.github.vjgorla.refdata.entity.RefDataTypeEntity;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.loader.RefDataLoader;

/**
 * 
 * @author Vijaya Gorla
 */
@SuppressWarnings("deprecation")
public class JpaRefDataLoader extends RefDataLoader {
    
    protected final EntityManagerFactory emFactory;
    
    public JpaRefDataLoader() {
        emFactory = buildEntityManagerFactory();
    }
    
    @Override
    public Set<RefDataValueEntity> loadEntities(String typeCode) {
        EntityManager em = emFactory.createEntityManager();
        try {
            RefDataTypeEntity typeEntity 
                = em.createNamedQuery("RefDataTypeEntity.findByCode", RefDataTypeEntity.class)
                    .setParameter("typeCode", typeCode)
                    .getSingleResult();
            return typeEntity.getValues();
        } catch (NoResultException nre) {
            throw new IllegalArgumentException("Code type [" + typeCode + "] is not found in the database");
        } finally {
            em.clear();
            em.close();
        }
    }
    
    private EntityManagerFactory buildEntityManagerFactory() {
        Properties properties = Config.getInstance().getJpaProperties();
        Ejb3Configuration cfg = new Ejb3Configuration();
        cfg.configure(properties);
        cfg.addAnnotatedClass(RefDataTypeEntity.class);
        cfg.addAnnotatedClass(RefDataValueEntity.class);
        return cfg.createEntityManagerFactory();
    }
}
