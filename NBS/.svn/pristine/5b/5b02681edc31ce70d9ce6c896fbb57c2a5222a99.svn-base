package com.nbs.dataEntry.search;

import java.io.IOException;
import org.junit.*;
import org.openqa.selenium.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Login;

public class Search_Place {
	public WebDriver driver;		
	public Log log;
	DataFile df;
	public String Data_File_Path,Log_File_Path;		
	private String street ="";

	//Condition names
	private String condition1, condition2, condition3, condition4 ="";

	//Place Name 
	private String condition1data_LN, condition2data_LN, condition3data_LN, condition4data_LN="";	

	//Street address
	private String condition2data_ST, StreetEqualData, StreetNotEqualData =""; 

	//City 
	private String cityContianData, cityEqualData, cityNotEqualData = ""; 

	//State 
	private String stateData="";

	//Zip 
	private String zipData="";

	//Telephone 
	private String phoneData="";

	//Field names
	private String Name, Street, City, State, Zip, Telephone= "";

	//Entity name
	private String entity = "Place";

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();

		Data_File_Path = login.Data_File_Path+"dataEntry.search\\Search.xls";
		Log_File_Path = login.Log_File_Path;
	}			

	@Test
	public void test() throws IOException {
		log = new Log("Search_Place", Log_File_Path);
		df = new DataFile(Data_File_Path, "Place");

		//Condition names
		condition1="Starts With";
		condition2="Contains";
		condition3 = "Equal";
		condition4 = "Not Equal";
		//condition5 = "Between";

		//Data from excel
		//Place Name 
		condition1data_LN=df.getDataFromColumn("Starts With", 1).trim();
		condition2data_LN=df.getDataFromColumn("Contains", 1).trim();
		condition3data_LN=df.getDataFromColumn("Equal", 1).trim();
		condition4data_LN=df.getDataFromColumn("Not Equal", 1).trim();

		//Street address
		condition2data_ST=df.getDataFromColumn("Contains", 2).trim();
		StreetEqualData = df.getDataFromColumn("Equal", 2).trim();
		StreetNotEqualData = df.getDataFromColumn("Not Equal", 2).trim();

		//City 
		cityContianData=df.getDataFromColumn("Contains", 3).trim();
		cityEqualData = df.getDataFromColumn("Equal", 3).trim();
		cityNotEqualData = df.getDataFromColumn("Not Equal", 3).trim();

		//State 
		stateData=df.getDataFromColumn("Equal", 4).trim();

		//Zip 
		zipData=df.getDataFromColumn("Equal", 5).trim();

		//Telephone 
		phoneData=df.getDataFromColumn("Equal", 6).trim();

		//Field names
		Name = "Name";						
		Street = "Street";
		City = "City";
		State = "State";
		Zip = "Zip";	
		Telephone = "Telephone";

		//Entity name
		entity = "Place";

		try{
			Search_Utils utils = new Search_Utils();
			utils.setDriver(driver);
			utils.setLog(log);

			///Place Name with "Start With" condition	
			System.out.println("\nVerify Place Name ----------");
			log.write("\nVerify Place Name ----------");
			submitData(condition1, condition1data_LN,Name);			
			utils.validateName(condition1, condition1data_LN,Name,entity);

			//Place Name with "Contains" condition
			submitData(condition2, condition2data_LN,Name);			
			utils.validateName(condition2,condition2data_LN,Name,entity);

			//Place Name with "Equals"
			submitData(condition3, condition3data_LN,Name);			
			utils.validateName(condition3,condition3data_LN,Name,entity);

			//Place Name with "Not Equals"	
			submitData(condition4, condition4data_LN,Name);			
			utils.validateName(condition4,condition4data_LN,Name,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Street with "contains"			 
			street = condition2data_ST.replace(".0", "");			
			System.out.println("\nVerify Street ----------");
			log.write("\nVerify Street ----------");
			submitData(condition2, street,Street);
			utils.validateStreet(condition2, street,Street,entity);

			//Street with "Equals"	
			street = StreetEqualData.replace(".0", "");
			submitData(condition3, street,Street);
			utils.validateStreet(condition3, street,Street,entity);

			//Street with "Not Equals"			   
			street = StreetNotEqualData.replace(".0", "");			
			submitData(condition4, street,Street);			
			utils.validateStreet(condition4, street,Street,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//city with "Contain" condition		
			System.out.println("\nVerify City----------");
			log.write("\nVerify City----------");
			submitData(condition2, cityContianData,City);		
			utils.validateCity(condition2, cityContianData,City,entity);

			//city with "equal" condition		
			submitData(condition3, cityEqualData,City);		
			utils.validateCity(condition3, cityEqualData,City,entity);

			//city with "Not Equal" condition
			submitData(condition4, cityNotEqualData,City);		
			utils.validateCity(condition4,cityNotEqualData,City,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//State data
			System.out.println("\nVerify State----------");
			log.write("\nVerify State----------");
			submitData(condition3,stateData,State);		
			utils.validateStateName(condition3,stateData,State,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Zip data
			String zip = zipData.replace(".0", "");
			System.out.println("\nVerify Zip ----------");
			log.write("\nVerify Zip ----------");
			submitData(condition3,zip,Zip);
			utils.validateZipcode(condition3,zip,Zip,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Telephone
			System.out.println("\nVerify Telephone ----------");
			log.write("\nVerify Telephone ----------");
			submitData(condition3,phoneData,Telephone);			
			utils.validatePhone(condition3,phoneData,Telephone,entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();
		}catch(Exception e){
			e.printStackTrace();
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
			driver.findElement(By.linkText("Place")).click();

			switch (fieldName) {
			case  "Name":				
				driver.findElement(By.name("nmoperator_textbox")).clear();
				driver.findElement(By.name("nmoperator_textbox")).sendKeys(condition);
				driver.findElement(By.name("placeSearch.nm")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();						 			 
				break;
				
			case "Street":
				driver.findElement(By.name("saoperator_textbox")).clear();
				driver.findElement(By.name("saoperator_textbox")).sendKeys(condition);
				driver.findElement(By.name("placeSearch.streetAddr1")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
				
			case "City":
				driver.findElement(By.name("cdoperator_textbox")).clear();
				driver.findElement(By.name("cdoperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("city")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();				
				break;
				
			case "State":
				driver.findElement(By.name("state_textbox")).clear();
				driver.findElement(By.name("state_textbox")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
			case "Zip":
				driver.findElement(By.name("placeSearch.zip")).clear();
				driver.findElement(By.name("placeSearch.zip")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;
				
			case "Telephone":				
				driver.findElement(By.name("placeSearch.phoneNbrTxt")).sendKeys(conditionData);				
				driver.findElement(By.name("Submit")).click();
				break;
			default:
				break;
				}
		}catch(Exception e){
		}
	}
}

