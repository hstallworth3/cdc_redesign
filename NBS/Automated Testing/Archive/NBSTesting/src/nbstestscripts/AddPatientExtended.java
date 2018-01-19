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
 * Login to NBS with username "pks" and add patient - extended
 * 
 * By Sarita Cherukuri on 12.19.2013
 * 
 */

public class AddPatientExtended {
	
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
		    
		    element = driver.findElement(By.xpath("/html/body/div[2]/form/div/div/input[3]"));
		    element.click();

		    System.out.println("Page title is: " + driver.getTitle());
		    
		   // Assert.assertEquals(driver.getTitle(),"Add Patient - Extended");
		    
		    element = driver.findElement(By.name("person.thePersonDT.asOfDateAdmin_s"));
		    String dateOnPage = element.getAttribute("value");
		    
		    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		    Date date = new Date();
		    String currentDate = dateFormat.format(date);

		    System.out.println("currentDate: " + currentDate);
		    System.out.println("dateOnPage: " + dateOnPage);

		    Assert.assertEquals(dateFormat.format(date), element.getAttribute("value"));
		    
		    element = driver.findElement(By.name("person.thePersonDT.description"));
		    element.sendKeys("extended page general comments");
		    
		    element = driver.findElement(By.name("nmAsOf"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("NmType_button"));
		    element.click();
		    
		    WebElement dropdown = driver.findElement(By.name("nmType")); 
		    Select select = new Select(dropdown);
		    select.selectByVisibleText("Legal");
		    
		    element = driver.findElement(By.name("NmPrefix_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("nmPrefix")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Mr.");
		    
		    element = driver.findElement(By.name("nmLast"));
		    element.sendKeys("LastName");
		    
		    element = driver.findElement(By.name("nmSecLast"));
		    element.sendKeys("Second LastName");
		    
		    element = driver.findElement(By.name("nmFirst"));
		    element.sendKeys("FirstName");
		    
		    element = driver.findElement(By.name("nmMiddle"));
		    element.sendKeys("MiddleName");
		    
		    element = driver.findElement(By.name("nmSecMiddle"));
		    element.sendKeys("Second MiddleName");
		    
		    element = driver.findElement(By.name("NmSuffix_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("nmSuffix")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Sr.");
		    
		    element = driver.findElement(By.name("NmDegree_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("nmDegree")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("MBA");
		     
		    element = driver.findElement(By.xpath("/html/body/div[2]/form/div/table[5]/tbody/tr[2]/td/div/table/tbody/tr/td/div/div[5]/div[3]/div/table/tbody/tr[12]/td/input"));
		    element.click();
		 
		    element = driver.findElement(By.name("addrAsOf"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("AddrType_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("addrType")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("House");

		    element = driver.findElement(By.name("AddrUse_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("addrUse")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Home");
		    
		    element = driver.findElement(By.name("addrStreet1"));
		    element.sendKeys("100 Main Street");
		    
		    element = driver.findElement(By.name("addrStreet2"));
		    element.sendKeys("Suite 100");
		    
		    element = driver.findElement(By.name("addrCity"));
		    element.sendKeys("Atlanta");
		    
		    element = driver.findElement(By.name("AddrState_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("addrState")); 
		    select = new Select(dropdown);
		    select.selectByVisibleText("Georgia");
		    
		    element = driver.findElement(By.name("addrZip"));
		    element.sendKeys("30345");
		    
		    element = driver.findElement(By.name("AddrCnty_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("addrCnty")); 
		    select = new Select(dropdown);
		    select.selectByVisibleText("Fulton County");
		    
		    element = driver.findElement(By.name("addrCensusTract"));
		    element.sendKeys("2222");	    
  
		    element = driver.findElement(By.name("AddrCntry_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("addrCntry")); 
		    select = new Select(dropdown);
		    select.selectByVisibleText("United States");

		    element = driver.findElement(By.name("addrComments"));
		    element.sendKeys("address comments");
		    
		    element = driver.findElement(By.xpath("/html/body/div[2]/form/div/table[5]/tbody/tr[2]/td/div/table/tbody/tr/td/div/div[5]/div[5]/div/table/tbody/tr[14]/td/input"));
		    element.click();
		    
		    element = driver.findElement(By.name("phAsOf"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("PhType_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("phType")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Cellular Phone");

		    element = driver.findElement(By.name("PhUse_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("phUse")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Mobile Contact");
		    
		    element = driver.findElement(By.name("phCntryCd"));
		    element.sendKeys("091");
		    
		    element = driver.findElement(By.name("phNum"));
		    element.sendKeys("4445556666");
		    
		    element = driver.findElement(By.name("phExt"));
		    element.sendKeys("123");
		    
		    element = driver.findElement(By.name("phEmail"));
		    element.sendKeys("firstname@lastname.com");
		    
		    element = driver.findElement(By.name("phUrl"));
		    element.sendKeys("phone url");
		    
		    element = driver.findElement(By.name("phComments"));
		    element.sendKeys("phone comments");
		    
		    element = driver.findElement(By.xpath("/html/body/div[2]/form/div/table[5]/tbody/tr[2]/td/div/table/tbody/tr/td/div/div[5]/div[7]/div/table/tbody/tr[11]/td/input"));
		    element.click();
		    
		    element = driver.findElement(By.name("idAsOf"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("IdType_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("idType")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Account number");

		    element = driver.findElement(By.name("IdAssgn_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("idAssgn")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("GA");
		    
		    element = driver.findElement(By.name("idsValue"));
		    element.sendKeys("987654321");
		    
		    element = driver.findElement(By.xpath("/html/body/div[2]/form/div/table[5]/tbody/tr[2]/td/div/table/tbody/tr/td/div/div[5]/div[9]/div/table/tbody/tr[6]/td/input"));
		    element.click();
		    
		    element = driver.findElement(By.name("raceAsOf"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("RaceType_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("raceType")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("White");
		    
		    element = driver.findElement(By.xpath("/html/body/div[2]/form/div/table[5]/tbody/tr[2]/td/div/table/tbody/tr/td/div/div[5]/div[11]/div/table/tbody/tr[5]/td/input"));
		    element.click();
		    
		    element = driver.findElement(By.name("person.thePersonDT.asOfDateEthnicity_s"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("INV2002_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.ethnicGroupInd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Not Hispanic or Latino");
		    
		    element = driver.findElement(By.name("person.thePersonDT.asOfDateSex_s"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("person.thePersonDT.birthTime_s"));
		    element.sendKeys("12/12/1955");
		    
		    element = driver.findElement(By.name("CurrSex_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.currSexCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Transgender, unspecified");
		    
		    element = driver.findElement(By.name("person.thePersonDT.additionalGenderCd"));
		    element.sendKeys("gender");

		    element = driver.findElement(By.name("BirSex_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.birthGenderCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Male");
		    
		    element = driver.findElement(By.name("MulBir_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.multipleBirthInd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Unknown");
		    
		    element = driver.findElement(By.name("person.thePersonDT.birthOrderNbrStr"));
		    element.sendKeys("2");
		    
		    element = driver.findElement(By.name("birthAddress.thePostalLocatorDT.cityDescTxt"));
		    element.sendKeys("birth city");

		    element = driver.findElement(By.name("BirState_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("birthAddress.thePostalLocatorDT.stateCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Georgia");

		    element = driver.findElement(By.name("BirCnty_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("birthAddress.thePostalLocatorDT.cntyCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Fulton County");

		    element = driver.findElement(By.name("BirCntry_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("birthAddress.thePostalLocatorDT.cntryCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("United States");
		    
		    element = driver.findElement(By.name("person.thePersonDT.asOfDateMorbidity_s"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("PatDeceased_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.deceasedIndCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Yes");
		    
		    element = driver.findElement(By.name("person.thePersonDT.deceasedTime_s"));
		    element.sendKeys("12/12/2003");
		    
		    element = driver.findElement(By.name("deceasedAddress.thePostalLocatorDT.cityDescTxt"));
		    element.sendKeys("deceased city");
		    
		    element = driver.findElement(By.name("DeathState_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("deceasedAddress.thePostalLocatorDT.stateCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Georgia");

		    element = driver.findElement(By.name("DeathCnty_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("deceasedAddress.thePostalLocatorDT.cntyCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Fulton County");

		    element = driver.findElement(By.name("DeathCntry_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("deceasedAddress.thePostalLocatorDT.cntryCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("United States");

		    element = driver.findElement(By.name("person.thePersonDT.asOfDateGeneral_s"));
		    element.sendKeys(currentDate);
		    
		    element = driver.findElement(By.name("MarStatus_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.maritalStatusCd")); 
		    select = new Select(dropdown);
		    select.selectByValue("M");
		   // select.selectByVisibleText("Married");
		    
		    element = driver.findElement(By.name("person.thePersonDT.mothersMaidenNm"));
		    element.sendKeys("mothers maiden name");
		    
		    element = driver.findElement(By.name("person.thePersonDT.adultsInHouseNbrStr"));
		    element.sendKeys("4");
		    
		    element = driver.findElement(By.name("person.thePersonDT.childrenInHouseNbrStr"));
		    element.sendKeys("1");
		    
		    element = driver.findElement(By.name("PriOccup_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.occupationCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Information");
		    
		    element = driver.findElement(By.name("HLevelEdu_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.educationLevelCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Master's Degree");
		    
		    element = driver.findElement(By.name("PriLang_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.primLangCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Telugu");
		    
		    element = driver.findElement(By.name("SpeaksEnglishCd_button"));
		    element.click();
		    dropdown = driver.findElement(By.name("person.thePersonDT.speaksEnglishCd")); 
		    select = new Select(dropdown); 
		    select.selectByVisibleText("Yes");		    
		    
		    element = driver.findElement(By.name("person.thePersonDT.eharsId"));
		    element.sendKeys("12345");
		    
		    //driver.findElement(By.name("Submit")).click();
		    
		    System.out.println("Page title is: " + driver.getTitle());

	    System.out.println("---- Sucessful :: Add Extended Patient ----");
		    
		}
		catch(Exception ex)
		{
			System.out.println("Exception.." + ex.getStackTrace());
		}
		    
		    //Close the browser
			//driver.quit();
		    
		}


}
