package web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class SingleWeb extends BrowserStackWebRunner {
    @Test
    public void test() throws Exception {

        driver.get("http://google.com/");

        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("BrowserStack");
        element.submit();
        Thread.sleep(5000);
        String title = driver.getTitle();

        SessionId session = ((RemoteWebDriver) driver).getSessionId();
        System.out.println(session);

        JavascriptExecutor jse = (JavascriptExecutor)driver;


        if(title.equals("BrowserStack - Google Search")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Title\"}}");

        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Title\"}}");
        }

    }
}

