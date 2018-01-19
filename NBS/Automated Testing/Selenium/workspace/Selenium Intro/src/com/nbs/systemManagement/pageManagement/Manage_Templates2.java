package com.nbs.systemManagement.pageManagement;

import org.junit.Test;
import java.awt.Robot;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import java.io.File;
import autoitx4java.AutoItX;
import com.jacob.com.LibraryLoader;

public class Manage_Templates2 {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public Robot robot = null;
	public String Data_File_Path, Log_File_Path, Screenshot_File_Path, Template_File_Path,JACOB_DLL;
	public Common_Utils comUtils = new Common_Utils();
	public PageManagement_Utils pmUtils = new PageManagement_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "systemManagement.pageManagement\\Manage_Templates.xls";
		Log_File_Path = login.Log_File_Path;
		Screenshot_File_Path = login.Screenshot_File_Path;
		JACOB_DLL = login.JACOB_DLL;
		Template_File_Path = login.File_Path+"Templates\\";
	}

	@Test
	public void ManageTemplates() throws Exception {
		log = new Log("PageMgmt_ManageTemplates", Log_File_Path);
		comUtils.setUpDriver(driver);
		pmUtils.setUp();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);

		File file = new File("lib", JACOB_DLL);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());

		Import_Templates(); // To Import the Template
		Export_Templates(); // To Export the Template
		Print_Templates(); // To print the Template
		Download_Templates();
		Import_Export_Activity_log();

		pmUtils.getDriver();
		pmUtils.getLog();
	}

	private void Import_Templates() throws Exception {
		df = new DataFile(Data_File_Path, "Import");
		comUtils.setUpDriver(driver);
		ReadMetaData manage_Templates = new ReadMetaData();
		manage_Templates.setUpDataFile(df);

		int colCount = df.getColCount()-1;	

		for(int col=1; col <=colCount; col++){	

			// Navigate to Manage_Templates from the Home page
			navigateToManage_Templates();

			// Read the Template Name from excel and write it to log
			String template_Name = manage_Templates.getFieldValue("Template Name", col);
			log.write("Importing the Template: " + template_Name);
			try{

				//Click on Import
				driver.findElement(By.xpath("//*[@id='bd']/div[1]/table/tbody/tr/td[2]/input[1]")).click();
				//Click on Browse to Import the template
				driver.findElement(By.xpath("//*[@id='addAttachmentBlock']/table/tbody/tr[3]/td[2]/input")).click();
				String stringSelection = Template_File_Path+template_Name;

				AutoItX x = new AutoItX();
				if (x.winExists("File Upload")){
					x.winActivate("File Upload");
					System.out.println(stringSelection);
					Thread.sleep(1000);
					//Enter the template name in the File name text box of File Upload window
					x.ControlSetText("File Upload", "", "1148",stringSelection);
					//Click on the Open button
					x.controlClick("File Upload", "", "1"); 
					x.sleep(2000);

				}
			
				//Check if the File Upload window is displayed when template is not available
				if (x.winExists("File Upload")){
				//	x.winWaitActive("File Upload");
					System.out.println("*******************")	;				
					//Click on the OK button by presssing enter
					x.send("{ENTER}",false);
					//	x.controlClick("File Upload", "", "CommandButton_1"); 
					//log.writeSubSection("Template: "+ template_Name+" is not available to import","",false);
					log.writeSubSection("Template: "+ template_Name+" is not available to import");
					log.takeSnapshot(driver);

					//Click on the Cancel button of the File Upload window
					//x.winActivate("File Upload");
					x.controlClick("File Upload", "", "2"); 
					log.writeSubSection_RecordStatus();
					continue;
				}  

					Thread.sleep(1000);
				driver.findElement(By.name("submitButton")).click();

				//Checks for the message
				if(comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[4]"))){
					if (driver.findElement(By.xpath("//*[@id='bd']/div[4]")).getText().contains("has been successfully added to the system"))
						log.writeSubSection("Import is done Successfully","",true);
					else{
						//Close the file dialog windows		
						if (x.winExists("File Upload")){
							x.winActivate("File Upload");
							Thread.sleep(1000);							
							//Click on the OK button
							x.controlClick("File Upload", "", "CommandButton_1"); 
							Thread.sleep(2000);
							x.controlClick("File Upload", "", "2"); 

						}

						log.writeSubSection("Template: "+ template_Name+" is not available to import","",false);
						log.takeSnapshot(driver);
					}

				} else if (driver.findElement(By.id("error1")).getText().contains("being imported already exists in the system")){
					log.writeSubSection("The given template is already imported into the system");
					log.takeSnapshot(driver);

				} else {
					log.writeSubSection("Import is NOT done successfully","",false);
					log.takeSnapshot(driver);
				}
			} catch(Exception e){
				e.printStackTrace();
				log.writeSubSection("There is an exception generated. Import is NOT done successfully","",false);
				log.takeSnapshot(driver);
			}
			log.writeSubSection_RecordStatus();
		}

	}

	private void Export_Templates() throws Exception {
		df = new DataFile(Data_File_Path, "Export");
		comUtils.setUpDriver(driver);
		ReadMetaData manage_Templates = new ReadMetaData();
		manage_Templates.setUpDataFile(df);
		int colCount = df.getColCount()-1;	
		for(int col=1; col <=colCount; col++){
			// Navigate to Manage_Templates
			navigateToManage_Templates();
			// Read the Template Name from excel and write it to log
			String template_Name = manage_Templates.getFieldValue("Template Name", col);
			log.write("Exporting the Template: " + template_Name);
			try{
				//To get the number of results
				if (comUtils.isElementDisplayed(By.xpath("//*[@id='result']/table/tbody/tr/td/span[1]/b"))){
					String numOfResults = driver.findElement(By.xpath("//*[@id='result']/table/tbody/tr/td/span[1]/b")).getText();
					System.out.println(numOfResults);
					//String resultCount = numOfResults.substring(numOfResults.lastIndexOf(" ") + 1);
					String result = StringUtils.substringBetween(numOfResults, "to ", " of");
					int res = Integer.valueOf(result); //Converting String to Integer
					boolean breakFlag = true;
					int i=0;
					while (comUtils.isElementDisplayed(By.linkText("Next")) && breakFlag){
						for (i=1;i<=res;i++){

							if (driver.findElement(By.xpath("//*[@id='parent']/tbody/tr["+i+"]/td[2]")).getText().equals(template_Name)){
								breakFlag = false;
								System.out.println("Element Found");
								break;
							} 
						}
						if (breakFlag)
							driver.findElement(By.linkText("Next")).click();
					} 

					if (!breakFlag){
						Thread.sleep(1000);
						if (comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr["+i+"]/td[1]/a/img")))
							driver.findElement(By.xpath("//*[@id='parent']/tbody/tr["+i+"]/td[1]/a/img")).click();
						else 
							log.writeSubSection("View Image is not present");
						if (comUtils.isElementDisplayed(By.xpath("//*[@id='doc3']/div[2]/table/tbody/tr/td[2]/input[2]"))){

							driver.findElement(By.xpath("//*[@id='doc3']/div[2]/table/tbody/tr/td[2]/input[2]")).click();
							Thread.sleep(2000);
							AutoItX x = new AutoItX();
							//x.winWaitActive("Opening Template Test Page2.xml");
							if (x.winExists("Opening Template Test Page2.xml")){
								x.winActivate("Opening Template Test Page2.xml");
								x.sleep(1000);
								x.send("!{s}", false );								
								x.send("{ENTER}",false);
								log.writeSubSection("Export Option is working as expected","",true);
							}else {
								log.writeSubSection("Export Options is not working as Expected","",false);
							}							
						}
						else 
							log.writeSubSection("Export Button is not present","",false);
					} else 
						log.writeSubSection("Template:"+ template_Name + " not found");

				} else {
					log.writeSubSection("There are no Templates found");
				}
			} catch(Exception e){
				e.printStackTrace();
				log.writeSubSection("There is an exception generated. Export is NOT done successfully","",false);
				log.takeSnapshot(driver);
			}
			log.writeSubSection_RecordStatus();
		}	

	}

	private void Print_Templates() throws Exception {
		String winHandleBefore = driver.getWindowHandle();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);
		// To verify the Print option
		log.write("Checking the Print option of Template Page");
		try {
			// Navigate to Manage Conditions link under Page Management
			navigateToManage_Templates();

			// Check if Print button if available
			if (comUtils.isElementDisplayed(By.xpath("html/body/form/div/div[1]/div[1]/table/tbody/tr/td[2]/input[2]"))) {
				// Click on the Print button
				driver.findElement(By.xpath("html/body/form/div/div[1]/div[1]/table/tbody/tr/td[2]/input[2]")).click();	
				Thread.sleep(1000);
				//pmUtils.Print_Download();
				AutoItX x = new AutoItX();
				if (x.winExists("Opening TemplateLibrary.pdf")){
					x.winActivate("Opening TemplateLibrary.pdf");
					x.send("!{o}", false); 
					Thread.sleep(2000);
					x.send("{ENTER}",false);
					Thread.sleep(3000);
					String  $title = x.winGetTitle("[ACTIVE]");
					x.winActivate($title);
					if ($title.contains("TemplateLibrary")){
						driver.switchTo().activeElement();
						log.takeSnapshot(Screenshot_File_Path, "CurrentScreenshot");
						//x.controlClick($title, "", "File");
						Thread.sleep(2000);
						x.send("!{f}{x}",false);

						log.writeSubSection("Template can be printed Successfully","",true);
					} else 
						log.writeSubSection("Template cannot be printed Successfully","",false);

				}else 
					log.writeSubSection("Template cannot be printed Successfully","",false);

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
	}

	private void Download_Templates() throws Exception {
		String winHandleBefore = driver.getWindowHandle();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);
		// To verify the Download option
		log.write("Checking the Download option");
		try {
			// Navigate to Manage Conditions link under Page Management
			navigateToManage_Templates();
			
			if (comUtils.isElementDisplayed(By.xpath("html/body/form/div/div[1]/div[1]/table/tbody/tr/td[2]/input[3]"))) {
				// Click on the Download button
				driver.findElement(By.xpath("html/body/form/div/div[1]/div[1]/table/tbody/tr/td[2]/input[3]")).click();
				Thread.sleep(3000);
				//pmUtils.Print_Download();

				AutoItX x = new AutoItX();
				//	x.winWaitActive("Opening TemplateLibrary.csv");
				if (x.winExists("Opening TemplateLibrary.csv")){
					x.winActivate("Opening TemplateLibrary.csv");
					x.send("!{s}", false); 
					x.send("!{o}", false); 					
					x.sleep(1000);
					x.send("{ENTER}",false);
					Thread.sleep(1500);
					String  $title = x.winGetTitle("[ACTIVE]");
					x.winActivate($title);
					if ($title.contains("TemplateLibrary")){

						//log.takeSnapshot(Screenshot_File_Path, "CurrentScreenshot");

						//x.controlClick($title, "", "File");
						Thread.sleep(2000);
						x.send("!{f}{x}",false);

						log.writeSubSection("Template can be downloaded Successfully","",true);
					} else 
						log.writeSubSection("Template cannot be downloaded Successfully","",false);

				}else 
					log.writeSubSection("Template cannot be downloaded Successfully","",false);

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

	private void Import_Export_Activity_log() throws Exception {
		String winHandleBefore = driver.getWindowHandle();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);

		//Click on the View Import/Export Activity Log
		driver.findElement(By.name("View Import/Export Activity Log")).click();

		// To verify the Print option
		log.write("Checking the Print option of Import/Export Activity Log");
		try {

			if (comUtils.isElementDisplayed(By.xpath("html/body/form/div/div[1]/div[1]/table/tbody/tr/td[2]/input[1]"))) {
				// Click on the Download button
				driver.findElement(By.xpath("html/body/form/div/div[1]/div[1]/table/tbody/tr/td[2]/input[1]")).click();
				Thread.sleep(1000);
				AutoItX x = new AutoItX();
			//	x.winWaitActive("Opening ImportExportLibrary.pdf");
				if (x.winExists("Opening ImportExportLibrary.pdf")){
					x.winActivate("Opening ImportExportLibrary.pdf");
					x.send("!{o}", false); 
					Thread.sleep(2000);
					x.send("{ENTER}",false);
					Thread.sleep(3000);
					String  $title = x.winGetTitle("[ACTIVE]");
					x.winActivate($title);
					if ($title.contains("ImportExportLibrary")){
						driver.switchTo().activeElement();
						log.takeSnapshot(Screenshot_File_Path, "CurrentScreenshot");
						//x.controlClick($title, "", "File");
						Thread.sleep(2000);
						x.send("!{f}{x}",false);

						log.writeSubSection("Print option is working as expected", "", true);
					} else 
						log.writeSubSection("Template cannot be printed Successfully","",false);

				}else 
					log.writeSubSection("Template cannot be printed Successfully","",false);


			} else {
				log.writeSubSection("Print Button is NOT Present", "", false);
				log.takeSnapshot(driver);
			}
		} catch (Exception e) {
			log.writeSubSection("There is an issue with Print option", "", false);
			log.takeSnapshot(driver);
		}
		log.writeSubSection_RecordStatus();
		// To verify the Download option
		log.write("Checking the Download option of Import/Export Activity Log");
		try {

			if (comUtils.isElementDisplayed(By.xpath("html/body/form/div/div[1]/div[1]/table/tbody/tr/td[2]/input[2]"))) {
				// Click on the Download button
				driver.findElement(By.xpath("html/body/form/div/div[1]/div[1]/table/tbody/tr/td[2]/input[2]")).click();
				Thread.sleep(1000);

				AutoItX x = new AutoItX();
				//	x.winWaitActive("Opening TemplateLibrary.csv");
				if (x.winExists("Opening ImportExportLibrary.csv")){
					x.winActivate("Opening ImportExportLibrary.csv");
					x.send("!{s}", false); 
					x.send("!{o}", false); 					
					x.sleep(1000);
					x.send("{ENTER}",false);
					Thread.sleep(1500);
					String  $title = x.winGetTitle("[ACTIVE]");
					x.winActivate($title);
					if ($title.contains("ImportExportLibrary")){

						//log.takeSnapshot(Screenshot_File_Path, "CurrentScreenshot");

						//x.controlClick($title, "", "File");
						Thread.sleep(2000);
						x.send("!{f}{x}",false);

						log.writeSubSection("Download option is working as expected", "", true);
					} else 
						log.writeSubSection("Template cannot be downloaded Successfully","",false);

				}else 
					log.writeSubSection("Template cannot be downloaded Successfully","",false);

				

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
	private void navigateToManage_Templates() throws Exception {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		try {
			// Click on the System Management link on the Home Page
			driver.findElement(By.linkText("System Management")).click();
			// Expand Page Management
			driver.findElement(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img")).click();
			// Click on Manage Templates link text
			driver.findElement(By.linkText("Manage Templates")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@After
	public void close() throws Exception {
		log.close();
		df.close();
		driver.close();
	}
}
