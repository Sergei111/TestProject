package mobile_screens.mobile_configuration;

public enum MobilePlatform {
    IOS("ios"),
    ANDROID("android"),
    ;

    private final String platform;

    public String getPlatform() {
        return platform;
    }

    MobilePlatform(String platform) {
        this.platform = platform;

    }
}
