package com.astoria.mtldataconvert.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoaderHeaders {
    public static Properties loadProperties() {
        Properties properties = new Properties();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("requestHeader.env"));
            String line;
            while ((line = reader.readLine()) != null) {
                int equalsIndex = line.indexOf('=');
                if (equalsIndex > 0) {
                    String key = line.substring(0, equalsIndex);
                    String value = line.substring(equalsIndex + 1);
                    properties.setProperty(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return properties;
    }
}
