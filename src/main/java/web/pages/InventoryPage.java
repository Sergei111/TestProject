package web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

import static java.lang.Thread.sleep;

public class InventoryPage {

    @FindBy(id = "add-to-cart-sauce-labs-backpack")
    private WebElement btnBackpack;

    @FindBy(id = "add-to-cart-sauce-labs-fleece-jacket")
    private WebElement btnFleeceJacket;

    @FindBy(id = "add-to-cart-sauce-labs-onesie")
    private WebElement btnOnesie;

    @FindBy(xpath = "//a[@class='shopping_cart_link']")
    private WebElement btnCart;

    @Step
    public InventoryPage selectProducts(){
        btnOnesie.click();
        btnBackpack.click();
        btnFleeceJacket.click();
        return this;
    }

    @Step
    public InventoryPage goToTheCart(){
        btnCart.click();
        return this;
    }
}