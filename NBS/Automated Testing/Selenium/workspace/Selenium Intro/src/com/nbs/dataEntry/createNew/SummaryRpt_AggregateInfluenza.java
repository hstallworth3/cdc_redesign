package com.nbs.dataEntry.createNew;

import org.junit.Test;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import org.openqa.selenium.Alert;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Common_Utils;
import com.nbs.common.Login;
import com.nbs.common.ReadMetaData;

public class SummaryRpt_AggregateInfluenza {
	public WebDriver driver;
	public Log log;
	DataFile df;
	public String Data_File_Path, Log_File_Path;
	public Robot robot = null;
	public String Investigation_Condition = "Syphilis, early latent";
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	String parentWindow = "";
	public boolean result = false;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "dataEntry.createNew\\SummaryRpt_AggregateInfluenza.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void Aggregate_InfluenzaReport() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("SummaryRpt_AggregateInfluenza", Log_File_Path);
		df = new DataFile(Data_File_Path, "AggregateReport");
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		driver.findElement(By.linkText("Data Entry")).click();
		driver.findElement(By.linkText("Summary Data")).click();
		driver.findElement(By.linkText("Click Here")).click();

		ReadMetaData InfluenzaRpt = new ReadMetaData();
		InfluenzaRpt.setUpDriver(driver);
		InfluenzaRpt.setUpDataFile(df);
		int colCount = df.getColCount() - 3;

		System.out.println("Col Count: " + colCount);

		for (int col = 1; col <= colCount; col++) {

			// Enter MMWR Year
			String MMWRYear = InfluenzaRpt.getFieldValue("MMWR Year", col);
			driver.findElement(By.name("SUM101S_textbox")).clear();
			;
			driver.findElement(By.name("SUM101S_textbox")).sendKeys(MMWRYear);
			robot.keyPress(KeyEvent.VK_TAB);

			// Enter MMWR Week
			String MMWRWeek = InfluenzaRpt.getFieldValue("MMWR Week", col);
			System.out.println("MMWRWeek: " + MMWRWeek);
			driver.findElement(By.name("SUM102S_textbox")).clear();
			;
			driver.findElement(By.name("SUM102S_textbox")).sendKeys(MMWRWeek);
			robot.keyPress(KeyEvent.VK_TAB);

			// Log the Year and Week
			log.write("Report Date: " + MMWRYear + " " + MMWRWeek);

			// click on search
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id='searchBlock_1']/tbody/tr[5]/td/input[1]")).click();

			// If no data exits - click on "click here" link to create a new
			// aggregate report
			// Check if the message box is present
			result = comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[3]/form/div[2]"));
			if (result) {
				System.out.println("Found Click Here Link box");
				String rslt = driver.findElement(By.xpath("//*[@id='bd']/div[3]/form/div[2]")).getText();

				// If the message contains "No aggregate report data currently
				// exists for this criteria" - click on "Click Here" Link
				if (rslt.contains("No aggregate report data currently exists for this criteria")) {
					System.out.println("Click Here box contains - No aggregate report data currently exists Msg");
					driver.findElement(By.linkText("Click Here")).click();

					// Read MetaData
					InfluenzaRpt.ReadData(col);

					// Click on Submit
					driver.findElement(By.xpath("//*[@id='editBlock']/div/table[4]/tbody/tr/td/input[1]")).click();

					// Look for Error Message
					result = driver.findElement(By.xpath("//*[@id='bd']/div[3]/form/div[4]")).isDisplayed();
					System.out.println("Find ErrorMsg box?  " + result);
					if (result) {
						log.writeSubSection("Report not added Sucessfully", "", false);

						// Click on Cancel Button
						driver.findElement(By.xpath("//*[@id='editBlock']/div/table[4]/tbody/tr/td/input[3]")).click();
						System.out.println("Clicked on Cancel Button");
						Alert alert = driver.switchTo().alert();
						alert.accept();

					} else {

						// Look for "Successfully saved" message
						result = comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[3]/form/div[5]"));
						System.out.println("Find SucessMsg box:   " + result);

						if (result) {
							String sucessMsg = driver.findElement(By.xpath("//*[@id='bd']/div[3]/form/div[5]"))
									.getText();

							if (sucessMsg.contains("The Aggregate Report was successfully saved")) {
								log.writeSubSection("The Aggregate Report was successfully added ", "", true);
								System.out.println("The msg is - : The Aggregate Report was successfully saved  ");
								result = comUtils.isElementDisplayed(
										By.xpath("//*[@id='aggregatesum']/table/tbody/tr/td/input[1]"));
								System.out.println("Find Edit button:   " + result);
								if (result) {

									// click on edit button
									driver.findElement(By.xpath("//*[@id='aggregatesum']/table/tbody/tr/td/input[1]"))
											.click();
									System.out.println("Clicked onEdit button:   ");

									// Read Edit MetaData
									df = new DataFile(
											"C:\\Selenium\\Data\\DataEntry_SummaryReport_AggregateInfluenza.xls",
											"Edit");
									ReadMetaData InfluenzaRpt_Edit = new ReadMetaData();
									InfluenzaRpt_Edit.setUpDriver(driver);
									InfluenzaRpt_Edit.setUpLog(log);
									InfluenzaRpt_Edit.setUpDataFile(df);
									InfluenzaRpt_Edit.ReadData(col);

									// click on Submit button
									driver.findElement(
											By.xpath("//*[@id='editBlock']/div/table[4]/tbody/tr/td/input[1]")).click();

									// Read the success message
									Thread.sleep(1000);
									result = comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[3]/form/div[5]"));

									if (result) {
										sucessMsg = driver.findElement(By.xpath("//*[@id='bd']/div[3]/form/div[5]"))
												.getText();

										if (sucessMsg.contains("The Aggregate Report was successfully saved"))
											log.writeSubSection(
													"The Aggregate Report was successfully Edited and saved", "", true);
									}
								}
							}
						}
					}

					// Log Record Status
					log.writeSubSection_RecordStatus();
				} else {

					// If "No Report present -click here" Message is not
					// displayed. Click on edit icon and edit
					System.out.println("Message is not - No report Present");
					result = comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr/td[2]/a/img"));
					System.out.println("Edit Icon?   " + result);
					if (result) {
						System.out.println("Click Edit Icon   ");
						driver.findElement(By.xpath("//*[@id='parent']/tbody/tr/td[2]/a/img")).click();

						// Read Edit MetaData
						ReadMetaData InfluenzaRpt_Edit = new ReadMetaData();
						InfluenzaRpt_Edit.setUpDriver(driver);
						InfluenzaRpt_Edit.setUpLog(log);
						InfluenzaRpt_Edit.setUpDataFile(df);
						InfluenzaRpt_Edit.ReadData(col);

						// click on Submit button
						driver.findElement(By.xpath("//*[@id='editBlock']/div/table[4]/tbody/tr/td/input[1]")).click();

						// Read the success message
						Thread.sleep(1000);
						result = comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[3]/form/div[5]"));

						if (result) {
							String sucessMsg1 = driver.findElement(By.xpath("//*[@id='bd']/div[3]/form/div[5]"))
									.getText();

							if (sucessMsg1.contains("The Aggregate Report was successfully saved"))
								log.writeSubSection("The Aggregate Report was successfully Edited and saved", "", true);
						}
					}
				}

				// Log Record Status
				log.writeSubSection_RecordStatus();
			} else {
				System.out.println("Click Here box not present");
			}
		}
	}


	@After
	public void close() throws Exception {
		log.close();
		driver.close();
	}
}
