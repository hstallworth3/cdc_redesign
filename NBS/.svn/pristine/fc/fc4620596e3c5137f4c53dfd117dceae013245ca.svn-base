package securityAdministration;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


/*
 * Security Administration - Add - View User Test Script 1.1
 * 
 * By Sarita Cherukuri on 01.02.2014
 * 
 */


public class AddViewUser {
	
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
	    	
	    	//click on "System Management" on the menu
		    element = driver.findElement(By.xpath("/html/body/div/div/form/table/tbody/tr/td/table/tbody/tr/td[11]/a"));
		    element.click();
		    System.out.println("Page title is: " + driver.getTitle());
	    
		    //click on "Security Management"
		    element = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/table[7]/thead/tr/th/a/img"));
		    element.click();
		    //click on "Manage Users"
		    element = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/table[7]/tbody/tr/td/table/tbody/tr[2]/td/a"));
		    element.click();
		    System.out.println("Page title is: " + driver.getTitle());
	    
		    //click on "Add" on the "Users" page
		    element = driver.findElement(By.name("Add"));
		    element.click();
		    System.out.println("Page title is: " + driver.getTitle());
		    
		    String parentWindowHandle = driver.getWindowHandle();
		    
		    //click on "Cancel" on the "Add User" page
		    element = driver.findElement(By.name("Cancel"));
		    element.click();
		    
		    //verify the popup
		    Alert alert = driver.switchTo().alert();
		    String alertText = alert.getText();
		    if  ( ! alertText.equalsIgnoreCase("If you continue with the Cancel action, you will lose any information you have entered. Select OK to continue, or Cancel to not continue."))
		    {
		    	System.err.println("ERROR :: Security Administration - Test Script 1.1 :::: Double dare message error when Add User is cancelled ");
		    }
		    //click on "Cancel" in the popup
		    alert.dismiss(); 
		    
		    //click on "Cancel" on the "Add User" page
		    element = driver.findElement(By.name("Cancel"));
		    element.click();
		    alert = driver.switchTo().alert();
		    //click on "OK" in the popup
			alert.accept();
					    
			driver.switchTo().window(parentWindowHandle);

			//click on "Add" on the "Users" page
		    element = driver.findElement(By.name("Add"));
		    element.click();
		    
		    System.out.println("Page title is: " + driver.getTitle());
	    
		    //User ID
		    element = driver.findElement(By.id("userProfile.theUser_s.userID"));
		    Random random = new Random();
		    int randomNum = random.nextInt(1000) + 900;
		    element.sendKeys(randomNum + "");
		    //First Name
		    element = driver.findElement(By.id("userProfile.theUser_s.firstName"));
		    element.sendKeys("test");
		    //Last Name
		    element = driver.findElement(By.id("userProfile.theUser_s.lastName"));
		    element.sendKeys("user");
		    //Status
		    element = driver.findElement(By.id("ACTIVE"));
		    element.click();
		    //External
//		    element = driver.findElement(By.id("External"));
//		    element.click();
		    
		    //Facility Information
		    element = driver.findElement(By.id("Org-ReportingOrganizationUIDButton"));
		    element.click();
		    
		    driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);

//		    WebDriverWait wait = new WebDriverWait(driver, 20);
//		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("organizationSearch.nmTxt")));

		    //Switch to new window opened
//		    for(String winHandle : driver.getWindowHandles()){
//		        driver.switchTo().window(winHandle);
//		    }

		    System.out.println(driver.getWindowHandles().size());
		    
		    for(String windowHandle  : driver.getWindowHandles())
		    {
		    	System.out.println("HEREEEEEEEEEEEEEEEE 1");
		    	System.out.println(parentWindowHandle);
		    	System.out.println(windowHandle);
		    	if(!windowHandle.equals(parentWindowHandle))
		        {
		           driver.switchTo().window(windowHandle);
		           System.out.println("HEREEEEEEEEEEEEEEEE");
		        }
		    }
		    
		    // Enter the name of the hospital, click "Submit" and click "Select" 
		    element = driver.findElement(By.name("organizationSearch.nmTxt"));
		    element.sendKeys("ABC Hospital");
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[5]/td/form/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/a"));
		    element.click();

		    //Close the new window, if that window no more required
		    // driver.close();
		  	
		    //Switch back to original browser
		  	driver.switchTo().window(parentWindowHandle);
		    
		    //Jurisdiction
		    element = driver.findElement(By.name("userProfile.theRealizedRole_s[i].jurisdictionCode_button"));
		    element.click();
		    WebElement dropdown = driver.findElement(By.name("userProfile.theRealizedRole_s[i].jurisdictionCode")); 
		    Select select = new Select(dropdown); 
		    select.selectByVisibleText("All");
		    //Program Area
		    element = driver.findElement(By.name("userProfile.theRealizedRole_s[i].programAreaCode_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("userProfile.theRealizedRole_s[i].programAreaCode")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("GCD");
		    //Permission Set
		    element = driver.findElement(By.name("userProfile.theRealizedRole_s[i].roleName_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("userProfile.theRealizedRole_s[i].roleName")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("NEDSS Epidemiologist");
		    //check if "Guest" is checked
		    if (driver.findElement(By.name("userProfile.theRealizedRole_s[i].guestString")).isSelected())
		    {	
		    	System.err.println("ERROR :: Security Administration - Test Script 1.1 :::: Add User screen, Guest checkbox is selected");
		    }
		    //"Add Role" button in "Role Setup"
		    element = driver.findElement(By.id("BatchEntryAddButtonRole"));
		    element.click();
		    
		    element = driver.findElement(By.name("Submit"));
		    //element.click();
	    
	    
	    
//	    String parentWindowHandle = driver.getWindowHandle(); // save the current window handle.
//	    WebDriver popup = null;
//	    Iterator<String> windowIterator = driver.getWindowHandles().iterator();
//	      while(windowIterator.hasNext()) { 
//	        String windowHandle = windowIterator.next(); 
//	        popup = driver.switchTo().window(windowHandle);
//	        if (popup.getTitle().equals("Message from webpage")) {
//	          break;
//	        }
//	      }
//	      
//	      popup.findElement(By.name("Cancel")).click();
// 
//	      driver.close(); // close the popup.
//	      driver.switchTo().window(parentWindowHandle);
		
		
		    System.out.println(" Suceesfully Completed :: Security Administration - Test Script 1.1 ");
	      
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    }
	    
	    
	}
	
	
	public boolean isElementPresent(By element, WebDriver driver) {
		   try {
		       driver.findElement(element);
		       return true;
		   } catch (NoSuchElementException e) {
		       return false;
		   }
		}
	
}
