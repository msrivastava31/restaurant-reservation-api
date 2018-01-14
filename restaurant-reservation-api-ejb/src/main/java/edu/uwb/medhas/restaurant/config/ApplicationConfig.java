/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.medhas.restaurant.config;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author medhasrivastava
 */
public class ApplicationConfig {
    private final Properties properties;
    private static ApplicationConfig instance;
    
    private ApplicationConfig() throws IOException {
        properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
    }
    
    public static ApplicationConfig getInstance() throws IOException {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
