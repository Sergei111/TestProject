package helpers.elements.iml;

import org.openqa.selenium.WebElement;
import helpers.elements.Button;


class ButtonImpl extends AbstractElement implements Button {

    ButtonImpl(final WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void click() {
        wrappedElement.click();
    }

    @Override
    public void clickInvisible() {

    }


    @Override
    public String getText() {
        return wrappedElement.getText();
    }

    @Override
    public String getAttribute(String field) {
        return wrappedElement.getAttribute(field);
    }

    @Override
    public WebElement getElement() {
        return wrappedElement;
    }
}

