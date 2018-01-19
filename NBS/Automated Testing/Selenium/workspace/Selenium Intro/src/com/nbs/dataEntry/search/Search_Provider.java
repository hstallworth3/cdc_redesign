package com.nbs.dataEntry.search;

import org.junit.*;
import org.openqa.selenium.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Login;

public class Search_Provider {
	public WebDriver driver;
	public Log log;
	DataFile df;
	public String Data_File_Path,Log_File_Path;	
	private String streetData ="";

	//Condition names
	private String condition1, condition2, condition3, condition4="";
	
	//LastName
	private String condition1data_LN, condition2data_LN, condition3data_LN, condition4data_LN ="";	

	//First Name
	private String condition1data_FN, condition2data_FN, condition3data_FN, condition4data_FN="";	
	
	//Street address
	private String condition2data_ST, StreetEqualData, StreetNotEqualData="";	

	//city
	private String cityContainData, cityEqualData,  cityNotEqualData= "";
	
	//state
	private String stateData = "";
	
	//zip
	private String zipData = "";
	
	//telephone
	private String telephone = "";
	
	//Field names
		private String LastName,  FirstName, Street, City, State, Zip, Telephone= "";
				
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
		log = new Log("Search_Provider", Log_File_Path);
		df = new DataFile(Data_File_Path, "Provider");
		
		//Condition names
		condition1="Starts With";
		condition2="Contains";
		condition3 = "Equal";
		condition4 = "Not Equal";
		
		//Data from excel
		//LastName
		condition1data_LN=df.getDataFromColumn("Starts With", 1).trim();
	    condition2data_LN=df.getDataFromColumn("Contains", 1).trim();
		condition3data_LN=df.getDataFromColumn("Equal", 1).trim();
	    condition4data_LN=df.getDataFromColumn("Not Equal", 1).trim();

		//First Name
		condition1data_FN=df.getDataFromColumn("Starts With", 2).trim();
		condition2data_FN=df.getDataFromColumn("Contains", 2).trim();
		condition3data_FN=df.getDataFromColumn("Equal", 2).trim();
		condition4data_FN=df.getDataFromColumn("Not Equal", 2).trim();
		
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
		
		//zip
		zipData = df.getDataFromColumn("Equal", 6).trim();
		
		//telephone
		telephone = df.getDataFromColumn("Equal", 8).trim();
		
		//Field names
			LastName = "LastName";
			FirstName = "FirstName";			
			Street = "Street";
			City = "City";
			State = "State";
			Zip = "Zip";	
			Telephone = "Telephone";
			
		//Entity name
			entity = "Provider";
		
