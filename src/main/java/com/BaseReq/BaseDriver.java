package com.BaseReq;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseDriver {
	
	public static WebDriver driver;
	
	 public static void initbrowser(String Browser)
	 {
	 	//Browser="edge";
	 	switch(Browser) {
	 	case "chrome":
	 		//WebDriverManager.chromedriver().setup();
	 		 driver=new ChromeDriver();
	 		break;
	 	case "edge":
	 		//WebDriverManager.edgedriver().setup();
	 		 driver=new EdgeDriver();
	 		System.out.println("");
	 		break;
	 	case "firefox":
	 		//WebDriverManager.firefoxdriver().setup();
	 		 driver=new FirefoxDriver();
	 		break;
	 	case "ie":
	 		//WebDriverManager.iedriver().setup();
	 		 driver=new InternetExplorerDriver();
	 		break;
	 		default:
	 			System.out.println("provide valide driver");
				
			
	 	}

//	public static void initbrowser(String Browser)
//	{
//		// Explicitly set the headless property to true
//                 System.setProperty("java.awt.headless", "true");
//		switch(Browser.toLowerCase()) { // Make sure the browser name is case insensitive
//			case "chrome":
//				WebDriverManager.chromedriver().setup();  // Automatically download ChromeDriver
//				ChromeOptions chromeOptions = new ChromeOptions();
//				chromeOptions.addArguments("--headless");
//				// Optionally add some arguments to the Chrome browser (e.g., headless mode)
//				chromeOptions.addArguments("--start-maximized");  // Starts browser maximized
//				chromeOptions.addArguments("--disable-extensions"); // Disable extensions
//				driver = new ChromeDriver(chromeOptions);  // Initialize Chrome with options
//				break;
//
//			case "edge":
//				WebDriverManager.edgedriver().setup();  // Automatically download EdgeDriver
//				EdgeOptions edgeOptions = new EdgeOptions();
//				edgeOptions.addArguments("--start-maximized");
//				driver = new EdgeDriver(edgeOptions);  // Initialize Edge with options
//				break;
//
//			case "firefox":
//				WebDriverManager.firefoxdriver().setup();  // Automatically download GeckoDriver
//				FirefoxOptions firefoxOptions = new FirefoxOptions();
//				firefoxOptions.addArguments("--start-maximized");
//				driver = new FirefoxDriver(firefoxOptions);  // Initialize Firefox with options
//				break;
//
//			case "ie":
//				WebDriverManager.iedriver().setup();  // Automatically download InternetExplorerDriver
//				driver = new InternetExplorerDriver();  // Initialize IE
//				break;
//
//			default:
//				System.out.println("Please provide a valid browser name (chrome, edge, firefox, ie).");
//				return;  // If no valid browser is provided, return from the method
//		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		
	}
	

}
