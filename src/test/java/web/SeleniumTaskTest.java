package web;

import org.testng.annotations.Test;
import helpers.DateTimeHelper;
import static org.testng.Assert.assertEquals;


public class SeleniumTaskTest extends WebBaseTest {


    @Test
    public void checkSystem() throws InterruptedException {
        long timestamp = DateTimeHelper.getTimestamp();
        String name = "name" + timestamp;
        String lastName = "lastName" + timestamp;
        loginToSystem();
        pageManager.getInventoryPage()
                .selectProducts()
                .goToTheCart();
        pageManager.getCartPage()
                .removeOnesie()
                .goToTheCheckoutPage();
        pageManager.getCheckoutPage()
                .completeCheckout(name,lastName, String.valueOf(timestamp));
        assertEquals( pageManager.getCheckoutStepTwoPage().getTotal(),"Total: $86.38");
    }
}