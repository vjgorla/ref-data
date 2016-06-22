package com.github.vjgorla.refdata.loader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.cache.impl.SimpleRefDataCache;
import com.github.vjgorla.refdata.config.Config;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.loader.RefDataLoader;
import com.github.vjgorla.refdata.loader.impl.JsonRefDataLoader;
import com.github.vjgorla.refdata.util.EmptyConfig;
import com.github.vjgorla.refdata.util.ReflectionUtils;
import com.github.vjgorla.refdata.util.TestUtils;
import com.google.common.cache.CacheBuilder;

/**
 * 
 * @author Vijaya Gorla
 */
@SuppressWarnings({"serial", "unused"})
public class RefDataLoaderTest {

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
    }
    
    @Test
    public void givenValuesLoaded_whenDecode_thenCacheIsNotReloaded() {
        assertThat(MockRefDataLoader.counter, is(0));
        TestValueType.type.decode("CODE1");
        assertThat(MockRefDataLoader.counter, is(1));
        TestValueType.type.values(false);
        assertThat(MockRefDataLoader.counter, is(1));
        TestValueType.type.decode("CODE2");
        assertThat(MockRefDataLoader.counter, is(1));
    }

    @Test
    public void givenLoaderReturnsEntitiesInIncorrectOrder_whenValues_thenValuesReturnedInCodeOrder() {
        List<TestValueType> values = TestValueType.type.values(false);
        assertThat(values.get(0).getCode(), is("CODE1"));
        assertThat(values.get(1).getCode(), is("CODE2"));
        assertThat(values.get(2).getCode(), is("CODE3"));
    }

    @Test
    public void givenTwoEffectiveCodesAndOneNotEffectiveCode_whenGetValues_thenThreeValuesAreReturned() {
        List<TestValueType> values = TestValueType.type.values(false);
        assertThat(values.size(), is(3));
        assertThat(values.get(0).getCode(), is("CODE1"));
        assertThat(values.get(1).getCode(), is("CODE2"));
        assertThat(values.get(2).getCode(), is("CODE3"));
    }

    @Test
    public void givenTwoEffectiveCodesAndOneNotEffectiveCode_whenGetEffectiveValues_thenTwoValuesAreReturned() {
        List<TestValueType> values = TestValueType.type.values(true);
        assertThat(values.size(), is(2));
        assertThat(values.get(0).getCode(), is("CODE1"));
        assertThat(values.get(1).getCode(), is("CODE2"));
    }

    private static class TestValueType extends AbstractRefDataValue {
        static RefDataType<TestValueType> type = new RefDataType<TestValueType>(TestValueType.class);
        private TestValueType(String code) {
            super(type, code);
        }
    }
    
    public static class MockRefDataLoader extends RefDataLoader {
        public static int counter = 0;
        @Override
        public Set<RefDataValueEntity> loadEntities(String typeCode) {
            counter++;
            Set<RefDataValueEntity> set = new TreeSet<>(new Comparator<RefDataValueEntity>() {
                @Override
                public int compare(RefDataValueEntity o1, RefDataValueEntity o2) {
                    return o2.getCode().compareTo(o1.getCode());
                }
            });
            set.add(newEntity("CODE1", null, null));
            set.add(newEntity("CODE2", null, null));
            try {
                set.add(newEntity("CODE3", null, new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01")));
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            return set;
        }
        
        private RefDataValueEntity newEntity(String code, Date effectiveFrom, Date effectiveTo) {
            RefDataValueEntity entity = new RefDataValueEntity(1, code, null, null, effectiveFrom, effectiveTo);
            return entity;
        }
        
    }
}
