package com.nbs.investigations;

import java.awt.Robot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.nbs.common.Login;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class GenericInvestigation_MorbidityReport {
	public WebDriver driver;
	private Log log;
	DataFile df;
	public Robot robot = null;
	String parentWindow = "";
	public String patID = "";
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "investigations\\GenericInvestigation_MorbidityReport.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void Generic_MorbReport() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("GenericInvestigation_MorbidityReport", Log_File_Path);
		df = new DataFile(Data_File_Path, "Morb_MetaData");
		Create_MorbidityReport demr = new Create_MorbidityReport();
		demr.setUpDriver(driver);
		demr.setUpLog(log);
		demr.setUpDataFile(df);

		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {

				// Populate Lab Report Screens
				patID = demr.Populate_DE_MorbReport(col);
				driver = demr.getDriver();
				log = demr.getLog();

				// Go to Home page
				driver.findElement(By.linkText("Home")).click();

				// Clear the Patient ID text box
				driver.findElement(By.id("DEM229")).clear();

				// Enter the Patient ID captured earlier in the Patient ID text
				// box
				driver.findElement(By.id("DEM229")).sendKeys(patID);

				// Click on Search
				driver.findElement(By.xpath("//*[@id='patientSearchByDetails']/table[2]/tbody/tr[8]/td[2]/input[1]"))
						.click();

				// Click on the Patient ID link in the search results
				driver.findElement(By.linkText(patID)).click();

				// Click on the Morb Report
				driver.findElement(By.linkText("Morb Report")).click();

				// Get the parent Window Handle
				parentWindow = driver.getWindowHandle();

				// click on CreateInvestigation
				driver.findElement(By.id("Create Investigation")).click();

				Thread.sleep(1000);

				// Populate Investigation Screens
				df = new DataFile(Data_File_Path, "GenericInvestigation");
				ReadMetaData newSTD_Patient = new ReadMetaData();
				newSTD_Patient.setUpDriver(driver);
				newSTD_Patient.setUpLog(log);
				newSTD_Patient.setUpDataFile(df);
				newSTD_Patient.ReadData(col);

				// Click on Submit to submit the investigation and log the
				// results
				driver.findElement(By.name("Submit")).click();

				if (driver.findElement(By.id("successMessages")).isDisplayed()) {
					System.out.println("\nInvestigation Created Successfully");
					String InvestigationNum = driver
							.findElement(By.xpath("//*[@id='bd']/table[4]/tbody/tr[2]/td[1]/span[2]")).getText();
					log.writeSubSection(
							"\n	Investigation Created Successfully for Investigation ID " + InvestigationNum, "", true);
				} else {
					log.writeSubSection("\nInvestigation NOT Created Successfully", "", false);

				}

				// Add Interview
				Create_Interview add_interview = new Create_Interview();
				df = new DataFile(Data_File_Path, "Interview");
				add_interview.setUpDriver(driver);
				add_interview.setUpLog(log);
				add_interview.setUpDataFile(df);

				add_interview.Add_Interview(col);

				driver = add_interview.getDriver();
				log = add_interview.getLog();
				df = add_interview.getDataFile();
				Thread.sleep(1000);

				// Add Contact Record
				Create_ContactRecord add_contact_metadata = new Create_ContactRecord();
				df = new DataFile(Data_File_Path, "Contact Record");
				add_contact_metadata.setUpDriver(driver);
				add_contact_metadata.setUpLog(log);
				add_contact_metadata.setUpDataFile(df);

				add_contact_metadata.Add_Contact(col);

				driver = add_contact_metadata.getDriver();
				log = add_contact_metadata.getLog();
				df = add_contact_metadata.getDataFile();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				if (e.getMessage().contains("Unable to locate element")) {
					System.out.println(e.getMessage());
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='botProcessingDecisionId']/input"))) {
						driver.findElement(By.xpath("//*[@id='botProcessingDecisionId']/input")).click();
					} else {
						log.writeSubSection("\nInvestigation NOT Created Successfully", "", false);
						log.takeSnapshot(driver);

					}
				}
				driver.switchTo().window(parentWindow);
			}

			log.writeSubSection_RecordStatus();

			Thread.sleep(2000);
			driver.findElement(By.linkText("Home")).click();
		}
	}

	public WebDriver getDriver() throws Exception {
		return driver;
	}

	public Log getLog() throws Exception {
		return log;
	}

	public DataFile getDataFile() throws Exception {
		return df;
	}

	@After
	public void close() throws Exception {
		df.close();
		log.close();
		driver.close();
	}

}
