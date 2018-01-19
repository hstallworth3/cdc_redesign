package com.nbs.investigations;

import org.junit.Test;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.*;

public class STDInvestigation_LabReport {
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
		Data_File_Path = login.Data_File_Path + "investigations\\STDInvestigation_LabReport.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void STD_LabReport() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("STDInvestigation_LabReport", Log_File_Path);
		df = new DataFile(Data_File_Path, "Lab_MetaData");
		Create_LabReport STDLab = new Create_LabReport();
		STDLab.setUpDriver(driver);
		STDLab.setUpLog(log);
		STDLab.setUpDataFile(df);

		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {

				// Populate Lab Report Screens
				patID = STDLab.Populate_DE_LabReport(col);
				driver = STDLab.getDriver();
				log = STDLab.getLog();

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

				// click on CreateInvestigation
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

				driver.findElement(By.id("Submit")).sendKeys(Keys.NULL);

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				// Handling pop up window
				String subWindowHandler = null;
				driver.getWindowHandles();
				Set<String> handles = driver.getWindowHandles();
				Iterator<String> iterator = handles.iterator();

				while (iterator.hasNext()) {
					subWindowHandler = iterator.next();
				}
				driver.switchTo().window(subWindowHandler);
				driver.switchTo().window(subWindowHandler);
				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

				driver.findElement(By.name("reviewReason_textbox")).click();
				driver.findElement(By.name("reviewReason_textbox")).sendKeys(headers.get("Processing Decision"));

				if (comUtils.isElementDisplayed(By.xpath("//*[@id='botProcessingDecisionId']/input[1]")))
					driver.findElement(By.xpath("//*[@id='botProcessingDecisionId']/input[1]")).click();
				else
					driver.findElement(By.xpath("//*[@id='topProcessingDecisionId']/input[2]")).click();

				driver.switchTo().window(parentWindow);

				// Populate Investigation Screens
				df = new DataFile(Data_File_Path, "STDInvestigation");
				ReadMetaData newSTD_Patient = new ReadMetaData();
				newSTD_Patient.setUpDriver(driver);
				newSTD_Patient.setUpDataFile(df);

				newSTD_Patient.ReadData(col);

				driver = newSTD_Patient.getDriver();
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
			// Log the status of the Investigation
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
