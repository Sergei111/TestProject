package web.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

import static java.lang.Thread.sleep;

public class CartPage {
    protected JavascriptExecutor jse;

    @FindBy(id = "remove-sauce-labs-onesie")
    private WebElement btnRemoveOnesie;

    @FindBy(id = "checkout")
    private WebElement btnCheckout;

    @Step
    public CartPage removeOnesie() throws InterruptedException {
        sleep(1000);
        btnRemoveOnesie.click();
        return this;
    }

    @Step
    public CartPage goToTheCheckoutPage(){
        btnCheckout.click();
        return this;
    }
}