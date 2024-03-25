package helpers;

public class ScreenHolder {
    private static ThreadLocal<ScreenManager> screens = new ThreadLocal<>();

    public static void setScreenHolder(ScreenManager driver) {
        screens.set(driver);
    }

    public static ScreenManager getScreenHolder() {
        return screens.get();
    }
}
