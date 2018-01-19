package com.nbs.reports.publicReports.STDReports;

import org.junit.Test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class PA01_CaseManagement_InterviewAssignDate_STD {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public Robot robot = null;
	public String Data_File_Path, Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "publicReports.STDReports//PublicReports_STDReports.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void Populate_STDReport() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("PA01_CaseManagement_InterviewAssignDate_STD", Log_File_Path);
		df = new DataFile(Data_File_Path, "PA01_CaseMgmt_Interview_STD");
		int colCount = df.getColCount() - 3;
		
		ReadMetaData std_Report = new ReadMetaData();
		std_Report.setUpDataFile(df);
		std_Report.setUpDriver(driver);
		std_Report.setUpLog(log);
		
		for (int col = 1; col <= colCount; col++) {
			
			try {
				// Click on the Reports link on the Home Page
				driver.findElement(By.linkText("Reports")).click();

				// Click on STD Report Section under Default Reports 
				int env_index = 0;
				for (int i = 1;i<=10;i++){
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='Public"+i+"']/thead/tr/th/a/img")))
						if (driver.findElement(By.xpath("//*[@id='Public"+i+"']/thead/tr/th")).getText().equals("STD Report Section")){
							env_index = i;
							break;
						}

				}

				// Need to change it according to diff environment
				driver.findElement(By.xpath("//*[@id='Public" + env_index + "']/thead/tr/th/a/img")).click();

				// Read the values from excel and write them to log
				String DIAGNOSIS_CD = std_Report.getFieldValue("DIAGNOSIS_CD", col);
				String DIAGNOSIS_CD_SelectAll = std_Report.getFieldValue("DIAGNOSIS_CD_SelectAll", col);
				String DIAGNOSIS_CD_IncludeNulls = std_Report.getFieldValue("DIAGNOSIS_CD_IncludeNulls", col);
				//log.write("Epi Link ID: " + EpiLinkID + ";" + "  DIAGNOSIS_CD:  " + DIAGNOSIS_CD+ ";" + " DIAGNOSIS_CD_SelectAll: " + DIAGNOSIS_CD_SelectAll + ";" + "  DIAGNOSIS_CD_IncludeNulls:  " + DIAGNOSIS_CD_IncludeNulls);
				if((DIAGNOSIS_CD_SelectAll).equalsIgnoreCase("Yes") && (DIAGNOSIS_CD_IncludeNulls).equalsIgnoreCase("Yes"))
					log.write("DIAGNOSIS_CD: "+"Select All;"+ "   DIAGNOSIS_CD: "+ "Include Nulls;");  
				else if((DIAGNOSIS_CD_SelectAll).equalsIgnoreCase("Yes")) 
					log.write("DIAGNOSIS_CD: "+"Select All;");
				else if((DIAGNOSIS_CD_IncludeNulls).equalsIgnoreCase("Yes")) 
					log.write(" DIAGNOSIS_CD:  " + DIAGNOSIS_CD+ ";" + "DIAGNOSIS_CD:  " + "Include Nulls");
				else
					log.write("DIAGNOSIS_CD:  " + DIAGNOSIS_CD+ ";" );
				
				// Click on Run - Get the index of the Run button
				
				int index = 0;
				WebElement baseTable = driver.findElement(By.id("DT-" + env_index + ""));
				List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
				Iterator<WebElement> itr = tableRows.iterator();
				while (itr.hasNext()) {
					index = index + 1;
					if (tableRows.get(index).getText().contains("PA01 Case Management Report (Interview Assign Date) - STD"))
						break;
				}
				// Store the current window handle
				String winHandleBefore = driver.getWindowHandle();
				driver.findElement(By.xpath("//*[@id='DT-" + env_index + "']/tbody/tr[" + index + "]/td[1]/a")).sendKeys(Keys.NULL);
				try {
					robot = new Robot();
				} catch (AWTException e) {
					e.printStackTrace();
				}



				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER); 
				Thread.sleep(1000);
				
				int noOfWindows= driver.getWindowHandles().size();
				if (noOfWindows > 1){
					log.writeSubSection("An error occurred while executing the report","",false);
					// Switch to new window opened
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
					}
					log.takeSnapshot(driver);
					driver.close();
					driver.switchTo().window(winHandleBefore);
					log.writeSubSection_RecordStatus();
					continue;
					
				}

					// Read the Meta Data and populate the fields
				std_Report.ReadData(col);
				// Click on Run to run the Report
				driver.findElement(By.id("id_run_bottom_ToolbarButtonGraphic")).click();
				Thread.sleep(7000);
								
				if (!comUtils.isElementDisplayed(By.id("error1"))) {

					// Switch to new window opened
					for (String winHandle : driver.getWindowHandles()) {
						
						driver.switchTo().window(winHandle);
					}
					
					//Wait till the window is completely loaded
					comUtils.waitForLoad(driver);
					Thread.sleep(2000);
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='pageContainer1']/xhtml:div[2]/xhtml:div[4]"))) {

						System.out.println(
								driver.findElement(By.xpath("//*[@id='pageContainer1']/xhtml:div[2]/xhtml:div[4]")).getText());
						
						if (driver.findElement(By.xpath("//*[@id='pageContainer1']/xhtml:div[2]/xhtml:div[4]")).getText()
								.contains("CASE MANAGEMENT REPORT"))
							log.writeSubSection("Report executed successfully", "", true);
						else
							log.writeSubSection("Report did NOT execute successfully", "", false);
					} else if (comUtils.isElementDisplayed(
							By.xpath("//*[@id='1']/tbody/tr/td/table/tbody/tr[1]/td[1]/a/font"))) {
						if (driver.findElement(By.xpath("//*[@id='1']/tbody/tr/td/table/tbody/tr[1]/td[1]/a")).getText()
								.contains("Error Message"))
							log.writeSubSection("Report did NOT execute successfuly", "", false);
					} else if (driver.findElement(By.xpath("html/body")).getText().contains(
							"There is no data for the criteria you selected. Please check your selection and try again"))
						log.writeSubSection("There is no data for criteria you selected", "", true);
					else if (driver.findElement(By.xpath("html/body")).getText().contains(
							"Your search has resulted in an abnormally large number of records. Please refine your search criteria entered."))
						log.writeSubSection("Search has resulted in an abnormally large number of records", "", true);
					else
						log.writeSubSection("Report did NOT execute successfully", "", false);

					log.takeSnapshot(driver);

					// Close the new window, if that window no more required
					driver.close();

					// Switch back to original browser (first window)
					driver.switchTo().window(winHandleBefore);

				} else {
					log.writeSubSection("Report did NOT execute successfully:Insufficient Data", "", false);
					log.takeSnapshot(driver);
				}
				// Switch back to original browser (first window)
				driver.switchTo().window(winHandleBefore);
			
			} catch (Exception e) {
				e.printStackTrace();
				log.writeSubSection("Report did NOT execute successfully:Exception Generated", "", false);
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
