//package web.testing;
//
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.ios.IOSDriver;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.openqa.selenium.*;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.LocalFileDetector;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Parameters;
//import org.testng.annotations.Test;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//public class DriverManager {
//    public String userName = System.getenv("BROWSERSTACK_USERNAME");
//    public String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
//    DesiredCapabilities caps;
//    public WebDriver driver;
//    public static String platforName;
//    @BeforeMethod
//    @Parameters({"platform"})
//    public void setup(String platform) throws IOException, ParseException {
//        platforName = platform;
//        JSONParser jsonParser = new JSONParser();
//        JSONObject platformsJSON = (JSONObject) jsonParser.parse(new FileReader("src/test/java/web/testing/platform.json"));
//        JSONObject allPlatforms = (JSONObject) platformsJSON.get("platform");
//        JSONObject currentPlatform = (JSONObject) allPlatforms.get(platform);
//        setCaps(currentPlatform);
//
//        if(platform.equals("android")){
//            driver = new AndroidDriver<WebElement>( new URL("https://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"),caps);
//        } else if (platform.equals("web")){
//            driver = new RemoteWebDriver( new URL("https://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"),caps);
//        }
//    }
//
//    @Test
//    public void test() throws IOException {
//        driver.get("https://the-internet.herokuapp.com/upload");
//        String sendKeyValue = uploadFile();
//        driver.findElement(By.id("file-upload")).sendKeys(sendKeyValue);
//        driver.findElement(By.id("file-submit")).click();
//        driver.quit();
//    }
//
//    public String uploadFile() throws IOException {
//        String filePath = null;
//        if(platforName.equals("android")){
//            ((AndroidDriver)driver).pushFile("/data/local/tmp/sample.jpg", new File("/Users/nithyamani/Desktop/images/sample.jpg"));
//            filePath = "/data/local/tmp/sample.jpg";
//        }else if (platforName.equals("web")){
//            ((RemoteWebDriver)driver).setFileDetector(new LocalFileDetector());
//            filePath = "/Users/nithyamani/Desktop/images/sample.jpg";
//        }
//        return filePath;
//    }
//
//    public void setCaps(JSONObject currentPlatform){
//        HashMap<String, String> map = currentPlatform;
//        Iterator iterator = map.entrySet().iterator();
//        caps = new DesiredCapabilities();
//        while (iterator.hasNext()){
//            Map.Entry entry = (Map.Entry) iterator.next();
//            caps.setCapability((String) entry.getKey(),entry.getValue());
//        }
//    }
//}
//
