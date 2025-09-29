package com.BaseReq;

import java.io.InputStreamReader;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.BufferedReader;

public class BaseDriver {
	
	public static WebDriver driver;
	
	public static void initbrowser(String Browser) throws Exception 
	{
		//Browser="edge";
		switch(Browser) {
		case "chrome":
			
			String driverPath = System.getProperty("user.dir") + "/drivers/chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", driverPath);
		    driver = new ChromeDriver();

		//	WebDriverManager.chromedriver().setup();
			

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
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		
	}
	
}
