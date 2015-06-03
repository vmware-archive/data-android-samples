package io.pivotal.android.data.demo;

import java.io.InputStream;
import java.util.Properties;

public class DataConfig {

    private static Properties sProperties;

    private static final class Keys {
        private static final String SERVICE_URL = "pivotal.data.serviceUrl";
    }

    public static String getServiceUrl() {
        final String value = getProperties().getProperty(Keys.SERVICE_URL);
        return value;
    }

    private static Properties getProperties() {
        if (sProperties == null) {
            sProperties = loadProperties("assets/pivotal.properties");
        }
        return sProperties;
    }

    private static Properties loadProperties(final String path) {
        try {
            final Properties properties = new Properties();
            properties.load(getInputStream(path));
            return properties;
        } catch (final Exception e) {
            return null;
        }
    }

    private static InputStream getInputStream(final String path) {
        final Thread currentThread = Thread.currentThread();
        final ClassLoader loader = currentThread.getContextClassLoader();
        return loader.getResourceAsStream(path);
    }

}