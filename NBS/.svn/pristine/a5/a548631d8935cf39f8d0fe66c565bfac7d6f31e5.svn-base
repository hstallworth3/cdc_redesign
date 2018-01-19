package com.nbs.dataEntry.search;

import org.junit.*;
import org.openqa.selenium.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Login;
import org.junit.Test;

public class Search_Patient {
	public WebDriver driver;
	public Log log;
	DataFile df;
	public String Data_File_Path,Log_File_Path;	
	private String streetAdd ="";
	
	//Condition names
	private String condition1,condition2,condition3,condition4,condition5="";	

	//LastName
	private String condition1data_LN,condition2data_LN,condition3data_LN,condition4data_LN="";	

	//First Name
	private String condition1data_FN,condition2data_FN,condition3data_FN,condition4data_FN="";
		
	//Street address
	private String condition2data_ST,StreetEqualData,StreetNotEqualData="";
		
	//city
	private String cityContainData,cityEqualData,cityNotEqualData = "";
		
	//state
	private String stateData = "";
	
	//Zip
	private String zipData = "";
	
	//PatientID
	private String patienId = "";
	
	//DOB
	private String DOB1, DOB2= "";
	
	//Field names
	private String LastName,FirstName,DateOfBirth,Street,City,State,Zip,PatientID = "";
		
	//Field name combinations
	private String StreetAndCity = "";
	
