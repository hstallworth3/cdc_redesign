package com.nbs.reports.templateReports.defaultReports;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class LineList_IndividualLabs_ProgramArea_JurisdictionSecurity {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public String Data_File_Path, Log_File_Path;

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "templateReports.defaultReports\\TemplateReports_DefaultReports.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void Populate_TemplateReport() throws Exception {
		log = new Log("Line_List_Indi_Labs_PA_JS", Log_File_Path);
		df = new DataFile(Data_File_Path, "LineList_IndiLabs_PA_JS");
		NBSUtils utils = new NBSUtils();
		utils.setLog(log);
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Click on the Reports link on the Home Page
				driver.findElement(By.linkText("Reports")).click();
				// Click on Default Report Section under Template Reports (in
				// some environment Template10 is available and in some
				// environments Template8 is available)
				int env_index = 0;
				if (isElementDisplayed(By.xpath("//*[@id='Template10']/thead/tr/th/a/img")))
					env_index = 10;
				else
					env_index = 8;
				driver.findElement(By.xpath("//*[@id='Template" + env_index + "']/thead/tr/th/a/img")).click();
				ReadMetaData public_Report = new ReadMetaData();
				public_Report.setUpDataFile(df);

				// Read the values of Disease, State, From_Date, To_Date from
				// excel and write them to log
				String states = public_Report.getFieldValue("States", col);
				log.write("States: " + states + ";");
				public_Report.setUpDriver(driver);
				public_Report.setUpLog(log);

				// Click on Run - Get the index of the Run button with Text
				// -Line List of Isolate Tracking
				int index = 0;
				WebElement baseTable = driver.findElement(By.id("DT-" + env_index + ""));
				List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
				Iterator<WebElement> itr = tableRows.iterator();
				while (itr.hasNext()) {
					index = index + 1;
					if (tableRows.get(index).getText()
							.contains("Line List of Individual Labs with Program Area and Jurisdiction Security"))
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
				if (!isElementDisplayed(By.id("error1"))) {
					// Switch to new window opened
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
					}
					Thread.sleep(2000);
					if (isElementDisplayed(By.xpath("html/body/div[1]/table[1]/tbody/tr[1]/td/p"))) {
						System.out.println(
								driver.findElement(By.xpath("html/body/div[1]/table[1]/tbody/tr[1]/td")).getText());
						if (driver.findElement(By.xpath("html/body/div[1]/table[1]/tbody/tr[1]/td")).getText()
								.contains("Custom Report For Table: Lab_Test_Report"))
							log.writeSubSection("Report is executed successfully", "", true);
						else
							log.writeSubSection("Report is NOT executed successfully", "", false);
					} else if (isElementDisplayed(
							By.xpath("//*[@id='1']/tbody/tr/td/table/tbody/tr[1]/td[1]/a/font"))) {
						if (driver.findElement(By.xpath("//*[@id='1']/tbody/tr/td/table/tbody/tr[1]/td[1]/a")).getText()
								.contains("Error Message"))
							log.writeSubSection("Report is NOT executed successfuly", "", false);

					} else if ((driver.findElement(By.xpath("html/body")).getText().contains(
							"There is no data for the criteria you selected. Please check your selection and try again")))

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

	@After
	public void close() throws Exception {
		log.close();
		df.close();
		driver.close();
	}

}
