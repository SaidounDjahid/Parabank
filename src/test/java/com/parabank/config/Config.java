package com.parabank.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Properties;

public final class Config {

    private static final Properties PROPERTIES = new properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                    "config.properties was noud found");

            }
            PROPERTIES.load(input);
        }
        catch (IOException exception){
            throw new IllegalStateException(
                "unable to read config.properties", exception);
        }
        
    }

private Config (){
}

public static String get(String key) {
    String systemValue = System.getProperty(key);
    
    if (systemValue != null && !systemValue.isBlank()){
        return systemValue.trim();
    }

    String fileValue = properties.getProperty(key); 
        
    if (fileValue == null || fileValue.isBlank()){
        throw new IllegalAccessException(
            "missing configuration value:" + key);
        
    }
    return fileValue.trim();
}

public static int getInt(String key){
    return Integer.parseInt(get(key));
}
public static boolean getBoolean(String key){
    return Boolean.parseBoolean(key);
}

}