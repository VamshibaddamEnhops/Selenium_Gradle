package com.BaseReq;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		
	}
	

}
