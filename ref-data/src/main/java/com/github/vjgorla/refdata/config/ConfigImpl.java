package com.github.vjgorla.refdata.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Vijaya Gorla
 */
public class ConfigImpl extends Config {
    
    private static final Logger LOG = LoggerFactory.getLogger(ConfigImpl.class);
    
    private static final String PROPS_FILE_NAME = "/ref-data-config.properties";
    
    private final Properties props;

    public ConfigImpl() {
        this.props = readConfigFile(PROPS_FILE_NAME);
    }

    @Override
    public String getLoaderImplClass() {
        return getString("ref-data.loader.implementation.class");
    }

    @Override
    public String getJsonDataFile() {
        return getString("ref-data.loader.json.data.file");
    }

    @Override
    public Properties getJpaProperties() {
        return getSubProperties("ref-data.loader.jpa.");
    }

    private Properties getSubProperties(String prefix) {
        Properties subProps = new Properties();
        for (String key : props.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                subProps.setProperty(key.substring(prefix.length()), props.getProperty(key));
            }
        }
        return subProps;
    }

    private String getString(String key) {
        String value = (String)props.get(key);
        if (value == null) {
            throw new IllegalArgumentException("No value found for key [" + key + "] in [" + PROPS_FILE_NAME + "]");
        }
        return value;
    }

    private static Properties readConfigFile(String configFile) {
        LOG.debug("Loading properties from [{}]", configFile);
        InputStream propsStream = null;
        try {
            propsStream = Config.class.getResourceAsStream(configFile);
            if (propsStream == null) {
                throw new IllegalArgumentException("Required property file [" + configFile + "] not found on classpath");
            }
            Properties props = new Properties();
            props.load(propsStream);
            return props;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (propsStream != null) {
                try {
                    propsStream.close();
                } catch (Exception ex) {
                    LOG.debug("Error closing input stream to file [{}]", configFile);
                }
            }
        }
    }
}
