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

public class CreateNew_Provider {
	public WebDriver driver;
	private Log log;
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	DataFile df;
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "dataEntry.createNew\\CreateNew_Provider.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void dataEntry_Provider_New() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("CreateNew_Provider", Log_File_Path);
		df = new DataFile(Data_File_Path, "DE_Provider_New");
		for (int row = 1; row < df.getRowCount(); row++) {
			try {
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				headers = df.HeadersBatchFiles("DE_Provider_New", row);
				driver.findElement(By.linkText("Data Entry")).click();
				driver.findElement(By.linkText("Provider")).click();
				driver.findElement(By.id("providerSearch.lastName")).sendKeys(headers.get("ProvName"));
				driver.findElement(By.name("Submit")).click();
				String result = driver.findElement(By.xpath("//*[@id='searchResultsTable']/tbody/tr/td")).getText();
				if (result.equals("There is no information to display")) {
					log.write("Provider details are not found, creating a new Provider");
					driver.findElement(By.id("Add")).click();
					driver.findElement(By.id("test")).sendKeys(headers.get("QuickCode"));
					driver.findElement(By.id("rolesList")).sendKeys(headers.get("Role"));
					driver.findElement(By.id("provider.thePersonDT.description"))
							.sendKeys(headers.get("GeneralComments"));
					driver.findElement(By.name("provider.nmPrefix_textbox")).sendKeys(headers.get("Prefix"));
					driver.findElement(By.id("provider.firstNm")).sendKeys(headers.get("FirstName"));
					driver.findElement(By.id("provider.middleNm")).sendKeys(headers.get("MiddleName"));
					driver.findElement(By.name("provider.nmSuffix_textbox")).sendKeys(headers.get("Suffix"));
					driver.findElement(By.name("provider.nmDegree_textbox")).sendKeys(headers.get("Degree"));
					driver.findElement(By.name("provider.entityIdDT_s[i].typeCd_textbox"))
							.sendKeys(headers.get("TypeName"));
					driver.findElement(By.name("provider.entityIdDT_s[i].assigningAuthorityCd_textbox"))
							.sendKeys(headers.get("AssigningAuthority"));
					driver.findElement(By.id("provider.entityIdDT_s[i].rootExtensionTxt")).click();
					if (driver.findElement(By.id("provider.entityIdDT_s[i].assigningAuthorityDescTxt")).isDisplayed())
						driver.findElement(By.id("provider.entityIdDT_s[i].assigningAuthorityDescTxt"))
								.sendKeys(headers.get("Description"));
					driver.findElement(By.id("provider.entityIdDT_s[i].rootExtensionTxt"))
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
					driver.findElement(By.name("Submit")).click();

					if (comUtils.isElementDisplayed(By.xpath("//span[@class='visible']"))) {
						String providerName = driver.findElement(By.xpath("//span[@class='visible']")).getText();
						log.writeSubSection("Provider is created successfully", providerName, true);

					} else {
						log.writeSubSection("Provider is not created successfully", "", false);
						log.takeSnapshot(driver);
					}

				} else {
					log.write("Provider details already exists " + headers.get("ProvName")
							+ " New Provider is not created");

				}
			} catch (Exception e) {
				log.writeSubSection("Provider is not created successfully", "", false);
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
