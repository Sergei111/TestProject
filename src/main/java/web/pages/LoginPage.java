package web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class LoginPage {

    @FindBy(name = "user-name")
    private WebElement txtName;

    @FindBy(name = "password")
    private WebElement txtPassword;

    @FindBy(id = "login-button")
    private WebElement btnLogin;


    @Step
    public void loginToSystem(String email,String password){
        txtName.sendKeys(email);
        txtPassword.sendKeys(password);
        btnLogin.click();
    }
}