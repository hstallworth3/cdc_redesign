package provider;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

public class FindProvider {
	
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
		    //Find Provider screen - click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    System.out.println("Page title is: " + driver.getTitle());
		    //Should get an error message
		    element = driver.findElement(By.id("errorRange"));
		    if ( ! element.getText().equalsIgnoreCase("Please enter at least one item to search on and try again."))
		    {
		    	System.err.println("ERROR :: Find Provider - Test Script 1.1 :::: Incorrect error message on not entering search criteria ");
		    }
		    //Verify Name, Street Address and City operators are defaulted to CONTAINS 
		    WebElement dropdown = driver.findElement(By.name("providerSearch.lastNameOperator"));
		    Select select = new Select(dropdown); 
		    System.out.println("1.." + select.getFirstSelectedOption().getText());
//		    System.out.println("2.." + select.getFirstSelectedOption().toString());
//		    System.out.println("4.." + driver.findElement(By.name("providerSearch.lastNameOperator")).getText());
//		    System.out.println("5.." + driver.findElement(By.name("providerSearch.lastNameOperator_textbox")).toString());
//		    
//		    List list = select.getAllSelectedOptions();
//		    for (Iterator i = list.iterator(); i.hasNext(); )
//		    {
//		    	System.out.println("List.." + i.toString());
//		  
//		    }
		    
		    
		    //enter last name = xxxxyyyyaaaa
		    element = driver.findElement(By.name("providerSearch.lastName"));
		    element.sendKeys("xxxxyyyyaaaa");
		    //click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    //Verify presence of "Add" button
		    if ( ! driver.findElement(By.xpath("//*[@id='Add']")).isDisplayed() )
		    {
		    	System.err.println("ERROR :: Find Provider - Test Script 1.1 :::: Add button not being displayed when search criteria returns 0 results ");
		    }
		  
		    //Click on "New Search"
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td[2]/a"));
		    element.click();
		    //enter last name = "test"
		    element = driver.findElement(By.name("providerSearch.lastName"));
		    element.sendKeys("test");
		    //click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    //Click on "New Search"
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td[2]/a"));
		    element.click();
		    //Enter Last Name = "ProviderLN"
		    element = driver.findElement(By.name("providerSearch.lastName"));
		    element.sendKeys("ProviderLN");
		    //Enter First Name = "ProviderFN"
		    element = driver.findElement(By.name("providerSearch.firstName"));
		    element.sendKeys("ProviderFN");
		    //Enter Street Address = 99 New Street
		    element = driver.findElement(By.name("providerSearch.streetAddr1"));
		    element.sendKeys("99 New Street");
		    //Enter City = Atlanta
		    element = driver.findElement(By.name("providerSearch.cityDescTxt"));
		    element.sendKeys("Atlanta");
		    //click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    //Click on "Refine Search"
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td[2]/a[2]"));
		    element.click();
		    //Verify prepopulated Last Name = "ProviderLN"
		    element = driver.findElement(By.name("providerSearch.lastName"));
		    if ( ! element.getAttribute("value").equalsIgnoreCase("ProviderLN"))
		    {
		    	System.err.println("ERROR :: Find Provider - Test Script 1.1 :::: Erroneous prepopulated values on clicking 'Refine Search' ");
		    }
		    //Verify prepopulated First Name = "ProviderFN"
		    element = driver.findElement(By.name("providerSearch.firstName"));
		    if ( ! element.getAttribute("value").equalsIgnoreCase("ProviderFN"))
		    {
		    	System.err.println("ERROR :: Find Provider - Test Script 1.1 :::: Erroneous prepopulated values on clicking 'Refine Search' ");
		    }
		    //Verify prepopulated Street Address = "99 New Street"
		    element = driver.findElement(By.name("providerSearch.streetAddr1"));
		    if ( ! element.getAttribute("value").equalsIgnoreCase("99 New Street"))
		    {
		    	System.err.println("ERROR :: Find Provider - Test Script 1.1 :::: Erroneous prepopulated values on clicking 'Refine Search' ");
		    }
		    //Verify prepopulated City = "Atlanta"
		    element = driver.findElement(By.name("providerSearch.cityDescTxt"));
		    if ( ! element.getAttribute("value").equalsIgnoreCase("Atlanta"))
		    {
		    	System.err.println("ERROR :: Find Provider - Test Script 1.1 :::: Erroneous prepopulated values on clicking 'Refine Search' ");
		    }
		    //click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    //Click on "New Search"
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td[2]/a"));
		    element.click();
		    //Enter Last Name = "test"
		    element = driver.findElement(By.name("providerSearch.lastName"));
		    element.sendKeys("test");
		    //click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    //Click on "View" link
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[5]/td/form/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/a"));
		    element.click();
		    //Click on "Return to Search Results" link
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td[2]/a"));
		    element.click();
		    //Click on "Logout
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[15]/a/font"));
		    element.click();
		    
		    //Close the browser
			driver.quit();
		    
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    System.out.println(" Sucessfully Completed :: Find Provider Test Script ");
	    
	}

}
