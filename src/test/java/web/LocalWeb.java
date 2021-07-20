package web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LocalWeb extends BrowserStackWebRunner {


    @Test
    public void test() throws Exception {

        SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
        System.out.println(GetSessionDetails.isLocalRunning());

        driver.get("http://localhost:8888");

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        String title = driver.getTitle();
        //String content = driver.findElement(By.xpath("/html/body")).getText();
        // content.contains("Percy - About Us")
        //content.contains("Up and running") || content.contains("This is an internal server for BrowserStack Local"
        if(title.equals("BrowserStack | Local Website")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
        }
//        String data = GetSessionDetails.sessionData(sessionId);
//        System.out.println(data);
    }
}
