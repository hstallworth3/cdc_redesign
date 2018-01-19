package com.nbs.investigations;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Hashtable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class GenericInvestigation_Patient {
	public WebDriver driver;
	private Log log;
	DataFile df;
	public Robot robot = null;
	public String parentWindow;
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "investigations\\GenericInvestigation_Patient.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void newStdInvestigation() throws Exception {

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		comUtils.setUpDriver(driver);
		log = new Log("GenericInvestigation_Patient", Log_File_Path);
		df = new DataFile(Data_File_Path, "GenericInvestigation");
		ReadMetaData newSTD_Patient = new ReadMetaData();
		newSTD_Patient.setUpDriver(driver);
		newSTD_Patient.setUpLog(log);
		newSTD_Patient.setUpDataFile(df);
		System.out.println("col count : " + df.getColCount());
		int colCount = df.getColCount() - 3;
		System.out.println("col count2 : " + colCount);

		for (int col = 1; col <= colCount; col++) {

			// Read the Patient Last Name from excel
			String lastName = newSTD_Patient.getFieldValue("Last Name", col);
			String parentWindow = driver.getWindowHandle();
			try {

				// Enter Patient Last Name
				driver.findElement(By.id("DEM102")).sendKeys(lastName);
				driver.findElement(By.xpath("//*[@id='patientSearchByDetails']/table[2]/tbody/tr[8]/td[2]/input[1]"))
						.click();

				if (comUtils.isElementDisplayed(By.xpath("//*[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")))
					driver.findElement(By.xpath("//*[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();
				else {
					driver.findElement(By.name("Submit")).click();
					driver.findElement(By.id("Submit")).click();
				}

				// Log the Patient Information
				log.write("Patient Last Name: " + lastName);

				driver.findElement(By.xpath("//*[@id='tabs0head1']")).click();
				driver.findElement(By.xpath("//*[@id='subsect_Inv']/table/tbody/tr/td[3]/input")).click();

				// Select Condition
				DataFile df_Condition = new DataFile(Data_File_Path, "Conditions");
				headers = df_Condition.Headers_Conditions("Conditions", col);

				Thread.sleep(2000);
				driver.findElement(By.name("ccd_textbox")).sendKeys(headers.get("Condition"));

				driver.findElement(By.id("nestedElementsControllerController")).click(); // to
																							// lose
																							// focus
				driver.findElement(By.id("Submit")).click();

				driver.switchTo().window(parentWindow);

				// Populate Investigation Screens
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
				DataFile df_interview = new DataFile(Data_File_Path, "Interview");
				add_interview.setUpDriver(driver);
				add_interview.setUpLog(log);
				add_interview.setUpDataFile(df_interview);
				add_interview.Add_Interview(col);
				driver = add_interview.getDriver();
				log = add_interview.getLog();
				df = add_interview.getDataFile();
				Thread.sleep(3000);

				// Add Contact Record
				Create_ContactRecord add_contact_metadata = new Create_ContactRecord();
				DataFile df = new DataFile(Data_File_Path, "Contact Record");
				add_contact_metadata.setUpDriver(driver);
				add_contact_metadata.setUpLog(log);
				add_contact_metadata.setUpDataFile(df);
				add_contact_metadata.Add_Contact(col);

				driver.switchTo().window(parentWindow);

			} catch (Exception e) {
				System.out.println(e.getMessage());
				if (e.getMessage().contains("Unable to locate element")) {
					System.out.println(e.getMessage());
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='botProcessingDecisionId']/input"))) {
						driver.findElement(By.xpath("//*[@id='botProcessingDecisionId']/input")).click();
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
