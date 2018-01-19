package provider;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

public class AddProvider {
	
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
//		    element = driver.findElement(By.id("buttonbartop"));
//		    WebElement addButton = element.findElement(By.name("Add"));
//		    addButton.click();
		    List<WebElement> buttonList = driver.findElements(By.name("Add"));
		    System.out.println("Buttons.. Add.." + buttonList.size());
		    element = buttonList.get(1);
		    element.click();
		    
		    //Verify values entered on search are pre-populated on Add page
		    element = driver.findElement(By.name("provider.lastNm"));
		    if ( ! element.getAttribute("value").equalsIgnoreCase("test"))
		    {
		    	System.err.println("ERROR :: Find Provider - Test Script 1.1 :::: Erroneous prepopulated values on 'Add Provider' page ");
		    }
			
		    
		    //click on "Cancel" on the "Add Provider" page
		    // element = driver.findElement(By.name("Cancel"));
		    String parentWindowHandle = driver.getWindowHandle();
		    element = driver.findElement(By.xpath("//*[@id='Cancel']"));
		    element.click();
		    
		    System.out.println("Cancel..");
		    
		    //verify the popup
		    Alert alert = driver.switchTo().alert();
		    String alertText = alert.getText();
		    if  ( ! alertText.equalsIgnoreCase("If you continue with the Cancel action, you will lose any information you have entered. Select OK to continue, or Cancel to not continue."))
		    {
		    	System.err.println("ERROR :: Add Provider :::: Erroneous double dare message when Add Provider is cancelled ");
		    }
		    //click on "Cancel" in the popup
		    alert.dismiss(); 
		    
		    //click on "Cancel" on the "Add Provider" page
		    element = driver.findElement(By.xpath("//*[@id='Cancel']"));
		    element.click();
		    alert = driver.switchTo().alert();
		    //click on "OK" in the popup
			alert.accept();
			driver.switchTo().window(parentWindowHandle);
		    
			//click on "Add"
			element = driver.findElement(By.xpath("//*[@id='Add']"));
		    element.click();
		    
		    //Enter the "Quick Code" 
		    element = driver.findElement(By.name("quickCodeIdDT.rootExtensionTxt"));
		    Random random = new Random();
		    int randomNum = random.nextInt(1000) + 900;
		    element.sendKeys(randomNum + "");
		    //Select "Roles"
		    new Select(driver.findElement(By.id("rolesList"))).selectByVisibleText("Physician");
		    new Select(driver.findElement(By.id("rolesList"))).selectByVisibleText("Public Health Physician");
		    //Enter "General Comments"
		    element = driver.findElement(By.id("provider.thePersonDT.description"));
		    element.sendKeys("add provider general comments");
			
		    //Select "Prefix"
		    driver.findElement(By.name("provider.nmPrefix_button")).click();
		    new Select(driver.findElement(By.id("provider.nmPrefix"))).selectByVisibleText("Doctor/Dr.");
		    //Enter "Last Name"
		    driver.findElement(By.id("provider.lastNm")).clear();
		    driver.findElement(By.id("provider.lastNm")).sendKeys("ProviderLN");
		    //Enter "First Name"
		    driver.findElement(By.id("provider.firstNm")).sendKeys("ProviderFN");
		    //Enter "Middle Name"
		    driver.findElement(By.id("provider.middleNm")).sendKeys("ProviderMN");
		    //Select "Suffix"
		    driver.findElement(By.name("provider.nmSuffix_button")).click();
		    new Select(driver.findElement(By.id("provider.nmSuffix"))).selectByVisibleText("Sr.");
		    //Select "Degree"
		    driver.findElement(By.name("provider.nmDegree_button")).click();
		    new Select(driver.findElement(By.id("provider.nmDegree"))).selectByVisibleText("MD");
		    
		    //Select "Identification Information - Type"
		    driver.findElement(By.name("provider.entityIdDT_s[i].typeCd_button")).click();
		    new Select(driver.findElement(By.id("provider.entityIdDT_s[i].typeCd"))).selectByVisibleText("UPIN");
		    //Select "Identification Information - Assigning Authority"
		    driver.findElement(By.name("provider.entityIdDT_s[i].assigningAuthorityCd_button")).click();
		    new Select(driver.findElement(By.id("provider.entityIdDT_s[i].assigningAuthorityCd"))).selectByVisibleText("Other");
		    //Enter "Identification Information - Other"
		    driver.findElement(By.id("provider.entityIdDT_s[i].assigningAuthorityDescTxt")).sendKeys("other identification");
		    //Enter "Identification Information - ID Value"
		    driver.findElement(By.id("provider.entityIdDT_s[i].rootExtensionTxt")).sendKeys("55555");
		    //Click on "Add Identification"
		    driver.findElement(By.id("BatchEntryAddButtonIdentification")).click();
		    
