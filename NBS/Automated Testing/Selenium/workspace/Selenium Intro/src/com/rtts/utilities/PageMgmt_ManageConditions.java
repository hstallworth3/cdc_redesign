package com.rtts.utilities;

import org.junit.Test;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;

import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class PageMgmt_ManageConditions {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public Robot robot = null;
	public String Data_File_Path, Log_File_Path;

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "SM_PM_ManageConditions.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void ManageConditions() throws Exception {
		log = new Log("PageMgmt_ManageConditions", Log_File_Path);
		df = new DataFile(Data_File_Path, "Add");
		NBSUtils utils = new NBSUtils();
		utils.setLog(log);
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Click on the System Management link on the Home Page
				driver.findElement(By.linkText("System Management")).click();
				// Expand Page Management
				driver.findElement(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img")).click();
				// Click on Manage Conditions link text
				driver.findElement(By.linkText("Manage Conditions")).click();
				// Click on Add New
				driver.findElement(By.id("submitCr")).click();
				ReadMetaData manage_Conditions = new ReadMetaData();
				manage_Conditions.setUpDataFile(df);

				// Read the value Condition Name from excel and write it to log
				String condition_name = manage_Conditions.getFieldValue("Condition Name", col);
				log.write("Adding the record for Condition name: " + condition_name);
				manage_Conditions.setUpDriver(driver);
				manage_Conditions.setUpLog(log);

				// Read the Meta Data and populate the fields
				manage_Conditions.ReadData(col);

				// Click on Submit button
				driver.findElement(By.id("submitB")).click();
				if (isElementDisplayed(By.xpath("//*[@id='bd']/div[3]"))) {

					if (driver.findElement(By.xpath("//*[@id='bd']/div[3]")).getText()
							.contains(condition_name + " has been successfully added to the system"))
						log.writeSubSection("Record for Condition Name: " + condition_name + " is added successfully",
								"", true);
					else {
						log.writeSubSection(
								"Record for Condition Name: " + condition_name + " is not added successfully", "",
								false);
						log.takeSnapshot(driver);
					}

				} else {
					log.writeSubSection("Record for Condition Name: " + condition_name + " is not added successfully",
							"", false);
					log.takeSnapshot(driver);
				}

			} catch (Exception e) {
				log.writeSubSection("There is an error adding the condition", "", false);
				log.takeSnapshot(driver);
			}
			log.writeSubSection_RecordStatus();
		}

		// To Edit the Condition names

		df = new DataFile(Data_File_Path, "Edit");
		ReadMetaData manage_Conditions = new ReadMetaData();
		manage_Conditions.setUpDataFile(df);
		colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Click on the System Management link on the Home Page
				driver.findElement(By.linkText("System Management")).click();
				// Expand Page Management
				driver.findElement(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img")).click();
				// Click on Manage Conditions link text
				driver.findElement(By.linkText("Manage Conditions")).click();

				// Read the value Condition Name from excel and write it to log
				String condition_name = manage_Conditions.getFieldValue("Condition Name", col);
				log.write("Editing the record for Condition name: " + condition_name);

				// Click on condition filter
				driver.findElement(By.id("queueIcon")).click();

				// Enter the Condition name in the condition filter and click Ok
				driver.findElement(By.id("SearchText1")).sendKeys(condition_name);
				driver.findElement(By.id("b2SearchText1")).click();

				if (isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr/td[2]/a/img"))) {
					// Click on the Edit icon to edit the record
					driver.findElement(By.xpath("//*[@id='parent']/tbody/tr/td[2]/a/img")).click();
					// Read the Meta Data and populate the fields
					manage_Conditions.ReadData(col);

					// Click on Submit button
					driver.findElement(By.id("submitB")).click();
					Thread.sleep(1000);
					if (isElementDisplayed(By.xpath("//*[@id='bd']/div[3]"))) {

						if (driver.findElement(By.xpath("//*[@id='bd']/div[3]")).getText()
								.contains(condition_name + " has been successfully updated in the system"))
							log.writeSubSection(
									"Record for Condition Name: " + condition_name + " is updated successfully", "",
									true);

					} else {
						log.writeSubSection(
								"Record for Condition Name: " + condition_name + " is NOT updated successfully", "",
								false);
						log.takeSnapshot(driver);
					}
				} else {
					log.writeSubSection("Condition Name:" + condition_name + " is not found", "", false);
					log.takeSnapshot(driver);
				}

			} catch (Exception e) {
				log.writeSubSection("There is an error updating the condition", "", false);
				log.takeSnapshot(driver);
			}

			log.writeSubSection_RecordStatus();

		}

		// To Inactivate the Conditions

		df = new DataFile(Data_File_Path, "Inactive");
		ReadMetaData inactive_Conditions = new ReadMetaData();
		inactive_Conditions.setUpDataFile(df);
		colCount = df.getColCount() - 1;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Click on the System Management link on the Home Page
				driver.findElement(By.linkText("System Management")).click();
				// Expand Page Management
				driver.findElement(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img")).click();
				// Click on Manage Conditions link text
				driver.findElement(By.linkText("Manage Conditions")).click();

				// Read the value Condition Name from excel and write it to log
				String condition_name = inactive_Conditions.getFieldValue("Condition Name", col);
				log.write("Inactivating the record for Condition name: " + condition_name);

				// Click on condition filter
				driver.findElement(By.id("queueIcon")).click();

				// Enter the Condition name in the condition filter and click Ok
				driver.findElement(By.id("SearchText1")).sendKeys(condition_name);
				driver.findElement(By.id("b2SearchText1")).click();
				// Check if the View icon is found
				if (isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr/td[1]/a/img"))) {
					// Click on the View icon to View the record
					driver.findElement(By.xpath("//*[@id='parent']/tbody/tr/td[1]/a/img")).click();

					// Click on Make Inactive button if present
					if (isElementDisplayed(By.xpath("html/body/form/div/div/div[1]/input[2]"))) {
						driver.findElement(By.xpath("html/body/form/div/div/div[1]/input[2]")).click();
						Thread.sleep(1000);
						// Click OK on the Alert if it is present
						if (isAlertPresent()) {
							if (driver.findElement(By.xpath("//*[@id='bd']/div[3]")).getText()
									.contains(condition_name + " condition has been made inactive"))
								log.writeSubSection(
										"Condition Name: " + condition_name + " is INACTIVATED successfully", "", true);
							else {
								log.writeSubSection(
										"Condition Name: " + condition_name + " is NOT INACTIVATED successfully", "",
										false);
								log.takeSnapshot(driver);
							}
						} else {
							log.writeSubSection(
									"Condition Name: " + condition_name + " is NOT INACTIVATED successfully", "",
									false);
							log.takeSnapshot(driver);
						}

					} else {
						log.writeSubSection("INACTIVE button is not present - Condition is already Inactive");
						log.takeSnapshot(driver);
					}

				} else {
					log.writeSubSection("Condition Name: " + condition_name + " is NOT Present", "", false);
					log.takeSnapshot(driver);
				}
			} catch (Exception e) {
				log.writeSubSection("There is an error inactivating the condition", "", false);
				log.takeSnapshot(driver);
			}

			log.writeSubSection_RecordStatus();

		}
		String winHandleBefore = driver.getWindowHandle();

		// To verify the Print option

		try {
			// Click on the System Management link on the Home Page
			driver.findElement(By.linkText("System Management")).click();
			// Expand Page Management
			driver.findElement(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img")).click();
			// Click on Manage Conditions link text
			driver.findElement(By.linkText("Manage Conditions")).click();

			log.write("Printing the Condition Page");
			// String winHandleBefore = driver.getWindowHandle();
			// Check if Print button if available

			if (isElementDisplayed(By.xpath("html/body/form/div/div[1]/div[1]/input[2]"))) {

				// Click on the Print button
				driver.findElement(By.xpath("html/body/form/div/div[1]/div[1]/input[2]")).click();

				Thread.sleep(1000);

				// Switch to File save window
				driver.switchTo().activeElement();

				// if ((windHandle).equals(winHandleBefore)){
				Thread.sleep(1000);
				try {
					robot = new Robot();
				} catch (Exception e) {
					e.printStackTrace();
				}
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_O);
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_O);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				Thread.sleep(2000);
				driver.switchTo().activeElement();
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_F);
				robot.keyPress(KeyEvent.VK_X);
				robot.keyRelease(KeyEvent.VK_ALT);
				BufferedImage image = new Robot()
						.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ImageIO.write(image, "png", new File("C:\\Screenshot\\CurrentScreenshot.png"));
				driver.get("C:\\Screenshot\\CurrentScreenshot.png");
				log.takeSnapshot(driver);
				driver.close();
				Thread.sleep(2000);
				// driver.switchTo().window(winHandleBefore);
				// System.out.println("*******************"+getURL);
				log.writeSubSection("Print option is working as expected", "", true);
			} else {
				log.writeSubSection("Print Button is NOT Present", "", false);
				log.takeSnapshot(driver);
			}
		} catch (Exception e) {
			log.writeSubSection("There is an issue with Print option", "", false);
			log.takeSnapshot(driver);
		}
		driver.switchTo().window(winHandleBefore);
		log.writeSubSection_RecordStatus();

		// To verify the Download option
		log.write("Checking the download option");
		try {
			// Click on the System Management link on the Home Page
			driver.findElement(By.linkText("System Management")).click();
			// Expand Page Management
			driver.findElement(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img")).click();
			// Click on Manage Conditions link text
			driver.findElement(By.linkText("Manage Conditions")).click();

			// log.write("Checking the download option");
			// String winHandleBefore = driver.getWindowHandle();
			// Check if Download button if available

			if (isElementDisplayed(By.xpath("html/body/form/div/div[1]/div[1]/input[3]"))) {

				// Click on the Download button
				driver.findElement(By.xpath("html/body/form/div/div[1]/div[1]/input[3]")).click();

				Thread.sleep(1000);

				// Switch to File save window
				driver.switchTo().activeElement();

				// if ((windHandle).equals(winHandleBefore)){
				Thread.sleep(1000);
				try {
					robot = new Robot();
				} catch (Exception e) {
					e.printStackTrace();
				}

				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_O);
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_O);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				Thread.sleep(2000);
				driver.switchTo().activeElement();
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_F);
				robot.keyPress(KeyEvent.VK_X);
				robot.keyRelease(KeyEvent.VK_ALT);
				BufferedImage image = new Robot()
						.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ImageIO.write(image, "png", new File("C:\\Screenshot\\CurrentScreenshot.png"));
				driver.get("C:\\Screenshot\\CurrentScreenshot.png");
				log.takeSnapshot(driver);
				driver.close();
				driver.switchTo().window(winHandleBefore);

				// System.out.println("*******************"+getURL);
				log.writeSubSection("Download option is working as expected", "", true);
			} else {
				log.writeSubSection("Download Button is NOT Present", "", false);
				log.takeSnapshot(driver);
			}
		} catch (Exception e) {
			log.writeSubSection("There is an issue with Download option", "", false);
			log.takeSnapshot(driver);
		}
		driver.switchTo().window(winHandleBefore);
		log.writeSubSection_RecordStatus();
	}

	private boolean isElementDisplayed(By by) {
		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS);
			if (driver.findElement(by).isDisplayed() && driver.findElement(by).isEnabled())
				// if( driver.findElement(by).isDisplayed())
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;

		}
	}

	public boolean isAlertPresent() {

		boolean presentFlag = false;

		try {

			Alert alert = driver.switchTo().alert();
			presentFlag = true;
			alert.accept();
		} catch (NoAlertPresentException ex) {
		}

		return presentFlag;
	}

	@After
	public void close() throws Exception {
		log.close();
		df.close();
	}

}