	//Entity name
	private String entity = ""; 
	
	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();		
		Data_File_Path = login.Data_File_Path+"dataEntry.search\\Search.xls";
		Log_File_Path = login.Log_File_Path;		
	}
	
	@Test
	public void test() {
		log = new Log("Search_Patient", Log_File_Path);
		df = new DataFile(Data_File_Path, "Patient");
		
		//Condition names
		condition1="Starts With";
		condition2="Contains";
		condition3 = "Equal";
		condition4 = "Not Equal";
		condition5 = "Between";
		
		//Data from excel
		//LastName
		condition1data_LN =df.getDataFromColumn("Starts With", 1);
		condition2data_LN=df.getDataFromColumn("Contains", 1);
		condition3data_LN=df.getDataFromColumn("Equal", 1);
		condition4data_LN=df.getDataFromColumn("Not Equal", 1);

		//First Name
		condition1data_FN=df.getDataFromColumn("Starts With", 2);
		condition2data_FN=df.getDataFromColumn("Contains", 2);
		condition3data_FN=df.getDataFromColumn("Equal", 2);
		condition4data_FN=df.getDataFromColumn("Not Equal", 2);
		
		//Street address
		condition2data_ST=df.getDataFromColumn("Contains", 3).trim();
		StreetEqualData = df.getDataFromColumn("Equal", 3).trim();
	    StreetNotEqualData = df.getDataFromColumn("Not Equal", 3).trim();
		
		//city
		cityContainData = df.getDataFromColumn("Contains", 4).trim();
		cityEqualData = df.getDataFromColumn("Equal", 4).trim();
		cityNotEqualData = df.getDataFromColumn("Not Equal", 4).trim();
		
		//state
	    stateData = df.getDataFromColumn("Equal", 5).trim();
		
		//Zip
		zipData = df.getDataFromColumn("Equal", 6).trim();
		
		//PatientID
		patienId = df.getDataFromColumn("Equal", 7).trim();
		
		//DOB
		DOB1 = df.getDataFromColumn("Date1", 8).trim();
		DOB2 = df.getDataFromColumn("Date2", 8).trim();

		//Field names
		LastName = "LastName";
		FirstName = "FirstName";
		DateOfBirth = "DateOfBirth";	
		Street = "Street";
		City = "City";
		State = "State";
		Zip = "Zip";	
		PatientID = "PatientID";
		
		//Field name combinations
		StreetAndCity = "StreetAndCity";
		
		//Entity name
		entity = "Patient"; 
		
		try{
			Search_Utils utils = new Search_Utils();
			utils.setDriver(driver);
			utils.setLog(log);

			//Verify Last Name -------------------------
			System.out.println("\nVerify Last Name--------------");
			log.write("\nVerify Last Name--------------");
			
			//'Start With'
			submitData(condition1, condition1data_LN, LastName);			
			utils.validateName(condition1, condition1data_LN, LastName,entity);	
			
			//'Contain'
			submitData(condition2, condition2data_LN, LastName);			
			utils.validateName(condition2, condition2data_LN, LastName,entity);	
			
			//'Equal'
			submitData(condition3, condition3data_LN, LastName);			
			utils.validateName(condition3, condition3data_LN, LastName,entity);
			
			//'NOT Equal'
			submitData(condition4, condition4data_LN, LastName);			
			utils.validateName(condition4, condition4data_LN, LastName,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Verify First Name -----------------------------------------
			System.out.println("\nVerify First Name--------------");
			log.write("\nVerify First Name--------------");	
			
			//'Start With'
			submitData(condition1, condition1data_FN, FirstName);			
			utils.validateName(condition1, condition1data_FN, FirstName,entity);
			
			//'Contain'
			submitData(condition2, condition2data_FN, FirstName);			
			utils.validateName(condition2, condition2data_FN, FirstName,entity);
			
			//'Equal'
			submitData(condition3, condition3data_FN, FirstName);			
			utils.validateName(condition3, condition3data_FN, FirstName,entity);
			
			//'NOT Equal'
			submitData(condition4, condition4data_FN, FirstName);			
			utils.validateName(condition4, condition4data_FN, FirstName,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//DOB for "Equal" -------------------------------------------------
			System.out.println("\nVerify DOB --------------");
			log.write("\nVerify DOB --------------");
			submitData(condition3,DOB1,DateOfBirth);
			utils.validateDOB_Equal(condition3,DOB1,DateOfBirth);

			//DOB for "Between"	------------------------------------------------			
			submitDOBBetween(condition5,DOB1,DOB2);			
			utils.validateDOB_Betweeen(DOB1,DOB2);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Verify street	---------------------------------------------
			System.out.println("\nVerify Street--------------");
			log.write("\nVerify Street--------------");			
			
			//'Contain'
			streetAdd = condition2data_ST.replace(".0", "");			
			submitData(condition2, streetAdd,Street);
			utils.validateStreet(condition2, streetAdd, Street,entity);			
			
			//'Equal'			
			streetAdd = StreetEqualData.replace(".0", "");
			submitData(condition3, streetAdd,Street);
			utils.validateStreet(condition3,streetAdd,Street,entity);			
			
			//'NOT Equal'	 		
			streetAdd = StreetNotEqualData.replace(".0", "");
			submitData(condition4, streetAdd,Street);
			utils.validateStreet(condition4,streetAdd,Street,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Verify City -----------------------------------------------------------
			System.out.println("\nVerify City--------------");
			log.write("\nVerify City--------------");
			
			//"Contains"
			submitData(condition2, cityContainData,City);			
			utils.validateCity(condition2, cityContainData,City,entity);			
			
			//"Equal"
			submitData(condition3, cityEqualData,City);		
			utils.validateCity(condition3, cityEqualData,City,entity);			
			
			//"Not Equal" (if name is empty - the address of the element changes)
			submitData(condition4, cityNotEqualData,City);				
			utils.validateCity(condition4, cityNotEqualData,City,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//State data
			submitData(condition3, stateData,State);
			System.out.println("\nVerify State --------------");
			log.write("\nVerify State --------------");
			utils.validateStateName(condition3, stateData,State,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//Zip data
			String zipNumber = zipData.replace(".0", "");
			System.out.println("\nVerify Zip----------");
			log.write("\nVerify Zip----------");
			submitData(condition3,zipNumber,Zip);
			utils.validateZipcode(condition3,zipNumber,Zip,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//PatientID ------------------------------------------------
			System.out.println("\nVerify PatientID --------------");
			log.write("\nVerify PatientID --------------");
			patienId = patienId.replace(".0", "");			
			submitData(condition3, patienId, PatientID);
			utils.validatePatientID(condition3,patienId,PatientID,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();			

			//Street and City	-------------------------------------------------		
			log.write("\nVerify Street and City --------------");
			submitFieldCombination(StreetAndCity,condition2data_ST,cityEqualData);			
			utils.validatePatientStreetAndCity(condition2,condition2data_ST,cityEqualData,StreetAndCity);			

			//Log Record Status
			log.writeSubSection_RecordStatus();
			
		}catch(Exception e){
			e.printStackTrace();
			log.takeSnapshot(driver);
		}
	}
	
	@After
	public void tearDown() throws Exception {		
		log.close();		
	}
	
	public void submitData(String condition, String conditionData, String fieldName)
	{
		try{
			driver.findElement(By.linkText("Data Entry")).click();
			driver.findElement(By.linkText("Patient")).click();
			switch (fieldName) {
			case  "LastName":				
				driver.findElement(By.name("DEM102O_textbox")).clear();
				driver.findElement(By.name("DEM102O_textbox")).sendKeys(condition);
				driver.findElement(By.id("DEM102")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();		 			 
				break;

			case "FirstName":			
				driver.findElement(By.name("DEM104O_textbox")).clear();
				driver.findElement(By.name("DEM104O_textbox")).sendKeys(condition);
				driver.findElement(By.id("DEM104")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();					 
				break;
				
			case "DateOfBirth":								
				driver.findElement(By.linkText("Data Entry")).click();
				driver.findElement(By.linkText("Patient")).click();
				//String[] dates = conditionData.split("/");				
				driver.findElement(By.name("DEM105O_textbox")).clear();
				driver.findElement(By.name("DEM105O_textbox")).sendKeys(condition);
				driver.findElement(By.id("patientDOB1")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;

			case "Sex":
				System.out.println("\nsex");
				break;
				
			case "Street":
				driver.findElement(By.name("DEM106O_textbox")).clear();
				driver.findElement(By.name("DEM106O_textbox")).sendKeys(condition);
				driver.findElement(By.id("DEM106")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
				
			case "City":				
				driver.findElement(By.name("DEM107O_textbox")).clear();
				driver.findElement(By.name("DEM107O_textbox")).sendKeys(condition);
				driver.findElement(By.id("DEM107")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
				
			case "State":
				driver.findElement(By.name("DEM108O_textbox")).clear();
				driver.findElement(By.name("DEM108O_textbox")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
				
			case "Zip":
				driver.findElement(By.id("DEM109")).clear();
				driver.findElement(By.id("DEM109")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
				
			case "PatientID":
				driver.findElement(By.id("DEM229")).clear();
				driver.findElement(By.id("DEM229")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
			case "StreetAndCity":
				driver.findElement(By.name("DEM106O_textbox")).clear();
				driver.findElement(By.name("DEM106O_textbox")).sendKeys(condition);
				driver.findElement(By.id("DEM106")).sendKeys(conditionData);
				
				driver.findElement(By.name("DEM107O_textbox")).clear();
				driver.findElement(By.name("DEM107O_textbox")).sendKeys(condition);
				driver.findElement(By.id("DEM107")).sendKeys(conditionData);
				break;
			default:
				break;}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void submitDOBBetween(String condition, String date1,String date2)
	{	
		driver.findElement(By.linkText("Data Entry")).click();
		driver.findElement(By.linkText("Patient")).click();		  
		driver.findElement(By.name("DEM105O_textbox")).clear();
		driver.findElement(By.name("DEM105O_textbox")).sendKeys(condition);
		driver.findElement(By.id("patientDOB1")).click();
		driver.findElement(By.id("patientDOBBet1")).sendKeys(date1);
		driver.findElement(By.id("patientDOBBet2")).sendKeys(date2);
		driver.findElement(By.name("Submit")).click();		  
	}
	
	private void submitFieldCombination(String fieldCombination, String field1Tx, String field2Tx)
	{	
		driver.findElement(By.linkText("Data Entry")).click();
		driver.findElement(By.linkText("Patient")).click();	
		
		switch (fieldCombination) {
	
		case  "StreetAndCity":
			driver.findElement(By.name("DEM106O_textbox")).clear();
			driver.findElement(By.name("DEM106O_textbox")).sendKeys(condition2);
			driver.findElement(By.id("DEM106")).sendKeys(field1Tx);
			
			driver.findElement(By.name("DEM107O_textbox")).clear();
			driver.findElement(By.name("DEM107O_textbox")).sendKeys(condition2);
			driver.findElement(By.id("DEM107")).sendKeys(field2Tx);
			driver.findElement(By.name("Submit")).click();
			break;			
		}
	}	
}