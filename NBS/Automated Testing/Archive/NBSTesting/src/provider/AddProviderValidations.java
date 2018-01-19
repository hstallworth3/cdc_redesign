package provider;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class AddProviderValidations {
	
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

	    try
	    {

	    	//click on "Data Entry" on the navigation bar
		    element = driver.findElement(By.xpath("/html/body/div/div/form/table/tbody/tr/td/table/tbody/tr/td[3]/a"));
		    element.click();
		    System.out.println("Page title is: " + driver.getTitle());
		    //click on "Provider" on the sub menu
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td[3]/table/tbody/tr/td[5]/a/font"));
		    element.click();
		    System.out.println("Page title is: " + driver.getTitle());
		    //Find Provider screen - enter last name = "test"
		    element = driver.findElement(By.name("providerSearch.lastName"));
		    element.sendKeys("test");
		    //click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    System.out.println("Page title is: " + driver.getTitle());
		    //click "Add"
		    element = driver.findElement(By.name("Add"));
		    element.click();
		    
		    //"Quick Code" 
		    element = driver.findElement(By.name("quickCodeIdDT.rootExtensionTxt"));
		    element.sendKeys("A%");
		    
		    //Click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    
		    //Check the error message for "Quick Code"
		    element = driver.findElement(By.id("error-message-tr"));
		    String quickCodeError = "Quick Code must consist of the following characters: Alphabetic characters from 'A' through 'Z'; numeric characters '0' through '9'; or hyphen '-'.  Please correct the data and try again.";
		    if ( ! element.getText().equalsIgnoreCase(quickCodeError))
		    {
		    	System.err.println("ERROR :: Add Provider Field Validation :::: Erroneous message on entering an alphabeic 'Quick Code' value ");
		    }
		    
		    
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    System.out.println(" Sucessfully Completed :: Add Provider Field Validations Test Script ");
	    
	}


}
