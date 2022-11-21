package web;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.List;


class SingleWeb extends BrowserStackWebRunner {
    public JavascriptExecutor jse;

    @Test
    public void samplePassedTest() throws Exception {

        /*** Fetch Session ID***/
        SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
        jse = (JavascriptExecutor)driver;

        driver.get("https://www.google.com/");
        try{
            driver.findElement(By.id("L2AGLb")).click();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("BrowserStack");
        element.submit();
        Thread.sleep(5000);
        Assert.assertEquals(driver.getTitle(),"BrowserStack - Google Search");

        /***Get session details***/
        //String data = GetSessionDetails.sessionData(sessionId);
        //System.out.println(data);
    }

    @Test
    public void sampleFailedTest() throws Exception {
        /*** Fetch Session ID***/
        SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();

        driver.get("https://www.google.com/");
        try{
            driver.findElement(By.id("L2AGLb")).click();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("BrowserStack");
        element.submit();
        Thread.sleep(5000);
        Assert.assertEquals(driver.getTitle(),"BrowserStack - Google Search - Wrong");

        /***Get session details***/
        String data = GetSessionDetails.sessionData(sessionId);
        System.out.println(data);
    }
}
