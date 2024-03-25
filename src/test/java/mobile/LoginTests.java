package mobile;

import org.testng.annotations.Test;

@Test(groups = {"ui", "app"})
public class LoginTests extends BaseMobileTest {

    @Test(groups = {"1", "ios", "android"}, description = "check login")
    public void checkLogin() {

    }
}