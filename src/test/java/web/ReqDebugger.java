package web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.URL;

public class ReqDebugger{
    public static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public static final String AUTOMATE_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    //public static final String URL = "http://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
    public static final String URL = "http://" + USERNAME + ":" + AUTOMATE_KEY + "@localhost:9690/wd/hub";

    @Test
    public static void test() throws Exception {
        //System.setProperty("webdriver.chrome.driver", "/Users/nithyamani/Desktop/chromedrivers/chromedriver80");
        //System.getProperties().put("http.proxyHost", "localhost");
        //System.getProperties().put("http.proxyPort", "9687");

        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("os","Windows");
        caps.setCapability("os_version","10");
        caps.setCapability("browser","Chrome");
        caps.setCapability("browser_version","83.0");

        caps.setCapability("build","Request Debugger");
        caps.setCapability("name","test");


        WebDriver driver = new RemoteWebDriver(new URL(URL),caps);

        driver.get("https://the-internet.herokuapp.com/");

        driver.getTitle();

        driver.quit();
    }
}
