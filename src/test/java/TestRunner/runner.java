
package TestRunner;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

import org.testng.annotations.Listeners;
import com.utils.TestResultListener;

@Listeners(TestResultListener.class)

@CucumberOptions(features="src/test/resources/Features/Reg_Opencart.feature",
glue={"StepDefinition","HooksApp"},
plugin= {"pretty","io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
		"html:target/opencart.html","json:target/opecart.json"
		},
monochrome =true,
dryRun=false,
publish=true
)


public class runner {
	private TestNGCucumberRunner testNGCucumberRunner;
	
	 
	@BeforeClass(alwaysRun = true)
	public void setUpClass() {
		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	}
	
	
 
	@Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
	public void scenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) throws Throwable {
		testNGCucumberRunner.runScenario(pickle.getPickle());
		
	}
 
	@DataProvider
	public Object[][] scenarios() {
 
		if (testNGCucumberRunner == null) {
			testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
		}
		return testNGCucumberRunner.provideScenarios();
	}
 
	@AfterClass(alwaysRun = true)
	public void tearDownClass() {
		testNGCucumberRunner.finish();
	}
}
