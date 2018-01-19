package com.nbs.investigations;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.nbs.common.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class STDInvestigation_MorbidityReport {
	public WebDriver driver;
	public Log log;
	DataFile df;
	public Robot robot = null;
	String parentWindow = "";
	String patID = "";
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "investigations\\STDInvestigation_MorbidityReport.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void STDInvestigation_MorbReport() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("STDInvestigation_MorbidityReport", Log_File_Path);
		df = new DataFile(Data_File_Path, "Morb_MetaData");
		Create_MorbidityReport demr = new Create_MorbidityReport();
		demr.setUpDriver(driver);
		demr.setUpLog(log);
		demr.setUpDataFile(df);
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {
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
				// Enter Processing Decision
				df = new DataFile(Data_File_Path, "Conditions");
				ReadMetaData get_ProcessingDecision = new ReadMetaData();
				get_ProcessingDecision.setUpDataFile(df);
				String processing_decision = get_ProcessingDecision.getFieldValue("Processing Decision", col);
				String parentWindow = driver.getWindowHandle();
				// click on CreateInvestigation
				try {
					robot = new Robot();
				} catch (AWTException e) {
					e.printStackTrace();
				}
				driver.findElement(By.id("Create Investigation")).sendKeys(Keys.NULL);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				Thread.sleep(1000);

				// Handling pop up window
				String subWindowHandler = null;
				driver.getWindowHandles();
				Set<String> handles = driver.getWindowHandles();
				Iterator<String> iterator = handles.iterator();
				System.out.println(iterator);
				while (iterator.hasNext()) {
					subWindowHandler = iterator.next();
					System.out.println(subWindowHandler);
				}
				driver.switchTo().window(subWindowHandler);
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				driver.findElement(By.name("reviewReason_textbox")).click();
				driver.findElement(By.name("reviewReason_textbox")).sendKeys(processing_decision);
				driver.findElement(By.xpath("//*[@id='botProcessingDecisionId']/input[1]")).click();
				driver.switchTo().window(parentWindow);

				// Enter Investigation details
				df = new DataFile(Data_File_Path, "STDInvestigation");
				ReadMetaData newSTD_Patient = new ReadMetaData();
				newSTD_Patient.setUpDriver(driver);
				newSTD_Patient.setUpLog(log);
				newSTD_Patient.setUpDataFile(df);
				newSTD_Patient.ReadData(col);
				driver = newSTD_Patient.getDriver();
				log = newSTD_Patient.getLog();
				driver.findElement(By.name("Submit")).click();
				/*
				 * if (isElementPresent(By.xpath("//*[@id='errorMessages']/b")))
				 * { driver.findElement(By.name("Cancel")).click(); Alert alert
				 * = driver.switchTo().alert(); alert.accept(); }
				 */
				if (comUtils.isElementDisplayed(By.id("successMessages"))) {
					System.out.println("\nInvestigation Created Successfully");
					String InvestigationNum = driver
							.findElement(By.xpath("//*[@id='bd']/table[4]/tbody/tr[2]/td[1]/span[2]")).getText();
					log.writeSubSection(
							"\n		Investigation Created Successfully for Investigation ID " + InvestigationNum, "",
							true);
				} else {
					log.writeSubSection("\nInvestigation NOT Created Successfully", "", false);
					log.takeSnapshot(driver);
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

			Thread.sleep(1000);
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
