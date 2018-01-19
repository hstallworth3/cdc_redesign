package nbstestscripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;


/*
 * Login to NBS with username "pks".
 * 
 * By Sarita Cherukuri on 12.13.2013
 * 
 */

public class NBSLogin {
	
	public static void main(String[] args) {
		
		// Create a new instance of the InternetExplorer driver
	    WebDriver driver = new InternetExplorerDriver();
	    driver.get("http://nedss-tstappsql:7001/nbs/login");
	    System.out.println("Page title is: " + driver.getTitle());

	    // Find the UserName element by its name
	    WebElement element = driver.findElement(By.name("UserName"));
	    element.sendKeys("pks");

	    // Find the Submit button by its name
	    element = driver.findElement(By.id("id_Submit_bottom_ToolbarButtonGraphic"));
	    element.click();
	    System.out.println("Page title is: " + driver.getTitle());
	    
	    System.out.println("---- Suceesful :: Login ----");
   
	    //Close the browser
	    driver.quit();
	    
	}

	

}
