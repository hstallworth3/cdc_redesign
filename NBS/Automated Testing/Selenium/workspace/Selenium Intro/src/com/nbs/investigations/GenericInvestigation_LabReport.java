package com.nbs.investigations;

import org.junit.Test;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.*;

public class GenericInvestigation_LabReport {
	public WebDriver driver;
	private Log log;
	DataFile df;
	public Robot robot = null;
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	String parentWindow = "";
	String patID = "";
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "investigations\\GenericInvestigation_LabReport.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void Generic_LabReport() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("GenericInvestigation_LabReport", Log_File_Path);
		df = new DataFile(Data_File_Path, "Lab_MetaData");
		Create_LabReport gerericLab = new Create_LabReport();
		gerericLab.setUpDriver(driver);
		gerericLab.setUpLog(log);
		gerericLab.setUpDataFile(df);

		int colCount = df.getColCount() - 3;

		for (int col = 1; col <= colCount; col++) {
			try {

				// Populate Lab Report Screens
				patID = gerericLab.Populate_DE_LabReport(col);
				driver = gerericLab.getDriver();
				log = gerericLab.getLog();

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

				// Click on the Lab Report
				driver.findElement(By.linkText("Lab Report")).click();

				// Get the parent Window Handle
				parentWindow = driver.getWindowHandle();

				try {
					robot = new Robot();
				} catch (AWTException e) {
					e.printStackTrace();
				}

				// Click on CreateInvestigation
				driver.findElement(By.id("Create Investigation")).sendKeys(Keys.NULL);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				Thread.sleep(1000);

				// Read and Enter Condition and Click on Submit Button
				df = new DataFile(Data_File_Path, "Conditions");
				headers = df.Headers_Conditions("Conditions", col);

				Thread.sleep(2000);
				driver.findElement(By.name("ccd_textbox")).sendKeys(headers.get("Condition"));

				driver.findElement(By.id("Submit")).click();

				driver.switchTo().window(parentWindow);

				// Populate Investigation Screens
				df = new DataFile(Data_File_Path, "GenericInvestigation");
				ReadMetaData newSTD_Patient = new ReadMetaData();
				newSTD_Patient.setUpDriver(driver);
				newSTD_Patient.setUpLog(log);
				newSTD_Patient.setUpDataFile(df);

				newSTD_Patient.ReadData(col);

				driver = newSTD_Patient.getDriver();
				log = newSTD_Patient.getLog();
				df = newSTD_Patient.getDataFile();
				Thread.sleep(1000);

				// Click on Submit to submit the investigation and log the
				// results
				driver.findElement(By.id("Submit")).click();

				if (comUtils.isElementDisplayed(By.id("successMessages"))) {
					System.out.println("\nInvestigation Created Successfully");
					String InvestigationNum = driver
							.findElement(By.xpath("//*[@id='bd']/table[4]/tbody/tr[2]/td[1]/span[2]")).getText();
					log.writeSubSection("\nInvestigation Created Successfully ", "InvestigationID: " + InvestigationNum,
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
				Thread.sleep(3000);

				// Add Contact Record
				Create_ContactRecord add_contact_metadata = new Create_ContactRecord();
				df = new DataFile(Data_File_Path, "Contact Record");
				add_contact_metadata.setUpDriver(driver);
				add_contact_metadata.setUpLog(log);
				add_contact_metadata.setUpDataFile(df);

				add_contact_metadata.Add_Contact(col);

				log = add_contact_metadata.getLog();

			} catch (Exception e) {
				// System.out.println(e.getMessage());
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

	@After
	public void close() throws Exception {
		log.close();
		df.close();
		driver.close();
	}
}
