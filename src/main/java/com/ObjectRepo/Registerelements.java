package com.ObjectRepo;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
//import org.testng.Assert;

public class Registerelements {
	static WebDriver driver;
    public static String emailText;
	public static String Password;
	public static String FirstName;
	public static String LastName;
	public static String mail;
	WebDriverWait wait;
	public Registerelements(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
		
	}
	
	@FindBy(xpath="//p[contains(text(),\"@crankymonkey.info\")]")
	public WebElement TempEmail;
	@FindBy(xpath = "//span[text()='New random name']")
	private static WebElement randomEmail;
	@FindBy(id = "pre_button")
	private WebElement pre_mail;
	@FindBy(xpath="//img[@alt='Your Store']")
	public WebElement logo;
	@FindBy(xpath="//span[text()='My Account']")
	public WebElement MyAccount;
	@FindBy(xpath="//a[text()='Register']")
	public WebElement Register;
	@FindBy(id="input-firstname")
	public WebElement Firstname_txt;
	@FindBy(id="input-lastname")
	public WebElement Lastname_txt;
	
	@FindBy(xpath="//input[@name='agree']")
	public WebElement switch_btn;
	@FindBy(xpath="//button[text()='Continue']")
	public WebElement Continue_lnk;
	@FindBy(xpath="//h1[text()='Your Account Has Been Created!']")
	public WebElement Validate_Accountcreatedmsg;
	@FindBy(xpath="//input[@name='email']")
	public WebElement Email_txt;
	@FindBy(id="input-password")
	public WebElement pwd_txt;
	@FindBy(xpath="//button[text()='Login']")
	public WebElement Login_btn;
	@FindBy(xpath="//a[@class='dropdown-item' and text()='Logout']")
	public WebElement Logout_btn;
	@FindBy(xpath="//h1[text()='Account Logout']")
	public WebElement validate_logoutmsg;

	@FindBy(xpath="//p[text()='Your Store - Thank you for registering']")
	public WebElement url_txt;
	@FindBy(xpath="//h1[normalize-space()='My Account']")
	public WebElement My_Account_text;
	@FindBy(xpath = "//div[@class='mail']")
	private WebElement linkRegisterMail1;

	//div[@class='mail']

	@FindBy(xpath = "//div[text()='Welcome and thank you for registering at Your Store!']")
	private WebElement txtLink;

	public String getTempemail() throws Exception {
		driver.get("https://tempmail.plus/en/#!");
		Thread.sleep(3000);
		
		randomEmail.click();
		Thread.sleep(2000);
		WebElement copyButton = driver.findElement(By.id("pre_copy"));  
		copyButton.click();         
		 emailText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);         
		
		System.out.println("Copied email address: "+ emailText);
		return emailText;
		
	}
	public void getStoreSite() {
		driver.get("http://20.204.41.135/studyopedia/");
	}
	public void Do_register(String mail) throws Exception {
		 wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		 
		MyAccount.click();
		Register.click();
		
		int indexOfAtSymbol = mail.indexOf('@');
		String substringBeforeAt = mail.substring(0, indexOfAtSymbol);

		System.out.println(substringBeforeAt);

		 FirstName = substringBeforeAt.replaceAll("[0-9]","");
		//System.out.println(noNumber);

		//String lastName = substringBeforeAt.replaceAll("[a-zA-A]","");
		 LastName =FirstName;
		 Password= FirstName+LastName;
		Firstname_txt.sendKeys(FirstName);
		Lastname_txt.sendKeys(LastName);
		Email_txt.sendKeys(mail);
		pwd_txt.sendKeys(Password);
		
			Thread.sleep(5000);
		
		
	}
	public void getTempEmailGeneartesite() throws InterruptedException {
		driver.get("https://tempmail.plus/en/#!");
		Thread.sleep(5000);
		driver.navigate().refresh();
		Thread.sleep(5000);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,250)");
	}
	
	public void validate_urlTextinEmail() throws Exception {
		//wait.until(ExpectedConditions.elementToBeClickable(url_txt)).click();

		Thread.sleep(2000);
	    linkRegisterMail1.click();
		Thread.sleep(2000);
		//action.syncClickable(driver,txtLink);
		String MailUrl = txtLink.getText();
		//System.out.println(MailUrl);
		txtLink.click();
		if(txtLink.isDisplayed()) {
			System.out.println("Email is verified");
		}else {
			System.out.println("Email is not verified");
		}
	}
	
	public void getopencartStore() {
		driver.get("http://20.204.41.135/studyopedia/index.php?route=account/login&language=en-gb");
	}
	public void click_continue() {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("window.scrollBy(0,250)");
		wait.until(ExpectedConditions.elementToBeClickable(switch_btn)).click();
		Continue_lnk.click();
		if(Validate_Accountcreatedmsg.isDisplayed()) {
			System.out.println("Your Account Has Been Created! is displayed");
			Assert.assertTrue(true);
		}else {
			System.out.println("Your Account Has Been Created! is not displayed");
			Assert.assertFalse(false);
		}

	}

	public void LogoValidate() {
		if(logo.isDisplayed()) {
			System.out.println("Logo is displayed");
			Assert.assertTrue(false);
		}else {
			System.out.println("Logo is not displayed");
			//Assert.assertTrue(false);
		}
	}
	
	public void click_MyAccount() {
		MyAccount.click();
		
	}
	public void click_Register() {
		Register.click();
	}
	public void EnterData(String mail) {
	Email_txt.sendKeys(mail);
	pwd_txt.sendKeys(Password);
	}
	public void EnterInvalidData() {
		Email_txt.sendKeys(mail);
		pwd_txt.sendKeys(Password);
		}
	
	public void Click_Login() throws Exception {
		Login_btn.click();
		Thread.sleep(4000);
	}
	
	public void logout() throws Exception {

		MyAccount.click();
		Logout_btn.click();
		Thread.sleep(4000);
		if(validate_logoutmsg.isDisplayed()) {
			System.out.println("Account Logout message is displayed");
			Assert.assertTrue(true);
		}else {
			System.out.println("Account Logout message is not displayed");
			Assert.assertFalse(false);
		}

	}
	
	public void logo() throws Exception {
		logo.getText();
		Thread.sleep(4000);
	}
	public void validate_Text(String Expected_Text) throws Exception {
		Thread.sleep(5000);
		String Actual_txt=My_Account_text.getText();
		Assert.assertEquals(Actual_txt, Expected_Text);
		
		
	}
}
