package mobile_screens;

import com.sun.istack.internal.NotNull;
import helpers.Logger;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidBy;
import io.appium.java_client.pagefactory.AndroidFindAll;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class LoginScreen extends BaseScreen {

    public LoginScreen(AppiumDriver driver) {
        //initPageObject
        super(driver);
    }

    @AndroidFindBy(xpath = "//android.widget.ScrollView/android.widget.EditText[1]")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeSecureTextField[1]")
    MobileElement createPasswordInput;

    @AndroidFindBy(xpath = "//android.widget.ScrollView/android.widget.EditText[2]")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeSecureTextField[2]")
    MobileElement reEnterCreatePasswordInput;

    @AndroidFindBy(xpath = "//*[@text='Continue']")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeButton[`label == \"Continue\"`]")
    MobileElement continueBtn;

    @AndroidFindBy(xpath = "//*[@text='Okay']")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeButton[`label == \"Okay\"`]")
    MobileElement okayBtn;

    @AndroidFindAll({
            @AndroidBy(xpath = "//*[@text='Invite to use HealthJoy']"),
            @AndroidBy(xpath = "//*[contains(@text, 'An invite has been sent to')]")
    })
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeStaticText[`label == \"Invite to use HealthJoy\" OR label contains \"An invite has been sent to\"`][1]")
    MobileElement inviteButton;

    @Step
    public LoginScreen tapOnInviteToUse() {
        Logger.info("Tap on invite to use button");
        waitAndClick(inviteButton);
        return this;
    }


    @Step
    public LoginScreen tapOnContinueButton() {
        Logger.info("Tap on continue button");
        verticalSwipeUp();
        waitAndClick(continueBtn);
        return this;
    }

    @Step
    @Override
    public LoginScreen validateTextOnScreen(@NotNull String expectedText) {
        return (LoginScreen) super.validateTextOnScreen(expectedText);
    }

    @Step
    @Override
    public LoginScreen validateTextOnScreen(@NotNull String... expectedText) {
        return (LoginScreen) super.validateTextOnScreen(expectedText);
    }

}