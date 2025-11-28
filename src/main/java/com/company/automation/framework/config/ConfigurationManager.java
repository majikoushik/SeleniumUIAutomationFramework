package com.company.automation.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration loader.
 *
 * Order of precedence:
 * 1. System properties (-Dkey=value)
 * 2. Properties file: config/config-<env>.properties
 *
 * env is passed as -Denv=qa (default is "qa").
 */
public final class ConfigurationManager {

    private static final Properties PROPS = new Properties();
    private static final String ENV = System.getProperty("env", "qa");

    static {
        String fileName = String.format("config/config-%s.properties", ENV);
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fileName)) {
            if (is == null) {
                throw new IllegalStateException("Config file not found on classpath: " + fileName);
            }
            PROPS.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + fileName, e);
        }
    }

    private ConfigurationManager() {
        // utility class
    }

    public static String get(String key) {
        // System property overrides file property
        String sys = System.getProperty(key);
        if (sys != null && !sys.isEmpty()) {
            return sys;
        }
        String val = PROPS.getProperty(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return val;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
