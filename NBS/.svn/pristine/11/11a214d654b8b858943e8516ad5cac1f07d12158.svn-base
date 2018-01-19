package com.nbs.reports.publicReports.defaultReports;

import org.junit.Test;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class SR10_MultiYear_LineGraph_DiseaseCases {

	public WebDriver driver;	
	public Log log;
	public Robot robot=null;
	DataFile df;
	public String Data_File_Path,Log_File_Path;
	public Common_Utils comUtils = new Common_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path+"publicReports.defaultReports\\PublicReports_DefaultReports.xls";
		Log_File_Path = login.Log_File_Path;		
	}

	@Test
	public void Populate_SR10() throws Exception {
		comUtils.setUpDriver(driver);
		log = new Log("SR10_Report", Log_File_Path);
		df = new DataFile(Data_File_Path, "SR10");
		ReadMetaData public_Report = new ReadMetaData();
		public_Report.setUpDataFile(df);
		public_Report.setUpDriver(driver);
		public_Report.setUpLog(log);	

		int colCount = df.getColCount()-3;	
		for(int col=1; col <=colCount; col++){	
			try {
				//Click on the Reports link on the Home Page
				driver.findElement(By.linkText("Reports")).click();
				//Click on Default Report Section under Public Reports (in some environment Public4 is available and in some environments Public2 is available)
				int env_index =0;
				for (int i = 1;i<=10;i++){
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='Public"+i+"']/thead/tr/th/a/img")))
						if (driver.findElement(By.xpath("//*[@id='Public"+i+"']/thead/tr/th")).getText().equals("Default Report Section")){
							env_index = i;
							break;
						}

				}

				driver.findElement(By.xpath("//*[@id='Public"+env_index+"']/thead/tr/th/a/img")).click();
		
				//Read the values of  Disease, State, From_Date, To_Date from excel and write them to log
				String disease = public_Report.getFieldValue("Diseases",col);
				String state = public_Report.getFieldValue("State",col);
				String from_Date = public_Report.getFieldValue("From_Date",col);
				String to_Date = public_Report.getFieldValue("To_Date",col);
				log.write("Disease: "+disease+ "; "+" State: "+state+"; "+" From Date: "+from_Date+"; "+" To Date: "+to_Date+":");	
				
				//Click on Run - Get the index of the Run button with Text - SR10: Multi-Year Line Graph of Disease Cases
				int index = 0;
				WebElement baseTable = driver.findElement(By.id("DT-"+env_index+""));
				List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
				Iterator<WebElement> itr = tableRows.iterator();
				while(itr.hasNext()) {
					index = index+1;
					if (tableRows.get(index).getText().contains("SR10: Multi-Year Line Graph of Disease Cases"))
						break;	
				}

				driver.findElement(By.xpath("//*[@id='DT-"+env_index+"']/tbody/tr["+index+"]/td[1]/a")).click();

				//Read the Meta Data and populate the fields
				public_Report.ReadData(col);
				// Store the current window handle
				String winHandleBefore = driver.getWindowHandle();

				//Click on Run to run the Report
				driver.findElement(By.id("id_run_bottom_ToolbarButtonGraphic")).click();
				Thread.sleep(8000);
				if (!comUtils.isElementDisplayed(By.id("error1"))){
					//Get all the Window Handles
					String windHandle = null;
					driver.getWindowHandles();
					Set<String> handles1 = driver.getWindowHandles(); 
					Iterator<String> iterator1 = handles1.iterator();
					while (iterator1.hasNext()){
						windHandle = iterator1.next();
					}
					if ((windHandle).equals(winHandleBefore)){ // To check if Save dialog box is displayed. If displayed, select the Save option, click on Ok button and log the test case as passed
						driver.switchTo().activeElement();
						Thread.sleep(1000);
						try{
							robot=new Robot();
						}catch (Exception e){
							e.printStackTrace();
						}
						robot.keyPress(KeyEvent.VK_ALT);
						robot.keyPress(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_ALT);
						robot.keyRelease(KeyEvent.VK_S);        
						robot.keyPress(KeyEvent.VK_ENTER);
						robot.keyRelease(KeyEvent.VK_ENTER); 
						log.writeSubSection("Report is executed successfully","",true);

					} else { 
						driver.switchTo().window(windHandle); //Switch to the new windowHandle 
						if (comUtils.isElementDisplayed(By.xpath("//*[@id='Error_Section']/tbody/tr[2]/td/table/tbody/tr/td"))){
							log.writeSubSection("Report is NOT executed successfully","",false);
							log.takeSnapshot(driver);
						} else if (comUtils.isElementDisplayed(By.xpath("//*[@id='1']/tbody/tr/td/table/tbody/tr[1]/td[1]/a/font"))) {
							if (driver.findElement(By.xpath("//*[@id='1']/tbody/tr/td/table/tbody/tr[1]/td[1]/a")).getText().contains("Error Message")){
								log.writeSubSection("Report is NOT executed successfuly","",false);
								log.takeSnapshot(driver);
							}

						}
						else if (driver.findElement(By.xpath("html/body")).getText().contains("There is no data for the criteria you selected. Please check your selection and try again")){
							log.writeSubSection("There is no data for criteria you selected","",true);
							log.takeSnapshot(driver);
						}
						else if (driver.findElement(By.xpath("html/body")).getText().contains("Your search has resulted in an abnormally large number of records. Please refine your search criteria entered.")){
							log.writeSubSection("Search has resulted in an abnormally large number of records","",true);
							log.takeSnapshot(driver);
						}

						else {
							log.writeSubSection("Report is Executed Successfuly","",true);
							log.takeSnapshot(driver);
						}

						driver.close();
						driver.switchTo().window(winHandleBefore);
					}
				} else {
					log.writeSubSection("Report is NOT executed successfully","",false);
					log.takeSnapshot(driver);
				}

			} catch(Exception e){
				e.printStackTrace();
				StackTraceElement[] elements = e.getStackTrace();
				log.writeSubSection(elements[0]+" Report is NOT executed successfully","",false);
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
