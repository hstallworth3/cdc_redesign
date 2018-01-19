package com.nbs.dataEntry.search;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.rtts.utilities.Log;

public class Search_Utils {
	private WebDriver driver;
	private Log log;	

	@Before	
	public void setDriver(WebDriver driver){
		  
		 this.driver=driver;
	  }
	
	public void setLog(Log log){
		  
		  this.log=log;
	  }	
	@Test
	public void test() {
		fail("Not yet implemented");
	}	
	@After
	public void tearDown() throws Exception {		
		log.close();
	}
	
	private String getPatientName(String nameTx, String fullName){
		
		String str = "Legal";
		String lastName ="";
		int beginIndex =0;
		int endingIndex =0;
		int index = 0;
		String substring ="";
		try {
			index = fullName.indexOf(str);			  			  
			beginIndex = index +5;			  
			endingIndex = fullName.length();
			substring = fullName.substring(beginIndex,endingIndex);			
			String[] name = substring.split(",");			
			if(nameTx.equals("LastName")){				  			  
				lastName = name[0].trim();				
				return lastName;
			}else{				  
				lastName = name[1].trim();
				return lastName;
			}
		}catch(Exception e){
			//e.printStackTrace();
		}		
		return lastName;
	}
	
	private ArrayList<String> getPatientStreet()	
	{	
		ArrayList<String> street = new ArrayList<String>();	
		String subString = "";
		int rowCount = 0;	
		
		try{
			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();		
			driver.findElement(By.id("tabs0head2")).click();
			int rowSize = driver.findElements(By.xpath("//table[@id='patSearch8']/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr")).size();		
			for(int i= 0; i<(rowSize-1);i++){
				rowCount = i+1;
				subString = driver.findElement(By.xpath("//tbody[@id='patternaddrTable']/tr["+rowCount+"]/td[4]")).getText();
				String[] st = subString.split(",");
				street.add(st[0]);	
				}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return street;
	}
	
	private ArrayList<String> getPatientCity()	
	{	
		ArrayList<String> city = new ArrayList<String>();		
		int rowCount = 0;	
		
		try{
			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();		
			driver.findElement(By.id("tabs0head2")).click();
			int rowSize = driver.findElements(By.xpath("//table[@id='patSearch8']/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr")).size();
			for(int i= 0; i<(rowSize-1);i++){
				rowCount = i+1;					
				city.add(driver.findElement(By.xpath("//tbody[@id='patternaddrTable']/tr["+rowCount+"]/td[5]")).getText());				
				}	
			city.add(driver.findElement(By.xpath("//*[@id='patSearch18']/tbody/tr[10]/td[2]")).getText());
		}catch(Exception e){
			//e.printStackTrace();
		}
		return city;
	}
	
	private ArrayList<String> getPatientState()	
	{
		ArrayList<String> city = new ArrayList<String>();		
		int rowCount = 0;	
		
		try{
			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();		
			driver.findElement(By.id("tabs0head2")).click();
			int rowSize = driver.findElements(By.xpath("//table[@id='patSearch8']/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr")).size();	
				
			for(int i= 0; i<(rowSize-1);i++){
				rowCount = i+1;					
				city.add(driver.findElement(By.xpath("//tbody[@id='patternaddrTable']/tr["+rowCount+"]/td[6]")).getText());				
			}	
			city.add(driver.findElement(By.xpath("//*[@id='patSearch18']/tbody/tr[11]/td[2]")).getText());
		}catch(Exception e){
			//e.printStackTrace();
		}
		return city;
	}
	
	private ArrayList<String> getPatientZip()	
	{	
		ArrayList<String> zip = new ArrayList<String>();		
		int rowCount = 0;	
		
		try{
			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();		
			driver.findElement(By.id("tabs0head2")).click();
			int rowSize = driver.findElements(By.xpath("//table[@id='patSearch8']/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr")).size();	
					
			for(int i= 0; i<(rowSize-1);i++){
				rowCount = i+1;					
				zip.add(driver.findElement(By.xpath("//tbody[@id='patternaddrTable']/tr["+rowCount+"]/td[7]")).getText());				
			}	
		}catch(Exception e){
			//e.printStackTrace();
		}
		return zip;
	}
	
	private String getPatientId()	
	{	
		String ptID = "";
		
		try{
			ptID = driver.findElement(By.xpath("//*[@id='searchResultsTable']/tbody/tr/td[1]/a")).getText();
		}catch(Exception e){
			//e.printStackTrace();
		}
		return ptID;
	}
	private String getPatientDOB(){
		String dtOfBirth = "";
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();
		driver.findElement(By.id("tabs0head2")).click();		 
		dtOfBirth = driver.findElement(By.id("NBS106")).getText();
		return dtOfBirth;
	}
	
	private Hashtable<String, String> getPatientStreetAndCity()	
	{	
		Hashtable<String, String> streetAndCity = new Hashtable<String, String>();	
		String street = "";
		String city = "";
		int rowCount = 0;	
		
		try{
			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();		
			driver.findElement(By.id("tabs0head2")).click();
			int rowSize = driver.findElements(By.xpath("//*[@id='patternaddrTable']/tr")).size();		
			for(int i= 0; i<(rowSize-1);i++){
				rowCount = i+1;
				street = driver.findElement(By.xpath("//*[@id='patternaddrTable']/tr["+rowCount+"]/td[4]")).getText();
				city = driver.findElement(By.xpath("//*[@id='patternaddrTable']/tr["+rowCount+"]/td[5]")).getText();
				
				streetAndCity.put(street, city);				
				}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return streetAndCity;
	}
	
	private String getOrganizationName()	
	{	
		String nameTx="";
		try{
			nameTx = driver.findElement(By.xpath("//*[@id='searchResultsTable']/tbody/tr[1]/td[2]/table/tbody/tr[2]/td")).getText();
			return nameTx;
		}catch(Exception e){
			//e.printStackTrace();
		}
		return nameTx;
	}
	
	private ArrayList<String> getOrganizationStreet()
	{	
		ArrayList<String> street = new ArrayList<String>();
		int rowCount = 0;	
		
		try{
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();		
		int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr")).size();		
		
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;			
			street.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr["+rowCount+"]/td[3]")).getText());				
		}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return street;
	}

	private ArrayList<String> getOrganizationCity()	
	{			
		ArrayList<String> city=new ArrayList<String>();
		int rowCount = 0;	
		
		try{
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
		int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr")).size();
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			city.add( driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr["+rowCount+"]/td[4]")).getText());				
		}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return city;
	}
	
	private ArrayList<String> getOrganizationState()	
	{	
		ArrayList<String> stateNames=new ArrayList<String>();	
		int rowCount = 0;	
		
		try{
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
		int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr")).size();

		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			stateNames.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr["+rowCount+"]/td[5]")).getText());		
		}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return stateNames;
	}

	private ArrayList<String> getOrganizationZip()	
	{	
		ArrayList<String> zipCode=new ArrayList<String>();		
		int rowCount = 0;	
		
		try{
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
		int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr")).size();

		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			zipCode.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr["+rowCount+"]/td[6]")).getText());		
		}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return zipCode;
	}

	private ArrayList<String> getOrganizationTelephone()	
	{	
		ArrayList<String> phone=new ArrayList<String>();		
		int rowCount = 0;	
		
		try{
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
		int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Telephone']/tr")).size();
		
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			phone.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Telephone']/tr["+rowCount+"]/td[4]")).getText());				
		}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return phone;
	}
	private String getProviderName(String nameTx, String fullName){
		
		String lastName ="";
		try {
			String[] name = fullName.split(",");
			if(nameTx.equals("LastName")){
				lastName = name[0].trim();				
				return lastName;
			}else{				  
				lastName = name[1].trim();
				return lastName;
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return lastName;
	}
	
	private ArrayList<String> getProviderStreet()	
	{	
		ArrayList<String> street = new ArrayList<String>();		
		int rowCount = 0;	
		
		try{
			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();		
			int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr")).size();
			for(int i= 0; i<rowSize;i++){
				rowCount = i+1;
				street.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr["+rowCount+"]/td[3]")).getText());				
			}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return street;
	}
	
	private ArrayList<String> getProviderCity()	
	{	
		ArrayList<String> city = new ArrayList<String>();
		int rowCount = 0;	
		
		try{
			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
			int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr")).size();
			for(int i= 0; i<rowSize;i++){
				rowCount = i+1;
				city.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr["+rowCount+"]/td[4]")).getText());				
			}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return city;
	}
	
	private ArrayList<String> getProviderState()	
	{	
		ArrayList<String> stateNames = new ArrayList<String>();
		int rowCount = 0;	
		
		try{
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
		int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr")).size();
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			stateNames.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr["+rowCount+"]/td[5]")).getText());		
		}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return stateNames;
	}
	
	private ArrayList<String> getProviderZip()	
	{	
		ArrayList<String> zipCode = new ArrayList<String>();	
		int rowCount = 0;			
		try{
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
		int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr")).size();
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			zipCode.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Address']/tr["+rowCount+"]/td[6]")).getText());		
		}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return zipCode;
	}
	
	private ArrayList<String> getProviderTelephone()	
	{	
		ArrayList<String> phone = new ArrayList<String>();		
		int rowCount = 0;	
		
		try{
		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
		int rowSize = driver.findElements(By.xpath("//*[@id='nestedElementsHistoryBox|Telephone']/tr")).size();
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			phone.add(driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Telephone']/tr["+rowCount+"]/td[4]")).getText());				
		}		
		}catch(Exception e){
			//e.printStackTrace();
		}
		return phone;
	}
	
	private String getPlaceName()	
	{	
		
		String nameTx="";
		String[] name;
		//try{			
		nameTx = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[1]/td[2]")).getText();												
		name = nameTx.split("\n");									  
		if(name.length > 1){
			return name[1];	
		}else{
			return name[0];
		}
	}
	
	private ArrayList<String> getPlaceStreet()	
	{	
		ArrayList<String> street = new ArrayList<String>();	
		int rowCount = 0;	

		driver.findElement(By.xpath("//table[@id='parent']/tbody/tr/td[1]/a")).click();			
		int rowSize = driver.findElements(By.xpath("//table[@id='tblAddresses']/tbody/tr")).size();			
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			street.add(driver.findElement(By.xpath("//table[@id='tblAddresses']/tbody/tr["+rowCount+"]/td[5]")).getText());
		}
		return street;
	}
	
	private ArrayList<String> getPlaceCity()	
	{	
		ArrayList<String> city = new ArrayList<String>();		
		int rowCount = 0;	
				
		try{
			driver.findElement(By.xpath("//table[@id='parent']/tbody/tr/td[1]/a")).click();
			int rowSize = driver.findElements(By.xpath("//table[@id='tblAddresses']/tbody/tr")).size();
			for(int i= 0; i<rowSize;i++){
				rowCount = i+1;
				city.add(driver.findElement(By.xpath("//table[@id='tblAddresses']/tbody/tr["+rowCount+"]/td[7]")).getText());
			}		
		}catch(Exception e){
			//e.printStackTrace();
		}			
		return city;
	}
	private ArrayList<String> getPlaceState()	
	{	
		ArrayList<String> stateNames = new ArrayList<String>();		
		int rowCount = 0;	
		
		driver.findElement(By.xpath("//table[@id='parent']/tbody/tr/td[1]/a")).click();
		int rowSize = driver.findElements(By.xpath("//table[@id='tblAddresses']/tbody/tr")).size();
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			stateNames.add(driver.findElement(By.xpath("//table[@id='tblAddresses']/tbody/tr["+rowCount+"]/td[8]")).getText());
		}
		return stateNames;
	}
	
	private ArrayList<String> getPlaceZip()	
	{	
		ArrayList<String> ZipCodes = new ArrayList<String>();		
		int rowCount = 0;	
		
		driver.findElement(By.xpath("//table[@id='parent']/tbody/tr/td[1]/a")).click();
		int rowSize = driver.findElements(By.xpath("//table[@id='tblAddresses']/tbody/tr")).size();
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			ZipCodes.add(driver.findElement(By.xpath("//table[@id='tblAddresses']/tbody/tr["+rowCount+"]/td[9]")).getText());
		}
		return ZipCodes;
	}
	
	private ArrayList<String> getPlacePhone()	
	{	
		ArrayList<String> Phone = new ArrayList<String>();		
		int rowCount = 0;	
		
		driver.findElement(By.xpath("//table[@id='parent']/tbody/tr/td[1]/a")).click();
		int rowSize = driver.findElements(By.xpath("//table[@id='tblPhones']/tbody/tr")).size();
		for(int i= 0; i<rowSize;i++){
			rowCount = i+1;
			Phone.add(driver.findElement(By.xpath("//table[@id='tblPhones']/tbody/tr["+rowCount+"]/td[6]")).getText());
		}
		return Phone;
	}

	public void  validateName(String conditionTx, String searchText , String fieldName, String entityTx) 
	{	
		String LName ="";
		try{	
			switch(entityTx){
			case  "Patient":
			String Patientname = "";
			Patientname = driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[2]")).getText();												  
			LName = getPatientName(fieldName,Patientname);
			break;
			case  "Organization":				
					LName = getOrganizationName();
					break;
			case  "Provider":	
				String nameTx = "";				
				nameTx = driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td[2]/table/tbody/tr[2]/td")).getText();
				LName = getProviderName(fieldName,nameTx);
				break;
			case  "Place":
				LName = getPlaceName();	
				break;
			}
					switch (conditionTx) {
					case  "Starts With":				
						check_StartWith(LName,searchText,fieldName);
						break;
					case  "Contains":				
						check_Contain(LName,searchText,fieldName);
						break;
					case  "Equal":
						check_Equal(LName,searchText,fieldName);
						break;
					case  "Not Equal":
						check_NotEqual(LName,searchText,fieldName);
						break;
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
	}

	public void validateStreet(String conditionTx,String streetText,String fldName, String entityTx)
	{		
		ArrayList<String> streetNames = new ArrayList<String>();
		try{			
			switch(entityTx){
			case  "Patient":
				streetNames = getPatientStreet();
			break;
			case  "Organization":				
				streetNames = getOrganizationStreet();				
				break;
			case  "Provider":
				streetNames = getProviderStreet();
				break;
			case  "Place":
				streetNames = getPlaceStreet();
				break;	
			}
			
			switch (conditionTx) {		
			case  "Contains":
				check_ContainArray(streetNames,streetText,fldName);
				break;
			case  "Equal":
				check_EqualArray(streetNames,streetText,fldName);
				break;		
			case  "Not Equal":
				check_NotEqualArray(streetNames,streetText,fldName);	
				break;
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
	}

	public void validateCity(String conditionTx,String cityText,String fldName, String entityTx)
	{
		ArrayList<String> cityNames = new ArrayList<String>();
		try{
			switch(entityTx){
			case  "Patient":		
				cityNames = getPatientCity();
				break;
			
			case  "Organization":		
				cityNames = getOrganizationCity();
				break;
			case  "Provider":
				cityNames = getProviderCity();
				break;
			case  "Place":
				cityNames = getPlaceCity();
				break;				
			}
					switch (conditionTx) {		
					case  "Contains":
						check_ContainArray(cityNames,cityText,fldName);
						break;
					case  "Equal":
						check_EqualArray(cityNames,cityText,fldName);
						break;
		
					case  "Not Equal":
						check_NotEqualArray(cityNames,cityText,fldName);	
						break;
				}
		}catch(Exception e){
			//e.printStackTrace();
		}
	}

	public void validateStateName(String conditionTx,String streetText,String fldName, String entityTx)
	{		
		ArrayList<String> states = new ArrayList<String>();
		try{
			switch(entityTx){
			case  "Patient":
				states = getPatientState();			
				//check_EqualArray(states,streetText,fldName);
				break;			
			case  "Organization":
					states = getOrganizationState();
					break;
			case  "Provider":
				states = getProviderState();
				break;
			case  "Place":				
				states = getPlaceState();
				break;				
			}			
			check_EqualArray(states,streetText,fldName);
		}catch(Exception e){
			//e.printStackTrace();
		}
	}

	public void validatePhone(String conditionTx,String streetText,String fldName, String entityTx)
	{	ArrayList<String> phoneNumbers = new ArrayList<String>();	
		try{
			switch(entityTx){
			case  "Organization":
					phoneNumbers = getOrganizationTelephone();			
					check_EqualArray(phoneNumbers,streetText,fldName);
					break;					
			case  "Provider":
				phoneNumbers = getProviderTelephone();			
				check_EqualArray(phoneNumbers,streetText,fldName);
				break;	
			case  "Place":
				phoneNumbers = getPlacePhone();			
				check_EqualArray(phoneNumbers,streetText,fldName);
				break;
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
	}

	public void validateZipcode(String conditionTx,String streetText,String fldName, String entityTx)
	{	
		ArrayList<String> zipCodes = new ArrayList<String>();
		try{
			switch(entityTx){
			case  "Patient":
				zipCodes = getPatientZip();			
				check_EqualArray(zipCodes,streetText,fldName);	
				break;			
			case  "Organization":
				zipCodes = getOrganizationZip();			
				check_EqualArray(zipCodes,streetText,fldName);	
			break;			
			case  "Provider":
				zipCodes = getProviderZip();			
				check_EqualArray(zipCodes,streetText,fldName);	
			break;			
			case  "Place":
				zipCodes = getPlaceZip();			
				check_EqualArray(zipCodes,streetText,fldName);	
			break;
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
	}
	
	public void validatePatientID(String conditionTx,String streetText,String fldName, String entityTx)
	{		
		String patientId = "";
		try{
			switch(entityTx){
			case  "Patient":
				patientId = getPatientId();			
					check_Equal(patientId,streetText,fldName);
					break;
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
	}
	
	public void validateDOB_Equal(String conditionTx, String searchText, String fieldName)
	{	
		try{
			conditionTx = conditionTx.trim();
			String dtOfBirth= "";			
			dtOfBirth = getPatientDOB();				
			check_Equal(dtOfBirth,searchText,fieldName);			
			}catch(Exception e){
				//e.printStackTrace();
			}		
	}
	
	public void validateDOB_Betweeen(String dob1,String dob2) throws IOException
	{	
		System.out.println("\nBetween Condition:");
		String dtOfBirth ="";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try{
			Date date1 = sdf.parse(dob1);
			Date date2 = sdf.parse(dob2);
			
			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr[1]/td[1]/a")).click();
			driver.findElement(By.id("tabs0head2")).click();
			dtOfBirth = driver.findElement(By.id("NBS106")).getText();
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
			//e.printStackTrace();
		}		   
	}
	
	public void validatePatientStreetAndCity(String conditionTx, String streetText,String cityText, String fldCombination)
	{	
			try{
				switch(fldCombination){
				case  "StreetAndCity":
					Hashtable<String, String> StreetAndCityResult = getPatientStreetAndCity();						
					check_ContainHashTable(StreetAndCityResult,streetText,cityText);
					break;
				default:
					break;
				}
			
		}catch(Exception e){
			//e.printStackTrace();
		}
	}
	
	public void validateID_Type(String conditionTx, String searchText, String fieldName)//not completed
	{	
		try{
			/*conditionTx = conditionTx.trim();
			String dtOfBirth= "";			
			dtOfBirth = getPatientDOB();				
			check_Equal(dtOfBirth,searchText,fieldName);*/			
			}catch(Exception e){
				//e.printStackTrace();
			}		
	}
	
	public String[] getIdTypeAndValue(String type, String value){//not completed
		String[] typeAndValue = new String[2];
		driver.findElement(By.xpath("//*[@id='searchResultsTable']/tbody/tr[1]/td[1]/table/tbody/tr/td/a")).click();
		typeAndValue[0] = driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Identification']/tr/td[2]")).getText();
		typeAndValue[1] = driver.findElement(By.xpath("//*[@id='nestedElementsHistoryBox|Identification']/tr/td[4]")).getText();
		
				//*[@id='nestedElementsHistoryBox|Identification']/tr/td[2]
		return typeAndValue;
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
			log.writeSubSection(  fldName+ " does NOT contian search text  --  Fail","",false);
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
			log.writeSubSection(fldName+ " is NOT equal to search text  --  Pass","",true);
		}else{
			System.out.println(  fldName+ " is equal to search text  --  Fail");
			log.writeSubSection(  fldName+ " is equal to search text  --  Fail","",false);
		}   
	}
	
	private void check_EqualArray(ArrayList<String> data, String searchTx, String fieldName) throws IOException
	{	
		boolean found = false;		
		int numberOfNames = data.size();		
		String actualData = "";
		int i;
		System.out.println("\n'Equal' Condition: ");
		log.writeSubSection("\n'Equal' Condition: ");
		for(i =0; i<numberOfNames; i++)	{	
			if( (data.get(i).toLowerCase().equals(searchTx.trim().toLowerCase()))  )
			{	
				found = true;				
				actualData = data.get(i);			
				break;
			}else{								
			} 			
		}
		System.out.println("Actual Data: " +data.get(i)+ "    Should 'Equal': "+searchTx);
		log.writeSubSection("Actual Data: " +data.get(i)+ "    Should 'Equal': "+searchTx);
		if(found){
			System.out.println(fieldName+" is equal to search text  --  Pass");
			log.writeSubSection(fieldName+ " is equal to search text  --  Pass","",true);
		}else{
			System.out.println(  fieldName+ " is NOT equal to search text  --  Fail");
			log.writeSubSection(  fieldName+ " is NOT equal to search text  --  Fail","",false);
		}
	}

	private void check_ContainArray(ArrayList<String> data, String searchTx, String fieldName) throws IOException
	{			
		System.out.println("Contain Condition: ");
		log.writeSubSection("Contain Condition: ");
		boolean found = false;		
		int numberOfNames = data.size();		
		String actualData = "";
		int i;
		for(i =0; i<numberOfNames; i++)		{	
			if( (data.get(i).toLowerCase().contains(searchTx.trim().toLowerCase()))  )
			{	
				found = true;
				actualData = data.get(i);						
				break;
			}else{

			}   
		}
		System.out.println("Actual Data: " +data.get(i)+ "    Should 'Contain': "+searchTx);
		log.writeSubSection("Actual Data: " +data.get(i)+ "    Should 'Contain': "+searchTx);
		if(found){
			System.out.println(fieldName+" contains search text  --  Pass");
			log.writeSubSection(fieldName+ " contains search text  --  Pass","",true);
		}else{
			System.out.println(  fieldName+ " NOT contains search text  --  Fail");
			log.writeSubSection(  fieldName+ " is NOT equal to search text  --  Fail","",false);
		}
	}

	private void check_NotEqualArray (ArrayList<String> data, String searchTx, String fieldName) throws IOException
	{	
		System.out.println("\nNot Equal Condition:");
		log.writeSubSection("\nNot Equal Condition:");
		int numberOfNames = data.size();
		String actualData = "";
		boolean found = false;
		int i;
		for(i =0; i<numberOfNames; i++)
		{	
			if( !(data.get(i).toLowerCase().equals(searchTx.trim().toLowerCase()))  )
			{	
				found = true;
				actualData = data.get(i);
				break;
			}else{

			}   
		}
		System.out.println("Actual Data: " +data.get(i)+ "    Should 'NOT Equal': "+searchTx);
		log.writeSubSection("Actual Data: " +data.get(i)+ "    Should 'NOT Equal': "+searchTx);
		if(found){
			System.out.println(fieldName+" is NOT equal to search text  --  Pass");
			log.writeSubSection(fieldName+ " is NOT equal to search text  --  Pass","",true);
			
		}else{
			System.out.println(  fieldName+ " is equal to search text  --  Fail");
			log.writeSubSection(  fieldName+ " is equal to search text  --  Fail","", false);
		}
	}
	
	private void check_ContainHashTable(Hashtable<String, String> data, String fieldData1, String fieldData2) throws IOException//not yet completed
	{			
		System.out.println("Contain Condition: ");
		log.writeSubSection("Contain Condition: ");
	      Enumeration names;
	      String data1 ="";
	      String data2 = "";
	      boolean found = false;

	      names = data.keys();
	      while(names.hasMoreElements()) {
	    	  data1 = (String) names.nextElement();
	         data2 = data.get(data1);
	          if(data1.toLowerCase().contains(fieldData1.toLowerCase()) && data2.toLowerCase().contains(fieldData2.toLowerCase())){
	        	  found = true;	 
	        	  System.out.println("Actual Data: " + data1 +"   "+data2);
	    	      System.out.println("Expected Data: " + fieldData1 +"   "+fieldData2);
	    	      break;
	          }	         
	      }
	      
		if(found){
			System.out.println("The data displayed is correct  --  Pass");
			log.writeSubSection(" is equal to search text  --  Pass","",true);
			
		}else{
			System.out.println(" NOT contains search text  --  Fail");
			log.writeSubSection(" is NOT equal to search text  --  Fail","",false);
		}
	}
}