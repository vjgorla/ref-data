package com.github.vjgorla.refdata.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.ejb.Ejb3Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.vjgorla.refdata.config.Config;
import com.github.vjgorla.refdata.entity.RefDataTypeEntity;
import com.github.vjgorla.refdata.entity.RefDataValueEntity;

public class RefDataJpaToJsonWriter {

    private static final Logger LOG = LoggerFactory.getLogger(RefDataJpaToJsonWriter.class);
    
    private static final String OUTPUT_FILE_PATH = "/temp/data.json";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    
    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
        HashMap<String, Set<RefDataValueEntity>> data = new HashMap<>();
        for (RefDataTypeEntity typeEntity : getTypes()) {
            LOG.debug("Loading type [{}]", typeEntity.getTypeCode());
            data.put(typeEntity.getTypeCode(), typeEntity.getValues());
        }
        write(data);
    }
    
    public static void write(HashMap<String, Set<RefDataValueEntity>> data) throws JsonGenerationException, JsonMappingException, IOException {
        LOG.debug("Writing data to json file [{}]", OUTPUT_FILE_PATH);
        OutputStream jsonDataStream = null;
        try {
            jsonDataStream = new FileOutputStream(new File(OUTPUT_FILE_PATH));
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(JsonMethod.ALL, Visibility.NONE);
            mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            mapper.getSerializationConfig().addMixInAnnotations(RefDataValueEntity.class, RefDataValueEntityMixIn.class);
            mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
            mapper.writeValue(jsonDataStream, data);
        } finally {
            if (jsonDataStream != null) {
                try {
                    jsonDataStream.close();
                } catch (Exception ex) {
                    LOG.debug("Error closing output stream to file [{}]", OUTPUT_FILE_PATH);
                }
            }
        }
    }
    
    private static EntityManagerFactory buildEntityManagerFactory() {
        Properties properties = Config.getInstance().getJpaProperties();
        Ejb3Configuration cfg = new Ejb3Configuration();
        cfg.configure(properties);
        cfg.addAnnotatedClass(RefDataTypeEntity.class);
        cfg.addAnnotatedClass(RefDataValueEntity.class);
        return cfg.createEntityManagerFactory();
    }

    private static List<RefDataTypeEntity> getTypes() {
        EntityManager em = buildEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("FROM RefDataTypeEntity", RefDataTypeEntity.class).getResultList();
        } finally {
            em.clear();
            em.close();
        }
    }
    
    abstract class RefDataValueEntityMixIn extends RefDataValueEntity {
        @JsonIgnore
        private long id;
        @JsonIgnore
        private Map<String, String> attributeMap;
    }
}
