package com.nbs.dataEntry.search;

import java.util.Set;
import java.awt.Robot;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.junit.*;
import org.openqa.selenium.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Login;


public class Search_LabReportAndMorbReport {
	public WebDriver driver;		
	public Log log;
	DataFile df;
	public String Data_File_Path,Log_File_Path;
	private String streetAdd ="";
	public Robot robot=null;
	private String parentwindow = "";
	
	//Condition names
	private String condition1, condition2, condition3, condition4, condition5="";
	
	//LastName
	private String condition1data_LN, condition2data_LN, condition3data_LN,condition4data_LN ="";	

	//First Name
	private String condition1data_FN, condition2data_FN, condition3data_FN, condition4data_FN="";
		
	//Street address
	private String condition2data_ST, StreetEqualData, StreetNotEqualData ="";
		
	//city
	private String cityContainData, cityEqualData, cityNotEqualData = "";
		
	//state
	private String stateData = "";
	
	//zip
	private String zipData = "";
		
	//DOB
	private String DOB1, DOB2 = "";
	
	//Field names
	private String LastName, FirstName, DateOfBirth, Street,City, State, Zip = "";
		
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
		log = new Log("Search_LabReportAndMorbReport", Log_File_Path);
		df = new DataFile(Data_File_Path, "Patient");
		
		//Condition names
		condition1 ="Starts With";
		condition2 ="Contains";
		condition3 = "Equal";
		condition4 = "Not Equal";
		condition5 = "Between";
		
		//Data from excel
		//LastName
		condition1data_LN=df.getDataFromColumn("Starts With", 1);
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
		
		//zip
		zipData = df.getDataFromColumn("Equal", 6).trim();
		
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
		
