package helpers.elements;

public interface TextField extends ClickableElement {

    void clear();

    void clearAndTypeEnter(String value);

    String getAttribute(String field);

    String getText();

    void clearAndType(String text);

    String getClassName();
}

