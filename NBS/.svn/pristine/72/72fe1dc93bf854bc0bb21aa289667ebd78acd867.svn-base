package com.nbs.reports.publicReports.defaultReports;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class SR13_CountsOf_SelectedDiseases_ByCaseStatus {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "publicReports.defaultReports\\PublicReports_DefaultReports.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void Populate_SR13_Report() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("SR13_Report", Log_File_Path);
		df = new DataFile(Data_File_Path, "SR13");
		
		ReadMetaData public_Report = new ReadMetaData();
		public_Report.setUpDataFile(df);
		public_Report.setUpDriver(driver);
		public_Report.setUpLog(log);
		
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Click on the Reports link on the Home Page
				driver.findElement(By.linkText("Reports")).click();
				// Click on Default Report Section under Public Reports (in some
				// environment Public4 is available and in some environments
				// PUblic2 is available...)
				int env_index = 0;
				for (int i = 1;i<=10;i++){
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='Public"+i+"']/thead/tr/th/a/img")))
						if (driver.findElement(By.xpath("//*[@id='Public"+i+"']/thead/tr/th")).getText().equals("Default Report Section")){
							env_index = i;
							break;
						}

				}
				driver.findElement(By.xpath("//*[@id='Public" + env_index + "']/thead/tr/th/a/img")).click();
				//ReadMetaData public_Report = new ReadMetaData();
				//public_Report.setUpDataFile(df);

				// Read the values of Disease, State, From_Date, To_Date from
				// excel and write them to log
				String disease = public_Report.getFieldValue("Diseases", col);
				String disease_selectall = public_Report.getFieldValue("Diseases_SelectAll", col);
				if ((disease_selectall).equalsIgnoreCase("Yes"))
					log.write("Disease: " + "Select All");
				else
					log.write("Disease " + disease + ";");

				//public_Report.setUpDriver(driver);
				//public_Report.setUpLog(log);

				// Click on Run - Get the index of the Run button with Text -
				// SR13: Counts of Selected Diseases By Case Status
				int index = 0;
				WebElement baseTable = driver.findElement(By.id("DT-" + env_index + ""));
				List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
				Iterator<WebElement> itr = tableRows.iterator();
				while (itr.hasNext()) {
					index = index + 1;
					if (tableRows.get(index).getText().contains("SR13: Counts of Selected Diseases By Case Status"))
						break;
				}

				driver.findElement(By.xpath("//*[@id='DT-" + env_index + "']/tbody/tr[" + index + "]/td[1]/a")).click();

				// Read the Meta Data and populate the fields
				public_Report.ReadData(col);
				// Store the current window handle
				String winHandleBefore = driver.getWindowHandle();
				// Click on Run to run the Report
				driver.findElement(By.id("id_run_bottom_ToolbarButtonGraphic")).click();
				Thread.sleep(8000);
				if (!comUtils.isElementDisplayed(By.id("error1"))) {

					// Switch to new window opened
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
					}
					Thread.sleep(2000);
					if (comUtils.isElementDisplayed(
							By.xpath("html/body/div[1]/table[1]/tbody/tr[2]/td/div/table/tbody/tr[1]/td/b/u"))) {
						System.out.println(driver
								.findElement(
										By.xpath("html/body/div[1]/table[1]/tbody/tr[2]/td/div/table/tbody/tr[1]/td/b"))
								.getText());
						if (driver
								.findElement(
										By.xpath("html/body/div[1]/table[1]/tbody/tr[2]/td/div/table/tbody/tr[1]/td/b"))
								.getText().contains("Report content"))
							log.writeSubSection("Report is executed successfully", "", true);
						// if
						// (driver.findElement(By.xpath("td[contains(text(),'Report
						// Content')]/following-sibling::td[contains(@class,'c
						// systemtitle2')]")) != null)
						else
							log.writeSubSection("Report is NOT executed successfully", "", false);
					} else if (comUtils
							.isElementDisplayed(By.xpath("//*[@id='1']/tbody/tr/td/table/tbody/tr[1]/td[1]/a/font"))) {
						if (driver.findElement(By.xpath("//*[@id='1']/tbody/tr/td/table/tbody/tr[1]/td[1]/a")).getText()
								.contains("Error Message"))
							log.writeSubSection("Report is NOT executed successfuly", "", false);

					} else if (driver.findElement(By.xpath("html/body")).getText().contains(
							"There is no data for the criteria you selected. Please check your selection and try again"))
						log.writeSubSection("There is no data for criteria you selected", "", true);
					else if (driver.findElement(By.xpath("html/body")).getText().contains(
							"Your search has resulted in an abnormally large number of records. Please refine your search criteria entered."))
						log.writeSubSection("Search has resulted in an abnormally large number of records", "", true);
					else
						log.writeSubSection("Report is NOT executed successfully", "", false);

					log.takeSnapshot(driver);
					// Close the new window, if that window no more required

					driver.close();
				} else {
					log.writeSubSection("Report is NOT executed successfully", "", false);
					log.takeSnapshot(driver);

				}
				// Switch back to original browser (first window)
				driver.switchTo().window(winHandleBefore);

			} catch (Exception e) {
				e.printStackTrace();
				StackTraceElement[] elements = e.getStackTrace();
				log.writeSubSection(elements[0] + " Report is NOT executed successfully", "", false);
				log.takeSnapshot(driver);
			}
			log.writeSubSection_RecordStatus();
		}

	}

	@After
	public void close() throws Exception {
		log.close();
		df.close();
		driver.close();
	}

}
