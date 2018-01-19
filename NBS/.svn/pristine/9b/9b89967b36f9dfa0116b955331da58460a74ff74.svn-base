/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package seleniumtest2;

/**
 *
 * @author SHUJATH ALI SYED
 */

import com.google.common.base.Function;
import java.awt.SecondaryLoop;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Seleniumtest2 {

    /**
     * @param args the command line arguments
     */
    
    
        
    public static void main(String[] args) {
        // TODO code application logic here
        //<editor-fold desc="VARIABLES">
        //VARIABLES SETUP
        DateFormat DateLastName=new SimpleDateFormat("MM/dd/yyyy");
        DateFormat DateFirstName=new SimpleDateFormat("HH:mm:ss");
        DateFormat DateDOB=new SimpleDateFormat("MM/dd/yyyy");
        String DOBString="11/25/1980";
        String UserName="pks";
        String Password="";
        String MiddleNameString="A";
        String StreetAddress1String="2 Jimmy Carter Blvd";
        String StreetAddress2String="Unit 1405";
        String CityString="Atlanta";
        String StateString="Georgia";
        String ZipString="30340";
        String CountyString="Dekalb";
        String CountryString="United States";
        String CellPhoneString="4041234567";
        String EmailString="abc@cba.com";
        String EthnicityString="Not Hispanic or Latino";
        String RaceString="americanIndianRace";
        String ConditionString="African Tick Bite Fever";
        String InvestigatorCodeString="54";
        String TransmissionModeString="Airborne";
        String DetectionMethodString="Routine Physical";
        String CaseStatusString="Confirmed";
        String ImmediateConditionString="No";
        String GeneralCommentsString="Selenium Automated Testing - New Investigation";
        String ReportingFacilityCodeString="ORG222";
        String OrderingProviderCodeString="PVD111";
        String ProgramAreaString="GCD";
        String ResultedTestString="Acid-Fast Stain";
        String MorbidityReportDateString="11/25/2013";
        
        //</editor-fold>
       
        
        //<editor-fold desc="BROWSER SETTING">
        //BROWSER SETTING - FIREFOX
        /*
        FirefoxProfile profile=new FirefoxProfile();
        WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("C:/Users/SyedS/AppData/Local/Mozilla Firefox/firefox.exe")),profile);
        */
         
        //BROWSER SETTING - INTERNET EXPLORER
        File file = new File("C:/Users/SyedS/Documents/NetBeansProjects/seleniumtest2/IEDriverServer.exe");
        System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
        
        //BROWSER CAPABILITIES
        DesiredCapabilities capabilities=new DesiredCapabilities();
        capabilities.isJavascriptEnabled();

        //BROWSER DRIVER SETUP
        WebDriver driver=new InternetExplorerDriver();
        driver.get("http://nedss-tstappsql:7001/nbs/login");
        System.out.println("Page title is: " + driver.getTitle());
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        //</editor-fold>
        
        //<editor-fold desc="LOGIN">
        //LOGIN CREDENTIALS
        WebElement EmailIDelement = driver.findElement(By.name("UserName"));
        driver.manage().timeouts().setScriptTimeout(100,SECONDS);
        EmailIDelement.sendKeys(UserName);
        WebElement Passwordelement=driver.findElement(By.id("id_Password"));
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        Passwordelement.sendKeys(Password);
        driver.findElement(By.id("id_Submit_top_ToolbarButtonGraphic")).click();
        //</editor-fold>
        
        //<editor-fold desc="PATIENT SEARCH">
        //PATIENT SEARCH - NAME
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Calendar cal=Calendar.getInstance();
        String LastNameString=DateLastName.format(cal.getTime());
        String FirstNameString=DateFirstName.format(cal.getTime());
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement LastNameelement=driver.findElement(By.id("DEM102"));
        LastNameelement.sendKeys(LastNameString);
        WebElement FirstNameelement=driver.findElement(By.id("DEM104"));
        FirstNameelement.sendKeys(FirstNameString);
        
        //PATIENT SEARCH - DATE OF BIRTH
        WebElement DOBelement=driver.findElement(By.id("DEM115"));
        DOBelement.sendKeys(DOBString);
        
        //PATIENT SEARCH - CURRENT SEX
        Random rand=new Random(); 
        int x=rand.nextInt(1); 
        String CurrentSexString=null;
        if(x==0){
            CurrentSexString="Male";
        }else{
            CurrentSexString="Female";
        }
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement CurrentSexelement=driver.findElement(By.name("DEM114_textbox"));
        CurrentSexelement.sendKeys(CurrentSexString);
        
        //SEARCH BUTTON
        driver.findElement(By.xpath("//input[@value='Search']")).click();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        //</editor-fold>
        
        //<editor-fold desc="NEW PATIENT DETAILS">
        //ADD NEW PATIENT BUTTON
        driver.findElement(By.xpath("//input[@value='Add New']")).click();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        
        //ADD PATIENT - MIDDLE NAME
        WebElement MiddleNameelement=driver.findElement(By.id("DEM105"));
        MiddleNameelement.sendKeys(MiddleNameString);
        
        //ADD PATIENT - PATIENT DECEASED
        Random rand1=new Random(); 
        int x1=rand1.nextInt(1); 
        String PatientDeceasedString=null;
        if(x==0){
            PatientDeceasedString="Yes";
        }else{
            PatientDeceasedString="No";
        }
        WebElement PatientDeceasedelement=driver.findElement(By.name("DEM127_textbox"));
        PatientDeceasedelement.sendKeys(PatientDeceasedString);
        
        //ADD PATIENT - MARITAL STATUS
        Random rand2=new Random(); 
        int x2=rand2.nextInt(1); 
        String MaritalStatusString=null;
        if(x==0){
            MaritalStatusString="Married";
        }else{
            MaritalStatusString="Single, never married";
        }
        WebElement MaritalStatuselement=driver.findElement(By.name("DEM140_textbox"));
        driver.findElement(By.name("DEM140_textbox")).clear();
        MaritalStatuselement.sendKeys(MaritalStatusString);
        
        //ADD PATIENT - ADDRESS
        WebElement StreetAddress1element=driver.findElement(By.id("DEM159"));
        StreetAddress1element.sendKeys(StreetAddress1String);
        WebElement StreetAddress2element=driver.findElement(By.id("DEM160"));
        StreetAddress2element.sendKeys(StreetAddress2String);
        WebElement Cityelement=driver.findElement(By.id("DEM161"));
        Cityelement.sendKeys(CityString);
        WebElement Stateelement=driver.findElement(By.name("DEM162_textbox"));
        driver.findElement(By.name("DEM162_textbox")).clear();
        Stateelement.sendKeys(StateString);
        WebElement Zipelement=driver.findElement(By.id("DEM163"));
        Zipelement.sendKeys(ZipString);
        WebElement Countyelement=driver.findElement(By.name("DEM165_textbox"));
        driver.findElement(By.name("DEM165_textbox")).clear();
        Countyelement.sendKeys(CountyString);
        WebElement Countryelement=driver.findElement(By.name("DEM167_textbox"));
        driver.findElement(By.name("DEM167_textbox")).clear();
        Countryelement.sendKeys(CountryString);
        
        //ADD PATIENT - CELL PHONE
        WebElement CellPhoneelement=driver.findElement(By.id("NBS006"));
        CellPhoneelement.sendKeys(CellPhoneString);
        
        //ADD PATIENT - EMAIL
        WebElement Emailelement=driver.findElement(By.id("DEM182"));
        Emailelement.sendKeys(EmailString);
        
        //ADD PATIENT - ETHNICITY
        WebElement Ethnicityelement=driver.findElement(By.name("DEM155_textbox"));
        driver.findElement(By.name("DEM155_textbox")).clear();
        Ethnicityelement.sendKeys(EthnicityString);
        
        //ADD PATIENT - RACE
        driver.findElement(By.id(RaceString)).click(); 
        
        //SUBMIT
        driver.findElement(By.id("Submit")).click(); 
        //</editor-fold>
        
        //EVENTS TAB
        driver.findElement(By.id("tabs0head1")).click();
        
        //<editor-fold desc="NEW INVESTIGATION">
        //ADD NEW INVESTIGATION BUTTON
        driver.findElement(By.xpath(".//input[contains(@onclick, 'AddInvestigation')]")).click();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        
        //ADD INVESTIGATION - CONDITION
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement Conditionelement=driver.findElement(By.name("ccd_textbox"));
        driver.findElement(By.name("ccd_textbox")).clear();
        Conditionelement.sendKeys(ConditionString);
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        
        //SUBMIT
        driver.findElement(By.id("Submit")).click();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        
        //CASE INFO TAB
        driver.findElement(By.id("tabs0head1")).click();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        
        //CASE INFO - INVESTIGATOR CODE LOOKUP
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement InvestigatorCodeelement=driver.findElement(By.id("INV180Text"));
        driver.findElement(By.id("INV180Text")).clear();
        InvestigatorCodeelement.sendKeys(InvestigatorCodeString);
        driver.findElement(By.id("INV180CodeLookupButton")).click();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        
        //CASE INFO - TRANSMISSION MODE
        WebElement TransmissionModeelement=driver.findElement(By.name("INV157_textbox"));
        driver.findElement(By.name("INV157_textbox")).clear();
        TransmissionModeelement.sendKeys(TransmissionModeString);
        
        //CASE INFO - DETECTION METHOD
        WebElement DetectionMethodelement=driver.findElement(By.name("INV159_textbox"));
        driver.findElement(By.name("INV159_textbox")).clear();
        DetectionMethodelement.sendKeys(DetectionMethodString);
        
        //CASE INFO - CASE STATUS
        WebElement CaseStatuselement=driver.findElement(By.name("INV163_textbox"));
        driver.findElement(By.name("INV163_textbox")).clear();
        CaseStatuselement.sendKeys(CaseStatusString);
        
        //CASE INFO - IMMEDIATE NATIONAL NOTIFIABLE CONDITION
        WebElement ImmediateConditionelement=driver.findElement(By.name("NOT120_textbox"));
        driver.findElement(By.name("NOT120_textbox")).clear();
        ImmediateConditionelement.sendKeys(ImmediateConditionString);
        
        //CASE INFO - GENERAL COMMENTS
        WebElement GeneralCommentselement=driver.findElement(By.id("INV167"));
        driver.findElement(By.id("INV167")).clear();
        GeneralCommentselement.sendKeys(GeneralCommentsString);
        
        //SUBMIT
        driver.findElement(By.id("Submit")).click(); 
        
        //RETURN TO EVENTS TAB
        driver.findElement(By.linkText("Return To File: Events")).click();
        //</editor-fold>
        
        //<editor-fold desc="NEW MORBIDITY REPORT">
        /*
        //ADD NEW MORBIDITY REPORT BUTTON
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        driver.findElement(By.xpath(".//input[contains(@onclick, 'AddMorb')]")).click();
        
        //ADD MORBIDITY REPORT - CONDITION
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement Condition2element=driver.findElement(By.name("conditionCd_textbox"));
        driver.findElement(By.name("conditionCd_textbox")).clear();
        Condition2element.sendKeys(ConditionString);
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        
        //ADD MORBIDITY REPORT - DATE OF BIRTH
        WebElement MorbidityReportDateelement=driver.findElement(By.id("morbidityReport.theObservationDT.activityToTime_s"));
        MorbidityReportDateelement.sendKeys(MorbidityReportDateString);
        
        //ADD MORBIDITY REPORT - REPORTING FACILITY CODE LOOKUP
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement ReportingFacilityCodeelement=driver.findElement(By.name("entity-codeLookupText-Org-ReportingOrganizationUID"));
        driver.findElement(By.name("entity-codeLookupText-Org-ReportingOrganizationUID")).clear();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        ReportingFacilityCodeelement.sendKeys(ReportingFacilityCodeString);
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        ReportingFacilityCodeelement.sendKeys(Keys.chord(Keys.TAB,Keys.RETURN));
        //driver.findElement(By.xpath(".//input[contains(@onclick, 'codeLookup(this,'entity-table-Org-ReportingOrganizationUID','organizationReportLab')')]")).click();
        //</editor-fold>
        */
        
        //<editor-fold desc="NEW LAB REPORT">
        
        //ADD NEW LAB REPORT BUTTON
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        driver.findElement(By.xpath(".//input[contains(@onclick, 'AddLab')]")).click();
        
        /*
        //ADD LAB REPORT - REPORTING FACILITY CODE LOOKUP
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement ReportingFacilityCodeelement=driver.findElement(By.name("entity-codeLookupText-Org-ReportingOrganizationUID"));
        driver.findElement(By.name("entity-codeLookupText-Org-ReportingOrganizationUID")).clear();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        ReportingFacilityCodeelement.sendKeys(ReportingFacilityCodeString);
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        ReportingFacilityCodeelement.sendKeys(Keys.chord(Keys.TAB,Keys.RETURN));
        //driver.findElement(By.xpath(".//input[contains(@onclick, 'codeLookup(this,'entity-table-Org-ReportingOrganizationUID','organizationReportLab')')]")).click();
        */
        
        //MARKING ORDERING FACILITY SAME AS REPORTING FACILITY
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.name("LAB277")).click();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        
        driver.findElement(By.name("resultedTest[i].theIsolateVO.theObservationDT.cdDescTxt_textbox")).click();
        new Select(driver.findElement(By.name("resultedTest[i].theIsolateVO.theObservationDT.cdDescTxt_textbox"))).selectByVisibleText(ResultedTestString);
        /*
        //ADD LAB REPORT - ORDERING PROVIDER CODE LOOKUP
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement OrderingProviderCodeelement=driver.findElement(By.name("entity-codeLookupText-Prov-entity.entityProvUID"));
        driver.findElement(By.name("entity-codeLookupText-Prov-entity.entityProvUID")).clear();
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        OrderingProviderCodeelement.sendKeys(OrderingProviderCodeString);
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        ReportingFacilityCodeelement.sendKeys(Keys.chord(Keys.TAB,Keys.RETURN));
        //driver.findElement(By.xpath(".//input[contains(@onclick, 'codeLookup(this,'entity-table-Prov-entity.entityProvUID','provider')')]")).click();
        
        
        //ADD LAB REPORT - PROGRAM AREA
       // Select ProgramAreaSelect=new Select(driver.findElement(By.xpath("//input[contains(@name='progAreaCd_textbox'])")));
        //ProgramAreaSelect.selectByValue(ProgramAreaString);
        WebElement ProgramAreaelement=driver.findElement(By.name("proxy.observationVO_s[0].theObservationDT.progAreaCd_textbox"));
        driver.findElement(By.name("proxy.observationVO_s[0].theObservationDT.progAreaCd_textbox")).clear();
        ProgramAreaelement.sendKeys(ProgramAreaString);
        
        WebElement ResultedTestelement=driver.findElement(By.name("resultedTest[i].theIsolateVO.theObservationDT.cdDescTxt_textbox"));
        ResultedTestelement.sendKeys(ResultedTestString);
        
        //Select ResultedTestelement=new Select(driver.findElement(By.name("resultedTest[i].theIsolateVO.theObservationDT.cdDescTxt_textbox")));
        //ResultedTestelement.selectByValue(ResultedTestString);
        */
        //</editor-fold>
        
        //<editor-fold desc="EXTRAS">
        //EXTRAS
        /*
        
        Iterator<String> itr = DataSetArray.iterator();        
        while(itr.hasNext()){            
            System.out.println(itr.next());        
        }
        
        String SampleString = DataSetArray.get(3).toString();
        
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
       .withTimeout(30, SECONDS)
       .pollingEvery(5, SECONDS)
       .ignoring(NoSuchElementException.class);

        WebElement EmailIDelement = wait.until(new Function<WebDriver, WebElement>() {
        public WebElement apply(WebDriver driver) {
        return driver.findElement(By.id("id_UserName"));
        }
        });
        driver.switchTo().frame(driver.findElement(By.xpath(".//*[@name='UserName']")));
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        WebElement EmailIDelement = driver.findElement(By.name("UserName"));
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement EmailIDelement = wait.until(ExpectedConditions.elementToBeClickable(By.name("UserName")));
        WebElement EmailIDelement=driver.findElement(By.xpath("//input[@id='id_UserName']"));
        driver.manage().timeouts().setScriptTimeout(100, SECONDS);
        driver.findElement(By.linkText("Data Entry")).click();
        driver.findElement(By.linkText("Patient")).click();
        WebElement LastNameelement=driver.findElement(By.id("DEM102"));
        LastNameelement.sendKeys("bond");
        driver.findElement(By.xpath("//input[@value='Submit']")).click();
        driver.manage().timeouts().pageLoadTimeout(100, SECONDS);
        driver.findElement(By.linkText("518005")).click();
        driver.findElement(By.id("tabs0head2")).click();
        driver.navigate().refresh();
        driver.quit();
        */
        //</editor-fold>
    }
    
   
}
