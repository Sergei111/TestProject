package web;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


import java.io.File;
import java.util.Date;
public class WebBaseTest {

    protected PageManager pageManager;
    public static WebDriver driver;

    @Rule
    public FailureTestWatcher testWatcher = new FailureTestWatcher();

    private void logBrowserConsoleLogs() {
        System.out.println("================== BROWSER LOGS =======================");
        LogEntries logEntries = driver.manage().logs().get(LogType.CLIENT);
        for (LogEntry entry : logEntries) {
            System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        }
        System.out.println("=======================================================");
    }

    public class FailureTestWatcher extends TestWatcher {

        protected void failed(Throwable e, Description description) {
            String testName = description.getMethodName();
            logBrowserConsoleLogs();

        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        pageManager = PageManager.getInstance();
        driver = pageManager.getDriver();
        System.out.println("setUp");


    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        pageManager.getApplicationManager().stop();
        pageManager.stop();
        System.out.println("tearDown");
        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                TakesScreenshot ts = (TakesScreenshot) driver;
                File source = ts.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(source, new File("./Screenshots/" + result.getName() + ".png"));

                System.out.println("Screenshot taken");
            } catch (Exception e) {

                System.out.println("Exception while taking screenshot " + e.getMessage());
            }
            logBrowserConsoleLogs();
        }

    }


    protected void loginToSystem() {
      pageManager.getLoginPage().loginToSystem("standard_user","secret_sauce");

    }
}