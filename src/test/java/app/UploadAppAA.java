package app;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class UploadAppAA {
    public static String userName = System.getenv("BROWSERSTACK_USERNAME"); //add your browserstack username
    public static String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");//add your browserstack accesskey
    public static String app_path = "/path/to/app/file"; //app file path
    public static String custom_id = "any_custom_id"; //this field for this script is mandate as it checks for any existing app with the same custom id before uploading the app.
    //https://www.browserstack.com/docs/app-automate/appium/upload-app-define-custom-id
    //this code works for both android and iOS

    public static void main (String args[]) throws Exception{

        //Upload app programmatically and retrieve hashed id
        String hash_id = uploadApp(app_path,custom_id);

        //Optional code to test the uploaded app
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("device", "Google Pixel 4");
        caps.setCapability("os_version", "10.0");
        caps.setCapability("build", "Upload app");
        caps.setCapability("name", "test");
        caps.setCapability("app", hash_id);

        AndroidDriver<AndroidElement> driver = new AndroidDriver<AndroidElement>(new URL("https://"+userName+":"+accessKey+"@hub-cloud.browserstack.com/wd/hub"), caps);
        //IOSDriver<IOSElement> driver = new IOSDriver<IOSElement>(new URL("https://"+userName+":"+accessKey+"@hub-cloud.browserstack.com/wd/hub"), caps);
        driver.quit();
    }
    public static String uploadApp(String app_path, String custom_id) throws IOException, ParseException {

        String app_hashed_id = checkIfAppUpload(custom_id);

        try{
            /**Checking if the app is already uploaded**/
            if (app_hashed_id!=null){
                System.out.println("Found uploaded app!");
                return app_hashed_id;
            }else{

                /**Upload app if not present**/
                System.out.println("Did not find uploaded app. Uploading...");
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost uploadFile = new HttpPost("https://api-cloud.browserstack.com/app-automate/upload");
                uploadFile.addHeader("Authorization", basicAuthHeaderGeneration(userName,accessKey));
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);

                /**This attaches the file to the POST:**/
                File f = new File(app_path);
                System.out.println("-- File found --");
                builder.addBinaryBody(
                        "file",
                        new FileInputStream(f),
                        ContentType.APPLICATION_OCTET_STREAM,
                        f.getName()
                );
                /**provides a custom id to the app**/
                builder.addTextBody("custom_id",custom_id);

                HttpEntity multipart = builder.build();
                uploadFile.setEntity(multipart);
                CloseableHttpResponse response = httpClient.execute(uploadFile);
                System.out.println("-- File uploaded --");

                HttpEntity responseEntity = response.getEntity();

                System.out.println("-- Received response --");
                String responseString = EntityUtils.toString(responseEntity, "UTF-8");
                System.out.println(responseString);

                /**Parsing the JSON repsonse to just get the app_url**/
                System.out.println("-- Parsing JSON --");
                JSONParser jsonParser = new JSONParser();
                JSONObject fetchHashedID = (JSONObject) jsonParser.parse(responseString);
                app_hashed_id = (String) fetchHashedID.get("app_url");
                System.out.println(app_hashed_id);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return app_hashed_id;
    }

    public static String checkIfAppUpload(String custom_id) throws IOException, ParseException {
        String appURL = null;
        String app_hashed_id = null;
        StringBuffer response = null;
        URL url = new URL("https://api-cloud.browserstack.com/app-automate/recent_apps/"+custom_id);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestProperty("Authorization", basicAuthHeaderGeneration(userName,accessKey));
        http.setDoOutput(true);

        /**If response is 200 OK check for the response parameters received from the http request**/
        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        if (http.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String output;
            response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            // print result
            //System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
            return null;
        }
        /**Returns a single element array, for simplification trimming the '[' & ']' from the output.**/
        appURL = response.substring(1, response.length()-1);
        System.out.println(appURL);

        /**Checking if the response has a "message" parameter which would indicate the that the app is not uploaded**/
        if(appURL.contains("message")){
            return null;
        }else {
            /**Else getting the uploaded apps hashed id by parsing the JSON response**/
            JSONParser jsonParser = new JSONParser();
            JSONObject fetchHashedID = (JSONObject) jsonParser.parse(appURL);
            app_hashed_id = (String) fetchHashedID.get("app_url");
            System.out.println(app_hashed_id);
        }

        http.disconnect();
        return app_hashed_id;
    }
    public static String basicAuthHeaderGeneration(String bstackUsername, String bstackPassword){
        String authCreds = bstackUsername+":"+bstackPassword;
        //System.out.println("Basic " + Base64.getEncoder().encodeToString(authCreds.getBytes()));
        return "Basic " + Base64.getEncoder().encodeToString(authCreds.getBytes());
    }
}