		try{

			Search_Utils utils = new Search_Utils();
			utils.setDriver(driver);
			utils.setLog(log);
			
			//Verify Last Name	
			System.out.println("\nVerify Last Name--------------");
			log.write("\nVerify Last Name--------------");
			//Last name with "Starts With" condition
			submitData(condition1, condition1data_LN, LastName);			
			utils.validateName(condition1, condition1data_LN, LastName,entity);
			
			//Last name with "Contains" condition			
			submitData(condition2, condition2data_LN,LastName);			
			utils.validateName(condition2,condition2data_LN,LastName,entity);
			
			//Last name with "Equals"			
			submitData(condition3, condition3data_LN,LastName);			
			utils.validateName(condition3, condition3data_LN,LastName,entity);
			
			//Last name with "Not Equals"			
			submitData(condition4, condition4data_LN,LastName);			
			utils.validateName(condition4, condition4data_LN,LastName,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//First name with "Start With" condition
			System.out.println("\nVerify First Name----------");
			log.write("\nVerify First Name----------");
			submitData(condition1, condition1data_FN,FirstName);			
			utils.validateName(condition1, condition1data_FN,FirstName,entity);
			
			//First name with "Contains" condition,
			submitData(condition2, condition2data_FN,FirstName);		
			utils.validateName(condition2, condition2data_FN,FirstName,entity);			  

			//First name with "Equals"
			submitData(condition3, condition3data_FN,FirstName);			
			utils.validateName(condition3, condition3data_FN,FirstName,entity);

			//First name with "Not Equals"			 
			submitData(condition4, condition4data_FN,FirstName);			
			utils.validateName(condition4, condition4data_FN,FirstName,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//Street with "contains"
			System.out.println("\nVerify Street----------");
			log.write("\nVerify Street----------");
			streetData = condition2data_ST.replace(".0", "");
			submitData(condition2, streetData,Street);
			utils.validateStreet(condition2, streetData,Street,entity);
			
			//Street with "Equals"	
			streetData = StreetEqualData.replace(".0", "");
			submitData(condition3, streetData,Street);
			utils.validateStreet(condition3, streetData,Street,entity);
			
			//Street with "Not Equals"			
			streetData = StreetNotEqualData.replace(".0", "");			
			submitData(condition4, streetData,Street);			
			utils.validateStreet(condition4, streetData,Street,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//city with "Contain" condition
			System.out.println("\nVerify City----------");
			log.write("\nVerify City----------");
			submitData(condition2, cityContainData,City);						
			utils.validateCity(condition2, cityContainData,City,entity);
			
			//city with "equal" condition
			submitData(condition3, cityEqualData,City);			
			utils.validateCity(condition3, cityEqualData,City,entity);
			
			//city with "Not Equal" condition
			submitData(condition4, cityNotEqualData,City);
			utils.validateCity(condition4,cityNotEqualData,City,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//State data
			submitData(condition3,stateData,State);
			System.out.println("\nVerify State----------");
			log.write("\nVerify State----------");			
			utils.validateStateName(condition3,stateData,State,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//Zip data			
			System.out.println("\nVerify Zip ----------");
			log.write("\nZip Condition----------");
			String zip = zipData.replace(".0", "");
			submitData(condition3,zip,Zip);			
			utils.validateZipcode(condition3,zip,Zip,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//Telephone
			System.out.println("\nVerify Telephone----------");
			log.write("\nVerify Telephone----------");
			submitData(condition3,telephone,Telephone);
			utils.validatePhone(condition3,telephone,Telephone,entity);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
		}catch(Exception e){			
			log.takeSnapshot(driver);
		}
	}

	@After
	public void tearDown() throws Exception {
		//driver.quit();
		log.close();		
	}
	
	public void submitData(String condition, String conditionData, String fieldName)
	{
		try{			
			driver.findElement(By.linkText("Data Entry")).click();
			driver.findElement(By.linkText("Provider")).click();

			switch (fieldName) {
			case  "LastName":
				driver.findElement(By.name("providerSearch.lastNameOperator_textbox")).clear();
				driver.findElement(By.name("providerSearch.lastNameOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("providerSearch.lastName")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();						 			 
				break;

			case "FirstName":			
				driver.findElement(By.name("providerSearch.firstNameOperator_textbox")).clear();
				driver.findElement(By.name("providerSearch.firstNameOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("providerSearch.firstName")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();				 
				break;
			
			case "Street":
				driver.findElement(By.name("providerSearch.streetAddr1Operator_textbox")).clear();
				driver.findElement(By.name("providerSearch.streetAddr1Operator_textbox")).sendKeys(condition);
				driver.findElement(By.id("providerSearch.streetAddr1")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
				
			case "City":
				driver.findElement(By.name("providerSearch.cityDescTxtOperator_textbox")).clear();
				driver.findElement(By.name("providerSearch.cityDescTxtOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("providerSearch.cityDescTxt")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();				
				break;
				
			case "State":
				driver.findElement(By.name("providerSearch.state_textbox")).clear();
				driver.findElement(By.name("providerSearch.state_textbox")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
			case "Zip":
				driver.findElement(By.id("providerSearch.zipCd")).clear();
				driver.findElement(By.id("providerSearch.zipCd")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
				
			case "Telephone":
				String str1 = "";
				String str2 = "";
				String str3 = "";				
				String[] number = conditionData.split("-");
				str1 = number[0];
				str2 = number[1];
				str3 = number[2];
				
				driver.findElement(By.id("providerSearch.phoneNbrTxt1")).sendKeys(str1);
				driver.findElement(By.id("providerSearch.phoneNbrTxt2")).sendKeys(str2);
				driver.findElement(By.id("providerSearch.phoneNbrTxt3")).sendKeys(str3);
				driver.findElement(By.name("Submit")).click();
				break;
			default:
				break;
				}
		}catch(Exception e){
		}
	}
}