		try{
			Search_Utils utils = new Search_Utils();
			utils.setDriver(driver);
			utils.setLog(log);
			
			driver.findElement(By.linkText("Data Entry")).click();
			
			//Uncomment the line based on which report you want to run
			//driver.findElement(By.linkText("Lab Report")).click();
			driver.findElement(By.linkText("Morbidity Report")).click();
			
			//Verify Last Name
			System.out.println("\nVerify Last Name--------------");
			log.write("\nVerify Last Name--------------");			
			//'Start With'
			parentwindow = submitData(condition1, condition1data_LN, LastName);
			validateData(condition1, condition1data_LN , LastName, parentwindow); 		
					
			//'Contain'
			parentwindow = submitData(condition2, condition2data_LN, LastName);			
			validateData(condition2, condition2data_LN, LastName,parentwindow);			
			
			//'Equal'
			parentwindow = submitData(condition3, condition3data_LN, LastName);			
			validateData(condition3, condition3data_LN, LastName,parentwindow);			
		  
		   //'NOT Equal'
			parentwindow = submitData(condition4, condition4data_LN, LastName);			
			validateData(condition4, condition4data_LN, LastName,parentwindow);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//Verify First Name
			System.out.println("\nVerify First Name--------------");
			log.write("\nVerify First Name--------------");
			//'Start With'
			parentwindow = submitData(condition1, condition1data_FN, FirstName);
			System.out.println("After Submit");
			validateData(condition1, condition1data_FN, FirstName,parentwindow);
			//'Contain'
			parentwindow = submitData(condition2, condition2data_FN, FirstName);			
			validateData(condition2, condition2data_FN, FirstName,parentwindow);
			//'Equal'
			parentwindow = submitData(condition3, condition3data_FN, FirstName);			
			validateData(condition3, condition3data_FN, FirstName,parentwindow);
			//'NOT Equal'
			parentwindow = submitData(condition4, condition4data_FN, FirstName);			
			validateData(condition4, condition4data_FN, FirstName,parentwindow);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//Verify street	
			System.out.println("\nVerify Street--------------");
			log.write("\nVerify Street--------------");			
			//'Contain'	
			streetAdd  = condition2data_ST.replace(".0", "");
			parentwindow = submitData(condition2, streetAdd,Street);
			validateData(condition2, streetAdd, Street,parentwindow);			
		    //'Equal'			
			streetAdd  = StreetEqualData.replace(".0", "");
			parentwindow = submitData(condition3, streetAdd,Street);
			validateData(condition3,streetAdd,Street,parentwindow);			
			//'NOT Equal'	 --- look for street 1 and street2  ---not working			
			streetAdd =StreetNotEqualData.replace(".0", "");
			parentwindow = submitData(condition4, streetAdd,Street);
			validateData(condition4,streetAdd,Street,parentwindow);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//Verify City
			System.out.println("\nVerify City--------------");
			log.write("\nVerify City--------------");
			//"Contains"
			parentwindow = submitData(condition2, cityContainData,City);			
			validateData(condition2, cityContainData,City,parentwindow);		
			//"Equal"
			parentwindow = submitData(condition3, cityEqualData,City);		
			validateData(condition3, cityEqualData,City,parentwindow);			
			//"Not Equal" (if name is empty - the address of the element changes)
			parentwindow = submitData(condition4, cityNotEqualData,City);				
		    validateData(condition4, cityNotEqualData,City,parentwindow);
		    
		  //Log Record Status
			log.writeSubSection_RecordStatus();
			
			//State data
			parentwindow = submitData(condition3, stateData,State);
			System.out.println("\nVerify State --------------");
			log.write("\nVerify State --------------");
			validateData(condition3, stateData,State,parentwindow);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
			//Zip data
			String zipNumber = zipData.replace(".0", "");
			System.out.println("\nVerify Zip----------");
			log.write("\nVerify Zip----------");
			parentwindow = submitData(condition3,zipNumber,Zip);
			validateData(condition3,zipNumber,Zip,parentwindow);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
						
			//DOB for "Equal"
			System.out.println("\nVerify DOB --------------");
			log.write("\nVerify DOB --------------");
			parentwindow = submitData(condition3,DOB1,DateOfBirth);
			validateData(condition3,DOB1,DateOfBirth,parentwindow);
			
			//DOB for "Between"	
			log.write("\nVerify DOB --------------");
			parentwindow = submitDOBBetween(condition5,DOB1,DOB2);			
			validateDOB_Betweeen(DOB1,DOB2,parentwindow);
			
			//Log Record Status
			log.writeSubSection_RecordStatus();
			
		}catch(Exception e){
			//log.write("Patient created: ", "Patient "+" created result", false);
			//log.takeSnapshot(driver);
		}
	}
	
	@After
	public void tearDown() throws Exception {
		//driver.quit();
		log.close();
		//String verificationErrorString = verificationErrors.toString();
		/*if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }*/
	}
	
	public String submitData(String condition, String conditionData, String fieldName) throws InterruptedException
	{
		//try{
			System.out.println("IN submit");			
			driver.findElement(By.id("Patient.personUidButton")).click();
			
			String subWindowHandler = null;
			driver.getWindowHandles();
			Set<String> handles = driver.getWindowHandles(); 
			Iterator<String> iterator = handles.iterator();
			String parentWindow= driver.getWindowHandle();
			System.out.println("Handles: "+handles.size());
			while (iterator.hasNext()){			
				subWindowHandler = iterator.next();				
			}
			driver.switchTo().window(subWindowHandler);
			
			switch (fieldName) {
			case  "LastName":
				Thread.sleep(2000);
				driver.findElement(By.name("personSearch.lastNameOperator_textbox")).clear();
				driver.findElement(By.name("personSearch.lastNameOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("personSearch.lastName")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/table/tbody/tr/td/a")).click();
				//driver.findElement(By.xpath("html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[5]/td/form/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td/a")).click();
					
				driver.switchTo().window(parentWindow);				
				break;

			case "FirstName":			
				driver.findElement(By.name("personSearch.firstNameOperator_textbox")).clear();
				driver.findElement(By.name("personSearch.firstNameOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("personSearch.firstName")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();				
				driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/table/tbody/tr/td/a")).click();				
				driver.switchTo().window(parentWindow);	
				break;
				
			case "DateOfBirth":
				String[] dates = conditionData.split("/");				
				driver.findElement(By.name("personSearch.birthTimeOperator_textbox")).clear();
				driver.findElement(By.name("personSearch.birthTimeOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("personSearch.birthTimeMonth")).sendKeys(dates[0]);
				driver.findElement(By.id("personSearch.birthTimeDay")).sendKeys(dates[1]);
				driver.findElement(By.id("personSearch.birthTimeYear")).sendKeys(dates[2]);				
				driver.findElement(By.name("Submit")).click();
				driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/table/tbody/tr/td/a")).click();
				break;

			case "Sex":
				//System.out.println("\nsex");
				break;
				
			case "Street":
				Thread.sleep(1000);				
				driver.findElement(By.name("personSearch.streetAddr1Operator_textbox")).clear();
				driver.findElement(By.name("personSearch.streetAddr1Operator_textbox")).sendKeys(condition);				
				driver.findElement(By.id("personSearch.streetAddr1")).sendKeys(conditionData);				
				driver.findElement(By.name("Submit")).click();				
				driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/table/tbody/tr/td/a")).click();				
				break;
				
			case "City":				
				driver.findElement(By.name("personSearch.cityDescTxtOperator_textbox")).clear();
				driver.findElement(By.name("personSearch.cityDescTxtOperator_textbox")).sendKeys(condition);
				driver.findElement(By.id("personSearch.cityDescTxt")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();				
				driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/table/tbody/tr/td/a")).click();
				break;
				
			case "State":				
				driver.findElement(By.name("personSearch.state_textbox")).clear();				
				driver.findElement(By.name("personSearch.state_textbox")).sendKeys(conditionData);				
				driver.findElement(By.name("Submit")).click();
				driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/table/tbody/tr/td/a")).click();				
				break;
				
			case "Zip":
				driver.findElement(By.id("personSearch.zipCd")).clear();
				driver.findElement(By.id("personSearch.zipCd")).sendKeys(conditionData);
				driver.findElement(By.name("Submit")).click();
				driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/table/tbody/tr/td/a")).click();				
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
				break;
				}
	//	}catch(Exception e){
		//}
			return parentWindow;
	}
	
	private String submitDOBBetween(String condition, String date1,String date2)
	{			
		driver.findElement(By.id("Patient.personUidButton")).click();
		
		String subWindowHandler = null;
		driver.getWindowHandles();
		Set<String> handles = driver.getWindowHandles(); // get all window handles
		Iterator<String> iterator = handles.iterator();
		String parentWindow= driver.getWindowHandle();
		System.out.println("Handles: "+handles.size());
		while (iterator.hasNext()){			
			subWindowHandler = iterator.next();			
		}
		driver.switchTo().window(subWindowHandler);		
		driver.findElement(By.name("personSearch.birthTimeOperator_textbox")).clear();		
		driver.findElement(By.name("personSearch.birthTimeOperator_textbox")).sendKeys(condition);
		driver.findElement(By.name("personSearch.birthTimeOperator_button")).click();
		driver.findElement(By.id("personSearch.beforeBirthTime")).click();
		driver.findElement(By.id("personSearch.beforeBirthTime")).sendKeys(date1);
		driver.findElement(By.id("personSearch.afterBirthTime")).sendKeys(date2);
		driver.findElement(By.name("Submit")).click();	
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/table/tbody/tr/td/a")).click();
		return parentWindow;
	}
	
	public void  validateData(String conditionTx, String searchText , String fieldName, String windowHandle) throws InterruptedException, IOException 
	{
		String Name ="";
		driver.switchTo().window(windowHandle);
		switch (fieldName) {
		case  "LastName":
			Name = driver.findElement(By.id("entity.lastNm")).getAttribute("value");			
			break;
		case "FirstName":
			Thread.sleep(1000);			
			Name = driver.findElement(By.id("entity.firstNm")).getAttribute("value");			
			break;
		case "DateOfBirth":
			Thread.sleep(1000);			
			Name = driver.findElement(By.id("entity.DOB")).getAttribute("value");			
			break;
		case "Street":
			Thread.sleep(1000);			
			Name = driver.findElement(By.id("entity.streetAddr1")).getAttribute("value");			
			break;
		case "City":
			Thread.sleep(1000);			
			Name = driver.findElement(By.id("entity.city")).getAttribute("value");			
			break;
		case "State":
			Thread.sleep(1000);			
			Name = driver.findElement(By.name("stateCd_textbox")).getAttribute("value");			
			break;
		case "Zip":
			Thread.sleep(1000);			
			Name = driver.findElement(By.id("entity.zip")).getAttribute("value");			
			break;			
		}
		switch (conditionTx) {
		case  "Starts With":
			check_StartWith(Name,searchText,fieldName);
			break;
		case "Contains":
			check_Contain(Name,searchText,fieldName);
			break;
		case "Equal":
			check_Equal(Name,searchText,fieldName);
			break;
		case "Not Equal":
			check_NotEqual(Name,searchText,fieldName);
			//driver.findElement(By.xpath("html/body/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[6]/td/form/div[1]/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[7]/td/table/tbody/tr/td/table/thead/tr/td/nobr/input[2]")).click();;
			break;			
		}
	}
	
	private void validateDOB_Betweeen(String dob1,String dob2, String windowHandle) throws IOException
	{			
		driver.switchTo().window(windowHandle);		
		String dtOfBirth ="";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		try{
			Date date1 = sdf.parse(dob1);
			Date date2 = sdf.parse(dob2);
						
			dtOfBirth = driver.findElement(By.id("entity.DOB")).getAttribute("value");
			Date ActualDate = sdf.parse(dtOfBirth);
			System.out.println("DOB:  " +ActualDate + "     FromDate:  "+date1 +"     ToDate:  "+ date2);
			log.writeSubSection("DOB:  " +ActualDate + "     FromDate:  "+date1 +"     ToDate:  "+ date2);
			//if((ActualDate.compareTo(date1)<0) && ActualDate.compareTo(date2)>0)
			if((date1.compareTo(ActualDate)<0) && date2.compareTo(ActualDate)>0)
			{
				System.out.println("Date is Between the specified dates  --  Pass");
				log.writeSubSection("Date is Between the specified dates  --  Pass","",true);
			}else{
				System.out.println("Date is NotBetween the specified dates  --  Fail");
				log.writeSubSection("Date is NotBetween the specified dates  --  Fail","",false);
			}
		}catch(Exception e){

		}		   
	}
	
	public void check_StartWith(String LstName, String srchText, String fldName) throws IOException
	{	
		System.out.println("\n'Starts With' Condition: ");		
		System.out.println(fldName+": "+LstName+"     Should 'Start With': "+srchText);
		log.writeSubSection("\n'Starts With' Condition: ");		
		log.writeSubSection(fldName+": "+LstName+"     Should 'Start With': "+srchText);
		if( (LstName.toLowerCase().startsWith(srchText.trim().toLowerCase()))  )
		{		 			  
			System.out.println(fldName+" starts with search text  --  Pass");
			log.writeSubSection(fldName+ " starts with search text  --  Pass","",true);
		}else{
			System.out.println(  fldName+ " does NOT start with search text  --  Fail");
			log.writeSubSection(  fldName+ " does NOT start with search text  --  Fail","",false);
		} 
	}
	
	private void check_Contain(String LstName, String srchText, String fldName) throws IOException
	{
		System.out.println("\n'Contain' Condition: ");
		log.writeSubSection("\n'Contain' Condition: "); 
		System.out.println(fldName+": "+LstName+"     Should 'Contain': "+srchText);
		log.writeSubSection(fldName+": "+LstName+"     Should 'Contain': "+srchText);
		if( (LstName.toLowerCase().contains(srchText.trim().toLowerCase()))  )
		{			 			  
			System.out.println(fldName+" contians search text  --  Pass");
			log.writeSubSection(fldName+ " contians search text  --  Pass","",true);
		}else{
			System.out.println(  fldName+ " contians search text  --  Fail");
			log.writeSubSection(  fldName+ " does NOT contians search text  --  Fail","",false);
		}   
	}
	
	private void check_Equal(String LstName, String srchText, String fldName) throws IOException
	{
		System.out.println("\n'Equal' Condition: "); 
		log.writeSubSection("\n'Equal' Condition: ");
		System.out.println(fldName+": "+LstName+"     Should 'Equal': "+srchText); 
		log.writeSubSection(fldName+": "+LstName+"     Should 'Equal': "+srchText);
		if( (LstName.toLowerCase().equals(srchText.trim().toLowerCase()))  )
		{			 			  
			System.out.println(fldName+" is equal to search text  --  Pass");
			log.writeSubSection(fldName+ " is equal to search text  --  Pass","",true);
		}else{
			System.out.println(  fldName+ " is NOT equal to search text  --  Fail");
			log.writeSubSection(  fldName+ " is NOT equal to search text  --  Fail","",false);
		}   
	}

	private void check_NotEqual(String LstName, String srchText, String fldName) throws IOException
	{
		System.out.println("\n'Not Equal' Condition: ");
		log.writeSubSection("\n'Not Equal' Condition: "); 
		System.out.println(fldName+": "+LstName+"     Should 'Not Equal': "+srchText);
		log.writeSubSection(fldName+": "+LstName+"     Should 'Not Equal': "+srchText);
		if( (!(LstName.toLowerCase().equals(srchText.trim().toLowerCase())))  )
		{			 			  
			System.out.println(fldName+" is NOT equal search text  --  Pass");
			log.writeSubSection(fldName+ " is NOT equal search text  --  Pass","",true);
		}else{
			System.out.println(  fldName+ " is equal search text  --  Fail");
			log.writeSubSection(  fldName+ " is equal search text  --  Fail","",false);
		}   
	}

}