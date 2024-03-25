package mobile_screens.mobile_configuration;


public enum SauceLabDevice {

    IPHONE_ANY("iphone_any"),
    SAMSUNG_ANY("samsung_any"),
    ;

    private final String device;

    public String getDevice() {
        return device;
    }

    SauceLabDevice(String device) {
        this.device = device;

    }
}