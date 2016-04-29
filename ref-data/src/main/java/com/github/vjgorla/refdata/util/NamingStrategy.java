package com.github.vjgorla.refdata.util;

/**
 * 
 * @author Vijaya Gorla
 */
public class NamingStrategy {

    private NamingStrategy() {
    }
    
    public static String generateTypeCode(Class<?> valueClass) {
        StringBuilder buf = new StringBuilder( valueClass.getSimpleName().replace('.', '_') );
        for (int i=1; i<buf.length()-1; i++) {
            if (Character.isLowerCase( buf.charAt(i-1) ) 
                    && Character.isUpperCase( buf.charAt(i) ) 
                    && Character.isLowerCase( buf.charAt(i+1) )) {
                    buf.insert(i++, '_');
            }
        }
        return buf.toString().toUpperCase();
    }
}
