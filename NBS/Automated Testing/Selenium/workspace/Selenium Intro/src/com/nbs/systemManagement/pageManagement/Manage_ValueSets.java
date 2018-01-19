package com.nbs.systemManagement.pageManagement;

import org.junit.Test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class Manage_ValueSets {

	public WebDriver driver;
	public Log log;
	DataFile df, df1;
	public Robot robot = null;
	public Common_Utils comUtils = new Common_Utils();
	public PageManagement_Utils pmUtils = new PageManagement_Utils();
	public String Data_File_Path, Log_File_Path, Screenshot_File_Path;
	//public Boolean Concept_Code_Found;

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "systemManagement.pageManagement\\Manage_ValueSets.xls";
		Log_File_Path = login.Log_File_Path;
		Screenshot_File_Path = login.Screenshot_File_Path;
	}

	@Test
	public void ManageValueSets() throws Exception {
		log = new Log("PageMgmt_ManageValueSets", Log_File_Path);
		comUtils.setUpDriver(driver);
		pmUtils.setUp();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);

		Add_ValueSets();
		Edit_ValueSets();
		Import();
		Inactivate_ValueSets();
		Print_ValueSets();
		Download_ValueSets();

		pmUtils.getDriver();
		pmUtils.getLog();
	}

	private void Add_ValueSets() throws Exception {
		df = new DataFile(Data_File_Path, "Add");
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Navigate to Manage_Value_Sets
				navigateToManage_ValueSets();
				// Click on Add New
				driver.findElement(By.id("submitA")).click();
				ReadMetaData manage_ValueSets = new ReadMetaData();
				manage_ValueSets.setUpDataFile(df);
				// Read the value Value Set code from excel and write it to log
				String valueSet_Code = manage_ValueSets.getFieldValue("Value Set Code", col);
				log.write("Adding the record for Value Set Code: " + valueSet_Code);
				manage_ValueSets.setUpDriver(driver);
				manage_ValueSets.setUpLog(log);

				// Read the Meta Data and populate the fields
				manage_ValueSets.ReadData(col);

				// Click on Submit button
				driver.findElement(By.id("submitA")).click();
				String winHandleBefore = driver.getWindowHandle();
				if (driver.findElement(By.xpath("//*[@id='bd']/div[2]")).getText().contains(valueSet_Code + " has been successfully added to the system")) {
					log.writeSubSection("Record for Value Set Code: " + valueSet_Code + " is added successfully", "",true);
					// if Add New button is present and enabled, click it to add
					// concepts
					if (comUtils.isElementDisplayed(By.id("submitCr"))) {
						// Handling pop up window
						driver.findElement(By.id("submitCr")).sendKeys(Keys.NULL);
						Thread.sleep(2000);
						// driver.findElement(By.id("submitCr")).click();
						try {
							robot = new Robot();
						} catch (Exception e) {
							e.printStackTrace();
						}

						robot.keyPress(KeyEvent.VK_ENTER);
						robot.keyRelease(KeyEvent.VK_ENTER);

						String subWindowHandler = null;
						driver.getWindowHandles();
						Set<String> handles = driver.getWindowHandles();
						Iterator<String> iterator = handles.iterator();

						while (iterator.hasNext()) {
							subWindowHandler = iterator.next();
						}
						driver.switchTo().window(subWindowHandler);

						df1 = new DataFile(Data_File_Path, "Add_Concept");
						ReadMetaData manage_Concept = new ReadMetaData();
						manage_Concept.setUpDataFile(df1);
						manage_Concept.setUpDriver(driver);
						manage_Concept.setUpLog(log);
						String concept_code = manage_Concept.getFieldValue("Concept Code", col);
						// Read the Meta Data and populate the fields
						manage_Concept.ReadData(col);
						Thread.sleep(2000);

						// Check if an error is displayed
						if (comUtils.isElementDisplayed(By.xpath("//*[@id='errorBlock']"))) {

							if (driver.findElement(By.xpath("//*[@id='errorBlock']/b")).getText().contains("Please fix the following errors")){
								log.writeSubSection("Concept Code is NOT added successfully","",false);
								log.takeSnapshot(driver);
							}
							else 
								log.writeSubSection("A record already exists with this code: " + concept_code);
							// Click on Cancel button
							driver.findElement(By.xpath("html/body/div[2]/form/div/div[1]/input[2]")).click();
							// Accept the Alert
							if (comUtils.isAlertPresent());

						}

						driver.switchTo().window(winHandleBefore);
						// Check if successfully added message is displayed
						if (comUtils.isElementDisplayed(By.xpath("//*[@id='subsec2']/tbody/tr/td/div"))) {
							if (driver.findElement(By.xpath("//*[@id='subsec2']/tbody/tr/td/div")).getText()
									.contains("has been successfully added to the"))
								log.writeSubSection("Concept Code: "+concept_code + " has been added successfully", "", true);

						}

						driver.findElement(By.id("submitA")).click();
						Thread.sleep(1000);
						if (comUtils.isElementDisplayed(By.xpath("html/body/div[2]/form/div/div[2]"))) {

							if (driver.findElement(By.xpath("html/body/div[2]/form/div/div[2]")).getText()
									.contains(valueSet_Code + " has been successfully updated in the system"))
								log.writeSubSection("Record for  Name: " + valueSet_Code + " is added successfully",
										"", true);

						} else {
							log.writeSubSection("Record for  Name: " + valueSet_Code + " is NOT added successfully",
									"", false);
							log.takeSnapshot(driver);
						}
					}

				} else if (driver.findElement(By.xpath("//*[@id='bd']/div[3]")).getText()
						.contains("A record already exists with this Value Set Code")) {
					log.writeSubSection("Record already exists with this Value Code: " + valueSet_Code);
					log.takeSnapshot(driver);
				} else {
					log.writeSubSection("Record for Value Set Code: " + valueSet_Code + " is not added successfully",
							"", false);
					log.takeSnapshot(driver);
				}

			} catch (Exception e) {
				log.writeSubSection("There is an error adding the Value Set", "", false);
				log.takeSnapshot(driver);
			}
			log.writeSubSection_RecordStatus();
		}
	}

	private void Edit_ValueSets() throws Exception {
		//String parentWindow = driver.getWindowHandle();
		// To Edit the Value Sets
		df = new DataFile(Data_File_Path, "Edit");
		ReadMetaData Edit_ValueSets = new ReadMetaData();
		Edit_ValueSets.setUpDataFile(df);
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {

				// Navigate to Manage_Value_Sets
				navigateToManage_ValueSets();
				// Read the value Name from excel and write it to log
				String valueSet_Code = Edit_ValueSets.getFieldValue("Value Set Code", col);
				log.write("Editing the record for Value Set Code: " + valueSet_Code);

				// Click on filter
				driver.findElement(By.xpath("html/body/div[2]/form/div/div[2]/div/fieldset/table/tbody/tr/td/table/thead/tr/th[4]/img")).click();
				int colnum = 4; // Column number of Value Set Code
				// Get the row number of the exact Condition Name
				int rowNum = pmUtils.getRowNum(valueSet_Code, By.id("SearchText1"), By.id("b2SearchText1"), colnum);
				System.out.println("rownum Returned: " + rowNum);
				if (rowNum != 0) {
					String valueSetCode = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td["+colnum+"]")).getText();
					System.out.println("Value Set Code: " + valueSetCode);
					if (valueSet_Code.equalsIgnoreCase(valueSetCode)) {

						// If Edit icon is enabled, click on edit icon and validate
						if (comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr/td[2]/a/img"))) {
							// Click on the Edit icon to edit the record
							driver.findElement(By.xpath("//*[@id='parent']/tbody/tr/td[2]/a/img")).click();
							// Read the Meta Data and populate the fields
							Edit_ValueSets.setUpDriver(driver);
							Edit_ValueSets.setUpLog(log);
							Edit_ValueSets.ReadData(col);

							if (comUtils.isElementDisplayed(By.id("submitCr"))){
								log.writeSubSection("Value Set Code: "+valueSetCode+" is edited successfully","",true);

								df1 = new DataFile(Data_File_Path, "Edit_Concept");
								ReadMetaData manage_Concept = new ReadMetaData();
								manage_Concept.setUpDataFile(df1);
								manage_Concept.setUpDriver(driver);
								manage_Concept.setUpLog(log);
								String concept_code = manage_Concept.getFieldValue("Concept Code", col);
								String winHandleBefore = driver.getWindowHandle();
								//Check if Results are displayed to edit  Value Set Concepts
								if (comUtils.isElementDisplayed(By.xpath("//*[@id='subsec2']/tbody/tr/td/table/tbody/tr[1]/td/span[2]/b"))){

									String numOfResults = driver.findElement(By.xpath("//*[@id='subsec2']/tbody/tr/td/table/tbody/tr[1]/td/span[2]/b")).getText();
									System.out.println(numOfResults);
									// String resultCount = numOfResults.substring(numOfResults.lastIndexOf(" ") + 1);
									int resultCount = Integer.parseInt((numOfResults.substring(numOfResults.lastIndexOf(" ") + 1)));
									System.out.println(resultCount);
									Boolean Concept_Code_Found = false;
									for (int i = 1; i <= resultCount;i++){
										//Check if the Results in Value Set Concepts match the Value set Concept in excel (Edit_Concept)
										if (driver.findElement(By.xpath("//*[@id='parent']/tbody/tr["+i+"]/td[3]")).getText().equals(concept_code)) {
											//Click on Edit icon to edit the Concept
											driver.findElement(By.xpath("//*[@id='parent']/tbody/tr["+i+"]/td[2]/a")).sendKeys(Keys.NULL);

											Thread.sleep(1000);
											try {
												robot = new Robot();
											} catch (Exception e) {
												e.printStackTrace();
											}

											robot.keyPress(KeyEvent.VK_ENTER);
											robot.keyRelease(KeyEvent.VK_ENTER);

											String subWindowHandler = null;
											driver.getWindowHandles();
											Set<String> handles = driver.getWindowHandles();
											Iterator<String> iterator = handles.iterator();

											while (iterator.hasNext()) {
												subWindowHandler = iterator.next();
											}
											driver.switchTo().window(subWindowHandler);

											//Read the excel sheet and populate the data
											manage_Concept.ReadData(col);
											Thread.sleep(1000);
											//After populating the data and clicking on submit buttton, if there are errors focus will be still on the popup page or else
											//the popup page will be closed and focus will be on the Value Set code page. 
											driver.switchTo().window(winHandleBefore);
											int noOfWindows= driver.getWindowHandles().size();
											
											 //Switch to popup window and perform operations if noOfWindows > 1

											if (noOfWindows > 1){ 
												driver.switchTo().window(subWindowHandler);  // Switch to popup window
												//Check if there is are any errors
												if (comUtils.isElementDisplayed(By.id("errorBlock"))){
													
													log.writeSubSection("There is an error editing the Concept","",false);
													log.takeSnapshot(driver);
													driver.findElement(By.id("submitB")).click();
													if (comUtils.isAlertPresent());
													driver.switchTo().window(winHandleBefore);
												}
											} else{			
												driver.switchTo().window(winHandleBefore);
												// Check if successfully added message is displayed
												if (comUtils.isElementDisplayed(By.xpath("//*[@id='subsec2']/tbody/tr/td/div"))) {
													if (driver.findElement(By.xpath("//*[@id='subsec2']/tbody/tr/td/div")).getText()
															.contains("has been successfully updated in the system"))
														log.writeSubSection("Concept Code: "+concept_code + " has been edited successfully", "", true);

												} 
											}
											Concept_Code_Found = true; //setting concept code to True if found
											break;
										} 

									}

									if (!Concept_Code_Found){
										log.writeSubSection("Concept Code: "+concept_code+ " is not found","",false );
										log.takeSnapshot(driver);
									}
								} else {
									log.writeSubSection("There are no Concepts to edit");
								}
							} else if (comUtils.isElementDisplayed(By.id("srtDataFormEntryErrors"))){

								log.writeSubSection("Value Set Code: "+valueSetCode+"is NOT edited successfully","",false);
								log.takeSnapshot(driver);
							}

						} else {
							log.writeSubSection("Edit icon is not present", "", false);
							log.takeSnapshot(driver);
						}
					}
				} else {
					log.writeSubSection("Value Set Code:" + valueSet_Code + " is not found", "", false);
					log.takeSnapshot(driver);
				}				
			} catch (Exception e) {
				log.writeSubSection("There is an error editing the Value Set", "", false);
				e.printStackTrace();
				log.takeSnapshot(driver);
			}
			log.writeSubSection_RecordStatus();
			//driver.switchTo().window(parentWindow);
		}
	}

	private void Inactivate_ValueSets() throws Exception {
		// To Inactivate the Value Sets
		df = new DataFile(Data_File_Path, "Inactive");
		comUtils.setUpDriver(driver);
		ReadMetaData inactive_ValueSets = new ReadMetaData();
		inactive_ValueSets.setUpDataFile(df);
		int colCount = df.getColCount() - 1;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Navigate to Manage_Value_Sets
				navigateToManage_ValueSets();

				// Read the value Name from excel and write it to log
				String valueSet_Code = inactive_ValueSets.getFieldValue("Value Set Code", col);
				log.write("Inactivating the record for name: " + valueSet_Code);

				// Click on filter
				// driver.findElement(By.id("queueIcon")).click();
				driver.findElement(By
						.xpath("html/body/div[2]/form/div/div[2]/div/fieldset/table/tbody/tr/td/table/thead/tr/th[4]/img"))
				.click();

				int colnum = 4; // Column number of Value Set Code
				// Get the row number of the exact Condition Name
				int rowNum = pmUtils.getRowNum(valueSet_Code, By.id("SearchText1"), By.id("b2SearchText1"), colnum);
				System.out.println("rownum Returned: " + rowNum);
				if (rowNum != 0) {
					String valueSetCode = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td["+colnum+"]")).getText();
					System.out.println("Value Set Code: " + valueSetCode);
					if (valueSet_Code.equalsIgnoreCase(valueSetCode)) {

						// Check if the View icon is found
						if (comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td[1]/a/img"))) {
							// Click on the View icon to View the record
							driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td[1]/a/img")).click();

							// Click on Make Inactive button if present
							if (driver.findElement(By.xpath("html/body/div[2]/form/div/div[1]/input[2]")).getAttribute("value")
									.contains("Make Inactive")) {
								driver.findElement(By.xpath("html/body/div[2]/form/div/div[1]/input[2]")).click();
								Thread.sleep(1000);
								// Click OK on the Alert if it is present
								if (comUtils.isAlertPresent()) {
									if (driver.findElement(By.xpath("//*[@id='bd']/div[2]")).getText()
											.contains("has been made inactive"))
										log.writeSubSection("Value Set Code: " + valueSet_Code + " is INACTIVATED successfully",
												"", true);
									else {
										log.writeSubSection(
												"Value Set Code: " + valueSet_Code + " is NOT INACTIVATED successfully", "",
												false);
										log.takeSnapshot(driver);
									}
								} else {
									log.writeSubSection("Value Set Code: " + valueSet_Code + " is NOT INACTIVATED successfully",
											"", false);
									log.takeSnapshot(driver);
								}
							} else if (driver.findElement(By.xpath("html/body/div[2]/form/div/div[1]/input[2]"))
									.getAttribute("value").contains("Make Active")) {
								log.writeSubSection("INACTIVE button is not present - Value Set is already Inactive");
								log.takeSnapshot(driver);
							}

						} else {
							log.writeSubSection("Value Set Code: " + valueSet_Code + " is NOT Present", "", false);
							log.takeSnapshot(driver);
						}
					} else { 
						log.writeSubSection("Value Set Code: "+valueSet_Code+" doesnot exist");
						log.takeSnapshot(driver);

					}
				} else {
					log.writeSubSection("Value Set Code: "+valueSet_Code+" doesnot exist");
					log.takeSnapshot(driver);
				}
			} catch (Exception e) {
				log.writeSubSection("There is an error inactivating the Value Set", "", false);
				log.takeSnapshot(driver);
			}

			log.writeSubSection_RecordStatus();

		}

	}

	private void Import() throws Exception {
		String parentWindow = driver.getWindowHandle();
		df = new DataFile(Data_File_Path, "Import");
		comUtils.setUpDriver(driver);

		int colCount = df.getColCount()-3;	
		for(int col=1; col <=colCount; col++){	
			try{

				// Navigate to Manage_Value_Sets
				navigateToManage_ValueSets();

				//String parentWindow= driver.getWindowHandle(); 

				driver.findElement(By.xpath("html/body/div[2]/form/div/div[1]/input[2]")).sendKeys(Keys.NULL);

				Thread.sleep(1000);
				try {
					robot=new Robot();
				} catch (AWTException e) {
					e.printStackTrace();
				}
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.delay(1000);
				robot.keyRelease(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_CONTROL);

				Thread.sleep(1000);

				//Handling pop up window
				String subWindowHandler = null;
				driver.getWindowHandles();
				Set<String> handles = driver.getWindowHandles(); 
				Iterator<String> iterator = handles.iterator();

				while (iterator.hasNext()){				
					subWindowHandler = iterator.next();
				}

				driver.switchTo().window(subWindowHandler); 

				ReadMetaData imprt = new ReadMetaData();
				imprt.setUpDataFile(df);
				imprt.setUpDriver(driver);
				imprt.setUpLog(log);

				//Write to Logs
				log.write("Checking the Import functionality");
				imprt.ReadData(col);	

				driver.findElement(By.xpath("html/body/div[1]/table/tbody/tr[4]/td/input[1]")).click();
				driver.switchTo().window(parentWindow); 
				int noOfWindows= driver.getWindowHandles().size();

				//After populating the data and clicking on submit buttton, if there are errors focus will be still on the popup page or else
				//the popup page will be closed and focus will be on the Value Set code page. if noOfWindows size = 2 then popupwindow is still open

				if (noOfWindows > 1){   //Switch to popup window and perform operations
					driver.switchTo().window(subWindowHandler); 
					//Checks for error message
					if(comUtils.isElementDisplayed(By.xpath("//*[@id='errorBlock']"))){
						log.writeSubSection("Error encountered, Value sets were not imported successfully","",false);					
						log.takeSnapshot(driver);								
						driver.findElement(By.xpath("html/body/div[1]/table/tbody/tr[4]/td/input[2]")).click();
					}
				}else{

					//Check for the Alert and take the snapshot
					if(checkIfAlertPresent()){
						System.out.println("Alert Present");
						Alert alert = driver.switchTo().alert();									
						log.writeSubSection("No Value Sets were found.");
						Thread.sleep(500);						
						log.takeSnapshot(Screenshot_File_Path, "CurrentScreenshot");						
						alert.accept();		
					}

					//Click OK to import the value set
					if(comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[2]"))&&driver.findElement(By.xpath("//*[@id='bd']/div[2]")).getText().contains("Please review the value set details below and then select OK to continue")){
						System.out.println("Please review");
						driver.findElement(By.xpath("//*[@id='submitA']")).click();

						//Write the results to the log.
						if(comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[2]"))&&driver.findElement(By.xpath("//*[@id='bd']/div[2]")).getText().contains("Please fix the following errors:")){
							log.writeSubSection("Value Set is NOT imported successfully.","",false);
							log.takeSnapshot(driver);
						}
						else if(comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[2]"))&&driver.findElement(By.xpath("//*[@id='bd']/div[2]")).getText().contains("successfully added to the system")){
							log.writeSubSection("Value Set has been imported successfully.","",true);
							log.takeSnapshot(driver);
						}
					}else{

					}				
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			log.writeSubSection_RecordStatus();
			driver.switchTo().window(parentWindow);
		}
		
	}
	private void Print_ValueSets() throws Exception {
		String winHandleBefore = driver.getWindowHandle();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);
		// To verify the Print option
		log.write("Checking the Print option of Value Set Page");

		try {

			// Navigate to Manage_Value_Sets
			navigateToManage_ValueSets();
			// Check if Print button if available
			if (comUtils.isElementDisplayed(By.xpath("html/body/div[2]/form/div/div[1]/input[3]"))) {

				// Click on the Print button
				driver.findElement(By.xpath("html/body/div[2]/form/div/div[1]/input[3]")).click();

				pmUtils.Print_Download();

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
	}

	private void Download_ValueSets() throws Exception {
		String winHandleBefore = driver.getWindowHandle();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);
		// To verify the Download option
		log.write("Checking the download option");
		try {

			// Navigate to Manage_Value_Sets
			navigateToManage_ValueSets();
			// Check if Download button if available
			if (comUtils.isElementDisplayed(By.xpath("html/body/div[2]/form/div/div[1]/input[4]"))) {
				// Click on the Download button
				driver.findElement(By.xpath("html/body/div[2]/form/div/div[1]/input[4]")).click();

				pmUtils.Print_Download();

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

	private void navigateToManage_ValueSets() throws Exception {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		try{
			// Click on the System Management link on the Home Page
			driver.findElement(By.linkText("System Management")).click();
			// Expand Page Management
			driver.findElement(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img")).click();
			// Click on Manage Value Sets link text
			driver.findElement(By.linkText("Manage Value Sets")).click();
		} catch (Exception e){
			e.printStackTrace();
		}
	}


	public boolean checkIfAlertPresent() 
	{ 
		try 
		{ 
			driver.switchTo().alert(); 
			return true; 
		}    
		catch (NoAlertPresentException Ex) 
		{ 
			return false; 
		}   
	}   


	@After
	public void close() throws Exception {
		log.close();
		df.close();
		df1.close();
		driver.close();
	}
}
