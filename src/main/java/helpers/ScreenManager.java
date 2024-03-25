package helpers;

import io.appium.java_client.AppiumDriver;
import mobile_screens.BaseScreen;
import mobile_screens.LoginScreen;

public class ScreenManager {

    private AppiumDriver driver;
    private BaseScreen baseScreen;
    private LoginScreen loginScreen;


    public ScreenManager(AppiumDriver driver) {
        this.driver = driver;
    }

    public BaseScreen getBaseScreen() {
        if (baseScreen == null) {
            baseScreen = new BaseScreen(driver);
        }
        return baseScreen;
    }

    public LoginScreen getLoginScreen() {
        if (loginScreen == null) {
            loginScreen = new LoginScreen(driver);
        }
        return loginScreen;

    }

}