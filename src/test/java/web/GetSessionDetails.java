package web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.SessionId;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class GetSessionDetails {
    public static String buildName="";
    public static String sessionName="";
    public static String sessionData(SessionId sessionId) throws Exception {
        String username = BrowserStackWebRunner.username;
        String accesskey = BrowserStackWebRunner.accessKey;

        URI uri = new URI("https://" + username + ":" + accesskey + "@api.browserstack.com/automate/sessions/" + sessionId + ".json"); //Automate
        String emailData = "";
        HttpGet getRequest = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse httpresponse = httpclient.execute(getRequest);

        String jsonResponseData = EntityUtils.toString(httpresponse.getEntity());
        String trimResposneData = jsonResponseData.substring(22, jsonResponseData.length() - 1);

        JSONParser parser = new JSONParser();
        JSONObject bsSessionData = (JSONObject) parser.parse(trimResposneData);
        buildName = (String) bsSessionData.get("build_name");
        sessionName = (String) bsSessionData.get("name");
        //status = (String)bsSessionData.get("status");
        emailData += "\n\nName: " + bsSessionData.get("name")
                + "\nBuild: " + bsSessionData.get("build_name")
                + "\nProject: " + bsSessionData.get("project_name")
                + "\nDevice: " + bsSessionData.get("device")
                + "\nOS: " + bsSessionData.get("os")
                + "\nOS Version: " + bsSessionData.get("os_version")
                + "\nBrowser: " + bsSessionData.get("browser")
                + "\nBrowser Version: " + bsSessionData.get("browser_version")
                + "\nStatus: " + bsSessionData.get("status")
                + "\nReason: " + bsSessionData.get("reason")
                + "\nPublic Session URL: " + bsSessionData.get("public_url");

        return emailData;
    }

    public static String isLocalRunning() throws Exception{
        String accesskey = BrowserStackWebRunner.accessKey;
        String printLocalData = "";

        URI uri = new URI("https://www.browserstack.com/local/v1/list?auth_token="+accesskey+"&last=5&state=running");
        //String emailData = "***** Session Data *****";
        HttpGet getRequest = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse httpresponse = httpclient.execute(getRequest);

        String jsonResponseData = EntityUtils.toString(httpresponse.getEntity());
        //String trimResposneData = jsonResponseData.substring(22, jsonResponseData.length()-1);

        JSONParser parser = new JSONParser();
        JSONObject bsLocalData = (JSONObject) parser.parse(jsonResponseData);

        JSONArray array = new JSONArray(bsLocalData.get("instances").toString());

        System.out.println("**** Local Testing Data ****");

        for(int i=0;i<array.length();i++){
            org.json.JSONObject jsonObject = array.getJSONObject(i);
            printLocalData += "\nID: "+jsonObject.get("id")+" | Local identifier: "+jsonObject.get("localIdentifier")+" | Last Active on: "+jsonObject.get("lastActiveOn");
        }
        return printLocalData;
    }
}
