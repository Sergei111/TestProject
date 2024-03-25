package helpers.elements;

import org.openqa.selenium.WebElement;

public interface Button extends ClickableElement {
    String getText();
    String getAttribute(String field);
    WebElement getElement();
}


