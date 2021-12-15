package web;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;


class SingleWeb extends BrowserStackWebRunner {
    public JavascriptExecutor jse;
    @Test
    public void test() throws Exception {

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

       String title = driver.getTitle();

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        if(title.contains("BrowserStack")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
            /**Create a jira ticket in case of failure in test**/
            JiraIntegration.createJira(sessionId);
        }


        /***Get session details***/
        String data = GetSessionDetails.sessionData(sessionId);
        System.out.println(data);
    }

    public void markSessionStatus(JavascriptExecutor jse, String status, String reason){
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+status+"\", \"reason\": \""+reason+"\"}}");
    }

   // @Test
    public void failedTest() throws Exception {
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
        String title = driver.getTitle();

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        if(title.contains("BrowserStack 1")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
            /**Create a jira ticket in case of failure in test**/
            JiraIntegration.createJira(sessionId);
        }

        /***Get session details***/
        String data = GetSessionDetails.sessionData(sessionId);
        System.out.println(data);
    }
}
