package web;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.concurrent.TimeUnit;

import static web.ApplicationManager.EBrowser.Chrome;

public class ApplicationManager {

    private static ApplicationManager instance = null;
    private final RemoteWebDriver driver;
    private final Wait<RemoteWebDriver> wait;

    public void closeTab() {
        driver.close();
    }

    public enum EBrowser {
        Firefox,Safari, Chrome
    }

    protected final String
            BASE_URL = "https://www.saucedemo.com/";

    private ApplicationManager() {
        // config = AppConfig.getInstance();
        driver = getDriver(Chrome);
        //driver.manage().window().setSize(new Dimension(1800, 1900));

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new FluentWait<>(driver)
                .withTimeout(20, TimeUnit.SECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
        driver.get(BASE_URL);

    }

    private RemoteWebDriver getDriver(EBrowser driverType) {

        System.setProperty("webdriver.chrome.driver", "/Users/serhiibyalik/IdeaProjects/QA/src/test/resources/chromedriver");
        //System.setProperty("webdriver.chrome.driver", "/Users/serhiibyalik/QA/src/test/resources/chromedriver");
        RemoteWebDriver dr = new ChromeDriver();
        return dr;
    }
    public static ApplicationManager init() {
        return instance = new ApplicationManager();
    }

    public static ApplicationManager getInstance() {
        return instance;
    }

    public void stop() {
        try {
            driver.close();
            driver.quit();

        } catch (Throwable ex) {
            System.out.println("Driver error preventing from Quitting.");
            ex.printStackTrace();
        }
        instance = null;
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public Wait<RemoteWebDriver> getWait() {
        return wait;
    }



}