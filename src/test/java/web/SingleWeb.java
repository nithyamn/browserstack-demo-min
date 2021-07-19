package web;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

class SingleWeb extends BrowserStackWebRunner {

    @Test
    public void test() throws Exception {
        SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
        driver.get("https://www.google.com/ncr");
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("BrowserStack");
        element.submit();
        Thread.sleep(5000);
        String title = driver.getTitle();

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        if(title.contains("BrowserStack - Google Search")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            //JiraIntegration.createJira(sessionId);
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
        }

        /***Get session details***/
        String data = GetSessionDetails.sessionData(sessionId);
        System.out.println(data);
    }
}
