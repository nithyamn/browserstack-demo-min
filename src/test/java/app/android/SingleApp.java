package app.android;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidElement;

import okhttp3.*;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import static app.UploadAppAA.userName;

public class SingleApp extends BrowserStackAppRunner{

    @Test
    public void wikiAppTest() throws Exception {
        SessionId sessionId = driver.getSessionId();

        AndroidElement searchElement = (AndroidElement) new WebDriverWait(driver, 30).until(
                ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Search Wikipedia")));
        searchElement.click();
        AndroidElement insertTextElement = (AndroidElement) new WebDriverWait(driver, 30).until(
                ExpectedConditions.elementToBeClickable(MobileBy.id("org.wikipedia.alpha:id/search_src_text")));
        insertTextElement.sendKeys("Syngenta");
        Thread.sleep(5000);

        List<AndroidElement> allProductsName = driver.findElementsByClassName("android.widget.TextView");
        Assert.assertTrue(allProductsName.size() > 0);

    }
    @Test
    public void offlineModeTest() throws IOException, InterruptedException {
        SessionId sessionId = driver.getSessionId();
        toggleNetwork(sessionId,"no-network");
        Thread.sleep(5000);
        driver.closeApp();
        driver.launchApp();
        Thread.sleep(5000);

        driver.findElementByAccessibilityId("Search Wikipedia").click();
        driver.findElementById("org.wikipedia.alpha:id/search_src_text").sendKeys("Syngenta");

        toggleNetwork(sessionId,"reset");
        Thread.sleep(5000);
        driver.closeApp();
        driver.launchApp();
        Thread.sleep(5000);
    }

    @Test
    public void killAndRelaunchAppTest(){
        driver.terminateApp("org.wikipedia.alpha");
        driver.launchApp();
    }

    public void toggleNetwork(SessionId sessionId, String networkProfile) throws IOException {
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("networkProfile",networkProfile)
                    .build();
            Request request = new Request.Builder()
                    .url("https://api-cloud.browserstack.com/app-automate/sessions/"+sessionId+"/update_network.json")
                    .method("PUT", body)
                    .addHeader("Authorization", basicAuthHeaderGeneration(username,accessKey))
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.body().toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public String basicAuthHeaderGeneration(String bstackUsername, String bstackPassword){
        String authCreds = bstackUsername+":"+bstackPassword;
        System.out.println("Basic " + Base64.getEncoder().encodeToString(authCreds.getBytes()));
        return "Basic " + Base64.getEncoder().encodeToString(authCreds.getBytes());
    }
}
