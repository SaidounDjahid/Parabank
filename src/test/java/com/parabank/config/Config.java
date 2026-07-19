package com.parabank.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties; 

public final class Config {

    private static final Properties PROPERTIES = new Properties(); // crée un nouvel object vide storé dans PROPERTIES PROPERTIES = {}  
    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException( //
                    "config.properties was noud found");

            }
            PROPERTIES.load(input); 
        }
        catch (IOException exception){ 
            throw new IllegalStateException(
                "unable to read config.properties", exception);
        }
        
    }

    private Config (){ }

    public static String get(String key) {
    String systemValue = System.getProperty(key);
    
    if (systemValue != null && !systemValue.isBlank()){
        return systemValue.trim();
    }

    String fileValue = PROPERTIES.getProperty(key); 
        
    if (fileValue == null || fileValue.isBlank()){
        throw new IllegalStateException(
            "missing configuration value: " + key);
        
    }
    
    return fileValue.trim();
    }



    public static int getInt(String key){
    return Integer.parseInt(get(key));
    }
    public static boolean getBoolean(String key) {
    return Boolean.parseBoolean(get(key));
}


}
