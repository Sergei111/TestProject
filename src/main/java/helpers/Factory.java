package helpers;

import org.openqa.selenium.WebElement;

public interface Factory<T> {

    <K extends T> K create(Class<K> elementClass, WebElement wrappedElement);
}

