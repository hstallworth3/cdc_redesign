package com.nbs.common;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Common_Utils;

public class Create_ContactRecord {
	public WebDriver driver;
	public Log log;
	DataFile df;
	public Robot robot = null;
	private int rowSizeBefore = 0;
	private int rowSizeAfter = 0;
	public boolean ContactFlag = true;
	public Common_Utils comUtils = new Common_Utils();

	public void setUpDriver(WebDriver driver) throws Exception {
		this.driver = driver;
	}

	public void setUpLog(Log logFile) throws Exception {

		this.log = logFile;
	}

	public void setUpDataFile(DataFile df) throws Exception {
		this.df = df;
	}

	// @Test
	public void Add_Contact(int col) throws Exception {

		comUtils.setUpDriver(driver);
		
		// Click on Contact Record Tab
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id='tabs0head5']")).click();

		// Read Number of Contact Records before adding a new Contact Record
		rowSizeBefore = driver.findElements(By.xpath("//*[@id='contactNamedByPatListID']/tbody/tr")).size();
		String BeforeText = driver.findElement(By.xpath("//*[@id='contactNamedByPatListID']/tbody/tr[1]/td")).getText();
		if (BeforeText.equals("Nothing found to display.")) {
			rowSizeBefore = rowSizeBefore - 1;
		}

		// Add New Contact Record
		String parentWindow1 = driver.getWindowHandle();

		if (comUtils.isElementDisplayed(By.name("submitct"))) {//##Changed from present to displayed
			driver.findElement(By.name("submitct")).sendKeys(Keys.NULL);

			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(3000);

			// Handling pop up window
			String subWindowHandler1 = null;

			driver.getWindowHandles();
			Set<String> handles1 = driver.getWindowHandles();
			Iterator<String> iterator1 = handles1.iterator();
			while (iterator1.hasNext()) {
				subWindowHandler1 = iterator1.next();
			}
			driver.switchTo().window(subWindowHandler1);

			// Search for Last Name
			driver.findElement(By.id("DEM102")).sendKeys("Patient");
			driver.findElement(By.name("Search")).click();

			// Click on Add button
			driver.findElement(By.id("Add")).sendKeys(Keys.NULL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(1000);

			// Handling pop up window
			String subWindowHandler2 = null;
			driver.getWindowHandles();
			Set<String> handles2 = driver.getWindowHandles();
			Iterator<String> iterator2 = handles2.iterator();
			while (iterator2.hasNext()) {
				subWindowHandler2 = iterator2.next();
			}
			driver.switchTo().window(subWindowHandler2);

			// Add New Contact Record and Submit
			ReadMetaData newSTD_Patient = new ReadMetaData();
			newSTD_Patient.setUpDriver(driver);
			newSTD_Patient.setUpLog(log);
			newSTD_Patient.setUpDataFile(df);
			newSTD_Patient.ReadData(col);
			driver.findElement(By.name("Submit")).click();

			try {
				if (comUtils.isElementDisplayed(By.xpath("//*[@id='errorMessages']/b"))) { //##Changed from present to displayed
					log.writeSubSection("\nContact Record NOT Created Successfully", "", false);
					log.takeSnapshot(driver);
					driver.findElement(By.name("Cancel")).click();
					Alert alert = driver.switchTo().alert();
					alert.accept();
					ContactFlag = false;
				}
			} catch (Exception e) { // NoSuchWindowException
				driver.switchTo().window(parentWindow1);
			}

			driver.switchTo().window(parentWindow1);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[@id='tabs0head5']")).click();
			Thread.sleep(2000);

			// Read Number of Contact Records after adding a new Contact Record
			if (ContactFlag) {
				rowSizeAfter = driver.findElements(By.xpath("//*[@id='contactNamedByPatListID']/tbody/tr")).size();
				String AfterText = driver.findElement(By.xpath("//*[@id='contactNamedByPatListID']/tbody/tr[1]/td"))
						.getText();

				if (AfterText.equals("Nothing found to display."))
					rowSizeAfter = rowSizeAfter - 1;
				
				// Log the Contact Record Results
				if (rowSizeAfter > rowSizeBefore)
					log.writeSubSection("\nContact Record Created Successfully", "", true);
				else
					log.writeSubSection("\nContact Record NOT Created Successfully", "", false);
			}
		} else
			log.writeSubSection("Add New Contact Button Not Present");
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
}
