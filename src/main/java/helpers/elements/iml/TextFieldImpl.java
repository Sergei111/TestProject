package helpers.elements.iml;

import org.openqa.selenium.WebElement;
import helpers.elements.TextField;


public class TextFieldImpl extends AbstractElement implements TextField {

    TextFieldImpl(final WebElement wrappedElement) {
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
    public void clear() {

    }

    @Override
    public void clearAndTypeEnter(String value) {

    }

    @Override
    public String getAttribute(String field) {
        return wrappedElement.getAttribute(field);
    }

    @Override
    public String getText() {
        return wrappedElement.getText();
    }

    @Override
    public void clearAndType(String text) {

    }

    @Override
    public String getClassName() {
        return wrappedElement.getAttribute("class");
        }
    }
