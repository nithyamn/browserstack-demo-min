package app.testing;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeviceChaining {

    public String userName = "nithyamani3";//System.getenv("BROWSERSTACK_USERNAME");
    public String accessKey = "Q9paSstxtiypvRuq1XkT" ;//System.getenv("BROWSERSTACK_ACCESS_KEY");
    public String appHashedID = "bstack_ios_app";//System.getenv("BROWSERSTACK_APP_ID");
    public IOSDriver driver;
    DesiredCapabilities caps;
    final String wikiUsername = "Nithyamani1506";
    final String wikiPass = "aBC12345";

    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        caps = new DesiredCapabilities();
        caps.setCapability("app",appHashedID);
        caps.setCapability("device","iPad Air 4");
//        caps.setCapability("os_version", "10.0");
        caps.setCapability("build","Device Chaining");
        //caps.setCapability("name",method.getName());
        caps.setCapability("name",method.getName() +" - DC");
        caps.setCapability("browserstack.reserveDevice","true");
        caps.setCapability("browserstack.maskCommands", "setValues,getValues");
        //caps.setCapability("noReset","true");
        //driver = new AndroidDriver(new URL("https://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"),caps);
        driver = new IOSDriver(new URL("https://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"),caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    @Test(priority = 0)
    public void signinAndCheckUsername(){
//        loginProcess();
//
//        String validateUsername = driver.findElementById("org.wikipedia.alpha:id/explore_overflow_account_name").getText();
//        if (validateUsername.equals(wikiUsername)){
//            markTestStatus("passed","Username validated!");
//        }else{
//            markTestStatus("failed","Username not validated!");
//        }
    }

    @Test(priority = 1)
    public void signinAndCheckLogoutButton(){
//        loginProcess();
//        driver.findElementById("org.wikipedia.alpha:id/menu_overflow_button").click();
//
//        if (driver.findElementById("org.wikipedia.alpha:id/explore_overflow_log_out").isDisplayed()){
//            markTestStatus("passed","Logout button is visible");
//        }else{
//            markTestStatus("failed","Logout button is not visible");
//        }
    }

    @AfterMethod
    public void teardown(){
        driver.quit();
    }

    public void loginProcess(){
        driver.findElementById("org.wikipedia.alpha:id/menu_overflow_button").click();
        driver.findElementById("org.wikipedia.alpha:id/explore_overflow_account_name").click(); //android.widget.TextView//Log in to Wikipedia
        driver.findElementById("org.wikipedia.alpha:id/login_username_text").sendKeys(wikiUsername);
        List<AndroidElement> password = driver.findElementsByClassName("android.widget.EditText");
        password.get(1).sendKeys(wikiPass);
        driver.findElementById("org.wikipedia.alpha:id/login_button").click();
        driver.findElementById("org.wikipedia.alpha:id/menu_overflow_button").click();
    }

    public void markTestStatus(String status, String reason){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+status+"\", \"reason\": \""+reason+"\"}}");
    }
}