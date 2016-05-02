package com.github.vjgorla.refdata.config;

import java.util.Properties;

/**
 * 
 * @author Vijaya Gorla
 */
public abstract class Config {
    
    private static Config INSTANCE; 

    public abstract String getLoaderImplClass();
    
    public abstract String getCacheImplClass();
    
    public abstract String getJsonDataFile();
    
    public abstract Properties getJpaProperties();
    
    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigImpl();
        }
        return INSTANCE;
    }
    
}
