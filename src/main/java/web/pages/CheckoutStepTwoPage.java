package web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class CheckoutStepTwoPage {

    @FindBy(xpath = "//div[@class='summary_info_label summary_total_label']")
    private WebElement total;

    @Step
    public String getTotal() {
        return total.getText();
    }
}