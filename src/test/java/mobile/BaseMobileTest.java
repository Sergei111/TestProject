package mobile;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.codec.language.bm.Languages;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Issue;
import services.SauceLabsServiceApi;
import helpers.*;
import io.appium.java_client.AppiumDriver;
import listeners.CommonTestNGListener;
import mobile_screens.BaseScreen;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Listeners(CommonTestNGListener.class)
public class BaseMobileTest implements IBaseMethods {

    protected SoftAssert softAssert;
    //https://docs.saucelabs.com/mobile-apps/automated-testing/appium/appium-2-migration/
    //https://docs.saucelabs.com/mobile-apps/automated-testing/appium/appium-versions/#real-devices
    private String appiumVersion = "appium2-20230901";
    private ScheduledExecutorService ses;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        Logger.info("TestNG Before Class");

    }

    @BeforeMethod(alwaysRun = true)
    public void setup(Method method) {
        if (method != null) {
            Initializations.testCaseName.set(method.getAnnotation(Test.class).description());
        }
        Logger.info("TestNG Before Method, test case name -> " + Initializations.testCaseName.get());
        Initializations.language.set(Languages.ANY_LANGUAGE);
        softAssert = new SoftAssert();
        if (method == null || method.getAnnotation(Issue.class) == null) {//do not create SL session if test have known issue
            if (isMobilePlatformIOS()) {
                setupIOSDriver();
            } else if (!isMobilePlatformIOS()) {
                setupAndroidDriver();
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    public void teardown(ITestResult result) {
        Logger.info("TestNG Tear Down - AfterMethod");
        try {
            Logger.info("TestNG Tear Down - set test result on SauceLab");
            ThreadLocalDriver.getTLDriver().executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        try {
            Logger.info("TestNG Tear Down - driver quit");
            ThreadLocalDriver.getTLDriver().quit();
        } catch (Exception e) {
            Logger.info("TestNG Tear Down - catch block");
            Logger.error(e.getMessage());
            Logger.error(e.getStackTrace());
        }
        Logger.info("TestNG Tear Down - remove driver");
        ThreadLocalDriver.removeTLDriver();
        Logger.info("Cancel background task");
        if (ses != null) {
            ses.shutdown();
        }
    }

    @AfterSuite(alwaysRun = true, description = "Delete created companies and users")
    public void deleteCompanies() {
        Logger.info("TestNG AfterSuite");

    }

    private static String getRandomiOSPlatformVersion() {
        String[] versions = {"15.", "16.", "17."};
        int randomNumber = RandomUtils.nextInt(0, versions.length);
        String iOSVersion = versions[randomNumber];
        Logger.info("Current iOS version -> " + iOSVersion);
        return iOSVersion;
    }

    private void setupIOSDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("appium:deviceName", ConfigProperties.Devices.DEVICE_NAME);
        caps.setCapability("appium:platformVersion", getRandomiOSPlatformVersion());
        caps.setCapability("platformName", "iOS");
        caps.setCapability("browserName", "Safari");
        caps.setCapability("appium:automationName", "XCUITest");
        caps.setCapability("appium:newCommandTimeout", 90);
        caps.setCapability("appium:idleTimeout", 90);
        caps.setCapability("appium:autoAcceptAlerts", true);
        caps.setCapability("appium:waitForIdleTimeout", 20);
        caps.setCapability("appium:commandTimeouts", 90000);
        caps.setCapability("appium:boundElementsByIndex", true); //need to avoid stale element exception in some cases when UI changes with high speed
        caps.setCapability("appium:simpleIsVisibleCheck", true);

        HashMap<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("appiumVersion", appiumVersion);
        sauceOptions.put("sauceLabsImageInjectionEnabled", true);
        sauceOptions.put("testobject_session_creation_timeout", "900000");
        caps.setCapability("sauce:options", sauceOptions);


        for (int i = 0; i < 3; i++) {
            try {
                ScreenHolder.setScreenHolder(new ScreenManager(ThreadLocalDriver.getTLDriver()));
                ses = sendKeepAliveRequestInBackground();
                break;
            } catch (WebDriverException e) {
                Logger.info("TestNG Before Method - catch block");
                Logger.error(e.getMessage());
                Logger.error(e.getStackTrace());
            }
        }
    }

    private String getRandomAndroidPlatformVersion() {
        String[] versions = {"9", "10", "11", "12", "13"};
        int randomNumber = RandomUtils.nextInt(0, versions.length);
        String androidVersion = versions[randomNumber];
        Logger.info("Current Android version -> " + androidVersion);
        return androidVersion;
    }

    private void setupAndroidDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("appium:platformVersion", getRandomAndroidPlatformVersion());
        caps.setCapability("platformName", "Android");
        caps.setCapability("browserName", "");
        caps.setCapability("appium:automationName", "UIAutomator2");
        caps.setCapability("appium:newCommandTimeout", 90);
        caps.setCapability("appium:idleTimeout", 90);
        caps.setCapability("appium:autoAcceptAlerts", true);
        caps.setCapability("appium:waitForIdleTimeout", 20);
        caps.setCapability("appium:commandTimeouts", 90000);
        caps.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
        caps.setCapability("appium:waitForAppScript", "$.delay(8000); $.acceptAlert();");
        caps.setCapability("appium:enforceXPath1", true);

        HashMap<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("appiumVersion", appiumVersion);
        sauceOptions.put("sauceLabsImageInjectionEnabled", true);
        sauceOptions.put("testobject_session_creation_timeout", "900000");
        caps.setCapability("sauce:options", sauceOptions);


        //3 tries to find available device, each try - 5 minutes
        for (int i = 0; i < 3; i++) {
            try {
                ScreenHolder.setScreenHolder(new ScreenManager(ThreadLocalDriver.getTLDriver()));
                ses = sendKeepAliveRequestInBackground();
                break;
            } catch (WebDriverException e) {
                Logger.info("TestNG Before Method - catch block");
                Logger.error(e.getMessage());
                Logger.error(e.getStackTrace());
            }
        }
    }


    public boolean isProdRun() {
        return ConfigProperties.Environment.ENVIRONMENT.equals("production");
    }

    public static boolean isNotLocalEnv() {
        return ConfigProperties.Environment.ENVIRONMENT.equals("hj01-dev") ||
                ConfigProperties.Environment.ENVIRONMENT.equals("hj01-stage") ||
                ConfigProperties.Environment.ENVIRONMENT.equals("production");
    }

    public String getPlatformVersion() {
        return ThreadLocalDriver.getTLDriver().getCapabilities().getCapability("platformVersion").toString();
    }

    //allow us to keep session alive when we wait for some action which may take more 90sec
    public ScheduledExecutorService sendKeepAliveRequestInBackground() {
        AppiumDriver driver = ThreadLocalDriver.getTLDriver();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> new BaseScreen(driver).sendTestRequestToKeepSessionAlive();

        executor.scheduleAtFixedRate(task, 60, 60, TimeUnit.SECONDS);
        return executor;

    }
    public boolean isMobilePlatformIOS() {
        if (ConfigProperties.Devices.MOBILE_PLATFORM.equalsIgnoreCase(MobilePlatform.IOS)) {
            return true;
        } else if (ConfigProperties.Devices.MOBILE_PLATFORM.equalsIgnoreCase(MobilePlatform.ANDROID)) {
            return false;
        } else {
            Assert.fail("Unexpected mobile platform value provided");
            return false;
        }
    }
}



