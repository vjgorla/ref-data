package com.github.vjgorla.refdata.util;

import java.util.Properties;

import com.github.vjgorla.refdata.config.Config;

/**
 * 
 * @author Vijaya Gorla
 */
public class EmptyConfig extends Config {

    @Override
    public String getLoaderImplClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJsonDataFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getJpaProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCacheImplClass() {
        throw new UnsupportedOperationException();
    }
}
