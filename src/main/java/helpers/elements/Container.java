package helpers.elements;

import org.openqa.selenium.WebElement;

public interface Container extends Element {
    void init(WebElement wrappedElement);
   }
