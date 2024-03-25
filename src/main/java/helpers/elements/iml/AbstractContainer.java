package helpers.elements.iml;

import org.openqa.selenium.WebElement;
import helpers.elements.Container;


public abstract class AbstractContainer implements Container {

    private WebElement wrappedElement;

    @Override
    public final void init(final WebElement wrappedElement) {
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