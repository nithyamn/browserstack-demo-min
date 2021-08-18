package web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AccessibilityTesting{
    public static final String AUTOMATE_USERNAME = "nithyamani2";
    public static final String AUTOMATE_ACCESS_KEY = "LqF3zpn1qQzG95ffrTAp";
    public static final String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    public static void main(String[] args) throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os_version", "10");
        caps.setCapability("browser", "chrome");
        caps.setCapability("browser_version", "latest");
        caps.setCapability("os", "Windows");
        caps.setCapability("name", "Accessibility Test - Google"); // test name
        caps.setCapability("build", "Java Accessibility Test Sample Build"); // CI/CD job or build name
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
        driver.get("https://www.google.com");

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        Path path = Paths.get("/Users/nithyamani/Desktop/Demo/DemoNew/src/test/java/web/cdnjs 2/axe.min.js");

        String content = new String(Files.readAllBytes(path));
        jse.executeScript(content);

        File output = new File("/Users/nithyamani/Desktop/Demo/DemoNew/src/test/resources/reports/acc_report.json");
        FileWriter writer = new FileWriter(output);
        String result = String.valueOf(jse.executeAsyncScript("var callback = arguments[arguments.length - 1]; axe.run().then(results => callback(results));"));
        writer.write(result);
        writer.flush();
        writer.close();
        driver.quit();
    }
}

