package testrailmanager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.ITestResult;

import com.BaseReq.ConfigReader;
import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;

import io.cucumber.java.Scenario;

public class TestTrailRunner {
	public static String Test_Run_ID = ConfigReader.get("testRunID");
	//
	//"
	public static String TEST_RAIL_USERNAME = ConfigReader.get("testRailUsername");
	public static String TEST_RAIL_PASSWORD = ConfigReader.get("testRailPassword");

	public static String TEST_RAIL_ENGINE_URL = ConfigReader.get("testRailEngineURL");

	public static int testcase_PASS_Status = 1;
	public static int testcase_FAIL_Status = 5;
	static String TestRunID =  Test_Run_ID;
	//String testCaseId;
	public static APIClient client = new APIClient(TEST_RAIL_ENGINE_URL);;
	public static void addResultsForTestCase(APIClient client,String testCaseId, int status, String Error) throws MalformedURLException, IOException, APIException
	{
		String TestRunID =  Test_Run_ID;
		// client = new APIClient(TEST_RAIL_ENGINE_URL);
		client.setUser(TEST_RAIL_USERNAME);
		client.setPassword(TEST_RAIL_PASSWORD);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("status_id", status);
		data.put("Comment", "This test passed");
		data.put("assignedto_id",1);


		try {
			client.sendPost("add_result_for_case/"+TestRunID+"/"+testCaseId+"", data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (APIException e) {

			e.printStackTrace();
		}
	}

	public  void getResult(APIClient client, String testCaseId) throws MalformedURLException, IOException, APIException {
		Object object=client.sendGet("get_results_for_case/"+TestRunID+"/"+testCaseId);
		System.out.println(object.toString());
	}

	public static void byID(APIClient client,String TestcaseId) throws  IOException, APIException {
		Object object=client.sendGet("get_cases/"+TestcaseId);
		System.out.println("TestID"+object.toString());
	}

	// Method to create a defect in TestRail (on failure)
	public static void createDefect(APIClient client, String testCaseId, String defectSummary, String defectDescription)
			throws IOException, APIException {
		String defectStatus = "Defect Raised";
		Map<String, Object> defectData = new HashMap<>();
		defectData.put("status_id", testcase_FAIL_Status); // Mark as failed
		defectData.put("title", defectSummary); // Defect summary (test case name or any relevant title)
		defectData.put("description", defectDescription); // Error description
		defectData.put("assignedto_id", 1); // Assign to user ID
		defectData.put("comment", "test comment");
		// defectData.put("defect_id", defectID); 
		// Send the defect details to TestRail (you may need to customize this based on your TestRail setup)
		try {
			client.sendPost("add_result_for_case/" + TestRunID + "/" + testCaseId, defectData);
			System.out.println("Defect created and linked to Test Case ID: " + testCaseId);
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
	}
	public static String defectAdd(Scenario scenario, String errorMessage, String screenshotPath) throws ParseException, APIException {
	    // Generate a defect summary and description
	    String defectSummary = "Failure in test case: " + scenario.getName();
	    String defectDescription = "Test Case Failed due to defect number: " + errorMessage;

	    try {
	        // Create JIRA defect
	        String defectID = JiraAPI.createJiraIssue(defectSummary, defectDescription);

	        if (defectID != null && !defectID.isEmpty()) {
	            System.out.println("Defect created in JIRA: " + defectID);

	            // Attach Screenshot to Jira
	            JiraAPI.attachScreenshotToJira(defectID, screenshotPath);

	            return defectID;
	        } else {
	            System.err.println("Failed to create JIRA defect.");
	            return null;
	        }
	    } catch (IOException e) {
	        System.err.println("Error while creating JIRA defect: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
	}
	public static void linkDefectToTestRail(String testCaseID, String defectID) throws APIException {
		if (defectID != null) {
			try {
				String endpoint = "add_result_for_case/" + TestRunID +"/"+ testCaseID;
				// Prepare the data to send to TestRail
				JSONObject data = new JSONObject();

				// Provide a valid status ID (1: Passed, 5: Failed, etc.)
				data.put("status_id", 5); 

				data.put("assignedto_id", 1); // Assigned to user ID (replace with valid user ID)

				data.put("comment", "Test failed due to: " + defectID); //
				data.put("defects", defectID); 
				// Link the defect to the TestRail test case
				client.sendPost(endpoint, data);
				System.out.println("Defect " + defectID + " linked to TestRail Test Case ID: " + testCaseID);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	// Attach screenshot to TestRail using TestRail REST API
	public static void attachScreenshotToTestRail(String screenshotPath, String testRailCaseId) throws IOException, org.apache.hc.core5.http.ParseException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(TEST_RAIL_ENGINE_URL + "index.php?/api/v2/add_attachment_to_case/" + testRailCaseId);
//
//		// Set authorization header
//		//test rail api key--Z55r.iZ7fx/ZjSZ6IUu0-LTbtriV8uDO7yqyI.rYV
		String auth = TEST_RAIL_USERNAME + ":" + "TEST_RAIL_PASSWORD";
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
		postRequest.setHeader("Authorization", "Basic " + encodedAuth);

		// Prepare the file to upload
		File screenshotFile = new File(screenshotPath);
		FileBody fileBody = new FileBody(screenshotFile);

	// Create multipart entity
		HttpEntity entity = MultipartEntityBuilder.create()
				.addPart("file", fileBody)
				.build();

		postRequest.setEntity(entity);
//
		
		httpClient.execute(postRequest);
		
		System.out.println("Screenshot uploaded to TestRail: " + screenshotPath);
	}
}
