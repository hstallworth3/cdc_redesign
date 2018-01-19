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

public class Create_Interview {
	public WebDriver driver;
	public Log log;
	DataFile df;
	public Robot robot = null;
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

	public void Add_Interview(int col) throws Exception {
		comUtils.setUpDriver(driver); 
		
		// Click on Contact Record Tab
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id='tabs0head5']")).click();
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		// Click on Add New Interview
		String parentWindow = driver.getWindowHandle();
		if (comUtils.isElementDisplayed(By.name("submitIXS"))) {//##Changed from present to displayed
			driver.findElement(By.name("submitIXS")).sendKeys(Keys.NULL);

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			if (comUtils.isAlertPresent()) {
				log.writeSubSection("Patient Interview Status is not Interviewed - Interview cannot be added");
			} else {

				try {

					// Handling pop up window
					String subWindowHandler = null;
					driver.getWindowHandles();
					Set<String> handles = driver.getWindowHandles();
					Iterator<String> iterator = handles.iterator();

					while (iterator.hasNext()) {
						subWindowHandler = iterator.next();
					}
					driver.switchTo().window(subWindowHandler);

					// Add Interview Details and Click on Submit Button
					ReadMetaData newSTD_Patient = new ReadMetaData();
					newSTD_Patient.setUpDriver(driver);
					newSTD_Patient.setUpLog(log);
					newSTD_Patient.setUpDataFile(df);

					newSTD_Patient.ReadData(col);

					driver = newSTD_Patient.getDriver();
					log = newSTD_Patient.getLog();
					df = newSTD_Patient.getDataFile();

					driver.findElement(By.name("Submit")).click();

					// Log the Interview Results
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='errorMessages']/b"))) {//##Changed from present to displayed
						log.writeSubSection("\nInterview Not Created Successfully", "", false);
						log.takeSnapshot(driver);

						driver.findElement(By.name("Cancel")).click();
						Alert alert = driver.switchTo().alert();
						alert.accept();
					} else {
						if (driver.findElement(By.id("successMessages")).isDisplayed()) {
							log.writeSubSection("\nInterview Created Successfully", "", true);
							driver.findElement(By.name("Cancel")).click();
						} else {
							driver.findElement(By.name("Cancel")).click();
						}
					}

				} catch (Exception e) {
					if (e.getMessage().contains("UnhandledAlertException")) {
						log.writeSubSection("Patient Interview Status is not Interviewed - Interview cannot be added");
						driver.switchTo().alert().accept();
					}
				}
			}
			driver.switchTo().window(parentWindow);
		} else {
			log.writeSubSection("Add New Interview Button Not Present");
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
}