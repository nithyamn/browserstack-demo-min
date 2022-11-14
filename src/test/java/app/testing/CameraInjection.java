package app.testing;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

public class CameraInjection {

    public String userName = System.getenv("BROWSERSTACK_USERNAME");
    public String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
    public String appHashedID = "camera_injection";
    public String mediaURL = "media://f78dd0fef4bf190a79ed205d810165d582306bc1";

    @Test
    public void cameraInjectionTest() throws MalformedURLException, InterruptedException {
        System.out.println(userName + accessKey +appHashedID+mediaURL);
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("device", "Google Pixel 5");
        //caps.setCapability("device","Google Pixel 4");
        caps.setCapability("build", "Camera Injection");
        caps.setCapability("name", "test");
        caps.setCapability("app", appHashedID);
        caps.setCapability("autoGrantPermissions", "true");
        caps.setCapability("browserstack.enableCameraImageInjection", "true");


        AppiumDriver<AndroidElement> driver = new AppiumDriver<AndroidElement>(new URL("https://"+userName+":"+accessKey+"@hub-cloud.browserstack.com/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        JavascriptExecutor jse = (JavascriptExecutor)driver;

        //driver.findElementById("permission_allow_button").click();
        System.out.println(driver.findElementById("com.example.cameainjection:id/showImage").isDisplayed());
        jse.executeScript("browserstack_executor: {\"action\":\"cameraImageInjection\", \"arguments\": {\"imageUrl\" : \""+mediaURL+"\" }}");
        driver.findElementById("com.example.cameainjection:id/openCamera").click();
        if(caps.getCapability("device").toString().contains("Samsung"))
        {
            try{
                driver.findElementByXPath("//android.widget.Button[@text='CANCEL']").click();
            }catch (Exception e){
                System.out.println(e);
            }
        }else{
            try{
                driver.findElementById("com.android.permissioncontroller:id/permission_allow_foreground_only_button").click();
            }catch (Exception e){
                System.out.println(e);
            }
        }

        //((AndroidDriver<AndroidElement>)driver).pressKey(new KeyEvent(AndroidKey.CAMERA));
        if(caps.getCapability("device").toString().contains("Google"))
        {
            driver.findElementById("com.google.android.GoogleCamera:id/shutter_button").click();
            Thread.sleep(1000);
            driver.findElementById("com.google.android.GoogleCamera:id/shutter_button").click();
            //driver.findElementById("com.sec.android.app.camera:id/okay").click();
        }else{
            driver.findElementByAccessibilityId("Take photo").click();
            driver.findElementByAccessibilityId("Done").click();
        }

        Thread.sleep(5000);
        driver.quit();
    }
}
