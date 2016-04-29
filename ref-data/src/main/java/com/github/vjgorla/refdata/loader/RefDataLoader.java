package com.github.vjgorla.refdata.loader;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.cache.RefDataCache.RefDataValueMap;
import com.github.vjgorla.refdata.config.Config;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.util.ReflectionUtils;
import com.google.common.cache.CacheLoader;

/**
 * 
 * @author Vijaya Gorla
 */
public abstract class RefDataLoader extends CacheLoader<RefDataType<?>, RefDataValueMap> {
    
    private static final Logger LOG = LoggerFactory.getLogger(RefDataLoader.class);
    
    @Override
    public RefDataValueMap load(RefDataType<?> type) throws NoSuchMethodException, SecurityException, 
        InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        LOG.debug("Loading values for type [{}]", type.getTypeCode());
        
        Date loadedTs = new Date();
        RefDataValueMap cacheEntry = new RefDataValueMap(loadedTs);
        for(RefDataValueEntity entity : loadEntities(type.getTypeCode())) {
            cacheEntry.put(entity.getCode(), entity);
        }
        return cacheEntry;
    }
    
    public abstract Set<RefDataValueEntity> loadEntities(String typeCode);
    
    public static <T extends RefDataLoader> T getLoaderImpl() {
        String implClassName = Config.getInstance().getLoaderImplClass();
        LOG.debug("Loader implementation [{}]", implClassName);
        return ReflectionUtils.newInstance(implClassName);
    }
}
