package helpers.elements.iml;

import org.openqa.selenium.WebElement;
import helpers.elements.Checkbox;


public class CheckboxImpl extends AbstractElement implements Checkbox {

    CheckboxImpl(final WebElement wrappedElement) {
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
    public boolean isSelected() {
        return wrappedElement.isSelected();
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
