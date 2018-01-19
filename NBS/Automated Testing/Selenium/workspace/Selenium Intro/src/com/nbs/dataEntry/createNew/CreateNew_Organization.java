package com.nbs.dataEntry.createNew;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;

import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class CreateNew_Organization {

	public WebDriver driver;
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	private Log log;
	DataFile df;
	public String Increment;
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();
	//public int i;

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "dataEntry.createNew\\CreateNew_Organization.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void dataEntry_Org_New() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("CreateNew_Organization", Log_File_Path);
		df = new DataFile(Data_File_Path, "DE_Org_New");
		for (int row = 1; row < df.getRowCount(); row++) {
			try {
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				headers = df.HeadersBatchFiles("DE_Org_New", row);
				driver.findElement(By.linkText("Data Entry")).click();
				driver.findElement(By.linkText("Organization")).click();
				driver.findElement(By.id("organizationSearch.nmTxt")).sendKeys(headers.get("OrganizationName"));
				driver.findElement(By.name("Submit")).click();
				String result = driver.findElement(By.xpath("//*[@id='searchResultsTable']/tbody/tr/td")).getText();
				if (result.equals("There is no information to display")) {
					log.write("Organization details are not found, creating a new Organization");
					driver.findElement(By.name("Add")).click();
					driver.findElement(By.id("test")).sendKeys(headers.get("QuickCode"));
					driver.findElement(By.name("organization.theOrganizationDT.standardIndustryClassCd_textbox"))
							.sendKeys(headers.get("StandardIndustryClass"));
					driver.findElement(By.id("rolesList")).sendKeys(headers.get("Role"));
					driver.findElement(By.id("organization.theOrganizationDT.description"))
							.sendKeys(headers.get("GeneralComments"));
					driver.findElement(By.name("organization.entityIdDT_s[i].typeCd_textbox"))
							.sendKeys(headers.get("IDType"));
					driver.findElement(By.name("organization.entityIdDT_s[i].assigningAuthorityCd_textbox"))
							.sendKeys(headers.get("AssigningAuthority"));
					driver.findElement(By.name("organization.entityIdDT_s[i].rootExtensionTxt"))
							.sendKeys(headers.get("IDValue"));
					driver.findElement(By.id("BatchEntryAddButtonIdentification")).click();

					driver.findElement(By.name("address[i].useCd_textbox")).sendKeys(headers.get("AddressUse"));
					driver.findElement(By.name("address[i].cd_textbox")).sendKeys(headers.get("Type"));
					driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr1"))
							.sendKeys(headers.get("StreetAddress1"));
					driver.findElement(By.id("address[i].thePostalLocatorDT_s.streetAddr2"))
							.sendKeys(headers.get("StreetAddress2"));
					driver.findElement(By.id("address[i].thePostalLocatorDT_s.cityDescTxt"))
							.sendKeys(headers.get("City"));
					driver.findElement(By.name("address[i].thePostalLocatorDT_s.stateCd_textbox")).clear();
					driver.findElement(By.name("address[i].thePostalLocatorDT_s.stateCd_textbox"))
							.sendKeys(headers.get("State"));
					driver.findElement(By.id("address[i].thePostalLocatorDT_s.zipCd")).sendKeys(headers.get("Zip"));
					driver.findElement(By.name("address[i].thePostalLocatorDT_s.cntyCd_textbox"))
							.sendKeys(headers.get("County"));
					driver.findElement(By.id("address[i].locatorDescTxt")).sendKeys(headers.get("AddressComments"));
					driver.findElement(By.id("BatchEntryAddButtonAddress")).click();

					driver.findElement(By.name("telephone[i].useCd_textbox")).sendKeys(headers.get("TelephoneUse"));
					driver.findElement(By.name("telephone[i].cd_textbox")).sendKeys(headers.get("TelephoneType"));
					driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.cntryCd"))
							.sendKeys(headers.get("CountryCode"));
					driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.phoneNbrTxt1"))
							.sendKeys(headers.get("Telephone"));
					driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.extensionTxt"))
							.sendKeys(headers.get("Extn"));
					driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.emailAddress"))
							.sendKeys(headers.get("Email"));
					driver.findElement(By.id("telephone[i].theTeleLocatorDT_s.urlAddress"))
							.sendKeys(headers.get("URL"));
					driver.findElement(By.id("telephone[i].locatorDescTxt")).sendKeys(headers.get("TelephoneComments"));
					driver.findElement(By.id("BatchEntryAddButtonTelephone")).click();
					Thread.sleep(1000);
					driver.findElement(By.id("Submit")).click();

					if (comUtils.isElementDisplayed(By.xpath("//span[@class='visible']")) ){
						String OrgIdGenerated = driver.findElement(By.xpath("//span[@class='visible']")).getText();
						log.writeSubSection(OrgIdGenerated + " is created successfully", "", true);

					} else {
						log.writeSubSection("Organization is not created successfully", "", false);
						log.takeSnapshot(driver);
					}

				} else {

					log.write("Organization details already exists " + headers.get("OrganizationName")
							+ " New organization is not created");

				}
			} catch (Exception e) {
				log.writeSubSection("Organization is not created successfully", "", false);
				log.takeSnapshot(driver);

			}
			log.writeSubSection_RecordStatus();
		}
	}

	@After
	public void close() throws Exception {
		driver.close();
		df.close();
		log.close();
	}

}
