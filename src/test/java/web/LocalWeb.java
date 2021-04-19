package web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class LocalWeb extends BrowserStackWebRunner {
    String platform;

    @Test
    public void test() throws Exception {

        SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();

        driver.get("http://bs-local.com/SampleWebsite");


        JavascriptExecutor jse = (JavascriptExecutor)driver;
        //String content = driver.findElement(By.xpath("/html/body")).getText();
        // content.contains("Percy - About Us")
        if(driver.getTitle().equals("Percy - About Us")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
        }
        String data = GetSessionDetails.sessionData(sessionId);
        //System.out.println(data);
    }
}
