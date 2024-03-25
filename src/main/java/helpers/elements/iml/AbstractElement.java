package helpers.elements.iml;

import org.openqa.selenium.WebElement;
import helpers.elements.Element;

abstract class AbstractElement implements Element {

    final WebElement wrappedElement;

    AbstractElement(final WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    @Override
    public boolean isDisplayed() {
        return wrappedElement.isDisplayed();
    }

    @Override
    public boolean isEnabled() {
        return wrappedElement.isEnabled();
      }
    }

