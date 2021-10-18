package web;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

class SingleWeb extends BrowserStackWebRunner {
    public JavascriptExecutor jse;
    @Test
    public void test() throws Exception {

        /*** Fetch Session ID***/
        SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
        jse = (JavascriptExecutor)driver;

//        driver.get("https://www.google.com/");
//        try{
//            driver.findElement(By.id("L2AGLb")).click();
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        WebElement element = driver.findElement(By.name("q"));
//        element.sendKeys("BrowserStack");
//        element.submit();
//        Thread.sleep(5000);
//        String title = driver.getTitle();
//
//        JavascriptExecutor jse = (JavascriptExecutor)driver;
//        if(title.contains("BrowserStack")) {
//            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
//        }
//        else{
//            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
//            /**Create a jira ticket in case of failure in test**/
//            JiraIntegration.createJira(sessionId);
//        }

        try{
            driver.get("https://www.johnlewis.com/");
            driver.findElement(By.cssSelector("button[data-test='allow-all']")).click();
            driver.findElement(By.id("mobileSearch")).sendKeys("apple watch",Keys.ENTER);

            System.out.println("platform device?"+capabilities.getCapability("device"));
            if(capabilities.getCapability("device")==null){
                WebElement watchLink = driver.findElement(By.cssSelector("a[href='/browse/electricals/smart-watches-wearable-tech/view-all-smart-watches/apple-watch-series-7/_/N-5nmcZn4qe?intcmp=bc_20211015_applewatch7hybridblock_bp_ele_'].image-container-link--831b8"));
                jse.executeScript("return arguments[0].scrollIntoView();",watchLink);
                watchLink.click();
            }
            //
            String title = driver.getTitle();

            if(capabilities.getCapability("device")==null){
                if(title.contains("View All Smart Watches | John Lewis & Partners")) {
                    markSessionStatus(jse,"passed","Expected");
                }
                else{
                    markSessionStatus(jse,"failed","Unexpected");
                    /**Create a jira ticket in case of failure in test**/
                    JiraIntegration.createJira(sessionId);
                }
            }else{
                if(title.contains("Filtered Search results for “apple watch” | John Lewis & Partners")) {
                    markSessionStatus(jse,"passed","Expected");
                }
                else {
                    markSessionStatus(jse, "failed", "Unexpected");
                    /**Create a jira ticket in case of failure in test**/
                    JiraIntegration.createJira(sessionId);
                    System.out.println("======================");
                }
            }
        }catch (Exception e){
            System.out.println("Exception: "+e);
            markSessionStatus(jse,"failed","Failed");
        }
        /***Get session details***/
        String data = GetSessionDetails.sessionData(sessionId);
        System.out.println(data);
    }

    public void markSessionStatus(JavascriptExecutor jse, String status, String reason){
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+status+"\", \"reason\": \""+reason+"\"}}");
    }

//    @Test
//    public void failedTest() throws Exception {
//        /*** Fetch Session ID***/
//        SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
//
//        driver.get("https://www.google.com/");
//        try{
//            driver.findElement(By.id("L2AGLb")).click();
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        WebElement element = driver.findElement(By.name("q"));
//        element.sendKeys("BrowserStack");
//        element.submit();
//        Thread.sleep(5000);
//        String title = driver.getTitle();
//
//        JavascriptExecutor jse = (JavascriptExecutor)driver;
//        if(title.contains("BrowserStack 1")) {
//            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
//        }
//        else{
//            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
//            /**Create a jira ticket in case of failure in test**/
//            JiraIntegration.createJira(sessionId);
//        }
//
//        /***Get session details***/
//        String data = GetSessionDetails.sessionData(sessionId);
//        System.out.println(data);
//    }
}
