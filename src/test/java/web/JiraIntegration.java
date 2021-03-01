package web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.SessionId;

import java.net.URI;
import java.net.URISyntaxException;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class JiraIntegration {
    public static String buildName="";
    public static void createJira(SessionId sessionId) {
        try {
            String jiraURL = System.getenv("JIRA_URL"); //e.g. something.atlassian.net
            String jiraEmail = System.getenv("JIRA_EMAIL"); // The email id linked to your Jira instance
            String jiraAccessToken = System.getenv("JIRA_ACCESS_TOKEN"); //Jira password or AccessToken

            URL url = new URL("https://"+jiraURL+"/rest/api/2/issue");
            String jiraCreds = jiraEmail+":"+jiraAccessToken;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String encodedData = getJSON_Body(sessionId);
            System.out.println(encodedData);

            System.out.println(getJSON_Body(sessionId));
            conn.setRequestMethod("POST");
            System.out.println();
            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(jiraCreds.getBytes()));
            conn.setRequestProperty("Content-Type", "application/json");
            conn.getOutputStream().write(encodedData.getBytes());

            try {
                InputStream inputStream = conn.getInputStream();
                System.out.println(inputStream);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getJSON_Body(SessionId sessionId) throws Exception {

        String desc = sessionData(sessionId);
        String summary = "Issue in build :"+buildName;
        JsonObject createIssue = Json.createObjectBuilder()
                .add("fields",
                        Json.createObjectBuilder().add("project",
                                Json.createObjectBuilder().add("key", "SAM"))
                                .add("summary", summary)
                                .add("description",desc)
                                .add("issuetype",
                                        Json.createObjectBuilder().add("name", "Bug"))
                ).build();

        return createIssue.toString();
    }
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
