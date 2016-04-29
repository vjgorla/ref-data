package com.github.vjgorla.refdata.loader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.vjgorla.refdata.config.Config;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;
import com.github.vjgorla.refdata.loader.RefDataLoader;

/**
 * 
 * @author Vijaya Gorla
 */
public class JsonRefDataLoader extends RefDataLoader {
    
    private static final Logger LOG = LoggerFactory.getLogger(JsonRefDataLoader.class);
    
    private HashMap<String, Set<RefDataValueEntity>> refData;
    private volatile boolean initialized = false;
    
    private void readJsonData() {
        String jsonDataFilePath = Config.getInstance().getJsonDataFile();
        LOG.debug("Loading json data from [{}]", jsonDataFilePath);
        InputStream jsonDataStream = null;
        try {
            jsonDataStream = this.getClass().getResourceAsStream(jsonDataFilePath);
            if (jsonDataStream == null) {
                throw new IllegalArgumentException("Required json data file [" + jsonDataFilePath + "] not found on classpath");
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(JsonMethod.ALL, Visibility.NONE);
            mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
            refData = mapper.readValue(jsonDataStream, new TypeReference<HashMap<String, Set<RefDataValueEntity>>>(){});
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (jsonDataStream != null) {
                try {
                    jsonDataStream.close();
                } catch (Exception ex) {
                    LOG.debug("Error closing input stream to file [{}]", jsonDataFilePath);
                }
            }
        }
    }
    
    private void intializeIfRequired() {
        if (!initialized) {
            readJsonData();
            initialized = true;
        }
    }

    @Override
    public Set<RefDataValueEntity> loadEntities(String typeCode) {
        intializeIfRequired();
        Set<RefDataValueEntity> valueEntities = refData.get(typeCode);
        if (valueEntities == null) {
            throw new IllegalArgumentException("No values found for code type [" + typeCode + "]");
        }
        return valueEntities;
    }
}
