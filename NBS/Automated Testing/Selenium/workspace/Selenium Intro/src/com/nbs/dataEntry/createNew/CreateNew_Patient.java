package com.nbs.dataEntry.createNew;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.*;

public class CreateNew_Patient {
	public WebDriver driver;
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	private Log log;
	DataFile df;
	public String Increment;
	public int i;
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "dataEntry.createNew\\CreateNew_Patient.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void dataEntry_Patient_New() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("CreateNew_Patient", Log_File_Path);
		df = new DataFile(Data_File_Path, "DE_Patient_New");
		for (int row = 1; row < df.getRowCount(); row++) {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			try {
				headers = df.HeadersBatchFiles("DE_Patient_New", row);
				driver.findElement(By.linkText("Data Entry")).click();
				driver.findElement(By.linkText("Patient")).click();
				driver.findElement(By.id("DEM102")).sendKeys(headers.get("LastName"));
				driver.findElement(By.name("Submit")).click();
				String result = driver.findElement(By.xpath("//*[@id='searchResultsTable']/tbody/tr/td")).getText();
				if (result.equals("Nothing found to display.")) {
					log.write("Patient details are not found, creating a new patient");
					driver.findElement(By.name("Submit")).click();
					if (headers.get("InformationAsOfDate") != "") {
						driver.findElement(By.id("DEM209")).clear();
						driver.findElement(By.id("DEM209")).sendKeys(headers.get("InformationAsOfDate"));
					}
					driver.findElement(By.id("DEM196")).sendKeys(headers.get("Comments"));
					driver.findElement(By.id("DEM104")).sendKeys(headers.get("FirstName"));
					driver.findElement(By.id("DEM105")).sendKeys(headers.get("MiddleName"));
					driver.findElement(By.name("DEM107_textbox")).sendKeys(headers.get("Suffix"));
					driver.findElement(By.id("patientDOB")).sendKeys(headers.get("DOB"));
					driver.findElement(By.name("DEM113_textbox")).sendKeys(headers.get("CurrentSex"));
					driver.findElement(By.name("DEM127_textbox")).sendKeys(headers.get("IsThePatientDeceased"));
					driver.findElement(By.id("DEM127L")).click();
					if (driver.findElement(By.id("deceasedDate")).isEnabled())
						driver.findElement(By.id("deceasedDate")).sendKeys(headers.get("DateOfDeath"));
					driver.findElement(By.name("DEM140_textbox")).sendKeys(headers.get("MaritalStatus"));
					driver.findElement(By.id("eHARSID")).sendKeys(headers.get("StateHIVCaseID"));
					driver.findElement(By.id("DEM159")).sendKeys(headers.get("StreetAddress1"));
					driver.findElement(By.id("DEM160")).sendKeys(headers.get("StreetAddress2"));
					driver.findElement(By.id("DEM161")).sendKeys(headers.get("City"));
					driver.findElement(By.name("DEM162_textbox")).clear();
					driver.findElement(By.name("DEM162_textbox")).sendKeys(headers.get("State"));
					driver.findElement(By.id("DEM163")).sendKeys(headers.get("Zip"));
					driver.findElement(By.name("DEM165_textbox")).sendKeys(headers.get("County"));
					driver.findElement(By.id("DEM168")).sendKeys(headers.get("CensusTract"));
					driver.findElement(By.name("DEM167_textbox")).clear();
					driver.findElement(By.name("DEM167_textbox")).sendKeys(headers.get("Country"));
					driver.findElement(By.id("DEM177")).sendKeys(headers.get("HomePhone"));
					driver.findElement(By.id("NBS002")).sendKeys(headers.get("WorkPhone"));
					driver.findElement(By.id("NBS003")).sendKeys(headers.get("WorkPhoneExt"));
					driver.findElement(By.id("NBS006")).sendKeys(headers.get("CellPhone"));
					driver.findElement(By.id("DEM182")).sendKeys(headers.get("Email"));
					driver.findElement(By.name("DEM155_textbox")).sendKeys(headers.get("Ethnicity"));
					switch (headers.get("Race")) {
					case "American Indian or Alaska Native":
						driver.findElement(By.id("americanIndianRace")).click();
						break;
					case "Asian":
						driver.findElement(By.id("asianRace")).click();
						break;
					case "Black or African American":
						driver.findElement(By.id("africanRace")).click();
						break;
					case "Native Hawaiian or Other Pacific Islander":
						driver.findElement(By.id("hawaiianRace")).click();
						break;
					case "White":
						driver.findElement(By.id("whiteRace")).click();
						break;
					case "Other":
						driver.findElement(By.id("otherRace")).click();
						break;
					case "Refused to answer":
						driver.findElement(By.id("refusedToAnswer")).click();
						break;
					case "Not Asked":
						driver.findElement(By.id("notAsked")).click();
						break;
					case "Unknown":
						driver.findElement(By.id("unknownRace")).click();
						break;
					default:
						driver.findElement(By.id("unknownRace")).click();
						break;
					}
					driver.findElement(By.name("typeID_textbox")).sendKeys(headers.get("Type"));
					driver.findElement(By.name("assigningAuthority_textbox"))
							.sendKeys(headers.get("AssigningAuthority"));
					driver.findElement(By.id("idValue")).sendKeys(headers.get("IDValue"));
					driver.findElement(By.xpath("//*[@id='AddButtonToggleIdSubSection']/td/input")).click();
					driver.findElement(By.id("Submit")).click();
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/table[3]/tbody/tr/td[1]/span[1]"))) {
						log.writeSubSection("Patient is created successfully", headers.get("LastName"), true);

					} else {
						log.writeSubSection("Patient is not created successfully", "", false);
						log.takeSnapshot(driver);
					}

				} else {

					log.write("Patient details already exists " + headers.get("LastName")
							+ " New patient is not created");

				}
			} catch (Exception e) {
				log.writeSubSection("Patient is not created successfully", "", false);
				log.takeSnapshot(driver);

			}
			log.writeSubSection_RecordStatus();

		}

	}

	@After
	public void close() throws Exception {
		df.close();
		log.close();
		driver.close();
	}

}
