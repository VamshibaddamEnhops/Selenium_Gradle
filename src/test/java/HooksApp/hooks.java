package HooksApp;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import io.cucumber.java.BeforeAll;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;

import com.BaseReq.BaseDriver;
import com.BaseReq.LocalizationUtil;
import com.BaseReq.ScreenRecorderUtil;
import com.google.common.io.Files;
import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import testrailmanager.JiraAPI;
import testrailmanager.TestTrailRunner;



public class hooks {

    private JiraAPI jiraAPI = new JiraAPI();
    public String TestCaseID;
    public static String testRailCaseId;
    static Properties p;

//	@Before(order=0)
//    public void setup() {
//        String language = System.getProperty("lang", "en");
//        System.out.println("Running tests in language: " + language);
//        LocalizationUtil.loadLanguage(language);
    //}

    @Before
    public void beforeScenario(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames();

        for (String tag : tags) {
            if (tag.matches("@C\\d+")) {
                testRailCaseId = tag.substring(2);
                System.out.println("Executing TestRail Test Case ID: " + testRailCaseId);
                break;
            }
        }
    }

    @Before(order = 0)
    public void setup() throws Exception {
        ScreenRecorderUtil.startRecord("setup");
    }


    @Before(order = 1)
    public static void getPropertyFile() throws Exception {
        //FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//config//config.properties");
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/config/config.properties");
        p = new Properties();
        p.load(fis);
        //System.out.println(p.getProperty("url"));
    }

    @Before(order = 2)
    public void getDriver() {
        BaseDriver.initbrowser(p.getProperty("browser"));
//        String url = p.getProperty("url");
//        BaseDriver.driver.get(url);

    }

    @After(order = 2)
    public void tearDown(Scenario scenario) throws Exception {

        if (scenario.isFailed()) {
            TakesScreenshot ts = (TakesScreenshot) BaseDriver.driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            File src = ts.getScreenshotAs(OutputType.FILE);
            File dest = new File(System.getProperty("user.dir") + "/Screenshots/" + scenario.getName() + ".png");
            FileUtils.copyFile(src, dest);
            Allure.addAttachment(scenario.getName(), new ByteArrayInputStream(((TakesScreenshot) BaseDriver.driver).getScreenshotAs(OutputType.BYTES)));
            //Allure.link("https://stackoverflow.com/questions/50941193/how-to-add-link-on-test-case-failure-in-allure-report");

            ScreenRecorderUtil.stopRecord();
            Thread.sleep(8000);
        }
    }

    @After(order = 1)
    public void teardown() {
        BaseDriver.driver.close();

    }

    @After(order = 1)
    public void recording() throws IOException {

        File video = new File(p.getProperty("testRec"));
        Allure.addAttachment("screenRecording.avi", "video/avi", Files.asByteSource(video).openStream(), "avi");
    }


    APIClient client = TestTrailRunner.client;

    @After(order = 1)
    public void addResultsToTestRail(Scenario scenario) throws MalformedURLException, IOException, APIException, Exception {
        TestTrailRunner runner = new TestTrailRunner();
        //String testCaseID = getTestRailCaseID(scenario); // Extract Test Case ID from tags
        String testCaseID = testRailCaseId;

//        if (testCaseID == null) {
//            System.out.println("No TestRail ID found for this scenario.");
//            return;
//        }

        if (scenario.isFailed()) {
            // Capture error message
            String errorMessage = scenario.getStatus().toString();

            // Capture screenshot and save as a file
            File screenshotFile = ((TakesScreenshot) BaseDriver.driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = "Screenshots/" + scenario.getName() + ".png";
            FileUtils.copyFile(screenshotFile, new File(screenshotPath));

            // Add defect to Jira (now passing errorMessage and screenshot path)
            String defectID = TestTrailRunner.defectAdd(scenario, errorMessage, screenshotPath);

            // Add test results to TestRail
            TestTrailRunner.addResultsForTestCase(client, testCaseID, TestTrailRunner.testcase_FAIL_Status, errorMessage);

            // Link defect to TestRail only if defectID is available
            if (defectID != null) {
                TestTrailRunner.linkDefectToTestRail(testCaseID, defectID);
            } else {
                System.err.println("Defect ID is null. Skipping defect linking.");
            }

        } else {
            TestTrailRunner.addResultsForTestCase(client, testCaseID, TestTrailRunner.testcase_PASS_Status, "Test Passed");
            runner.getResult(client, testCaseID);
//            TestTrailRunner.byID(client, testCaseID);
        }
    }
    @BeforeAll
    public static void cleanUpBeforeRun() {
        LocalizationUtil.deleteFilesAndFolder(Paths.get("Screenshots").toString());
        LocalizationUtil.deleteFile(Paths.get("src", "main", "resources", "setup.avi").toString());
        LocalizationUtil.deleteFilesAndFolder(Paths.get("allure-results").toString());

    }

    private String getTestRailCaseID(Scenario scenario) {
        for (String tag : scenario.getSourceTagNames()) {
            if (tag.startsWith("@C")) { // Assuming @C1234 format for TestRail IDs
                return tag.substring(1);
            }
        }
        return null;
    }
}
