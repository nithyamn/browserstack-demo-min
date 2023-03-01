package web;
import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BrowserStackWebRunner {
    public Local l;
    public String buildName,isLocalEnabled;
    public WebDriver driver;
    public AppiumDriver<WebElement> appium_driver;
    public DesiredCapabilities capabilities;
    public Capabilities platformCaps;
    public static String username = System.getenv("BROWSERSTACK_USERNAME");
    public static String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
    public static String localIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");
    public String failedTests="";
    @BeforeSuite
    @Parameters({"local"}) //set to "yes" in XML file(s) if you want to use local testing via code bindings
    public void startLocal(String local) throws Exception {
        if(local.equals("yes")){
            System.out.println("Starting Local");
            l = new Local();
            Map<String, String> options = new HashMap<String, String>();
            options.put("key", accessKey);
            l.start(options);
            System.out.println("isRunning: "+l.isRunning());
        }
    }

    @BeforeMethod(alwaysRun=true)
    @org.testng.annotations.Parameters(value={"config", "environment"})
    public void setUp(String config_file, String environment, ITestContext context, Method method) throws Exception {
        try{
            JSONParser parser = new JSONParser();
            JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/web/conf/" + config_file));
            JSONObject envs = (JSONObject) config.get("environments");

            capabilities = new DesiredCapabilities();

            capabilities.setCapability("name",method.getName());
            buildName = System.getenv("BROWSERSTACK_BUILD_NAME");
            if(buildName==null || buildName=="") {
                buildName  =((Map<String, String>) config.get("capabilities")).get("build");
            }
            capabilities.setCapability("build",buildName);
            try{
                isLocalEnabled = ((Map<String, String>) config.get("capabilities")).get("browserstack.local");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            if (localIdentifier!= null && !localIdentifier.equals("") && isLocalEnabled!=null){
                System.out.println("Local Identifier: "+localIdentifier);
                capabilities.setCapability("browserstack.localIdentifier",localIdentifier);
            }


            Map<String, String> envCapabilities = (Map<String, String>) envs.get(environment);
            Iterator it = envCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }

            Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
            it = commonCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(capabilities.getCapability(pair.getKey().toString()) == null){
                    capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
                }
            }

            if(username == null) {
                username = (String) config.get("user");
            }

            if(accessKey == null) {
                accessKey = (String) config.get("key");
            }
            driver = new RemoteWebDriver(new URL("https://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }catch (WebDriverException webDriverException){
            if(webDriverException.getMessage().contains("All parallel tests are currently in use")){
                System.out.println("Retry, as cant queue more.");
                System.out.println("testname: "+context.getName());
                failedTests+=" "+context.getName();
            }
            else
                System.out.println("Some other issue. Debug."+webDriverException.getMessage());
        }
    }


    @AfterMethod(alwaysRun=true)
    public void tearDown(ITestResult result) throws Exception {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        try{
            if( result.getStatus() != ITestResult.SUCCESS) {
                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \""+result.getThrowable()+"\"}}");
            }
            else{
                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \""+result.getName()+" passed!\"}}");
            }
        }catch (Exception e){
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Exception!\"}}");
        }
        driver.quit();
    }

    @AfterSuite
    @Parameters({"local"})
    public void stopLocal(String local) throws Exception {
        if(local.equals("yes")) {
            l.stop();
            System.out.println("Stopping Local");
        }
    }
}
