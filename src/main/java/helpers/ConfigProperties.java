package helpers;

import mobile_screens.mobile_configuration.MobilePlatform;
import mobile_screens.mobile_configuration.SauceLabDevice;

import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private static Properties PROPERTIES;
    private static final String TESTS_PROPERTIES = "config.properties";
    private static final String DEVICES_PROPERTIES = "devices.properties";;


    static {
        PROPERTIES = new Properties();
        InputStream configProperties = ConfigProperties.class.getClassLoader().getResourceAsStream(TESTS_PROPERTIES);
        InputStream devicesProperties = ConfigProperties.class.getClassLoader().getResourceAsStream(DEVICES_PROPERTIES);

        try {
            PROPERTIES.load(configProperties);
            PROPERTIES.load(devicesProperties);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        }


    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }

    public static class Environment {
        public static final String ENVIRONMENT = getProperty("env");
    }
    public static class Devices {

        private static final String DEVICE = System.getProperty("sauce_lab_device", SauceLabDevice.IPHONE_ANY.getDevice());
        public static final String DEVICE_NAME = getProperty(DEVICE);

        public static final String MOBILE_PLATFORM = System.getProperty("mobile_platform", MobilePlatform.IOS.getPlatform());
    }

}