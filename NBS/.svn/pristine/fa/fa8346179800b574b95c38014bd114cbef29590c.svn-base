package com.nbs.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Login {
	public WebDriver driver;
	public String Data_File_Path, Log_File_Path, Screenshot_File_Path,File_Path,JACOB_DLL;
	InputStream input_Stream;
	String baseUrl, browser,IEdriverpath,Chromedriverpath,username,password;

	public Login() {
		// Loading the dataFile.properties
				input_Stream = getClass().getResourceAsStream("/dataFile.properties");
				Properties prop = new Properties();
				try {
					prop.load(input_Stream);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Setting the browser based on the properties file
				 baseUrl = prop.getProperty("URL");
				 browser = prop.getProperty("browser");
				 IEdriverpath = prop.getProperty("IE_Driver_Path");
				 Chromedriverpath = prop.getProperty("Chrome_Driver_Path");
				 username = prop.getProperty("username");
				 password = prop.getProperty("password");
				Data_File_Path = prop.getProperty("Data_File_Path");
				Log_File_Path = prop.getProperty("Log_File_Path");
				Screenshot_File_Path = prop.getProperty("Screenshot_File_Path");
				File_Path = prop.getProperty("File_Path");
				JACOB_DLL = prop.getProperty("JACOB_DLL");
	}	
	
	public void Setup() throws Exception {	
		// DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
	
		switch (browser) {
		case "IE":
			System.setProperty("webdriver.ie.driver", IEdriverpath);
			DesiredCapabilities caps1 = DesiredCapabilities.internetExplorer();
			caps1.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			// caps.setCapability("ignoreZoomSetting", true);
			driver = new InternetExplorerDriver();
			break;
		case "Firefox":
			driver = new FirefoxDriver();
			break;
		case "Chrome":
			System.setProperty("webdriver.chrome.driver", Chromedriverpath);
			driver = new ChromeDriver();
			break;
		default:
			driver = new FirefoxDriver();
			break;
		}
		
		// Opening the NBS application
		// driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(baseUrl + "/nbs/login");
		driver.manage().window().maximize();
		// driver.manage().window().setPosition(new Point(-2000, 0));
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(By.id("id_UserName")).clear();
		driver.findElement(By.id("id_UserName")).sendKeys(username);
		driver.findElement(By.id("id_Password")).clear();
		driver.findElement(By.id("id_Password")).sendKeys(password);
		driver.findElement(By.id("id_Submit_bottom_ToolbarButtonGraphic")).click();
		}
	

	
	public WebDriver getDriver() {
		return driver;
	}

	public String getDataFilePath() {
		return Data_File_Path;
	}

	public String getLogFilePath() {
		return Log_File_Path;
	}
}