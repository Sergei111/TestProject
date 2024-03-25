package helpers;

import io.appium.java_client.AppiumDriver;

public class ThreadLocalDriver {
    private static ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();

    public static void setTLDriver(AppiumDriver driver) {
        tlDriver.set(driver);
    }

    public static AppiumDriver getTLDriver() {
        return tlDriver.get();
    }

    public static void removeTLDriver() {
        tlDriver.remove();
    }
}