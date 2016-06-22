package com.github.vjgorla.refdata.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.cache.impl.SimpleRefDataCache;
import com.github.vjgorla.refdata.config.Config;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.loader.RefDataLoader;
import com.github.vjgorla.refdata.util.ReflectionUtils;

/**
 * 
 * @author Vijaya Gorla
 */
@SuppressWarnings({"serial", "unused"})
public class ReflectionUtilsTest {

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
    
    @Test(expected = IllegalArgumentException.class)
    public void givenValueClassHasNoType_whenGetType_thenAnExceptionIsThrown() {
        ReflectionUtils.getType(ValueTestNoType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenValueClassHasTypeButNotStatic_whenGetType_thenAnExceptionIsThrown() {
        ReflectionUtils.getType(ValueTestWithTypeButNotStatic.class);
    }

    @Test
    public void givenValueClassHasType_whenGetType_thenTypeIsReturned() {
        RefDataType<ValueTestWithType> type = ReflectionUtils.getType(ValueTestWithType.class);
        assertThat(type.getValueClass(), is((Object)ValueTestWithType.class));
    }

    private static class ValueTestNoType extends AbstractRefDataValue {
        private ValueTestNoType(String code) {
            super(null, code);
        }
    }

    private static class ValueTestWithTypeButNotStatic extends AbstractRefDataValue {
        RefDataType<ValueTestWithTypeButNotStatic> type 
            = new RefDataType<ValueTestWithTypeButNotStatic>(ValueTestWithTypeButNotStatic.class);
        private ValueTestWithTypeButNotStatic(String code) {
            super(null, code);
        }
    }

    private static class ValueTestWithType extends AbstractRefDataValue {
        static RefDataType<ValueTestWithType> type = new RefDataType<ValueTestWithType>(ValueTestWithType.class);
        private ValueTestWithType(String code) {
            super(type, code);
        }
    }

    public static class MockRefDataLoader extends RefDataLoader {
        @Override
        public Set<RefDataValueEntity> loadEntities(String typeCode) {
            return null;
        }
    }
}
