package helpers;


import helpers.elements.Container;
import org.openqa.selenium.WebElement;



public class DefaultContainerFactory implements Factory<Container> {

    @Override
    public <K extends Container> K create(final Class<K> containerClass, final WebElement wrappedElement) {
        final K container = createInstanceOf(containerClass);
        container.init(wrappedElement);
        return container;
    }

    private <C extends Container> C createInstanceOf(final Class<C> containerClass) {
        try {
            return containerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }



}
