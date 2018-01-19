package provider;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class EditProvider {
	
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
		    //click "Add"
		    element = driver.findElement(By.id("buttonbartop"));
		    WebElement addButton = element.findElement(By.name("Add"));
		    addButton.click();

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
		    driver.findElement(By.id("provider.lastNm")).sendKeys("EditProviderLN");
		    //Enter "First Name"
		    driver.findElement(By.id("provider.firstNm")).sendKeys("EditProviderFN");
		    //Enter "Middle Name"
		    driver.findElement(By.id("provider.middleNm")).sendKeys("EditProviderMN");
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
		    driver.findElement(By.id("provider.entityIdDT_s[i].rootExtensionTxt")).sendKeys("4444");
		    //Click on "Add Identification"
		    driver.findElement(By.id("BatchEntryAddButtonIdentification")).click();
		    
		    //Select "Address Information - Use"
		    driver.findElement(By.name("address[i].useCd_button")).click();
		    new Select(driver.findElement(By.id("address[i].useCd"))).selectByVisibleText("Primary Work Place");
		    //Select "Address Information - Type"
		    driver.findElement(By.name("address[i].cd_button")).click();
		    new Select(driver.findElement(By.id("address[i].cd"))).selectByVisibleText("Office");
		    //Enter "Address Information - Street Address 1"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr1")).sendKeys("100 New Street");
		    //Enter "Address Information - Street Address 2"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr2")).sendKeys("Suite 46");
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
		    
		    //Enter "Telephone Information - Country Code"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.cntryCd")).sendKeys("1");
		    //Enter "Telephone Information - Telephone"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt1")).sendKeys("678");
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt2")).sendKeys("333");
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt3")).sendKeys("4444");
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
		    
		    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)  
		             .withTimeout(60, TimeUnit.SECONDS)  
		             .pollingEvery(2, TimeUnit.SECONDS)  
		             .ignoring(NoSuchElementException.class); 

		    WebElement e= wait.until(new Function<WebDriver, WebElement>() {  
		           public WebElement apply(WebDriver d) {  
		             return d.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td"));  
		            }  
		     });  
		     

		    //Get the Provider ID
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td"));
		    String providerId = element.getText();
		    providerId = providerId.substring(13);
		    System.out.println("....." + providerId);
		    
		    //Click "Edit"
		    element = driver.findElement(By.name("Edit"));
		    element.click();
		    
		    //Select radio button for Typographical error: 'Typographical error correction or additional information'
		    element = driver.findElement(By.xpath("//*[@id='c']"));
		    element.click();
		    
		    // ***** Edit data in all applicable fields *****
		    //Enter the "Quick Code" 
		    element = driver.findElement(By.name("quickCodeIdDT.rootExtensionTxt"));
		    randomNum = random.nextInt(1000) + 900;
		    element.clear();
		    element.sendKeys(randomNum + "");
		    //Select "Roles"
		    new Select(driver.findElement(By.id("rolesList"))).selectByVisibleText("Public Health Epidemiologist");
		    //Enter "General Comments"
		    element = driver.findElement(By.id("provider.thePersonDT.description"));
		    element.clear();
		    element.sendKeys("general comments edited");

		    //Select "Prefix"
		    driver.findElement(By.name("provider.nmPrefix_button")).click();
		    new Select(driver.findElement(By.id("provider.nmPrefix"))).selectByVisibleText("Mr.");
		    //Enter "Last Name"
		    driver.findElement(By.id("provider.lastNm")).clear();
		    driver.findElement(By.id("provider.lastNm")).sendKeys("EditProvLN");
		    //Enter "First Name"
		    driver.findElement(By.id("provider.firstNm")).clear();
		    driver.findElement(By.id("provider.firstNm")).sendKeys("EditProvFN");
		    //Enter "Middle Name"
		    driver.findElement(By.id("provider.middleNm")).clear();
		    driver.findElement(By.id("provider.middleNm")).sendKeys("EditProvMN");
		    //Select "Suffix"
		    driver.findElement(By.name("provider.nmSuffix_button")).click();
		    new Select(driver.findElement(By.id("provider.nmSuffix"))).selectByVisibleText("Jr.");
		    //Select "Degree"
		    driver.findElement(By.name("provider.nmDegree_button")).click();
		    new Select(driver.findElement(By.id("provider.nmDegree"))).selectByVisibleText("MS");
		    
		    //Click on "Edit" in the Identification Information section
		    element = driver.findElement(By.xpath("//TBODY[@id='nestedElementsHistoryBox|Identification']/TR/TD[1]/A[1]"));
		    element.click();
		    
		    //Select "Identification Information - Type"
		    driver.findElement(By.name("provider.entityIdDT_s[i].typeCd_button")).click();
		    new Select(driver.findElement(By.id("provider.entityIdDT_s[i].typeCd"))).selectByVisibleText("Employer number");
		    //Select "Identification Information - Assigning Authority"
		    driver.findElement(By.name("provider.entityIdDT_s[i].assigningAuthorityCd_button")).click();
		    new Select(driver.findElement(By.id("provider.entityIdDT_s[i].assigningAuthorityCd"))).selectByVisibleText("CMS Provider");
		    //Enter "Identification Information - ID Value"
		    driver.findElement(By.id("provider.entityIdDT_s[i].rootExtensionTxt")).sendKeys("333");
		    //Click on "Update Identification"
		    driver.findElement(By.id("BatchEntryAddButtonIdentification")).click();
		    
		   
		    //Click on "Edit" in the Address Information section
		    element = driver.findElement(By.xpath("//TBODY[@id='nestedElementsHistoryBox|Address']/TR/TD[1]/A[1]"));
		    element.click();
		    
		    //Select "Address Information - Use"
		    driver.findElement(By.name("address[i].useCd_button")).click();
		    new Select(driver.findElement(By.id("address[i].useCd"))).selectByVisibleText("Alternate Work Place");
		    //Select "Address Information - Type"
		    driver.findElement(By.name("address[i].cd_button")).click();
		    new Select(driver.findElement(By.id("address[i].cd"))).selectByVisibleText("Postal/Mailing");
		    //Enter "Address Information - Street Address 1"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr1")).clear();
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr1")).sendKeys("1000 New Street");
		    //Enter "Address Information - Street Address 2"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr2")).clear();
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr2")).sendKeys("Suite 46");
		    //Enter "Address Information - City"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.cityDescTxt")).clear();
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.cityDescTxt")).sendKeys("Decatur");
		    //Select "Address Information - State"
		    driver.findElement(By.name("address[i].thePostalLocatorDT_s.stateCd_button")).click();
		    new Select(driver.findElement(By.id("address[i].thePostalLocatorDT_s.stateCd"))).selectByVisibleText("Georgia");
		    //Enter "Address Information - Zip"
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.zipCd")).clear();
		    driver.findElement(By.id("address[i].thePostalLocatorDT_s.zipCd")).sendKeys("30005");
		    //Select "Address Information - County"
		    driver.findElement(By.name("address[i].thePostalLocatorDT_s.cntyCd_button")).click();
		    new Select(driver.findElement(By.id("address[i].thePostalLocatorDT_s.cntyCd"))).selectByVisibleText("Fulton County");
		    //Enter "Address Information - Address Comments"
		    driver.findElement(By.id("address[i].locatorDescTxt")).clear();
		    driver.findElement(By.id("address[i].locatorDescTxt")).sendKeys("edited address comments");
		    //Click on "Update Address"
		    driver.findElement(By.id("BatchEntryAddButtonAddress")).click();
		    
		    
		    //Click on "Edit" in the Telephone Information section
		    element = driver.findElement(By.xpath("//TBODY[@id='nestedElementsHistoryBox|Telephone']/TR/TD[1]/A[1]"));
		    element.click();
		    
		    //Enter "Telephone Information - Country Code"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.cntryCd")).sendKeys("1");
		    //Enter "Telephone Information - Telephone"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt1")).clear();
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt1")).sendKeys("678");
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt2")).clear();
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt2")).sendKeys("999");
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt3")).clear();
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt3")).sendKeys("0000");
		    //Enter "Telephone Information - Email"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.emailAddress")).clear();
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.emailAddress")).sendKeys("provider@edit.test.com");
		    //Enter "Telephone Information - URL"
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.urlAddress")).clear();
		    driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.urlAddress")).sendKeys("edit.com");
		    //Enter "Telephone Information - Telephone comments"
		    driver.findElement(By.id("telephone[i].locatorDescTxt")).clear();
		    driver.findElement(By.id("telephone[i].locatorDescTxt")).sendKeys("edited telephone comments");
		    
		    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);  
		    
		    //Select "Telephone Information - Use"
		    driver.findElement(By.name("telephone[i].useCd_button")).click();
		    new Select(driver.findElement(By.id("telephone[i].useCd"))).selectByVisibleText("Alternate Work Place");
		    //Select "Telephone Information - Type"
		    driver.findElement(By.name("telephone[i].cd_button")).click();
		    new Select(driver.findElement(By.id("telephone[i].cd"))).selectByVisibleText("Phone");
		    //Click on "Update Telephone"
		    driver.findElement(By.id("BatchEntryAddButtonTelephone")).click();
		    
		    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);  

		    //Click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    
		    System.out.println("Page title is: " + driver.getTitle() + "..HERE..");
		    
		    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		    
		    
		    
		    //***** Access Find Provider Search *****
		    //click on "Data Entry" on the navigation bar
		    //element = driver.findElement(By.xpath("//DIV[@id='bd']/FORM[1]/TABLE[1]/TBODY/TR/TD[1]/TABLE/TBODY/TR/TD[3]/A"));
		    element = driver.findElement(By.xpath("/html/body/div/div/form/table/tbody/tr/td/table/tbody/tr/td[3]/a"));
		    element.click();
		    //click on "Provider" on the sub menu
		    element = driver.findElement(By.xpath("/HTML/BODY/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[2]/TD[3]/TABLE/TBODY/TR/TD[5]/A/FONT"));
		    element.click();
		    
		    
		    //Find Provider screen - enter the first added provider information with name = "EditProviderLN", "EditProviderFN"
		    //Enter "Last Name"
		    driver.findElement(By.id("providerSearch.lastName")).sendKeys("EditProviderLN");
		    //Enter "First Name"
		    driver.findElement(By.id("providerSearch.firstName")).sendKeys("EditProviderFN");
		    //Enter "Street Address" and "City"
		    driver.findElement(By.id("providerSearch.streetAddr1")).sendKeys("100 New Street");
		    driver.findElement(By.id("providerSearch.cityDescTxt")).sendKeys("Atlanta");
		    		    
		    //click "Submit"
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    
		    //Click on "New Search"
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td[2]/a"));
		    element.click();
		    
		    //Find Provider screen - enter the edited provider information with name = "EditProvLN", "EditProvFN"
		    //Enter "Last Name"
		    driver.findElement(By.id("providerSearch.lastName")).sendKeys("EditProvLN");
		    //Enter "First Name"
		    driver.findElement(By.id("providerSearch.firstName")).sendKeys("EditProvFN");
		    //Enter "Street Address" and "City"
		    driver.findElement(By.id("providerSearch.streetAddr1")).sendKeys("1000 New Street");
		    driver.findElement(By.id("providerSearch.cityDescTxt")).sendKeys("Decatur");
		    
		    //Click on "View" link
		    element = driver.findElement(By.xpath("//TABLE[@id='searchResultsTable']/TBODY/TR/TD[1]"));
		    element.click();
		    
		    //Verify that the Provider ID has not changed
		    element = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/thead/tr/td/table/tbody/tr/td"));
		    if ( ! element.getText().equalsIgnoreCase(providerId))
		    {
		    	System.err.println("ERROR :: Edit Provider :::: Provider ID is updated when a provider is edited for typographical errors ");
		    }
		    
		    
	
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    System.out.println(" Sucessfully Completed :: Edit Provider Test Script ");
	    
	}
		

}
