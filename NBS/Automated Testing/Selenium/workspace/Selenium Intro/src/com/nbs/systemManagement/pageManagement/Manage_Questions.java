package com.nbs.systemManagement.pageManagement;

import org.junit.Test;
import java.awt.Robot;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import org.openqa.selenium.Alert;
import com.nbs.common.Common_Utils;

public class Manage_Questions {

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
		Data_File_Path = login.Data_File_Path+"systemManagement.pageManagement\\Manage_Questions.xls";
		Log_File_Path = login.Log_File_Path;
		Screenshot_File_Path = login.Screenshot_File_Path;
	}

	@Test
	public void PageMangament_ManageQuestions() throws Exception {
		log = new Log("PageManagement_ManageQuestions", Log_File_Path);
		comUtils.setUpDriver(driver);		

		pmUtils.setUp();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);

		Add_Question();
		Edit_Question();
		Search_Question();
		MakeInactive_Question();		
		Print();
		Download();

		pmUtils.getDriver();
		pmUtils.getLog();
	}

	private void Add_Question() throws IOException{
		df = new DataFile(Data_File_Path, "ManageQuestions_Add");
		int colCount = df.getColCount()-3;

		for(int col=1; col <=colCount; col++){	
			try {
				
				//Navigate to ManageQuestions
				navigateToManageQuestions();

				ReadMetaData questions = new ReadMetaData();
				questions.setUpDataFile(df);
				questions.setUpDriver(driver);
				questions.setUpLog(log);

				// To Read QuestionID from excel
				String Unique_ID = questions.getFieldValue("Unique ID", col);				

				//Write to logs
				log.write("Adding Question for a Unique ID - "+Unique_ID);


				//Click on Add New Button
				driver.findElement(By.xpath("//*[@id='bd']/div[2]/input[1]")).click();

				//Read the Meta Data and populate the fields
				questions.ReadData(col);

				//Click on Submit Button
				driver.findElement(By.name("Submit")).click();

				if (comUtils.isElementDisplayed(By.id("successMessages")))
					log.writeSubSection("Question was Added successfully","",true);					
				else if (comUtils.isElementDisplayed(By.id("errorMessages"))){
					log.writeSubSection("Question was NOT Added successfully","",false);
					log.takeSnapshot(driver);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			log.writeSubSection_RecordStatus();
		}
	}

	private void Edit_Question() throws IOException{

		//String id = "";
		df = new DataFile(Data_File_Path, "ManageQuestions_Edit");		
		int colCount = df.getColCount()-3;
		System.out.println("colCount:"+colCount);

		for(int col=1; col <=colCount; col++){	
			try {

				//Navigate to ManageQuestions
				navigateToManageQuestions();

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

	private void Search_Question() throws IOException{

		String id = "";
		df = new DataFile(Data_File_Path, "ManageQuestions_Search");		
		int colCount = df.getColCount()-3;
		System.out.println("colCount:"+colCount);
		for(int col=1; col <=colCount; col++){	
			try {

				//Navigate to ManageQuestions
				navigateToManageQuestions();

				ReadMetaData questions = new ReadMetaData();
				questions.setUpDataFile(df);
				questions.setUpDriver(driver);

				// To Read QuestionID from excel
				String Unique_ID = questions.getFieldValue("Unique ID", col);
				System.out.println("UniqueID: "+ Unique_ID);

				//Write to logs
				log.write("Search Question for UniqueID: "+Unique_ID );

				//Click on Search button			
				driver.findElement(By.id("submitCr")).click();				

				//Read the Meta Data and populate the fields
				questions.ReadData(col);

				//Click on Search Button
				driver.findElement(By.name("Submit")).click();

				//Check if search returned any values
				if(comUtils.isElementPresent(By.xpath("html/body/form/div[4]"))){
					log.writeSubSection("Search Did not return any results:");					
				}else{

					//To read Number of Questions returned from search.....
					int NumOfRows = driver.findElements(By.xpath("//*[@id='parent']/tbody/tr")).size();
					System.out.println("Number of Rows: " + NumOfRows);

					//Loop for the number of results returned				
					for(int i =1;i<=(NumOfRows);i++){	

						//Read Unique id
						if(comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr["+i+"]/td[4]")))
							id = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr["+i+"]/td[4]")).getText();

						if(id.equalsIgnoreCase(Unique_ID)){
							log.writeSubSection("Question for unique id is found ","",true);							
							break;
						}
					}				
				}				
			} catch(Exception e){
				e.printStackTrace();
			}
			log.writeSubSection_RecordStatus();
		}			
	}

	private void MakeInactive_Question() throws IOException{
		
		df = new DataFile(Data_File_Path, "ManageQuestions_MakeInactive");		
		int colCount = df.getColCount()-3;
		System.out.println("colCount:"+colCount);

		for(int col=1; col <=colCount; col++){	
			try {
				//Navigate to ManageQuestions
				navigateToManageQuestions();				

				ReadMetaData questions = new ReadMetaData();
				questions.setUpDataFile(df);
				questions.setUpDriver(driver);

				// To Read QuestionID from excel
				String Unique_ID = questions.getFieldValue("Unique ID", col);
				System.out.println("UniqueID: "+ Unique_ID);

				//Write to logs
				log.write("Inactivate Question for UniqueID: "+Unique_ID );

				//Click on search filter for Unique ID			
				driver.findElement(By.xpath("//*[@id='parent']/thead/tr/th[4]/img")).click();	

				int colnum = 4; //Column number of Condition
				//Get the row number of the exact Unique ID
				int rowNum = pmUtils.getRowNum(Unique_ID,By.id("SearchText1"), By.id("b1SearchText1"),colnum);
				System.out.println("rownum Returned: " +rowNum);
				
				if(rowNum != 0){

					//If View icon is displayed, click on View icon and validate
					if(comUtils.isElementDisplayed(By.xpath("//*[@id='parent']/tbody/tr["+rowNum+"]/td[1]/a/img"))){

						//Click on View Icon
						driver.findElement(By.xpath("//*[@id='parent']/tbody/tr["+rowNum+"]/td[1]/a/img")).click();

						if(comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[2]/input[2]"))){

							if(driver.findElement(By.xpath("//*[@id='bd']/div[2]/input[2]")).getAttribute("value").contains("Make Inactive")){

								driver.findElement(By.xpath("//*[@id='bd']/div[2]/input[2]")).click();									
								Alert alert = driver.switchTo().alert();
								alert.accept();										
							}
							//break;
						}else if(driver.findElement(By.xpath("//*[@id='bd']/div[2]/input")).getAttribute("value").equalsIgnoreCase("Make Active")){
							log.writeSubSection("Question is already Inactive");									
						}

						if (comUtils.isElementDisplayed(By.id("successMessages"))){
							log.writeSubSection("Question was made inactive successfully","",true);
							log.takeSnapshot(driver);
						}
						else if (comUtils.isElementDisplayed(By.id("errorMessages"))){
							log.writeSubSection("Question was NOT made inactive","",false);
							log.takeSnapshot(driver);					
						}
						//break;
					}else{
						log.writeSubSection("The Question is Inactive.");									
					}
				}else{
					log.writeSubSection("The Question is not found.");
				}

			} catch(Exception e){
				e.printStackTrace();
			}
			log.writeSubSection_RecordStatus();
		}	
	}

	private void Print() throws IOException{
		log.write("Checking the Print option of Questions Page");
		try {			
			//Navigate to ManageQuestions
			navigateToManageQuestions();			

			//Check if Print button if available
			if (comUtils.isElementDisplayed(By.xpath("html/body/div[2]/form/div/div[2]/input[3]"))){

				//Click on the Print button
				driver.findElement(By.xpath("html/body/div[2]/form/div/div[2]/input[3]")).click();

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
		log.write("Checking the Download option of Questions Page");
		try {			
			
			//Navigate to ManageQuestions
			navigateToManageQuestions();			

			//Check if Print button if available
			if (comUtils.isElementDisplayed(By.xpath("//html/body/div[2]/form/div/div[2]/input[4]"))){

				//Click on the Print button
				driver.findElement(By.xpath("//html/body/div[2]/form/div/div[2]/input[4]")).click();

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

	public void navigateToManageQuestions() throws Exception{

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
		driver.findElement(By.linkText("Manage Questions")).click();
	}

	@After
	public void close() throws Exception {			
		log.close();
		df.close();		
		driver.close();
	}
}

