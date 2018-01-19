package com.nbs.dataEntry.search;


import java.io.IOException;
import org.junit.*;
import org.openqa.selenium.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Login;

public class Search_Organization {
	public WebDriver driver;
		
	public Log log;
	DataFile df;
	public String Data_File_Path,Log_File_Path;	
	private String street ="";

	//Condition names
	private String condition1, condition2, condition3, condition4="";	

	//Field names
	private String Name, Street,  City, State, Zip,Telephone= "";

	//Entity name
	private String entity = "";
	
	//Organization Name 
	private String condition1data_LN, condition2data_LN, condition3data_LN, condition4data_LN="";
	
	//Street address
	private String condition2data_ST, StreetEqualData, StreetNotEqualData="";

	//City 
	private String cityContianData, cityEqualData, cityNotEqualData ="";

	//State 
	private String stateData="";

	//Zip 
	private String zipData="";

	//Telephone 
	private String phoneData="";	

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
		log = new Log("Search_Organization", Log_File_Path);
		df = new DataFile(Data_File_Path, "Organization");

		//Condition names
		condition1="Starts With";
		condition2="Contains";
		condition3 = "Equal";
		condition4 = "Not Equal";

		//Field names
		Name = "Name";						
		Street = "Street";
		City = "City";
		State = "State";
		Zip = "Zip";	
		Telephone = "Telephone";

		//Entity name
		entity = "Organization";

		//Data from excel
		//Organization Name 
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
		try{
			Search_Utils utils = new Search_Utils();
			utils.setDriver(driver);
			utils.setLog(log);

			//Organization Name with "Start With" condition	
			System.out.println("\nVerify Organization Name----------");
			log.write("\nVerify Organization Name----------");
			submitData(condition1, condition1data_LN,Name);			
			utils.validateName(condition1, condition1data_LN, Name, entity);

			//Organization Name with "Contains" condition
			submitData(condition2, condition2data_LN,Name);			
			utils.validateName(condition2,condition2data_LN,Name, entity);

			//Organization name with "Equals"
			submitData(condition3, condition3data_LN,Name);			
			utils.validateName(condition3,condition3data_LN,Name, entity);

			//Organization name with "Not Equals"
			submitData(condition4, condition4data_LN,Name);			
			utils.validateName(condition4,condition4data_LN,Name, entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Street with "contains"					
			System.out.println("\nVerify Street ----------");
			log.write("\nVerify Street----------");
			street = condition2data_ST.replace(".0", "");
			submitData(condition2, street,Street);
			utils.validateStreet(condition2, street,Street, entity);

			//Street with "Equals"		
			street = StreetEqualData.replace(".0", "");
			submitData(condition3, street,Street);
			utils.validateStreet(condition3, street,Street, entity);

			//Street with "Not Equals"		
			street = StreetNotEqualData.replace(".0", "");			
			submitData(condition4, street,Street);			
			utils.validateStreet(condition4, street,Street, entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//city with "Contain" condition
			System.out.println("\nVerify City----------");
			log.write("\nVerify City----------");		
			submitData(condition2, cityContianData,City);		
			utils.validateCity(condition2, cityContianData,City, entity);

			//city with "equal" condition
			submitData(condition3, cityEqualData,City);			
			utils.validateCity(condition3, cityEqualData,City, entity);

			//city with "Not Equal" condition
			submitData(condition4, cityNotEqualData,City);		
			utils.validateCity(condition4,cityNotEqualData,City, entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//State data
			System.out.println("\nVerify State----------");
			log.write("\nVerify State----------");
			submitData(condition3,stateData,State);
			utils.validateStateName(condition3,stateData,State, entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Zip data
			System.out.println("\nVerify Zip ----------");
			log.write("\nVerify Zip----------");
			String zip = zipData.replace(".0", "");
			submitData(condition3,zip,Zip);
			utils.validateZipcode(condition3,zip,Zip, entity);

			//Log Record Status
			log.writeSubSection_RecordStatus();

			//Telephone
			System.out.println("\nVerify Telephone ----------");
			log.write("\nVerify Telephone----------");
			submitData(condition3,phoneData,Telephone);
			utils.validatePhone(condition3,phoneData,Telephone, entity);

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
			driver.findElement(By.linkText("Organization")).click();

			switch (fieldName) {
			case  "Name":				
				driver.findElement(By.name("organizationSearch.nmTxtOperator_textbox")).clear();
				driver.findElement(By.name("organizationSearch.nmTxtOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("organizationSearch.nmTxt")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();						 			 
				break;

			case "Street":
				driver.findElement(By.name("organizationSearch.streetAddr1Operator_textbox")).clear();
				driver.findElement(By.name("organizationSearch.streetAddr1Operator_textbox")).sendKeys(condition);
				driver.findElement(By.id("organizationSearch.streetAddr1")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;

			case "City":
				driver.findElement(By.name("organizationSearch.cityDescTxtOperator_textbox")).clear();
				driver.findElement(By.name("organizationSearch.cityDescTxtOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("organizationSearch.cityDescTxt")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();				
				break;

			case "State":
				driver.findElement(By.name("organizationSearch.stateCd_textbox")).clear();
				driver.findElement(By.name("organizationSearch.stateCd_textbox")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				break;

			case "Zip":
				driver.findElement(By.id("organizationSearch.zipCd")).clear();
				driver.findElement(By.id("organizationSearch.zipCd")).sendKeys(conditionData);
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
				driver.findElement(By.id("organizationSearch.phoneNbrTxt1")).sendKeys(str1);
				driver.findElement(By.id("organizationSearch.phoneNbrTxt2")).sendKeys(str2);
				driver.findElement(By.id("organizationSearch.phoneNbrTxt3")).sendKeys(str3);
				driver.findElement(By.name("Submit")).click();
				break;
			default:
				break;
			}
		}catch(Exception e){
		}
	}
}