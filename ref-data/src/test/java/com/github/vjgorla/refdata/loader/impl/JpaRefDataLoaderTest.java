package com.github.vjgorla.refdata.loader.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.cache.impl.SimpleRefDataCache;
import com.github.vjgorla.refdata.entity.RefDataTypeEntity;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.loader.impl.JpaRefDataLoader;
import com.github.vjgorla.refdata.util.EmptyConfig;
import com.github.vjgorla.refdata.util.TestUtils;

/**
 * 
 * @author Vijaya Gorla
 */
@SuppressWarnings("serial")
public class JpaRefDataLoaderTest {

    @Before
    public void setUp() {
        TestUtils.resetRefDataConfig(new EmptyConfig() {
            @Override
            public String getLoaderImplClass() {
                return MockJpaRefDataLoader.class.getName();
            }
            @Override
            public String getCacheImplClass() {
                return SimpleRefDataCache.class.getName();
            }
            @Override
            public Properties getJpaProperties() {
                Properties props = new Properties();
                props.put("javax.persistence.provider", "org.hibernate.ejb.HibernatePersistence");
                props.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
                props.put("hibernate.connection.username", "su");
                props.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
                props.put("hibernate.connection.url", "jdbc:hsqldb:mem:refdata");
                props.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
                props.put("hibernate.hbm2ddl.auto", "create");
                props.put("hibernate.show_sql", "true");
                return props;
            }
        });
        TestUtils.resetRefDataCache();
    }
    
    @Test
    public void givenStaticTypesLoaded_whenDecode_thenValueEqualsStaticType() {
        TestValueType code1 = TestValueType.type.decode("CODE1");
        assertThat(code1, is(TestValueType.CODE1));
        assertThat(TestValueType.type.values(false).size(), is(3));
    }

    @Test(expected = RuntimeException.class)
    public void givenCodeDoesntExist_whenDecode_thenRuntimeException() {
        TestValueType.type.decode("CODE4");
    }

    @Test
    public void givenLoaderReturnsEntitiesInUndefinedOrder_whenValues_thenValuesReturnedInCodeOrder() {
        List<TestValueType> values = TestValueType.type.values(false);
        assertThat(values.get(0).getCode(), is("CODE1"));
        assertThat(values.get(1).getCode(), is("CODE2"));
        assertThat(values.get(2).getCode(), is("CODE3"));
    }

    private static class TestValueType extends AbstractRefDataValue {
        static RefDataType<TestValueType> type = new RefDataType<TestValueType>(TestValueType.class);
        static final TestValueType CODE1 = type.decode("CODE1");
        private TestValueType(String code) {
            super(type, code);
        }
    }
    
    public static class MockJpaRefDataLoader extends JpaRefDataLoader {
        @Override
        public Set<RefDataValueEntity> loadEntities(String typeCode) {
            EntityManager em = emFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            
            RefDataTypeEntity typeEntity = new RefDataTypeEntity();
            typeEntity.setId(1);
            typeEntity.setTypeCode(typeCode);
            
            Set<RefDataValueEntity> values = new HashSet<>();
            values.add(newEntity(1, "CODE1"));
            values.add(newEntity(2, "CODE2"));
            values.add(newEntity(3, "CODE3"));
            
            typeEntity.setValues(values);
            em.persist(typeEntity);
            
            tx.commit();
            em.clear();
            em.close();
            return super.loadEntities(typeCode);
        }
        
        private RefDataValueEntity newEntity(long id, String code) {
            RefDataValueEntity entity = new RefDataValueEntity(id, code, code, null, null, null);
            return entity;
        }
        
    }

}
