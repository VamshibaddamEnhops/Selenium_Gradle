package com.BaseReq;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        // Always load from config.properties first
        loadFromPropertiesFile();

        // If running in Azure DevOps (CI), override specific keys from env
        if ("true".equalsIgnoreCase(System.getenv("CI"))) {
            overrideFromEnv();
        }
    }

    private static void overrideFromEnv() {
        // Only override sensitive values, not URLs like OpenCart or TempMail
        overrideIfSet("apiToken", "API_TOKEN");
        overrideIfSet("jiraURL", "JIRA_URL");
        overrideIfSet("username", "JIRA_USERNAME");
        overrideIfSet("testRailEngineURL", "TEST_RAIL_ENGINE_URL");
        overrideIfSet("testRunID", "TEST_RUN_ID");
        overrideIfSet("testRailUsername", "TEST_RAIL_USERNAME");
        overrideIfSet("testRailPassword", "TEST_RAIL_PASSWORD");
        // DO NOT override OpenCartAPPURL, tempmailurl, etc.
    }

    private static void overrideIfSet(String propertyKey, String envVarKey) {
        String value = System.getenv(envVarKey);
        if (value != null && !value.isEmpty()) {
            properties.setProperty(propertyKey, value);
        }
    }

    private static void loadFromPropertiesFile() {
        try (FileInputStream inputStream = new FileInputStream("src/test/resources/config/config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
