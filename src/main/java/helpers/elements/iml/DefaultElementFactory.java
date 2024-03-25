package helpers.elements.iml;

import org.openqa.selenium.WebElement;
import helpers.Factory;
import helpers.elements.Element;

import java.lang.reflect.InvocationTargetException;

import static java.text.MessageFormat.format;



public class DefaultElementFactory implements Factory<Element> {

    @Override
    public <K extends Element> K create(Class<K> elementClass, WebElement wrappedElement) {
        try {
            return findImplementationFor(elementClass)
                    .getDeclaredConstructor(WebElement.class)
                    .newInstance(wrappedElement);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    private <E extends Element> Class<? extends E> findImplementationFor(final Class<E> elementClass) {
        try {
            return (Class<? extends E>) Class.forName(format("{0}.{1}Impl", getClass().getPackage().getName(), elementClass.getSimpleName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}