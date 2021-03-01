package web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.SessionId;

import java.net.URI;

public class GetSessionDetails {
    public static String buildName="";
    public static String sessionData(SessionId sessionId) throws Exception{
        String username = BrowserStackWebRunner.username;
        String accesskey = BrowserStackWebRunner.accessKey;

        URI uri = new URI("https://"+username+":"+accesskey+"@api.browserstack.com/automate/sessions/"+sessionId+".json"); //App Automate
        String emailData = "***** Session Data *****";
        HttpGet getRequest = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse httpresponse = httpclient.execute(getRequest);

        String jsonResponseData = EntityUtils.toString(httpresponse.getEntity());
        String trimResposneData = jsonResponseData.substring(22, jsonResponseData.length()-1);

        JSONParser parser = new JSONParser();
        JSONObject bsSessionData = (JSONObject) parser.parse(trimResposneData);
        buildName = (String) bsSessionData.get("build_name");
        //status = (String)bsSessionData.get("status");
        emailData += "\n\nName: "+bsSessionData.get("name")
                +"\nBuild: "+bsSessionData.get("build_name")
                +"\nProject: "+bsSessionData.get("project_name")
                +"\nDevice: "+bsSessionData.get("device")
                +"\nOS: "+bsSessionData.get("os")
                +"\nOS Version: "+bsSessionData.get("os_version")
                +"\nBrowser: "+bsSessionData.get("browser")
                +"\nBrowser Version: "+bsSessionData.get("browser_version")
                +"\nStatus: "+bsSessionData.get("status")
                +"\nReason: "+bsSessionData.get("reason")
                +"\nPublic Session URL: "+bsSessionData.get("public_url");

        return emailData;
    }
}
