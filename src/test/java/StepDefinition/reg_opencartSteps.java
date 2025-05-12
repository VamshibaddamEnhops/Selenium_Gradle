package StepDefinition;

import com.BaseReq.BaseDriver;
import com.ObjectRepo.Registerelements;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
@Epic("Registration Functonality")
@Story("Create Temparary Emial and Do register")
public class reg_opencartSteps extends BaseDriver{
	Registerelements data=new Registerelements(driver);
	
	public String mail;
	public String password;
	@Given("user generate temperary email")
	public void user_generate_temperary_email() throws Exception {
     mail= data.getTempemail();
	}

	@Given("user navigate to store site")
	public void user_navigate_to_store_site() {
	    data.getStoreSite();
	}

	@When("user enter the Registration details")
	public void user_enter_the_Registration_details() throws Exception {
		Thread.sleep(3000);
	    data.Do_register(mail);
	}

	@When("user click on continue")
	public void user_click_on_continue() {
	   data.click_continue();
	}

	@Then("Validate Account created with Generated Email")
	public void validate_Account_created_with_Generated_Email() throws Exception {
	  Thread.sleep(3000);
			data.getTempEmailGeneartesite();
		
	    data.validate_urlTextinEmail();
	}

	@When("user navigates the Opencart site")
	public void user_navigates_the_Opencart_site() throws Exception {
		  Thread.sleep(3000);
	    data.getopencartStore();
	}

	@When("User enter userEmail and Password")
	public void user_enter_userEmail_and_Password() throws InterruptedException {
		  Thread.sleep(3000);
	    data.EnterData(mail);

	}
	
	@Then("user validates logo of My cart Application")
	public void user_validates_logo_of_My_cart_Application() throws InterruptedException {
		  Thread.sleep(3000);
	    data.LogoValidate();

	}
	
	
	@Then("log out from the application and verify successful log out")
	public void log_out_from_the_application_and_verify_successful_log_out() throws Exception {
		  Thread.sleep(3000);
	    data.logout();

	}
	

	@When("user click on Login")
	public void user_click_on_Login() {
	    try {
			data.Click_Login();
		    System.out.println("User successfully Login to Opencart");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("validate the {string} text")
	public void validate_the_text(String ExpectedText) throws Exception {
data.validate_Text(ExpectedText);	
		}



}
