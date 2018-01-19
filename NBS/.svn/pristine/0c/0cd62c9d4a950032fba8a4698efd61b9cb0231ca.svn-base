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

public class CreateNew_Place {
	private WebDriver driver;
	private Log log;
	DataFile df;
	private Hashtable<String, String> headers = new Hashtable<String, String>();
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "dataEntry.createNew\\CreateNew_Place.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void dataEntry_Place_New() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("CreateNew_Place", Log_File_Path);
		df = new DataFile(Data_File_Path, "DE_Place_New");
		for (int row = 1; row < df.getRowCount(); row++) {
			try {
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				headers = df.HeadersBatchFiles("DE_Place_New", row);
				driver.findElement(By.linkText("Data Entry")).click();
				driver.findElement(By.linkText("Place")).click();
				driver.findElement(By.name("placeSearch.nm")).sendKeys(headers.get("PlaceName"));
				driver.findElement(By.name("Submit")).click();
				String result = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr/td")).getText();

				if (result.equals("Nothing found to display.")) {
					log.write("Place details are not found, creating a new Place");
					driver.findElement(By.id("Add1")).click();
					driver.findElement(By.name("quick.rootExtensionTxt")).sendKeys(headers.get("QuickCode"));
					driver.findElement(By.name("placeType_textbox")).sendKeys(headers.get("PlaceType"));
					driver.findElement(By.name("place.thePlaceDT.description"))
							.sendKeys(headers.get("GeneralComments"));
					driver.findElement(By.id("AddrAsOf")).sendKeys(headers.get("AddressInfoAsOf"));
					driver.findElement(By.name("addrType_textbox")).sendKeys(headers.get("AddressType"));
					driver.findElement(By.name("addrUse_textbox")).sendKeys(headers.get("AddressUse"));
					driver.findElement(By.id("streetAddr1")).sendKeys(headers.get("StreetAddress1"));
					driver.findElement(By.name("streetAddr2")).sendKeys(headers.get("StreetAddress2"));
					driver.findElement(By.id("city")).sendKeys(headers.get("City"));
					driver.findElement(By.name("state_textbox")).sendKeys(headers.get("State"));
					driver.findElement(By.id("zip")).sendKeys(headers.get("Zip"));
					driver.findElement(By.name("county_textbox")).sendKeys(headers.get("County"));
					driver.findElement(By.name("county_button")).click();
					driver.findElement(By.name("censusTract")).sendKeys(headers.get("CensusTract"));
					driver.findElement(By.name("country_textbox")).sendKeys(headers.get("Country"));
					driver.findElement(By.name("locatorDescTxt")).sendKeys(headers.get("AddressComments"));
					driver.findElement(By.id("btnAddAddress")).click();
					driver.findElement(By.id("PhoneAsOf")).sendKeys(headers.get("ContactInfoAsOf"));
					driver.findElement(By.xpath("html/body/div[3]/div/form/div/div/table[5]/tbody/tr[3]/td[2]/input"))
							.sendKeys(headers.get("PhoneType"));
					driver.findElement(By.name("phoneUse_textbox")).sendKeys(headers.get("Use"));
					driver.findElement(By.name("cntryCd")).sendKeys(headers.get("CountryCode"));
					driver.findElement(By.id("phoneNbrTxt")).sendKeys(headers.get("Telephone"));
					driver.findElement(By.name("phoneExt")).sendKeys(headers.get("Extn"));
					driver.findElement(By.name("email")).sendKeys(headers.get("Email"));
					driver.findElement(By.name("urlAddress")).sendKeys(headers.get("URL"));
					driver.findElement(By.name("locatorDescTxt_p")).sendKeys(headers.get("TelephoneComments"));
					driver.findElement(By.id("btnAddTelephone")).click();
					Thread.sleep(1000);
					driver.findElement(By.id("btnSubmitB")).click();

					if (comUtils.isElementDisplayed(By.xpath("html/body/div[2]/b"))) {
						String place = driver.findElement(By.xpath("html/body/div[2]/b")).getText();
						log.writeSubSection("Place is created successfully", place, true);
					} else {

						log.writeSubSection("Place is not created successfully", "", false);
						log.takeSnapshot(driver);
					}
				} else {
					log.write("Place details already exists " + headers.get("PlaceName") + " New Place is not created");

				}
			} catch (Exception e) {
				log.writeSubSection("Place is not created successfully", "", false);
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
