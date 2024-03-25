package web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class CheckoutPage {

    @FindBy(id = "first-name")
    private WebElement txtFirstName;

    @FindBy(id = "last-name")
    private WebElement txtLastName;

    @FindBy(id = "postal-code")
    private WebElement txtPostalCode;

    @FindBy(id = "continue")
    private WebElement btnContinue;


    @Step
    public CheckoutPage completeCheckout(String name, String lastName, String code){
        txtFirstName.sendKeys(name);
        txtLastName.sendKeys(lastName);
        txtPostalCode.sendKeys(code);
        btnContinue.click();
        return this;
    }
}