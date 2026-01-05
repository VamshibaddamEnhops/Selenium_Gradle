package testrailmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.BaseReq.ConfigReader;

public class JiraAPI {
	 private static String jiraURL = ConfigReader.get("jiraURL");
	    private static String username = ConfigReader.get("username");
	    		//password=Integratetestrail@321!
	   // Org key-----5e8eeedf-7636-4002-bc2d-7c9be6a566fc
	    private static String apiToken = ConfigReader.get("apiToken");
    
    public static void attachScreenshotToJira(String defectID, String filePath) throws IOException {
    	String jiraurl = "https://proarch-team-9.atlassian.net/rest/api/2/issue/" + defectID + "/attachments";
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Screenshot file not found: " + filePath);
            return;
        }

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(jiraurl);
        post.setHeader("X-Atlassian-Token", "no-check"); // Required for attachments

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName());
        HttpEntity entity = builder.build();
        post.setEntity(entity);

        // Authentication
        String auth = username + ":" + apiToken;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        post.setHeader("Authorization", "Basic " + new String(encodedAuth));

        CloseableHttpResponse response = client.execute(post);
        System.out.println("JIRA Screenshot Upload Response: " + response.getStatusLine());

        client.close();
    }
    
    public static String createJiraIssue(String defectSummary, String defectDescription) throws IOException, ParseException {
        // Set up the URL and connection for the JIRA API
    	 String projectKey = "OSG";
        URL url = new URL(jiraURL+"issue");
        System.out.println("url :"+url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
       // connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + apiToken).getBytes()));
        // Set Authorization header using Basic Authentication (email + API token)
        String auth = username + ":" + apiToken;
        System.out.println("auth :"+ auth);
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

        // Create the JSON payload
        String inputJson = String.format(
            "{\"fields\": {" +
            "\"project\": {\"key\": \"%s\"}, " +
            "\"summary\": \"%s\", " +
            "\"description\": \"%s\", " +
            "\"issuetype\": {\"name\": \"Bug\"}}}",
            projectKey, defectSummary, defectDescription);


        // Send the request to create the JIRA issue
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = inputJson.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Get the response and extract the issue key (defect ID)
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            // Read the response to extract the issue key
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String responseString = response.toString();
            System.out.println("Response from JIRA: " + responseString);

            // Parse the JSON response with JSON.simple
            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(responseString);
            String defectID = (String) jsonResponse.get("key");
           String defectURL = (String) jsonResponse.get("self");
            
            System.out.println("Defect ID: " + defectID + "Defect URL :"+defectURL);

            // Return the defect ID
            return defectID;
  
        } else {
            throw new IOException("Failed to create JIRA issue, response code: " + responseCode);
        }
    }
}

