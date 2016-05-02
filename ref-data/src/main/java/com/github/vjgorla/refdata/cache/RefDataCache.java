package com.github.vjgorla.refdata.cache;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.config.Config;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.util.ReflectionUtils;

/**
 * 
 * @author Vijaya Gorla
 */
public abstract class RefDataCache {

    private static final Logger LOG = LoggerFactory.getLogger(RefDataCache.class);
    private static RefDataCache CACHE_IMPL;
    
    public RefDataValueEntity get(RefDataType<?> type, String code) {
        RefDataValueMap refDataValueMap = getNotExpired(type);
        RefDataValueEntity entity = refDataValueMap.get(code);
        if (entity == null) {
            throw new IllegalArgumentException("Value [" + code + "] not found for type [" + type.getTypeCode() + "]");
        }
        return entity;
    }

    public Set<String> getCodes(RefDataType<?> type) {
        RefDataValueMap refDataValueMap = getNotExpired(type);
        return Collections.unmodifiableSet(refDataValueMap.keySet());
    }
    
    protected abstract RefDataValueMap getNotExpired(RefDataType<?> type);
    
    public static RefDataCache getCacheImpl() {
        if (RefDataCache.CACHE_IMPL == null) {
            String implClassName = Config.getInstance().getCacheImplClass();
            LOG.debug("Cache implementation [{}]", implClassName);
            RefDataCache.CACHE_IMPL = ReflectionUtils.newInstance(implClassName);
        }
        return RefDataCache.CACHE_IMPL;
    }
    
    @SuppressWarnings("serial")
    public static class RefDataValueMap extends TreeMap<String, RefDataValueEntity> {
        
        private Date loadedTs;
        
        public RefDataValueMap(Date loadedTs) {
            this.loadedTs = loadedTs;
        }

        public Date getLoadedTs() {
            return this.loadedTs;
        }
    }
}
