package com.nbs.systemManagement.pageManagement;

import java.awt.Robot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import java.awt.event.KeyEvent;

public class PageManagement_Utils {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public Robot robot = null;
	public String Data_File_Path, Log_File_Path, Screenshot_File_Path;

	// @Before
	public void setUp() throws Exception {
		Login login= new Login();
		Screenshot_File_Path = login.Screenshot_File_Path;
	}

	public void setUpDriver(WebDriver driver) throws Exception {
		this.driver = driver;
	}

	public void setUpLog(Log logFile) throws Exception {

		this.log = logFile;
	}

	// @Test

	public void Print_Download() throws Exception {

		// Switch to File save window
		driver.switchTo().activeElement();

		Thread.sleep(2000);
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
		//driver.switchTo().activeElement();
		robot.delay(1000);
		Thread.sleep(1000);
		log.takeSnapshot(Screenshot_File_Path, "CurrentScreenshot");
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_F);
		robot.keyPress(KeyEvent.VK_X);
		robot.keyRelease(KeyEvent.VK_ALT);
		Thread.sleep(1000);
	}

	public int getRowNum(String Unique_ID, By searchBox, By searchButton, int col) throws Exception {
		int rowNum = 0;
		String msg = "";
		String id = "";

		Common_Utils comUtils = new Common_Utils();
		comUtils.setUpDriver(driver);

		// Enter the Unique ID in the Search filter and click on OK button
		driver.findElement(searchBox).sendKeys(Unique_ID);
		Thread.sleep(2000);
		driver.findElement(searchButton).click();

		// To read Number of Questions returned from search.....
		int NumOfRows = driver.findElements(By.xpath("//*[@id='parent']/tbody/tr")).size();
		System.out.println("Number of Rows: " + NumOfRows);

		// Get the contents of the first row to check if it returns " Not Found"
		// message
		if (comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr/td"))) {
			msg = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr/td")).getText();
		}

		if (NumOfRows == 1 && msg.trim().contains("Nothing found to display")) {// If search returned some values
			// log.writeSubSection("Unique ID for the Question is NOT Present.");
			return rowNum;
		} else {

			// Loop for the number of results returned
			for (int i = 1; i <= (NumOfRows); i++) {

				// Read Unique id
				if (comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr[" + i + "]/td["+col+"]")))
					id = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[" + i + "]/td["+col+"]")).getText();

				if (id.equalsIgnoreCase(Unique_ID)) {
					return i;
				}
			}
		}
		return rowNum;
	}

	public WebDriver getDriver() throws Exception {
		return driver;
	}

	public Log getLog() throws Exception {
		return log;
	}
}
