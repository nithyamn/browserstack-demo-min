package web;
import com.browserstack.local.Local;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BrowserStackWebRunner {
    public Local l;
    public String buildName;
    public WebDriver driver;
    public DesiredCapabilities capabilities;

    public static String username = System.getenv("BROWSERSTACK_USERNAME");
    public static String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

    @BeforeSuite
    @Parameters({"local"}) //set to "yes" in XML file(s) if you want to use local testing via code bindings
    public void startLocal(String local) throws Exception {
        /*if(local.equals("yes")){
            System.out.println("Starting Local");
            l = new Local();
            Map<String, String> options = new HashMap<String, String>();
            options.put("key", accessKey);
            l.start(options);
            System.out.println("isRunning: "+l.isRunning());

        }*/
    }

    @BeforeMethod(alwaysRun=true)
    @org.testng.annotations.Parameters(value={"config", "environment"})
    public void setUp(String config_file, String environment) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/web/conf/" + config_file));
        JSONObject envs = (JSONObject) config.get("environments");

        capabilities = new DesiredCapabilities();

        buildName  =((Map<String, String>) config.get("capabilities")).get("build");
        //System.out.println(buildName);
        if(buildName.equals("BROWSERSTACK_BUILD_NAME")){
            buildName = System.getenv("BROWSERSTACK_BUILD_NAME");
            capabilities.setCapability("build",buildName);
        }

        //capabilities.setCapability("build", System.getenv("BUILD_NUMBER"));
        //capabilities.setCapability("name", "parallel_test "+System.getenv("BUILD_NUMBER"));


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
        //driver = new RemoteWebDriver(new URL("https://"+username+":"+accessKey+"@"+"localhost:9688/wd/hub"), capabilities); //Request Debugger Reverse proxy
    }

    @AfterMethod(alwaysRun=true)
    public void tearDown() throws Exception {
        driver.quit();
    }

    @AfterSuite
    @Parameters({"local"})
    public void stopLocal(String local) throws Exception {
        /*if(local.equals("yes")) {
            l.stop();
            System.out.println("Stopping Local");
        }*/
    }
}
