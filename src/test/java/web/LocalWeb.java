package web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class LocalWeb extends BrowserStackWebRunner {

    @Test
    public void test() throws Exception {
        driver.get("http://bs-local.com:45691/check");

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        String content = driver.findElement(By.xpath("/html/body")).getText();
        if(content.contains("This is an internal server for BrowserStack Local")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
        }

    }
}
