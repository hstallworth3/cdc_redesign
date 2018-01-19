package com.nbs.systemManagement.pageManagement;

import org.junit.Test;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Common_Utils;

public class Manage_Pages {

	public WebDriver driver;	
	public Log log;
	DataFile df;
	public String Data_File_Path,Log_File_Path,Screenshot_File_Path;
	public Robot robot=null;
	public Common_Utils comUtils = new Common_Utils();
	public PageManagement_Utils pmUtils = new PageManagement_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path+"systemManagement.pageManagement\\Manage_Pages.xls";
		Log_File_Path = login.Log_File_Path;
		Screenshot_File_Path = login.Screenshot_File_Path;
	}

	@Test
	public void PageMangament_ManagePages() throws Exception {
		log = new Log("PageManagement_ManagePages", Log_File_Path);
		comUtils.setUpDriver(driver);		

		pmUtils.setUp();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);

		Add_Page();					
		Print();
		Download();
		Edit_Page();//Not yet worked on
		BuildPage();
		SaveAsTemplate();
		Publish();		

		pmUtils.getDriver();
		pmUtils.getLog();
	}

	private void Add_Page() throws IOException{
		df = new DataFile(Data_File_Path, "ManagePages_Add");
		int colCount = df.getColCount()-3;

		for(int col=1; col <=colCount; col++){	
			try {
				
				//Navigate to ManagePages
				NavigateToManagePages();

				ReadMetaData ManagePages = new ReadMetaData();
				ManagePages.setUpDataFile(df);
				ManagePages.setUpDriver(driver);
				ManagePages.setUpLog(log);

				// To Read QuestionID from excel
				String Page_Name = ManagePages.getFieldValue("Page Name", col);				

				//Write to logs
				log.write("Adding Question for a Unique ID - "+Page_Name);

				//Click on Add New Button
				driver.findElement(By.xpath("//*[@id='bd']/div[2]/input[2]")).click();

				//Read the Meta Data and populate the fields
				ManagePages.ReadData(col);

				//Click on Submit Button
				//driver.findElement(By.xpath("//*[@id='bd']/div[5]/input[1]")).click();

				if (comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[4]"))){					
					if(driver.findElement(By.xpath("//*[@id='bd']/div[4]")).getText().contains("page has been successfully added"))
						log.writeSubSection("Page was Added successfully","",true);
				}else if(comUtils.isElementDisplayed(By.xpath("//*[@id='errorMessages']"))){
					log.writeSubSection("Page was NOT Added successfully","",false);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			log.writeSubSection_RecordStatus();
		}
	}
	
	private void BuildPage() throws IOException{

		df = new DataFile(Data_File_Path, "ManagePages_Add");
		int colCount = df.getColCount()-3;
		boolean alert;

		for(int col=1; col <=colCount; col++){	
			try {

				//Navigate to ManagePages
				NavigateToManagePages();

				//Search for Page Name
				alert = FilterPageName(df,col);

				Thread.sleep(8000);

				//Log the results
				log.write("Testing Build Page functionality: ");

				//Check if alert is present. 
				if(alert){
					comUtils.isAlertPresent();
					break;
				}

				//Get parent window handle
				String parentWindow = driver.getWindowHandle();

				//Click on Add Tab Icon			 														
				driver.findElement(By.xpath("//*[@id='canvas']/form[2]/div[1]/a")).sendKeys(Keys.NULL);

				Thread.sleep(1000);
				Thread.sleep(1000);
				try {
					robot = new Robot();
				} catch (Exception e) {
					e.printStackTrace();
				}

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				Thread.sleep(1000);

				//Populate the data in Pop up Window for Tab
				df = new DataFile(Data_File_Path, "ManagePages_BuildPage_Tab");
				PopulateDataInPopUp(col,df);

				//Click on Submit Button				
				driver.findElement(By.name("SubmitForm")).click();

				if (comUtils.isElementDisplayed(By.xpath("//*[@id='successMessages']"))){//For Tab					
					if(driver.findElement(By.xpath("//*[@id='successMessages']")).getText().contains("has been successfully updated in the system.")){
						log.writeSubSection("Tab was Added successfully","",true);

						//Click on Close button
						driver.findElement(By.xpath("//input[@type='button' and @value='Close']")).click();
						driver.switchTo().window(parentWindow);						
						System.out.println("Add new Tab: ");
						Thread.sleep(4000);

						//Click on Add New Section
						driver.findElement(By.xpath("//*[@id='-2']/div[1]/a/span")).click();

						String windowHandle = driver.getWindowHandle();
						Thread.sleep(3000);
						try {
							robot = new Robot();
						} catch (Exception e) {
							e.printStackTrace();
						}
						robot.keyPress(KeyEvent.VK_TAB);
						robot.delay(1000);

						robot.keyPress(KeyEvent.VK_ENTER);
						robot.keyRelease(KeyEvent.VK_ENTER);

						//Populate the Pop up Window for Add Section
						df = new DataFile(Data_File_Path, "ManagePages_BuildPage_Sec");
						PopulateDataInPopUp(col,df);

						//Click on Submit Button				
						driver.findElement(By.name("SubmitForm")).click();

						if (comUtils.isElementDisplayed(By.xpath("//*[@id='successMessages']"))){ //For Section					
							if(driver.findElement(By.xpath("//*[@id='successMessages']")).getText().contains("has been successfully updated in the system.")){
								log.writeSubSection("Section was Added successfully","",true);

								//Click on Close button for Add Section
								driver.findElement(By.xpath("//input[@type='button' and @value='Close']")).click();
								driver.switchTo().window(windowHandle);//was parentWindow					


								//Click on Add New SubSection	
								//driver.findElement(By.xpath("html/body/div[2]/div/div[5]/form[2]/div[2]/div/div/div["+numOfTabs+ "]/div[2]/a[1]/img")).click();
								driver.findElement(By.xpath("//*[@id='-2']/div[1]/a/span")).click();
								//driver.findElement(By.xpath("//div[@class='controls' and @title='Add New Subsection to this Section' and @src ='add.gif']")).click();
								
								String windowHandle2 = driver.getWindowHandle();
								Thread.sleep(3000);
								try {
									robot = new Robot();
								} catch (Exception e) {
									e.printStackTrace();
								}
								robot.keyPress(KeyEvent.VK_TAB);								
								robot.delay(500);
								
								robot.keyPress(KeyEvent.VK_TAB);								
								robot.delay(500);								
								
								robot.keyPress(KeyEvent.VK_TAB);							
								robot.delay(500);
								
								robot.keyPress(KeyEvent.VK_TAB);								
								robot.delay(500);
								
								robot.keyPress(KeyEvent.VK_TAB);
								robot.delay(500);
								
								robot.keyPress(KeyEvent.VK_TAB);
								robot.delay(500);
								
								robot.keyPress(KeyEvent.VK_TAB);
								robot.delay(500);
								
								robot.keyPress(KeyEvent.VK_TAB);
								robot.delay(500);

								robot.keyPress(KeyEvent.VK_ENTER);
								robot.keyRelease(KeyEvent.VK_ENTER);
								
								//Populate subsection popup screen
								df = new DataFile(Data_File_Path, "ManagePages_BuildPage_SubSec");
								PopulateDataInPopUp(col,df);
								
								//Click on Submit
								driver.findElement(By.name("SubmitForm")).click();
								
								if (comUtils.isElementDisplayed(By.xpath("//*[@id='successMessages']"))){ //For SubSection					
									if(driver.findElement(By.xpath("//*[@id='successMessages']")).getText().contains("has been successfully updated in the system.")){
										System.out.println("SubSection was Added successfully");
										log.writeSubSection("SubSection was Added successfully","",true);
										
										//Click on Close button for Add SubSection
										driver.findElement(By.xpath("//input[@type='button' and @value='Close']")).click();
										driver.switchTo().window(windowHandle2);//was parentWindow
										
										//Read number of Tabs
										int numOfTabs = driver.findElements(By.xpath("html/body/div[2]/div/div[5]/form[2]/div[2]/div/div/ul/li")).size();
										System.out.println("Num of Tabs:" +numOfTabs );	
										
										//Get the number of questions before adding a questions
										int numOfQuestionsBefore = driver.findElements(By.xpath("html/body/div[2]/div/div[5]/form[2]/div[2]/div/div/div["+numOfTabs+"]/ul/li/div/div[2]/ul/li[1]/div/div[2]/ul/li")).size();
										System.out.println("Num of Questions Before:" +numOfQuestionsBefore );										
																			
										//Click on Add New Question	
										//driver.findElement(By.xpath("html/body/div[2]/div/div[5]/form[2]/div[2]/div/div/div["+numOfTabs+ "]/div[2]/a[1]/img")).click();
										Thread.sleep(1000);
										//driver.findElement(By.xpath("//*[@id='-3']/div[1]/div[1]/a[1]/img")).click();	//click onexpand icon for section							

										String windowHandle3 = driver.getWindowHandle();
										Thread.sleep(3000);
										try {
											robot = new Robot();
										} catch (Exception e) {
											e.printStackTrace();
										}
										robot.keyPress(KeyEvent.VK_TAB);								
										robot.delay(1000);
										
										robot.keyPress(KeyEvent.VK_TAB);								
										robot.delay(1000);										
										
										robot.keyPress(KeyEvent.VK_TAB);							
										robot.delay(1000);
										
										robot.keyPress(KeyEvent.VK_TAB);								
										robot.delay(1000);
										
										robot.keyPress(KeyEvent.VK_TAB);
										robot.delay(1000);
										
										robot.keyPress(KeyEvent.VK_TAB);
										robot.delay(1000);

										robot.keyPress(KeyEvent.VK_ENTER);
										robot.keyRelease(KeyEvent.VK_ENTER);
										
										//Populate Question PopUp screen
										df = new DataFile(Data_File_Path, "ManagePages_BuildPage_Question");
										PopulateDataInPopUp(col,df);
																				
										//Click on submit
										driver.findElement(By.name("SubmitForm")).click();
										Thread.sleep(1000);
										
										if (!comUtils.isElementDisplayed(By.id("errorMessages"))){
											//Click on the first search option
											//driver.findElement(By.xpath("//*[@id='searchResultsDT']/tbody/tr[1]/td[2]/input")).click();								
											driver.findElement(By.xpath("html/body/div[5]/div/div/table/tbody/tr/td/table/tbody/tr[1]/td[1]/input")).click();

											//Click on Add selected Question/submit button
											driver.findElement(By.xpath("html/body/div[6]/input[1]")).click();
											
											Thread.sleep(2000);
											
											//Go back to parent window
											driver.switchTo().window(windowHandle3);
											
											Thread.sleep(2000);
											int numOfQuestionsAfter = driver.findElements(By.xpath("html/body/div[2]/div/div[5]/form[2]/div[2]/div/div/div["+numOfTabs+"]/ul/li/div/div[2]/ul/li[1]/div/div[2]/ul/li")).size();											
											System.out.println("Num of Questions after:" +numOfQuestionsAfter );
											
											//if (comUtils.isElementDisplayed(By.xpath("//*[@id='pageBuilderInfoMessagesBlock']"))){
											if(numOfQuestionsAfter> numOfQuestionsBefore){
											
											if (comUtils.isElementDisplayed(By.xpath("html/body/div[2]/div/div[1]/input[2]"))){
												log.writeSubSection("Question was Added Successfully","",true);
												System.out.println("before clicking submit");
												Thread.sleep(1000);
												
												//Uncomment it to submit
												//driver.findElement(By.xpath("//*[@id='bd']/div[1]/input[2]")).click();//click to submit the page
												
												if (comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[4]"))){
													log.writeSubSection("Page was updated successfully","",true);
												}
											}else{
												log.writeSubSection("Question was NOT Added Successfully","",false);
											}
										}else {
											log.writeSubSection("Question was NOT Added successfully","",false);
											//driver.findElement(By.xpath("html/body/div[2]/input[2]")).click();
											break;
										}
										}
									}
								}else if(comUtils.isElementDisplayed(By.id("errorMessages"))){//For SubSection
									System.out.println("In error msg");
									Thread.sleep(2000);
									log.writeSubSection("SubSection was NOT Added successfully","",false);
									driver.findElement(By.xpath("html/body/form/div[6]/input[2]")).click();									
									break;									
								}
							}else if(comUtils.isElementDisplayed(By.id("errorMessages"))){//For Section
								System.out.println("In error msg ss");
								log.writeSubSection("Section was NOT Added successfully","",false);
								Thread.sleep(2000);
								driver.findElement(By.xpath("html/body/form/div[6]/input[2]")).click();								
								break;
							}
						}
					}
				}else if(comUtils.isElementDisplayed(By.id("errorMessages"))){//For Tab
					log.writeSubSection("Tab was NOT Added successfully","",false);
					driver.findElement(By.xpath("html/body/form/div[6]/input[2]")).click();
					break;
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			log.writeSubSection_RecordStatus();
		}
	}
	
	private void SaveAsTemplate() throws IOException{

		df = new DataFile(Data_File_Path, "ManagePages_Add");
		int colCount = df.getColCount()-3;

		for(int col=1; col <=colCount; col++){	
			try {

				//Write to logs
				log.write("Testing 'Save As Template' functionality: ");

				//Navigate to ManagePages
				NavigateToManagePages();

				//Search for Page Name
				FilterPageName(df,col);

				Thread.sleep(4000);

				//Get parent window handle
				String parentWindow = driver.getWindowHandle();

				//Click on submit button to update/save the page
				driver.findElement(By.name("Submit")).click();

				//Click on Save as Template button
				driver.findElement(By.name("Save As Template")).sendKeys(Keys.NULL);
				
				try {
					robot = new Robot();
				} catch (Exception e) {
					e.printStackTrace();
				}

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				
				//Populates the data in the Popup Window
				df = new DataFile(Data_File_Path, "ManagePages_BuildPage_Template");
				PopulateDataInPopUp(col,df);

				//Click on submit
				driver.findElement(By.name("Submit")).click();

				if(comUtils.isElementDisplayed(By.id("errorMessages"))){
					log.writeSubSection("Page Template was NOT saved successfully","",false);
					System.out.println("Page Template was NOT saved successfully");
					driver.findElement(By.name("Cancel")).click();
					break;
				}else{
					driver.switchTo().window(parentWindow);
					if (comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[4]"))){
						if(driver.findElement(By.xpath("//*[@id='bd']/div[4]")).getText().contains("page template has been successfully saved.")){
							System.out.println("Page Template was saved successfully");
							log.writeSubSection("Page Template was saved successfully","",true);
						}
					}	
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void Publish() throws IOException{
		boolean alert;
		df = new DataFile(Data_File_Path, "ManagePages_Add");
		int colCount = df.getColCount()-3;

		for(int col=1; col <=colCount; col++){	
			try {

				//Write to logs
				log.write("Testing publish functionality: "+col);

				//Navigate to ManagePages
				NavigateToManagePages();

				//Search for Page Name
				alert = FilterPageName(df,col);

				Thread.sleep(4000);

				//Get parent window handle
				String parentWindow = driver.getWindowHandle();

				//Check if alert is present. 
				if(!alert){
					//Click on submit button to update/save the page					
					driver.findElement(By.name("Submit")).click();
				}else{
					comUtils.isAlertPresent();
				}

				//Click on Publish button				
				driver.findElement(By.name("Publish")).sendKeys(Keys.NULL);
				try {
					robot = new Robot();
				} catch (Exception e) {
					e.printStackTrace();
				}

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
											
				//if(comUtils.isElementDisplayed(By.xpath("//html/body/div[1]/div/form/div[2]/div/div[3]/div"))){
				if(comUtils.isElementDisplayed(By.id("errorMessages"))){
				log.writeSubSection("Page was NOT Published successfully","",false);
					System.out.println("Page was NOT Published successfullyy");
					break;
				}else{
					
					//Populate the data in Popup Window
					df = new DataFile(Data_File_Path, "ManagePages_BuildPage_Publish");
					PopulateDataInPopUp(col,df);
													
					driver.findElement(By.name("Submit")).click();
					driver.switchTo().window(parentWindow);
					Thread.sleep(3000);
					
					if (comUtils.isElementDisplayed(By.xpath("//html/body/div[1]/div/form/div[2]/div/div[4]"))){							
						if(driver.findElement(By.xpath("//*[@id='bd']/div[4]")).getText().contains("page has been successfully published.")){								
							System.out.println("Page was Published successfully");
							log.writeSubSection("Page was Published successfully","",true);
						}
					}
				}				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void Edit_Page() throws IOException{

		//String id = "";
		df = new DataFile(Data_File_Path, "ManageQuestions_Edit");		
		int colCount = df.getColCount()-3;
		System.out.println("colCount:"+colCount);

		for(int col=1; col <=colCount; col++){	
			try {

				//Navigate to ManageQuestions
				NavigateToManagePages();

				ReadMetaData questions = new ReadMetaData();
				questions.setUpDataFile(df);				
				questions.setUpDriver(driver);

				// To Read QuestionID from excel
				String Unique_ID = questions.getFieldValue("Unique ID", col);
				System.out.println("UniqueID: "+ Unique_ID);

				//Write to logs
				log.write("Editing Question for UniqueID: "+Unique_ID );							

				//Click on search filter for Unique ID			
				driver.findElement(By.xpath("//*[@id='parent']/thead/tr/th[4]/img")).click();
				
				int colnum = 4; //Column number of Condition
				//Get the row number of the exact Unique_id
				int rowNum = pmUtils.getRowNum(Unique_ID,By.id("SearchText1"), By.id("b1SearchText1"),colnum);

				System.out.println("rownum Returned: " +rowNum);
				
				if(rowNum != 0){

					//If Edit icon is enabled, click on edit icon and validate
					if(comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr["+rowNum+"]/td[2]/a/img"))){

						//Click on Edit Icon
						driver.findElement(By.xpath("//*[@id='parent']/tbody/tr["+rowNum+"]/td[2]/a/img")).click();

						//Read the Meta Data and populate the fields
						questions.ReadData(col);

						//Click on Submit Button
						driver.findElement(By.name("Submit")).click();

						if (comUtils.isElementDisplayed(By.id("successMessages"))){
							log.writeSubSection("Question was Edited successfully","",true);
							log.takeSnapshot(driver);									
						}
						else if (comUtils.isElementDisplayed(By.id("errorMessages"))){
							log.writeSubSection("Question was NOT Edited successfully","",false);
							log.takeSnapshot(driver);									
						}
						//break;
					}else{
						log.writeSubSection("The Question is inactive.");								
					}
				}else{
					log.writeSubSection("The Question was not found.");
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			log.writeSubSection_RecordStatus();
		}
	}
	
	private void Print() throws IOException{
		log.write("Checking the Print option of Manage Page");
		try {			
			//Navigate to ManagePages
			NavigateToManagePages();			
			System.out.println("print1");
			
			//Check if Print button is available
			if (comUtils.isElementDisplayed(By.xpath("html/body/div[2]/div/div[2]/input[3]"))){
				System.out.println("print2");
				Thread.sleep(1000);
				
				//Click on the Print button
				driver.findElement(By.xpath("html/body/div[2]/div/div[2]/input[3]")).click();
			    
				//Call Print_Download()
				Thread.sleep(1000);
				pmUtils.setUpDriver(driver);
				pmUtils.Print_Download();
				pmUtils.getDriver();

			}else {
				log.writeSubSection("Print Button is NOT Present","",false);
				log.takeSnapshot(driver);
			}			
		}
		catch (Exception e)	{
			log.writeSubSection("There is an issue with Print option","",false);
			log.takeSnapshot(driver);
		}		
		log.writeSubSection_RecordStatus();			
	}

	private void Download() throws IOException{
		log.write("Checking the Download option of Manage Page");
		try {	
			
			//Navigate to ManagePages
			NavigateToManagePages();			

			//Check if Print button if available
			if (comUtils.isElementDisplayed(By.xpath("html/body/div[2]/div/div[2]/input[4]"))){

				//Click on the Print button
				driver.findElement(By.xpath("html/body/div[2]/div/div[2]/input[4]")).click();

				//Call Print_Download()
				Thread.sleep(1000);				
				pmUtils.setUpDriver(driver);
				pmUtils.Print_Download();
				pmUtils.getDriver();

			}else {
				log.writeSubSection("Print Button is NOT Present","",false);
				log.takeSnapshot(driver);
			}			
		}
		catch (Exception e)	{
			log.writeSubSection("There is an issue with Print option","",false);
			log.takeSnapshot(driver);
		}		
		log.writeSubSection_RecordStatus();			
	}

	public void NavigateToManagePages() throws Exception{

		Common_Utils comUtils = new Common_Utils();
		comUtils.setUpDriver(driver);

		//Click on the Reports link on the Home Page
		driver.findElement(By.linkText("System Management")).click();

		//Expand Page Management (in some environment Public4 is available and in some environments PUblic2 is available)
		int env_index =0;

		if (comUtils.isElementDisplayed(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img"))){
			env_index = 5;					
		}
		else {
			env_index = 2;										
		}		
		driver.findElement(By.xpath("//*[@id='systemAdmin"+env_index+"']/thead/tr/th/a/img")).click();

		//Click Manage Questions link
		driver.findElement(By.linkText("Manage Pages")).click();
	}
	
	public boolean FilterPageName(DataFile df,int col) throws Exception{
		boolean alertPrestnt = false;
		ReadMetaData MPAdd = new ReadMetaData();
		MPAdd.setUpDataFile(df);
		MPAdd.setUpDriver(driver);
		MPAdd.setUpLog(log);

		// To Read QuestionID from excel
		String Page_Name = MPAdd.getFieldValue("Page Name", col);

		//Click on search filter for Unique ID			
		driver.findElement(By.xpath("//*[@id='parent']/thead/tr/th[4]/img")).click();
		
		//Column number of Condition
		int colnum = 4; 
		
		//Get the row number of the exact Unique_id
		int rowNum = pmUtils.getRowNum(Page_Name,By.id("SearchText1"), By.id("b1SearchText1"),colnum);

		System.out.println("rownum Returned: " +rowNum);
		
		if(rowNum != 0){

			//If Edit icon is enabled, click on edit icon and validate
			if(comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr["+rowNum+"]/td[2]/a/img"))){

				//Click on Edit Icon
				driver.findElement(By.xpath("//*[@id='parent']/tbody/tr["+rowNum+"]/td[2]/a/img")).click();
				
				if(comUtils.isAlertPresent()){
					alertPrestnt = true;
				}
			}
		}
		return alertPrestnt;
	}
	
	public void PopulateDataInPopUp(int col,DataFile df) throws Exception{
		
		//Handling pop up window
		String subWindowHandler3 = null;
		driver.getWindowHandles();
		Set<String> handles3 = driver.getWindowHandles(); 
		Iterator<String> iterator3 = handles3.iterator();

		while (iterator3.hasNext()){				
			subWindowHandler3 = iterator3.next();
		}

		driver.switchTo().window(subWindowHandler3);
		//df = new DataFile(Data_File_Path, "ManagePages_BuildPage_Template");

		ReadMetaData BuildPage_SubSect = new ReadMetaData();
		BuildPage_SubSect.setUpDataFile(df);
		BuildPage_SubSect.setUpDriver(driver);
		BuildPage_SubSect.setUpLog(log);

		//Read the Meta Data and populate the fields
		BuildPage_SubSect.ReadData(col);
	}

	@After
	public void close() throws Exception {			
		log.close();
		//df.close();				 
	}
}