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

            //System.out.println(encodedData);

            System.out.println("\n**** Jira JSON Body ****");
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

        String desc = GetSessionDetails.sessionData(sessionId);
        String buildName = GetSessionDetails.buildName;
        String sessionName = GetSessionDetails.sessionName;
        String summary = "Issue in Build: "+buildName+" - Session: "+sessionName;
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
}
