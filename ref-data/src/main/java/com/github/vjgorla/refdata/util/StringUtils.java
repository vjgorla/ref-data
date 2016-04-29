package com.github.vjgorla.refdata.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Vijaya Gorla
 */
public class StringUtils {
    
    private StringUtils() {
    }

    public static Map<String, String> convertToMap(String str, 
        String keyValuePairDelimiter, String keyValueSeparator) {
        
        if (isEmpty(str)) {
            return Collections.emptyMap();
        }
        
        final Map<String, String> map = new HashMap<>();
        for (String keyValuePair : str.split(keyValuePairDelimiter)) {
            String[] keyValueArray = keyValuePair.trim().split(keyValueSeparator);
            if (keyValueArray == null || keyValueArray.length != 2) {
                throw new IllegalArgumentException("String [" + keyValuePair.trim() 
                    + "] can't be split into a key-value pair");
            }
            map.put(keyValueArray[0].trim(), keyValueArray[1].trim());
        }
        
        return map;
    }
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
