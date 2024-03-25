package mobile_screens;

import com.sun.istack.internal.NotNull;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.*;
import io.appium.java_client.remote.HideKeyboardStrategy;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Step;
import helpers.Logger;
import helpers.ThreadLocalDriver;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.io.ByteStreams.toByteArray;
import static helpers.ScreenHolder.getScreenHolder;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertTrue;
public class BaseScreen {

    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait waitForChat;
    protected TouchAction touchAction;

    public BaseScreen(AppiumDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 20);
        waitForChat = new WebDriverWait(driver, 30);
        touchAction = new TouchAction(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(xpath = "//android.widget.ProgressBar")
    @iOSXCUITFindBy(accessibility = "loaderView")
    private MobileElement loaderView;

    @AndroidFindBy(xpath = "//*[@text='Not now' or @text='No, thanks']")
    private MobileElement dismissPasswordSaveAndroid;

    @AndroidFindAll({
            @AndroidBy(id = "android:id/big_text"),
            @AndroidBy(id = "android:id/text")
    })
    private List<MobileElement> notificationsText;

    @AndroidFindBy(xpath = "//*[@text='SKIP']")
    @iOSXCUITFindBy(accessibility = "SKIP")
    private MobileElement btnSkipNotificationsPopup;

    @AndroidFindBy(xpath = "//*[@text='TURN ON NOTIFICATIONS']")
    @iOSXCUITFindBy(accessibility = "TURN ON NOTIFICATIONS")
    private MobileElement btnTurnOnNotificationsPopup;

    @AndroidFindAll({
            @AndroidBy(xpath = "//android.webkit.WebView"),
            @AndroidBy(xpath = "//androidx.compose.ui.platform.ComposeView")
    })
    private MobileElement androidWebView;

    @AndroidFindAll({
            @AndroidBy(xpath = "//*[@text='Take picture']"),//10
            @AndroidBy(xpath = "//*[@content-desc='Take picture']"),//11
            @AndroidBy(xpath = "//*[@text='Shutter']")//9
    })
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeButton' and name == 'PhotoCapture'")
    private MobileElement takePhoto;

    @iOSXCUITFindBy(accessibility = "Use Photo")
    @AndroidFindBy(xpath = "//*[contains(@resource-id,'okay')]")
    private MobileElement confirmPhoto;

    @iOSXCUITFindBy(iOSNsPredicate = "name == 'BigCloseIcon'")
    @AndroidFindBy(xpath = "//*[contains(@resource-id,'composeView')]//android.widget.Button")
    private MobileElement closeProfilePopup;

    @AndroidFindBy(id = "tvSourceChooserCamera")
    private MobileElement cameraOption;


    @Step
    public BaseScreen tryToTurnOnNotificationsPopup() {
        Logger.info("Trying to turn ON notifications popup!");
        if (isPlatformIOS()) {
            try {
                waitForLoader();
                waitForElement(btnTurnOnNotificationsPopup, 5);
                waitAndClick(btnTurnOnNotificationsPopup);
                safeAlertAcceptIfAvailable();
            } catch (TimeoutException e) {
                Logger.info("There was no notifications popup!");
                Logger.error(e.getMessage());
            }
        } else {
            safeAlertAcceptIfAvailable();
        }
        return this;
    }

    @Step
    public BaseScreen tryToSkipNotificationsPopup() {
        if (isPlatformIOS()) {
            try {
                waitForLoader();
                Logger.info("Trying to SKIP notifications popup!");
                waitForElement(btnSkipNotificationsPopup, 5);
                waitAndClick(btnSkipNotificationsPopup);
                waitAndClick(closeProfilePopup);
            } catch (TimeoutException e) {
                Logger.info("There was no notifications or close profile popup!");
                Logger.error(e.getMessage());
            }
        } else {
            safeAlertDismissIfAvailable();
            try {
                waitAndClick(closeProfilePopup);
            } catch (TimeoutException e) {
                Logger.info("There was no close profile popup!");
                Logger.error(e.getMessage());
            }
        }
        return this;
    }

    public boolean isPlatformIOS() {
        return driver.getPlatformName().equalsIgnoreCase("ios");
    }

    public BaseScreen setContextToWebview() {
        Logger.info("Set context to webview");
        AtomicReference<Set<String>> handles = new AtomicReference<>();
        await().atMost(30, TimeUnit.SECONDS)
                .until(() -> {

                    handles.set(driver.getContextHandles());

                    return handles.get() != null && handles.get().size() > 1;
                });

        Set<String> availableContexts = handles.get();
        Logger.info("Available contexts -> " + availableContexts.size());
        availableContexts.stream()
                .filter(context -> context.toLowerCase().contains("webview"))
                .forEach(newcontext -> driver.context(newcontext));
        return this;
    }

    public BaseScreen setContextToNative() {
        Logger.info("Set context to native app");
        driver.context("NATIVE_APP");
        return this;
    }

    public void waitAndClick(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by)).click();
    }

    public void waitAndClickOnWebView(By by) {
        setContextToWebview();
        waitAndClick(by);
        setContextToNative();
    }

    public void waitAndClick(MobileElement toClick) {
        wait.until(ExpectedConditions.visibilityOf(toClick)).click();
    }

    public void waitAndClickWithLoaderWait(MobileElement toClick) {
        waitAndClick(toClick);
        waitForLoader();
    }

    public void waitAndClickWithLoaderWait(By by) {
        waitAndClick(by);
        waitForLoader();
    }

    public void waitForElement(MobileElement toClick) {
        wait.until(ExpectedConditions.visibilityOf(toClick));
    }

    protected void waitForElement(MobileElement element, int seconds) {
        new WebDriverWait(driver, seconds).until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElement(By by, int seconds) {
        new WebDriverWait(driver, seconds).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void waitForElementToBeInvisible(MobileElement element) {
        for (int i = 0; i < 10; i++) {
            boolean elementDisplayed = isElementDisplayed(element);
            if (elementDisplayed) {
                sleep(1000);
            } else if (i == 9) {
                Logger.info("Element " + element.toString() + " should became invisible but he's not");
            } else {
                break;
            }
        }
    }

    public void waitForLoader() {
        Logger.info("Wait for loader");
        waitForElementToBeInvisible(loaderView);
    }

    protected void hideKeyboard() {
        Logger.info("Hide keyboard");
        if (isPlatformIOS()) {
            ((IOSDriver) driver).hideKeyboard(HideKeyboardStrategy.TAP_OUTSIDE);
        } else {
            driver.hideKeyboard();
        }
    }

    protected boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementDisplayed(MobileElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected List<WebElement> waitAndFindElements(By by) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    protected WebElement waitAndFindElement(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement waitAndFindElement(MobileElement toWaitFor) {
        return wait.until(ExpectedConditions.visibilityOf(toWaitFor));
    }

    protected void waitForText(MobileElement toWaitFor, String textToBePresent) {
        wait.until(ExpectedConditions.textToBePresentInElement(toWaitFor, textToBePresent));
    }

    protected void waitForValue(MobileElement toWaitFor, String textToBePresentInValue) {
        wait.until(ExpectedConditions.textToBePresentInElementValue(toWaitFor, textToBePresentInValue));
    }

    protected void waitForElementNameToBe(MobileElement toWaitFor, String nameText) {
        wait.until(ExpectedConditions.attributeToBe(toWaitFor, "name", nameText));
    }

    protected void waitForElementToBeEnabled(MobileElement toWaitFor) {
        wait.until(ExpectedConditions.attributeToBe(toWaitFor, "enabled", "true"));
    }

    protected void waitForElementToBeEnabled(By toWaitFor) {
        wait.until(ExpectedConditions.attributeToBe(toWaitFor, "enabled", "true"));
    }

    protected String getAttributeValue(MobileElement element, String attribute) {
        waitForElement(element);
        return element.getAttribute(attribute);
    }

    protected String getText(By by) {
        return waitAndFindElement(by).getText();
    }

    protected String getText(MobileElement toGetText) {
        return waitAndFindElement(toGetText).getText();
    }

    protected void sendText(By by, String text) {
        waitAndFindElement(by).sendKeys(text);
    }

    protected void sendText(MobileElement toSendText, String text) {
        waitAndFindElement(toSendText).sendKeys(text);
    }

    protected void clear(By by) {
        waitAndFindElement(by).clear();
    }

    protected void clear(MobileElement toClear) {
        waitAndFindElement(toClear).clear();
    }

    protected void waitVisibility(MobileElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public boolean textAvailable(String text, String message) {
        boolean res = false;
        try {
            if (isPlatformIOS()) {
                text = text.replaceAll("'", "\\\\'");
                String predicateString = "value contains '" + text + "' or label contains '" + text + "' or name contains '" + text + "'";
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(MobileBy.iOSNsPredicateString(predicateString), 0));
            } else {
                String xpath1 = "//*[contains(@value,\"" + text + "\")]";
                String xpath2 = "//*[contains(@label,\"" + text + "\")]";
                String xpath3 = "//*[contains(@name,\"" + text + "\")]";
                String xpath4 = "//*[contains(text(),\"" + text + "\")]";
                String xpath5 = "//*[contains(@text,\"" + text + "\")]";
                String finalXPath = xpath1 + " | " + xpath2 + " | " + xpath3 + " | " + xpath4 + " | " + xpath5;
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath(finalXPath), 0));
            }
            Logger.info(message + ". Text is found: " + text);
            res = true;
        } catch (Exception e) {
            Logger.info(message + ". Text not found: " + text);
            Logger.info(e.toString());
        }
        return (res);
    }

    /**
     * @param text should be in lowercase
     */
    public boolean textAvailableCaseInsensitive(String text, String message) {
        boolean res = false;
        try {
            if (isPlatformIOS()) {
                text = text.replaceAll("'", "\\\\'");
                String predicateString = "value contains[c] '" + text + "' or label contains[c] '" + text + "' or name contains[c] '" + text + "'";
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(MobileBy.iOSNsPredicateString(predicateString), 0));
            } else {
                String xpath1 = "//*[contains(translate(@value, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \"" + text + "\")]";
                String xpath2 = "//*[contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \"" + text + "\")]";
                String xpath3 = "//*[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \"" + text + "\")]";
                String xpath4 = "//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \"" + text + "\")]";
                String finalXPath = xpath1 + " | " + xpath2 + " | " + xpath3 + " | " + xpath4;
                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath(finalXPath), 0));
            }
            Logger.info(message + ". Text is found: " + text);
            res = true;
        } catch (Exception e) {
            Logger.info(message + ". Text not found: " + text);
            Logger.info(e.toString());
        }
        return (res);
    }


    public boolean textNotAvailable(String text, String message) {
        boolean res = false;
        try {
            if (isPlatformIOS()) {
                text = text.replaceAll("'", "\\\\'");
                String predicateString = "value contains '" + text + "' or label contains '" + text + "' or name contains '" + text + "'";
                wait.until(ExpectedConditions.numberOfElementsToBe(MobileBy.iOSNsPredicateString(predicateString), 0));
            } else {
                String xpath1 = "//*[contains(@value,\"" + text + "\")]";
                String xpath2 = "//*[contains(@label,\"" + text + "\")]";
                String xpath3 = "//*[contains(@name,\"" + text + "\")]";
                String xpath4 = "//*[contains(text(),\"" + text + "\")]";
                String xpath5 = "//*[contains(@text,\"" + text + "\")]";
                String finalXPath = xpath1 + " | " + xpath2 + " | " + xpath3 + " | " + xpath4 + " | " + xpath5;
                wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(finalXPath), 0));
            }
            Logger.info(message + ". Text is absent: " + text);
            res = true;
        } catch (Exception e) {
            Logger.info(message + ". Text found, but should be absent: " + text);
            Logger.info(e.toString());
        }
        return (res);
    }

    public BaseScreen validateTextOnScreen(@NotNull String expectedText) {
        assertTrue(getScreenHolder().getBaseScreen().textAvailable(expectedText, "Text expected to be available on UI"), "Text -> \"" + expectedText + "\" <- IS ABSENT");
        return this;
    }

    public BaseScreen validateTextOnScreenCaseInsensitive(@NotNull String expectedText) {
        assertTrue(getScreenHolder().getBaseScreen().textAvailableCaseInsensitive(expectedText, "Case insensitive text expected to be available on UI"), "Text -> \"" + expectedText + "\" <- IS ABSENT");
        return this;
    }

    @Step
    public BaseScreen validateTextOnScreen(@NotNull String... expectedText) {
        for (String text : expectedText) {
            validateTextOnScreen(text);
        }
        return this;
    }

    @Step
    public BaseScreen validateTextOnScreenDependsOnMobilePlatform(String forIos, String forAndroid) {
        if (isPlatformIOS()) {
            validateTextOnScreen(forIos);
        } else {
            validateTextOnScreen(forAndroid);
        }
        return this;
    }

    @Step
    public BaseScreen sleep(long timeMillis) {
        Logger.info("Wait for -> " + (double) timeMillis / 1000 + " second(s)");
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            Logger.info(e.getMessage());
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public BaseScreen verticalSwipeUp() {
        Logger.info("Vertical swipe up");
        verticalSwipeUpFromTo(0.8, 0.2);
        return this;
    }

    public BaseScreen verticalSwipeDown() {
        Logger.info("Vertical swipe down");
        verticalSwipeUpFromTo(0.2, 0.8);
        return this;
    }

    public boolean verticalSwipeUpUntilVisibilityOfElement(MobileElement element, double from, double to) {
        int count = 0;
        boolean isFound = false;

        verticalSwipeUpFromTo(from, to);
        do {
            count++;
            if (isPlatformIOS()) {
                isFound = Boolean.parseBoolean(element.getAttribute("visible"));
            } else {
                isFound = isElementDisplayed(element);
            }

            if (isFound) {
                Logger.info("Element is displayed!");
                return isFound;
            } else {
                verticalSwipeUpFromTo(from, to);
                Logger.info("Element is not displayed!");
            }
        } while (count < 5);
        Logger.info("Element is not displayed after all attempts!");
        return isFound;
    }

    public BaseScreen verticalSwipeUpFromTo(double from, double to) {
        waitForLoader();
        verticalSwipeUpFromToWithoutLoaderWait(from, to);
        return this;
    }

    public BaseScreen verticalSwipeUpFromToWithoutLoaderWait(double from, double to) {
        sleep(500);
        Dimension dimensions = driver.manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * from;
        int heightStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * to;
        int heightEnd = screenHeightEnd.intValue();
        int widthCenter = dimensions.getWidth() / 2;
        sleep(500);
        return this;
    }

    //use on iOS 15 webviews if regular tap is not working, IOS ONLY METHOD
    public void tapOnElementByCoordinatesScript(MobileElement element) {
        Map<String, Object> params = new HashMap<>();
        params.put("x", element.getCenter().getX());
        params.put("y", element.getCenter().getY());
        driver.executeScript("mobile_screens: tap", params);
    }

    //use for webviews
    public BaseScreen swipeUp() {
        Logger.info("Swipe screen up");
        Map<String, Object> params = new HashMap<>();
        params.put("direction", "up");
        if (isPlatformIOS()) {
            params.put("velocity", 1000);
            driver.executeScript("mobile_screens: swipe", params);
        } else {
            params.put("percent", 0.75);
            params.put("left", androidWebView.getLocation().getX());
            params.put("top", androidWebView.getLocation().getY());
            params.put("width", androidWebView.getSize().getWidth());
            params.put("height", androidWebView.getSize().getHeight());
            driver.executeScript("mobile_screens: swipeGesture", params);
        }
        return this;
    }

    //use for webviews
    public BaseScreen swipeDown() {
        Logger.info("Swipe screen down");
        Map<String, Object> params = new HashMap<>();
        params.put("direction", "down");
        if (isPlatformIOS()) {
            params.put("velocity", 100);
            driver.executeScript("mobile_screens: swipe", params);
        } else {
            params.put("percent", 0.75);
            params.put("left", androidWebView.getLocation().getX());
            params.put("top", androidWebView.getLocation().getY());
            params.put("width", androidWebView.getSize().getWidth());
            params.put("height", androidWebView.getSize().getHeight());
            driver.executeScript("mobile_screens: swipeGesture", params);
        }
        return this;
    }

    public BaseScreen swipeLeftElement(MobileElement element) {
        Logger.info("Swipe element left");
        Map<String, Object> params = new HashMap<>();
        params.put("direction", "left");
        if (isPlatformIOS()) {
            params.put("velocity", 1000);
            params.put("element", element.getId());
            driver.executeScript("mobile_screens: swipe", params);
        } else {
            params.put("percent", 0.75);
            params.put("speed", 2500);
            params.put("left", element.getLocation().getX());
            params.put("top", element.getLocation().getY());
            params.put("width", element.getSize().getWidth());
            params.put("height", element.getSize().getHeight());
            driver.executeScript("mobile_screens: swipeGesture", params);
        }
        return this;
    }

    public BaseScreen swipeRightElement(MobileElement element) {
        Logger.info("Swipe element right");
        Map<String, Object> params = new HashMap<>();
        params.put("direction", "right");
        if (isPlatformIOS()) {
            params.put("velocity", 1000);
            params.put("element", element.getId());
            driver.executeScript("mobile_screens: swipe", params);
        } else {
            params.put("percent", 0.75);
            params.put("speed", 2500);
            params.put("left", element.getLocation().getX());
            params.put("top", element.getLocation().getY());
            params.put("width", element.getSize().getWidth());
            params.put("height", element.getSize().getHeight());
            driver.executeScript("mobile_screens: swipeGesture", params);
        }
        return this;
    }

    public void safeAlertAcceptIfAvailable() {
        try {
            Alert alert = (new WebDriverWait(driver, 5))
                    .until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            Logger.info("Alert accepted -> " + alert.getText());
        } catch (Exception e) {
            //no alert, do nothing
        }
    }

    public void safeAlertDismissIfAvailable() {
        try {
            Alert alert = (new WebDriverWait(driver, 5))
                    .until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
            Logger.info("Alert accepted -> " + alert.getText());
        } catch (Exception e) {
            //no alert, do nothing
        }
    }

    public void dismissPasswordSaveOnAndroid() {
        if (isElementDisplayed(dismissPasswordSaveAndroid)) {
            waitAndClick(dismissPasswordSaveAndroid);
        }
    }


    @Step
    public BaseScreen hideAppForSeconds(int seconds) {
        Logger.info("Hide app for -> " + seconds + "s");
        driver.runAppInBackground(Duration.ofSeconds(seconds));
        return this;
    }


    @Step
    public BaseScreen openNotifications() {
        ((AndroidDriver) driver).openNotifications();
        return this;
    }

    @Step
    public BaseScreen waitForPushNotificationAndTapOnIt(String pushText, int seconds) {
        int count = 0;
        do {
            count++;
            Logger.info("Notifications size  -> " + notificationsText.size());

            MobileElement notificationElement = notificationsText.stream().filter(p -> p.getText().contains(pushText)).findFirst().orElse(null);
            if (notificationElement != null) {
                Logger.info("Push notification was found, click on IT!");
                notificationElement.click();
                return this;
            }
            if (count == seconds) {
                Assert.fail("Push notification was never received!");
            }
            Logger.info("Push notification is not received yet...");
            sleep(1000);

        } while (count < seconds);
        return this;
    }

    public BaseScreen selectValueFromList(String valueToSelect) {
        if (isPlatformIOS()) {
            By by = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == '" + valueToSelect + "'");
            waitAndClick(by);
        } else {
            By by = MobileBy.xpath("//android.widget.TextView[@text='" + valueToSelect + "']");
            waitAndClick(by);
        }
        return this;
    }

    //can be issues with using on Android(if do not keep activities mode enabled)
    @Step
    public BaseScreen makePhoto() {
        Logger.info("Make photo");
        if (!isPlatformIOS()) {
            waitAndClick(cameraOption);
            safeAlertAcceptIfAvailable();
            safeAlertAcceptIfAvailable();
        }
        waitAndClick(takePhoto);
        waitAndClickWithLoaderWait(confirmPhoto);
        return this;
    }

    @Step
    private void injectImage(File imageFile) {
        Logger.info("Inject image file to camera");//now works only on iOS
        try {
            waitForElement(takePhoto);
            FileInputStream in = new FileInputStream(imageFile);
            driver.executeScript("sauce:inject-image=" + Base64.getEncoder().encodeToString(toByteArray(in)));
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }

    @Step
    public void sendTestRequestToKeepSessionAlive() {
        Logger.info("Send keep alive request");
        new WebDriverWait(driver, 1)
                .until(ExpectedConditions
                        .numberOfElementsToBeMoreThan(MobileBy.AccessibilityId("SomeTestRequestToKeepAlive"), 0));}

    @Step
    public String getPlatformVersion() {
        return ThreadLocalDriver.getTLDriver().getCapabilities().getCapability("platformVersion").toString();
    }

}