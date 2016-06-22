package com.github.vjgorla.refdata.jpa.bind;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.ejb.Ejb3Configuration;
import org.junit.Before;
import org.junit.Test;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.cache.impl.SimpleRefDataCache;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.jpa.bind.RefDataValueJpaBinder;
import com.github.vjgorla.refdata.loader.RefDataLoader;
import com.github.vjgorla.refdata.util.EmptyConfig;
import com.github.vjgorla.refdata.util.TestUtils;

/**
 * 
 * @author Vijaya Gorla
 */
@SuppressWarnings({"deprecation","serial"})
public class RefDataValueJpaBinderTest {

    private EntityManagerFactory emFactory;
    
    @Before
    public void setUp() {
        TestUtils.resetRefDataConfig(new EmptyConfig() {
            @Override
            public String getLoaderImplClass() {
                return MockRefDataLoader.class.getName();
            }
            @Override
            public String getCacheImplClass() {
                return SimpleRefDataCache.class.getName();
            }
        });
        TestUtils.resetRefDataCache();
        Properties props = new Properties();
        props.put("javax.persistence.provider", "org.hibernate.ejb.HibernatePersistence");
        props.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        props.put("hibernate.connection.username", "su");
        props.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        props.put("hibernate.connection.url", "jdbc:hsqldb:mem:customers");
        props.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "create");
        props.put("hibernate.show_sql", "true");

        Ejb3Configuration cfg = new Ejb3Configuration();
        cfg.configure(props);
        cfg.addAnnotatedClass(Customer.class);
        emFactory = cfg.createEntityManagerFactory();
    }
    
    @Test
    public void givenJpaPersistence_whenValueNotNull_thenValuePersistedAsCode() {
        EntityManager em = emFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = new Customer();
        customer.id = 1;
        customer.name = "Test Customer";
        customer.country = Country.AUS;
        em.persist(customer);
        tx.commit();
        em.clear();
        em.close();
        
        em = emFactory.createEntityManager();
        customer = em.find(Customer.class, 1L);
        em.clear();
        em.close();
        
        assertThat(customer.country, is(Country.AUS));
    }
    
    @Test
    public void givenJpaPersistence_whenValueNull_thenValuePersistedAsNull() {
        EntityManager em = emFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = new Customer();
        customer.id = 1;
        customer.name = "Test Customer";
        em.persist(customer);
        tx.commit();
        em.clear();
        em.close();
        
        em = emFactory.createEntityManager();
        customer = em.find(Customer.class, 1L);
        em.clear();
        em.close();
        
        assertThat(customer.country, nullValue());
    }

    private static class Country extends AbstractRefDataValue {
        static RefDataType<Country> type = new RefDataType<Country>(Country.class);
        static final Country AUS = type.decode("AUS");
        private Country(String code) {
            super(type, code);
        }
    }
    
    public static class MockRefDataLoader extends RefDataLoader {
        @Override
        public Set<RefDataValueEntity> loadEntities(String typeCode) {
            RefDataValueEntity entity = new RefDataValueEntity(1, "AUS", null, null, null, null);
            return Collections.singleton(entity);
        }
    }

    @Entity
    @Table(name = "CUSTOMER")
    private static class Customer {
        
        @Id
        @Column(name = "ID", nullable = false)
        private long id;
        
        @Column(name = "NAME", nullable = false)
        private String name;
        
        @Column(name = "COUNTRY") 
        @Type(type=RefDataValueJpaBinder.BINDER)
        private Country country;
        
    }
}
