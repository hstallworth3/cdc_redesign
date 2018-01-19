package nbstestscripts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;


/*
 * Login to NBS with username "pks" and add patient - basic
 * 
 * By Sarita Cherukuri on 12.16.2013
 * 
 */

public class AddPatientBasic {
	
	public static void main(String[] args) {
		
		// Create a new instance of the InternetExplorer driver
	    WebDriver driver = new InternetExplorerDriver();
	    driver.get("http://nedss-tstappsql:7001/nbs/login");
	    
	    System.out.println("Page title is: " + driver.getTitle());
	    
		try
		{
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
		    element = driver.findElement(By.name("Submit"));
		    element.click();
		    
		    System.out.println("Page title is: " + driver.getTitle());

		    element = driver.findElement(By.name("patientAsOfDateGeneral"));
		    String dateOnPage = element.getAttribute("value");
		    
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    Date date = new Date();
		    String currentDate = dateFormat.format(date);

		    System.out.println("currentDate: " + currentDate);
		    System.out.println("dateOnPage: " + dateOnPage);

		    Assert.assertEquals(dateFormat.format(date), element.getAttribute("value"));
		    
		    element = driver.findElement(By.name("person.thePersonDT.description"));
		    element.sendKeys("general comments");
		    
		    element = driver.findElement(By.name("person.thePersonDT.lastNm"));
		    element.clear();
		    element.sendKeys("lastname");
		    
		    element = driver.findElement(By.name("person.thePersonDT.firstNm"));
		    element.sendKeys("firstname");
		    
		    element = driver.findElement(By.name("person.thePersonDT.middleNm"));
		    element.sendKeys("middleName");
		    
		    element = driver.findElement(By.name("DEM107_button"));
		    element.click();

		    WebElement dropdown = driver.findElement(By.name("person.thePersonDT.nmSuffix")); 
		    Select select = new Select(dropdown); 
		    select.selectByValue("JR");
		    
////		    List<WebElement> suffixList = suffixSelect.getOptions();
////		    System.out.println(suffixList.size());
////		    for (WebElement suffix : suffixList) {
////		        System.out.println(String.format("value : %s", suffix.getAttribute("value")));
////		        suffixSelect.selectByValue(suffix.getAttribute("value"));
////		    }
		    
		    
		    element = driver.findElement(By.name("patientBirthTime"));
		    element.sendKeys("12/12/1955");
		    
		    element = driver.findElement(By.name("DEM113_button"));
		    element.click();
		    
		    dropdown = driver.findElement(By.name("person.thePersonDT.currSexCd")); 
		    select = new Select(dropdown);
		    select.selectByValue("S");
		    
		    element = driver.findElement(By.name("person.thePersonDT.additionalGenderCd"));
		    element.sendKeys("Gender");
		    
		    element = driver.findElement(By.name("DEM127_button"));
		    element.click();

		    dropdown = driver.findElement(By.name("person.thePersonDT.deceasedIndCd")); 
		    select = new Select(dropdown);
		    select.selectByValue("Y");
		    
		    element = driver.findElement(By.name("patientDeceasedDate"));
		    element.sendKeys("12/12/1955");
		    
		    element = driver.findElement(By.name("DEM140_button"));
		    element.click();

		    dropdown = driver.findElement(By.name("person.thePersonDT.maritalStatusCd")); 
		    select = new Select(dropdown);
		    select.selectByValue("M");

		    element = driver.findElement(By.name("person.thePersonDT.eharsId"));
		    element.sendKeys("12345");
		    
		    element = driver.findElement(By.name("address[0].thePostalLocatorDT_s.streetAddr1"));
		    element.sendKeys("1000 Main Street");
		    
		    element = driver.findElement(By.name("address[0].thePostalLocatorDT_s.streetAddr2"));
		    element.sendKeys("Suite 100");
		    
		    element = driver.findElement(By.name("address[0].thePostalLocatorDT_s.cityDescTxt"));
		    element.sendKeys("Atlanta");
		    
		    element = driver.findElement(By.name("DEM162_button"));
		    element.click();

		    dropdown = driver.findElement(By.name("address[0].thePostalLocatorDT_s.stateCd")); 
		    select = new Select(dropdown);
		    select.selectByValue("13");
		    
		    element = driver.findElement(By.name("address[0].thePostalLocatorDT_s.zipCd"));
		    element.sendKeys("30345");
		    
		    element = driver.findElement(By.name("DEM165_button"));
		    element.click();

		    dropdown = driver.findElement(By.name("address[0].thePostalLocatorDT_s.cntyCd")); 
		    select = new Select(dropdown);
		    select.selectByValue("13053");
		    
		    element = driver.findElement(By.name("address[0].thePostalLocatorDT_s.censusTract"));
		    element.sendKeys("2222");	    
		    
		    element = driver.findElement(By.name("DEM167_button"));
		    element.click();

		    dropdown = driver.findElement(By.name("address[0].thePostalLocatorDT_s.cntryCd")); 
		    select = new Select(dropdown);
		    select.selectByValue("840");
	  
		   
		    driver.findElement(By.name("patientHomePhone")).sendKeys("1234567890");

		    driver.findElement(By.name("patientWorkPhone")).sendKeys("9876543210");
		    
		    driver.findElement(By.name("patientWorkPhoneExt")).sendKeys("111");
		    
		    driver.findElement(By.name("patientCellPhone")).sendKeys("2224446666");
		    
		    driver.findElement(By.name("patientEmail")).sendKeys("patient@test.com");
		    
		    driver.findElement(By.name("DEM155_button")).click();

		    select = new Select(driver.findElement(By.name("person.thePersonDT.ethnicGroupInd")));
		    select.selectByVisibleText("Not Hispanic or Latino");
		    
		    driver.findElement(By.name("pamClientVO.americanIndianAlskanRace")).click();
		    
		    driver.findElement(By.name("pamClientVO.whiteRace")).click();
		    
		    driver.findElement(By.name("typeID_button")).click();
		    
		    select = new Select(driver.findElement(By.name("idTypeDescCd")));
		    select.selectByVisibleText("Account number");
		    
		    driver.findElement(By.name("assigningAuthority_button")).click();
		    
		    select = new Select(driver.findElement(By.name("idAssigningAuthorityCd")));
		    select.selectByVisibleText("GA");
		    
		    driver.findElement(By.name("idValue")).sendKeys("3456");
		    
		    driver.findElement(By.xpath("/html/body/div[2]/form/div/div[3]/div/table[8]/tbody/tr[5]/td/input")).click();
		    
		    driver.findElement(By.name("Submit")).click();
		    
		    System.out.println("Page title is: " + driver.getTitle());

		    System.out.println("---- Sucessful :: Add Basic Patient ----");
		    
		}
		catch(Exception ex)
		{
			System.out.println("Exception.." + ex.getStackTrace());
		}
		    
		    //Close the browser
			//driver.quit();
		    
		}


}