		    //Select "Address Information - Use"
		    driver.findElement(By.name("address[i].useCd_button")).click();
		    new Select(driver.findElement(By.id("address[i].useCd"))).selectByVisibleText("Primary Work Place");
		    //Select "Address Information - Type"
		    driver.findElement(By.name("address[i].cd_button")).click();
		    new Select(driver.findElement(By.id("address[i].cd"))).selectByVisibleText("Office");
		    //Enter "Address Information - Street Address 1"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr1")).sendKeys("99 New Street");
		    //Enter "Address Information - Street Address 2"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr2")).sendKeys("Suite 100");
		    //Enter "Address Information - City"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.cityDescTxt")).sendKeys("Atlanta");
		    //Select "Address Information - State"
		    driver.findElement(By.name("address[i].thePostalLocatorDT_s.stateCd_button")).click();
		    new Select(driver.findElement(By.id("address[i].thePostalLocatorDT_s.stateCd"))).selectByVisibleText("Georgia");
		    //Enter "Address Information - Zip"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.zipCd")).sendKeys("30345");
		    //Select "Address Information - County"
		    driver.findElement(By.name("address[i].thePostalLocatorDT_s.cntyCd_button")).click();
		    new Select(driver.findElement(By.id("address[i].thePostalLocatorDT_s.cntyCd"))).selectByVisibleText("Fulton County");
		    //Enter "Address Information - Address Comments"
		    driver.findElement(By.id("address[i].locatorDescTxt")).sendKeys("address comments");
		    //Click on "Add Address"
		    driver.findElement(By.id("BatchEntryAddButtonAddress")).click();
		    
		    //driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
		    
		    
		    //Enter "Telephone Information - Country Code"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.cntryCd")).sendKeys("1");
		    //Enter "Telephone Information - Telephone"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt1")).sendKeys("678");
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt2")).sendKeys("444");
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt3")).sendKeys("6666");
		    //Enter "Telephone Information - Email"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.emailAddress")).sendKeys("provider@test.com");
		    //Enter "Telephone Information - URL"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.urlAddress")).sendKeys("test.com");
		    //Enter "Telephone Information - Telephone comments"
		    driver.findElement(By.id("telephone[i].locatorDescTxt")).sendKeys("telephone comments");
		    //Select "Telephone Information - Use"
		    driver.findElement(By.name("telephone[i].useCd_button")).click();
		    new Select(driver.findElement(By.id("telephone[i].useCd"))).selectByVisibleText("Primary Work Place");
		    //Select "Telephone Information - Type"
		    driver.findElement(By.name("telephone[i].cd_button")).click();
		    new Select(driver.findElement(By.id("telephone[i].cd"))).selectByVisibleText("Office");
		    //Click on "Add Telephone"
		    driver.findElement(By.id("BatchEntryAddButtonTelephone")).click();
		    
		    
		    //Click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    
		    
		    //click on "Add" -- Adding provider with the same quick code as above
			element = driver.findElement(By.name("Add"));
		    element.click();
		    
		    //Enter the "Quick Code" 
		    element = driver.findElement(By.name("quickCodeIdDT.rootExtensionTxt"));
		    element.sendKeys(randomNum + "");
		    //Select "Roles"
		    new Select(driver.findElement(By.id("rolesList"))).selectByVisibleText("Public Health Epidemiologist");
		    //Enter "General Comments"
		    element = driver.findElement(By.id("provider.thePersonDT.description"));
		    element.sendKeys("add provider with exisiting quick code");
		    //Enter "Last Name"
		    driver.findElement(By.id("provider.lastNm")).sendKeys("ProviderLN");
		    //Enter "First Name"
		    driver.findElement(By.id("provider.firstNm")).sendKeys("ProviderFN");
		    
		    //Select "Identification Information - Type"
		    driver.findElement(By.name("provider.entityIdDT_s[i].typeCd_button")).click();
		    new Select(driver.findElement(By.id("provider.entityIdDT_s[i].typeCd"))).selectByVisibleText("UPIN");
		    //Enter "Identification Information - ID Value"
		    driver.findElement(By.id("provider.entityIdDT_s[i].rootExtensionTxt")).sendKeys("555");
		    //Click on "Add Identification"
		    driver.findElement(By.id("BatchEntryAddButtonIdentification")).click();
		    
		    //Select "Address Information - Use"
		    driver.findElement(By.name("address[i].useCd_button")).click();
		    new Select(driver.findElement(By.id("address[i].useCd"))).selectByVisibleText("Primary Work Place");
		    //Select "Address Information - Type"
		    driver.findElement(By.name("address[i].cd_button")).click();
		    new Select(driver.findElement(By.id("address[i].cd"))).selectByVisibleText("Office");
		    //Click on "Add Address"
		    driver.findElement(By.id("BatchEntryAddButtonAddress")).click();

		    //Enter "Telephone Information - Telephone"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt1")).sendKeys("678");
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt2")).sendKeys("444");
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt3")).sendKeys("6666");
		    //Enter "Telephone Information - Email"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.emailAddress")).sendKeys("provider@test.com");
		    //Select "Telephone Information - Use"
		    driver.findElement(By.name("telephone[i].useCd_button")).click();
		    new Select(driver.findElement(By.id("telephone[i].useCd"))).selectByVisibleText("Primary Work Place");
		    //Select "Telephone Information - Type"
		    driver.findElement(By.name("telephone[i].cd_button")).click();
		    new Select(driver.findElement(By.id("telephone[i].cd"))).selectByVisibleText("Office");
		    //Click on "Add Telephone"
		    driver.findElement(By.id("BatchEntryAddButtonTelephone")).click();
		    
		    //Click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    
		    //Get the error message for "Quick Code"
		    element = driver.findElement(By.id("error-message-tr"));
		    String quickCodeError = "Quick Code must be a unique Code. The code you have entered already exists. Please enter a different code and try again.";
		    if ( ! element.getText().equalsIgnoreCase(quickCodeError))
		    {
		    	System.err.println("ERROR :: Add Provider :::: Erroneous message on entering an existing 'Quick Code' value ");
		    }
		    
		    
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    System.out.println(" Sucessfully Completed :: Add Provider Test Script ");
	    
	}
	
}
