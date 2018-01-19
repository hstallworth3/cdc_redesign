package nbstestscripts;

import java.awt.SecondaryLoop;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * Login to NBS with username "pks" and search for patient with last name = "test"
 * 
 * By Sarita Cherukuri on 12.13.2013
 * 
 */

public class FindPatient {
	
public static void main(String[] args) {
		
	try
	{
		// Create a new instance of the InternetExplorer driver
	    WebDriver driver = new InternetExplorerDriver();
	    driver.get("http://nedss-tstappsql:7001/nbs/login");
	    System.out.println("Page title is: " + driver.getTitle());

	    WebElement element = driver.findElement(By.name("UserName"));
	    element.sendKeys("pks");

	    element = driver.findElement(By.id("id_Submit_bottom_ToolbarButtonGraphic"));
	    element.click();
	    System.out.println("Page title is: " + driver.getTitle());
	    
	    element = driver.findElement(By.id("DEM102"));
	    element.sendKeys("test");
	    
	    element = driver.findElement(By.xpath("/html/body/div/div/form/div/div[2]/ul/li/div/div/div[3]/div/table/tbody/tr[7]/td/input"));
	    element.click();
   
	    System.out.println("Page title is: " + driver.getTitle());
	    
	    System.out.println("---- Suceesful :: Find Patient ----");
	   
	    
	}
	catch(Exception ex)
	{
		System.out.println("Exception.." + ex.getStackTrace());
	}
	    
	    
	    //Close the browser
	    //driver.quit();
	    
	}


}
