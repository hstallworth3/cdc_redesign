package com.nbs.investigations;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
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

public class HIVInvestigation_Patient {
	public WebDriver driver;
	public Log log;
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
		Data_File_Path = login.Data_File_Path + "investigations\\HIVInvestigation_Patient.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void HIVInvestigation() throws Exception {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		comUtils.setUpDriver(driver);
		log = new Log("HIVInvestigation_Patient", Log_File_Path);
		df = new DataFile(Data_File_Path, "HIVInvestigation");
		ReadMetaData newHIV_Patient = new ReadMetaData();
		newHIV_Patient.setUpDriver(driver);
		newHIV_Patient.setUpDataFile(df);
		int colCount = df.getColCount() - 3;

		for (int col = 1; col <= colCount; col++) {
			String lastName = newHIV_Patient.getFieldValue("Last Name", col);
			log.write("Patient Last Name: " + lastName);
			String parentWindow = driver.getWindowHandle();
			try {
				// patient
				driver.findElement(By.id("DEM102")).sendKeys(lastName);
				driver.findElement(By.xpath("//*[@id='patientSearchByDetails']/table[2]/tbody/tr[8]/td[2]/input[1]"))
						.click();

				if (comUtils.isElementDisplayed(By.xpath("//*[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")))
					driver.findElement(By.xpath("//*[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();
				else {
					driver.findElement(By.name("Submit")).click();
					driver.findElement(By.id("Submit")).click();
				}

				driver.findElement(By.xpath("//*[@id='tabs0head1']")).click();
				driver.findElement(By.xpath("//*[@id='subsect_Inv']/table/tbody/tr/td[3]/input")).click();

				// Select Condition
				DataFile df_Condition = new DataFile(Data_File_Path, "Conditions");
				headers = df_Condition.Headers_Conditions("Conditions", col);
				driver.findElement(By.name("ccd_textbox")).sendKeys(headers.get("Condition"));

				driver.findElement(By.id("nestedElementsControllerController")).click(); // to
																							// lose
																							// focus

				// Select Referral Basis
				driver.findElement(By.name("referralBasis_textbox")).sendKeys(headers.get("Referral Basis"));
				driver.findElement(By.xpath("//*[@id='referralBasis_ac_table']/tbody/tr[1]/td")).click();

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

				newHIV_Patient.ReadData(col);
				driver = newHIV_Patient.getDriver();
				df = newHIV_Patient.getDataFile();

				// Click on Submit to submit the investigation
				driver.findElement(By.id("Submit")).click();

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
				DataFile df_interview = new DataFile(Data_File_Path, "Interview");
				add_interview.setUpDriver(driver);
				add_interview.setUpLog(log);
				add_interview.setUpDataFile(df_interview);
				add_interview.Add_Interview(col);
				driver = add_interview.getDriver();
				log = add_interview.getLog();
				df = add_interview.getDataFile();
				Thread.sleep(1000);

				// Add Contact Record
				Create_ContactRecord add_contact_metadata = new Create_ContactRecord();
				DataFile df = new DataFile(Data_File_Path, "Contact Record");
				add_contact_metadata.setUpDriver(driver);
				add_contact_metadata.setUpLog(log);
				add_contact_metadata.setUpDataFile(df);
				add_contact_metadata.Add_Contact(col);

				driver.switchTo().window(parentWindow);

			} catch (Exception e) {
				if (e.getMessage().contains("Unable to locate element")) {
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='botProcessingDecisionId']/input"))) {
						log.writeSubSection("\nInvestigation NOT Created");
						log.writeSubSection("Investigation for this conditon already exists");
						log.takeSnapshot(driver);
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
