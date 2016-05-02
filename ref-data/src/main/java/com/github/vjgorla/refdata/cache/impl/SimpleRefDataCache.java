package com.github.vjgorla.refdata.cache.impl;

import java.util.Date;

import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.cache.RefDataCache;
import com.github.vjgorla.refdata.loader.RefDataLoader;
import com.github.vjgorla.refdata.util.DateUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

/**
 * 
 * @author Vijaya Gorla
 */
public class SimpleRefDataCache extends RefDataCache {

    private LoadingCache<RefDataType<?>, RefDataValueMap> CACHE  
        = CacheBuilder.newBuilder().build(RefDataLoader.getLoaderImpl());

    public RefDataValueMap getNotExpired(RefDataType<?> type) {
        RefDataValueMap refDataValueMap = CACHE.getUnchecked(type);
        Date now = new Date();
        // Entries are only valid for today
        boolean expired = DateUtils.isAfterIgnoreTime(now, refDataValueMap.getLoadedTs());
        if (expired) {
            CACHE.invalidate(type);
            refDataValueMap = CACHE.getUnchecked(type);
        }
        return refDataValueMap;
    }
}
