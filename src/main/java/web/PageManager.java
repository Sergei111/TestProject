package web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import helpers.elements.iml.ExtendedFieldDecorator;
import web.pages.*;

public class PageManager {

    private static PageManager instance;
    private static LoginPage loginPage;
    private final ApplicationManager applicationManager;
    private final InventoryPage inventoryPage;
    private final CartPage cartPage;
    private final CheckoutStepTwoPage checkoutStepTwoPage;
    private final CheckoutPage checkoutPage;

    private PageManager() {
        applicationManager =  ApplicationManager.init();
        loginPage = initElements(new LoginPage());
        inventoryPage = initElements(new InventoryPage());
        cartPage = initElements(new CartPage());
        checkoutStepTwoPage = initElements(new CheckoutStepTwoPage());
        checkoutPage = initElements(new CheckoutPage());


    }

    public static PageManager getInstance() {
        if (instance == null) {
            instance = new PageManager();
        }
        return instance;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public CheckoutStepTwoPage getCheckoutStepTwoPage() {
        return checkoutStepTwoPage;
    }

    public CheckoutPage getCheckoutPage() {
        return checkoutPage;
    }

    public WebDriver getDriver() {
        return applicationManager.getDriver();
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public InventoryPage getInventoryPage() {
        return inventoryPage;
    }

    public CartPage getCartPage() {
        return cartPage;
    }

    public  void stop() {
        instance = null;
    }

    private <T> T initElements(T page) {
        PageFactory.initElements(new ExtendedFieldDecorator(applicationManager.getDriver()), page);
        return page;
    }

}
