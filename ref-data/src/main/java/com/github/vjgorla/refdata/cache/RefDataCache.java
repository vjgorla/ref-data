package com.github.vjgorla.refdata.cache;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;

import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.loader.RefDataLoader;
import com.github.vjgorla.refdata.util.DateUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

/**
 * 
 * @author Vijaya Gorla
 */
public class RefDataCache {

    private static LoadingCache<RefDataType<?>, RefDataValueMap> CACHE  
        = CacheBuilder.newBuilder().build(RefDataLoader.getLoaderImpl());

    private RefDataCache() {
    }
    
    public static RefDataValueEntity get(RefDataType<?> type, String code) {
        RefDataValueMap refDataValueMap = getNotExpired(type);
        RefDataValueEntity entity = refDataValueMap.get(code);
        if (entity == null) {
            throw new IllegalArgumentException("Value [" + code + "] not found for type [" + type.getTypeCode() + "]");
        }
        return entity;
    }

    public static Set<String> getCodes(RefDataType<?> type) {
        RefDataValueMap refDataValueMap = getNotExpired(type);
        return Collections.unmodifiableSet(refDataValueMap.keySet());
    }
    
    private static RefDataValueMap getNotExpired(RefDataType<?> type) {
        RefDataValueMap refDataValueMap = CACHE.getUnchecked(type);
        if (refDataValueMap.isExpired()) {
            CACHE.invalidate(type);
            refDataValueMap = CACHE.getUnchecked(type);
        }
        return refDataValueMap;
    }
    
    @SuppressWarnings("serial")
    public static class RefDataValueMap extends TreeMap<String, RefDataValueEntity> {
        
        private Date loadedTs;
        
        public RefDataValueMap(Date loadedTs) {
            this.loadedTs = loadedTs;
        }

        public boolean isExpired() {
            Date now = new Date();
            return DateUtils.isAfterIgnoreTime(now, loadedTs);
        }
    }
}
