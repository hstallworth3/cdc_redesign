package com.nbs.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
//import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxBinary;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxProfile;
//import org.openqa.selenium.ie.InternetExplorerDriver;

import com.rtts.utilities.Log;
//import com.thoughtworks.selenium.webdriven.commands.IsVisible;

public class NBSUtils {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private WebElement element, element2;
	private Log log;// = new Log("ELRActivityLog", "C:\\Selenium\\Logs\\");
	private String database;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	@Before
	public void setUp() throws Exception {

		// driver = new FirefoxDriver();
		// IE
		// System.setProperty("webdriver.ie.driver",
		// "C:\\Selenium\\IEDriverServer_x64_2.52.0\\IEDriverServer.exe");
		// driver = new InternetExplorerDriver();
		// IE
		// System.setProperty("webdriver.ie.driver",
		// "C:\\Selenium\\IEDriverServer_xg4_2.40\\IEDriverServer.exe");
		// driver = new InternetExplorerDriver();

		baseUrl = "http://nedss-tstappsql:7001/";
		// driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void setLog(Log log) {

		this.log = log;
	}

	public void setDriver(WebDriver driver) {

		this.driver = driver;
	}

	/**
	 * logInNBS(): it logs into the system as the user pks
	 */

	public void logInNBS(String baseUrl) {
		driver.manage().window().maximize();
		driver.get(baseUrl + "/nbs/login");
		// driver.findElement(By.id("id_UserName")).clear();
		driver.findElement(By.id("id_UserName")).sendKeys("pks");
		driver.findElement(By.id("id_Submit_top_ToolbarButtonGraphic")).click();
	}

	public void readActivityLog(int nrows) {

		ArrayList<String> innerStatus = new ArrayList<>();
		String obsID = "";
		int loopVar;
		int i = 1;
		int j = 1;
		int fail = 0;
		int total = 1;
		int queueSize = 20;
		// int nrows = 15; //get from the table Results 10 of 10
		try {
			for (total = 1; total <= nrows; total++) {

				driver.findElement(By.linkText("System Management")).click();
				driver.findElement(
						By.cssSelector("#systemAdmin4 > thead > tr > th > a.toggleIconHref > img[alt=\"Maximize\"]"))
						.click();
				driver.findElement(By.linkText("Manage ELR Activity Log")).click();
				// driver.findElement(By.id("from_date")).clear();
				// driver.findElement(By.id("from_date")).sendKeys("03/03/2016");
				// driver.findElement(By.id("to_date")).clear();
				// driver.findElement(By.id("to_date")).sendKeys("03/04/2016");
				driver.findElement(By.id("searchButton")).click();

				if (total > queueSize) {
					int pagina = (total / queueSize) + 1;

					for (int l = 1; l < pagina; l++)
						driver.findElement(By.linkText("Next")).click();

					i = (total % queueSize);

				} else
					i = total;
				fail = 0;
				element = driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + i + "]/td[2]"));
				element2 = driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + i + "]/td[10]"));
				String element2Text = element2.getText();
				obsID = getObsID(driver, i);
				if (element.getText().equalsIgnoreCase("Failure")) {
					logFailureDetails(driver, log, i, obsID);
					// return to activity log
					driver.findElement(By.cssSelector("a.backToTopLink")).click();
				} else if (element2Text.contains("Documents Requiring Security Assignment")) {
					String msg = "was logged in Documents Requiring Security Assignment queue";
					logDetails(driver, log, i, obsID, msg);
					// return to activity log
					driver.findElement(By.cssSelector("a.backToTopLink")).click();
				} else if (element2Text.contains("Documents Requiring Review")) {
					String msg = "was logged in Documents Requiring Review queue";
					logDetails(driver, log, i, obsID, msg);
					// return to activity log
					driver.findElement(By.cssSelector("a.backToTopLink")).click();
				} else if ((element.getText().equalsIgnoreCase("Success"))) {
					driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + i + "]/td/a")).click();
					loopVar = getLoopVar(driver); // variable to be used inside
													// the second loop
					innerStatus = new ArrayList<>();
					// method to check for any failures inside
					int numOfFails = checkForFail(j, loopVar, fail, innerStatus);

					// logging activity in log file
					logSuccessDetails(driver, log, i, numOfFails, obsID, element2Text);

					// Verify the Lab Report
					// testOpenLabReport(obsID);

				}
			}

		} catch (NoSuchElementException e) {
			System.out.println("no such element exception thrown:");
			e.printStackTrace();

		} catch (Exception e) {
			System.out.println("no such element exception thrown:");
			e.printStackTrace();
		}

	}

	@Test
	public void testGetObsId() throws Exception {
		logInNBS(baseUrl);
		readActivityLog(15);
	}

	private boolean isElementDisplayed(By by) {
		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS);
			if (driver.findElement(by).isDisplayed() && driver.findElement(by).isEnabled())
				// if( driver.findElement(by).isDisplayed())
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		log.close();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	private int checkForFail(int j, int loopVar, int fail, ArrayList<String> innerStatus) {
		for (j = 1; j <= loopVar; j++) {
			element = driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + j + "]/td"));
			innerStatus.add(element.getText());
			if (element.getText() != null && element.getText().contains("Failure"))
				fail++;
		}
		return fail;
	}

	private void logFailureDetails(WebDriver driver, Log log, int i, String obsID) throws IOException {
		driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + i + "]/td/a")).click();
		log.write("ELR " + obsID + ": Import process failed, details shown below", "", false);
		log.takeSnapshot(driver);
	}

	private void logDetails(WebDriver driver, Log log, int i, String obsID, String msg) throws IOException {

		String name = driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + i + "]/td[7]")).getText();

		driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + i + "]/td/a")).click();
		log.write("ELR " + obsID + ": " + msg + " , details shown below", "", true);

		if (name.contains("NotImported"))
			log.write("It was expected not to be imported", "", true);
		else
			log.write("It was expected to be imported", "", false);

		log.takeSnapshot(driver);
	}

	private void logSuccessDetails(WebDriver driver, Log log, int i, int numOfFails, String obsID, String text)
			throws IOException {
		String textPatientName = driver.findElement(By.id("bd")).getText();
		String patientName = textPatientName.substring(textPatientName.indexOf("Patient Name"));
		String name = patientName.substring(0, patientName.indexOf("\n"));

		if (numOfFails > 0) {
			log.write("ELR " + obsID + ": Successfully imported, but with some failures shown below", "", true);

			if (name.contains("NotImported"))
				log.write("It was expected not to be imported", "", true);
			else
				log.write("It was expected to be imported", "", false);

			log.takeSnapshot(driver);
			System.out.println(" Fail inside success");
		} else {
			log.write("ELR " + obsID + ": Successfully imported, no failures as indicated below: " + text, "", true);

			if (name.contains("NotImported"))
				log.write("It was expected not to be imported", "", false);
			else
				log.write("It was expected to be imported", "", true);

			log.takeSnapshot(driver);
			System.out.println(" success inside and out");

		}
	}

	private String getObsID(WebDriver driver, int i) {
		String id;
		id = driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + i + "]/td[8]")).getText();
		System.out.println("OBS id: " + id);
		return id;
	}

	private int getLoopVar(WebDriver driver) {
		String innerResultsText;
		int len = 0;
		int n = 0;
		char ch;
		innerResultsText = driver.findElement(By.xpath("//fieldset[@id='result']/table/tbody/tr/td/span[4]/b"))
				.getText();
		len = innerResultsText.length();
		ch = innerResultsText.charAt(len - 1);
		return n = Character.getNumericValue(ch);
	}

	/**
	 * testOpenLabReport(): it starts on home page. It verifies the data between
	 * the view lab report and the xml on the database
	 * 
	 * @param eventId:
	 *            OBSid of the lab report to search
	 * @throws Exception
	 */
	public void testOpenLabReport(String eventId) throws Exception {

		// Come back to the beginning
		driver.findElement(By.linkText("Home")).click();

		// Data to search (get from Avinav's test case)
		// String eventId = "OBS10015025GA01";
		// Get xml from database
		Database db = new Database();
		String xml = db.getXMLLabReport(database, eventId);
		// get data to validate from xml
		String name = getName(xml);
		String dob = getDob(xml);

		testCaseReadLabReport(name, dob, eventId);

	}

	public void testCaseReadLabReport(String name, String dob, String eventId) throws Exception {
		try {

			// driver.get(baseUrl + "/nbs/login");
			// driver.findElement(By.id("id_UserName")).clear();
			// driver.findElement(By.id("id_UserName")).sendKeys("pks");
			// driver.findElement(By.id("id_Submit_bottom_ToolbarButtonGraphic")).click();

			driver.findElement(By.name("ESR100_textbox")).clear();
			driver.findElement(By.name("ESR100_textbox")).sendKeys("Lab ID");
			driver.findElement(By.id("DEM229")).clear();// for losing the focus
														// on the previous one
														// so the business rule
														// works
			// driver.findElement(By.id("patientSearchByDetails")).click();

			driver.findElement(By.id("ESR101")).clear();
			driver.findElement(By.id("ESR101")).sendKeys(eventId);
			driver.findElement(By.xpath("//input[@value='Search']")).click();
			// driver.findElement(By.xpath("//input[@value='Search']")).click();

			driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/a")).click();
			driver.findElement(By.id("tabs0head1")).click();

			// Open lab report
			selectLabByEventId(eventId);

			// Read the information from the lab report
			String patientInformation = driver
					.findElement(
							By.xpath("//form[@id='nedssForm']/div/table[2]/tbody/tr/td/table/tbody/tr[3]/td/table"))
					.getText();

			String NAME = "Name: ";
			String HOME_ADDRESS = "Home Address:";
			String DOB = "DOB: ";
			String AGE = "Age:";

			String namePatient = patientInformation.substring(patientInformation.indexOf(NAME) + NAME.length(),
					patientInformation.indexOf(HOME_ADDRESS)).replace("\n", "");
			String dobPatient = patientInformation
					.substring(patientInformation.indexOf(DOB) + DOB.length(), patientInformation.indexOf(AGE)).trim();

			boolean nameFound = false;
			boolean dobFound = false;

			if (namePatient.contains(name))// contains because of lastname and
											// middle name
				nameFound = true;
			if (dobPatient.equalsIgnoreCase(dob))
				dobFound = true;

			log.write("_____________ Verifying the patient's name: ", "Expected " + name + " | Actual " + namePatient,
					nameFound);

			log.write("_____________ Verifying the patient's DOB: ", "Expected " + dob + " | Actual " + dobPatient,
					dobFound);

			log.takeSnapshot(driver);

		} catch (IOException e) {
		}

	}

	public String getName(String xml) {
		String name = "";
		String PATIENT_NAME = "<PatientName>";
		String PATIENT_NAME_END = "</PatientName>";
		String FAMILY_NAME = "<HL7GivenName>";
		String FAMILY_NAME_END = "</HL7GivenName>";

		String patientNameSection = xml.substring(xml.indexOf(PATIENT_NAME), xml.indexOf(PATIENT_NAME_END));

		name = patientNameSection.substring(patientNameSection.indexOf(FAMILY_NAME) + FAMILY_NAME.length(),
				patientNameSection.indexOf(FAMILY_NAME_END));

		return name;
	}

	public String getDob(String xml) {
		String dob = "";
		String month = "";
		String day = "";
		String year = "";

		String DOB = "<DateTimeOfBirth>";
		String DOB_END = "</DateTimeOfBirth>";
		String YEAR = "<year>";
		String YEAR_END = "</year>";
		String MONTH = "<month>";
		String MONTH_END = "</month>";
		String DAY = "<day>";
		String DAY_END = "</day>";

		dob = xml.substring(xml.indexOf(DOB) + DOB.length(), xml.indexOf(DOB_END));
		year = dob.substring(dob.indexOf(YEAR) + YEAR.length(), dob.indexOf(YEAR_END));
		month = dob.substring(dob.indexOf(MONTH) + MONTH.length(), dob.indexOf(MONTH_END));
		day = dob.substring(dob.indexOf(DAY) + DAY.length(), dob.indexOf(DAY_END));

		if (month.length() == 1)
			month = "0" + month;

		if (day.length() == 1)
			day = "0" + day;

		return month + "/" + day + "/" + year;
	}

	public void selectLabByEventId(String eventId) {

		String EventIDCellPosition = "7";
		boolean found = false;
		int i = 1;// for selecting the row where lab report is

		// Open the lab report by the name
		while (!found) {
			// Find the Event ID
			String eventIDTable = driver
					.findElement(By
							.xpath("//table[@id='eventLabReport']/tbody/tr[" + i + "]/td[" + EventIDCellPosition + "]"))
					.getText();

			if (eventIDTable.equalsIgnoreCase(eventId)) {
				found = true;
				// Click on the lab report
				driver.findElement(By.xpath("//table[@id='eventLabReport']/tbody/tr[" + i + "]/td/a")).click();
			}
			i++;
		}

	}

	public void printNotificationStatusandIdFromLabId(String eventId) {
		try {
			String EventIDCellPosition = "7";
			String InvestigationIDCellPosition = "5";
			String InvestigationIDTableInvestigationCellPosition = "8";
			String notificationIDTableCellPosition = "5";
			String notificationStatus = "";
			String investigationIDTable = "";

			boolean found = false;
			int i = 1;// for selecting the row where lab report is

			// Open the lab report by the name
			while (!found) {
				// Find the Event ID
				String eventIDTable = driver
						.findElement(By.xpath(
								"//table[@id='eventLabReport']/tbody/tr[" + i + "]/td[" + EventIDCellPosition + "]"))
						.getText();

				if (eventIDTable.equalsIgnoreCase(eventId)) {
					found = true;
					// Click on the lab report
					// driver.findElement(By.xpath("//table[@id='eventLabReport']/tbody/tr["+i+"]/td/a")).click();
					investigationIDTable = driver.findElement(By.xpath("//table[@id='eventLabReport']/tbody/tr[" + i
							+ "]/td[" + InvestigationIDCellPosition + "]")).getText();
					if (investigationIDTable.contains("\n"))
						investigationIDTable = investigationIDTable.substring(0, investigationIDTable.indexOf("\n"));
				}
				i++;
			}
			found = false;
			i = 1;
			// Find investigation
			while (!found) {
				// Find the Event ID
				String eventIDTable = driver.findElement(By.xpath("//table[@id='eventSumaryInv']/tbody/tr[" + i
						+ "]/td[" + InvestigationIDTableInvestigationCellPosition + "]")).getText();

				if (eventIDTable.equalsIgnoreCase(investigationIDTable)) {
					found = true;
					// Click on the lab report
					// driver.findElement(By.xpath("//table[@id='eventLabReport']/tbody/tr["+i+"]/td/a")).click();
					notificationStatus = driver.findElement(By.xpath("//table[@id='eventSumaryInv']/tbody/tr[" + i
							+ "]/td[" + notificationIDTableCellPosition + "]")).getText();

					Database db = new Database();
					String notificationId = db.getNotificationIdFromInvestigationId(database, investigationIDTable);
					log.write("The notification created is " + notificationId + " and the status is: "
							+ notificationStatus, "", true);
				}
				i++;
			}
			log.takeSnapshot(driver);
		} catch (IOException e) {
		}

		// return notificationStatus;
	}

	/**
	 * createLoincCode(): this methods creates the loinc code indicated as
	 * parameter
	 * 
	 * @param loinc
	 */

	public void createLoincCode(String loinc) {
		try {
			driver.findElement(By.linkText("System Management")).click();
			driver.findElement(
					By.cssSelector("#systemAdmin3 > thead > tr > th > a.toggleIconHref > img[alt=\"Maximize\"]"))
					.click();
			driver.findElement(By.cssSelector("#subSec2 > thead > tr > th > a.toggleIconHref > img[alt=\"Maximize\"]"))
					.click();
			driver.findElement(By.linkText("Manage LOINCs")).click();
			driver.findElement(By.id("srchLoinc")).clear();
			driver.findElement(By.id("srchLoinc")).sendKeys(loinc);
			driver.findElement(By.id("submit")).click();
			// read all the page
			// String message = driver.findElement(By.id("bd")).getText();

			// This if may be not necessary since we already know from the
			// database if it exists or not..
			// If the Loinc code exist, this method is not called

			// if(message.contains("Your Search Criteria resulted in 0 possible
			// matches")){
			driver.findElement(By.xpath("(//input[@id='submit'])[3]")).click();

			// driver.findElement(By.id("loinc_cd")).clear();
			// driver.findElement(By.id("loinc_cd")).sendKeys(loinc);
			driver.findElement(By.id("compName")).clear();
			driver.findElement(By.id("compName")).sendKeys("MICROORGANISM IDENTIFIED");
			driver.findElement(By.xpath("(//input[@id='submit'])[3]")).click();
			log.write("The loinc code " + loinc + " has been created.", "", true);
			log.takeSnapshot(driver);
			// ERROR: Caught exception [Error: Dom locators are not implemented
			// yet!]
			driver.findElement(By.id("manageLink")).click();

			// }
		} catch (IOException e) {

		}
	}

	/**
	 * createLoincCodeCondition(): this methods create a link between the LOINC
	 * code and the Condition
	 * 
	 * @param loinc
	 * @param condition
	 */

	public void createLoincCodeCondition(String loinc, String condition) {
		try {
			driver.findElement(By.linkText("System Management")).click();
			driver.findElement(
					By.cssSelector("#systemAdmin3 > thead > tr > th > a.toggleIconHref > img[alt=\"Maximize\"]"))
					.click();
			driver.findElement(By.cssSelector("#subSec2 > thead > tr > th > a.toggleIconHref > img[alt=\"Maximize\"]"))
					.click();
			driver.findElement(By.linkText("Manage Link between LOINC and Condition")).click();
			driver.findElement(By.name("srchCondition_textbox")).clear();
			driver.findElement(By.name("srchCondition_textbox")).sendKeys(condition);
			driver.findElement(By.cssSelector("td.InputField > #submit")).click();
			// TODO: automatically
			// driver.findElement(By.cssSelector("td.InputButton >
			// #submit")).click();//When there's a previous loinc-condition link
			driver.findElement(By.name("submit")).click();// When there's no
															// existing
															// loinc-condition
															// link
			/*****************************************************************************************************************/
			String parentWindow = driver.getWindowHandle();
			driver.findElement(By.xpath("(//input[@id='submit'])[4]")).click();

			Set<String> handles = driver.getWindowHandles();
			for (String windowHandle : handles) {
				if (!windowHandle.equals(parentWindow)) {
					driver.switchTo().window(windowHandle);
					driver.findElement(By.id("srchLoinc")).clear();
					driver.findElement(By.id("srchLoinc")).sendKeys(loinc);
					driver.findElement(By.id("submit")).click();
					driver.findElement(By.linkText(loinc)).click();

				}
			}

			// driver.close(); //closing child window
			driver.switchTo().window(parentWindow); // cntrl to parent window

			/*****************************************************************************************************************/

			driver.findElement(By.xpath("(//input[@id='submit'])[5]")).click();

			// ERROR: Caught exception [ERROR: Unsupported command [selectWindow
			// | null | ]]
			// ERROR: Caught exception [Error: Dom locators are not implemented
			// yet!]
			// ERROR: Caught exception [Error: Dom locators are not implemented
			// yet!]
			log.write("The loinc code " + loinc + " has been linked to the condition " + condition, "", true);
			log.takeSnapshot(driver);
			driver.findElement(By.id("manageLink")).click();
		} catch (IOException e) {
		}

	}

	public void getTestCaseReadNotificationStatus(String eventId) throws Exception {
		// try{
		driver.findElement(By.linkText("Home")).click();
		driver.findElement(By.name("ESR100_textbox")).clear();
		driver.findElement(By.name("ESR100_textbox")).sendKeys("Lab ID");
		driver.findElement(By.id("DEM229")).clear();// for losing the focus on
													// the previous one so the
													// business rule works
		// driver.findElement(By.id("patientSearchByDetails")).click();

		driver.findElement(By.id("ESR101")).clear();
		driver.findElement(By.id("ESR101")).sendKeys(eventId);
		driver.findElement(By.xpath("//input[@value='Search']")).click();
		// driver.findElement(By.xpath("//input[@value='Search']")).click();

		driver.findElement(By.xpath("//table[@id='searchResultsTable']/tbody/tr/td/a")).click();
		driver.findElement(By.id("tabs0head1")).click();

		// Open lab report
		printNotificationStatusandIdFromLabId(eventId);

	}

	/**
	 * existCreateInvestWithNotif(): it returns if the WDS for creating
	 * investigation with notification already exist.
	 * 
	 * @return
	 */
	protected boolean existCreateInvestWithoutNotif(String testResultData, String resultedTestCodeData,
			String testNameData) {
		boolean createAlg = true;
		String numOfResults = "";
		int len;
		String str;
		int rows;
		String testResult;

		try {
			driver.findElement(By.linkText("System Management")).click();
			driver.findElement(By.cssSelector("img[alt=\"Maximize\"]")).click();
			driver.findElement(By.linkText("Manage Workflow Decision Support")).click();
			driver.findElement(By.id("queueIcon")).click();
			driver.findElement(By.cssSelector("input.selectAll")).click();
			driver.findElement(By.xpath("(//input[@name='answerArray(EVENTTYPE)'])[2]")).click();
			driver.findElement(By.xpath("(//input[@id='b1'])[2]")).click();
			driver.findElement(By.xpath("(//img[@id='queueIcon'])[4]")).click();
			driver.findElement(By.xpath("(//input[@type='checkbox'])[4]")).click();
			// driver.findElement(By.xpath("(//input[@name='answerArray(ACTION)'])[1]")).click();//create
			// investigation
			driver.findElement(By.xpath("(//input[@name='answerArray(ACTION)'])[2]")).click();// create
																								// investigation
																								// with
																								// notification
																								// added
																								// bby
																								// Deepthi
			driver.findElement(By.xpath("(//input[@id='b1'])[4]")).click();
			driver.findElement(By.xpath("(//img[@id='queueIcon'])[6]")).click();
			driver.findElement(By.xpath("(//input[@type='checkbox'])[15]")).click();
			driver.findElement(By.name("answerArray(STATUS)")).click();
			driver.findElement(By.xpath("(//input[@id='b1'])[8]")).click();

			numOfResults = driver.findElement(By.xpath("//fieldset[@id='result']/table/tbody/tr/td/span[4]/b"))
					.getText();
			len = numOfResults.length();
			Character ch = numOfResults.charAt(len - 2);
			if (Character.isDigit(ch))
				str = numOfResults.substring(len - 2);
			else
				str = numOfResults.substring(len - 1);

			rows = Integer.parseInt(str);
			System.out.println("number of rows: " + rows);
			String[] testName = new String[rows];
			for (int i = 0; i < rows; i++) {
				testName[i] = driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + (i + 1) + "]/td[5]"))
						.getText();
				testNameData = testNameData.replace(".0", "");
				if ((testName[i].contains(resultedTestCodeData)) && (testName[i].contains(testNameData))) {
					driver.findElement(By.xpath("(//img[@title='View'])[" + (i + 1) + "]")).click();
					driver.findElement(By.id("tabs0head1")).click();
					testResult = driver
							.findElement(
									By.xpath("//table[@id='ElrIdAdvancedSubSection']/tbody/tr/td/table/tbody/tr/td[3]"))
							.getText();
					if (testResult.equalsIgnoreCase(testResultData))
						createAlg = false;
					driver.findElement(By.id("manageLink")).click();
				}
			}

			if (createAlg == true)
				System.out.println("algorithm doesnot exist, creating new one");

			else
				System.out.println("algorithm already exists, new one will not be created");

			return createAlg;

		} catch (Exception e)// if there are no active algorithms in the system,
								// exception is thrown and caught here
		{
			if (createAlg == true)
				System.out.println("no active algorithms exist, creating new one. This is coming from catch statement");
			return createAlg;
		}

	}

	/**
	 * existAlgorithm(): returns false is the algorithm exists and true if the
	 * algorithm doesn't exist
	 * 
	 * @param rows
	 * @return
	 */

	public boolean existWDSMarkAsReview() throws Exception {
		/*
		 * driver.get(baseUrl + "/nbs/login");
		 * driver.findElement(By.id("id_UserName")).clear();
		 * driver.findElement(By.id("id_UserName")).sendKeys("pks");
		 * driver.findElement
		 * (By.id("id_Submit_top_ToolbarButtonGraphic")).click();
		 */

		// boolean acceptNextAlert = true;
		// StringBuffer verificationErrors = new StringBuffer();
		String numOfResults, str;
		int len, rows, i;
		char ch;
		// ArrayList<String> algName = new ArrayList<>();
		boolean createAlg = true;

		driver.findElement(By.linkText("System Management")).click();
		driver.findElement(By.cssSelector("img[alt=\"Maximize\"]")).click();
		driver.findElement(By.linkText("Manage Workflow Decision Support")).click();
		driver.findElement(By.id("queueIcon")).click();
		driver.findElement(By.cssSelector("input.selectAll")).click();
		driver.findElement(By.xpath("(//input[@name='answerArray(EVENTTYPE)'])[2]")).click();
		driver.findElement(By.xpath("(//input[@id='b1'])[2]")).click();
		driver.findElement(By.xpath("(//img[@id='queueIcon'])[4]")).click();
		driver.findElement(By.xpath("(//input[@type='checkbox'])[4]")).click();
		driver.findElement(By.xpath("(//input[@name='answerArray(ACTION)'])[3]")).click();
		driver.findElement(By.xpath("(//input[@id='b1'])[4]")).click();
		driver.findElement(By.xpath("(//img[@id='queueIcon'])[6]")).click();
		driver.findElement(By.xpath("(//input[@type='checkbox'])[15]")).click();
		driver.findElement(By.name("answerArray(STATUS)")).click();
		driver.findElement(By.xpath("(//input[@id='b1'])[8]")).click();

		try {
			numOfResults = driver.findElement(By.xpath("//fieldset[@id='result']/table/tbody/tr/td/span[4]/b"))
					.getText();
			len = numOfResults.length();
			ch = numOfResults.charAt(len - 2);
			if (Character.isDigit(ch))
				str = numOfResults.substring(len - 2);
			else
				str = numOfResults.substring(len - 1);

			rows = Integer.parseInt(str);
			System.out.println("number of rows: " + rows);
			createAlg = findAlgorithm(rows); // return true or false based on
												// which Fatima will create or
												// not create an algorithm

		} catch (Exception e)// if there are no active algorithms in the system,
								// exception is thrown and caught here
		{
			if (createAlg == true)
				System.out.println("no active algorithms exist, creating new one. This is coming from catch statement");
		}

		return createAlg;
	}

	/**
	 * findAlgorithm(): returns false is the algorithm exists and true if the
	 * algorithm doesn't exist
	 * 
	 * @param rows
	 * @return
	 */

	private boolean findAlgorithm(int rows) {
		String testResult;
		String[] testName = new String[rows];
		int i;
		boolean createAlg = true;
		for (i = 0; i < rows; i++) {
			testName[i] = driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[" + (i + 1) + "]/td[5]"))
					.getText();

			if (testName[i].contains("T-57185")) {
				driver.findElement(By.xpath("(//img[@title='View'])[" + (i + 1) + "]")).click();
				driver.findElement(By.id("tabs0head1")).click();
				testResult = driver
						.findElement(
								By.xpath("//table[@id='ElrIdAdvancedSubSection']/tbody/tr/td/table/tbody/tr/td[4]"))
						.getText();
				if (testResult.equalsIgnoreCase("HAEMOPHILUS INFLUENZAE")) {
					createAlg = false;
					log.write("The Algorithm already exists");
					log.takeSnapshot(driver);
				}
				driver.findElement(By.id("manageLink")).click();
			}
		}

		if (createAlg == true) {
			System.out.println("algorithm doesnot exist, creating new one");
			return true;
		} else {
			System.out.println("algorithm already exists, new one will not be created");
			return false;
		}

	}

	/**
	 * getLastObsID(): it returns the obsId of the last lab report imported into
	 * the system
	 * 
	 * @param driver
	 * @return
	 */

	protected String getLastObsID() {
		String ObsId = "";
		try {

			driver.findElement(By.linkText("System Management")).click();
			driver.findElement(
					By.cssSelector("#systemAdmin4 > thead > tr > th > a.toggleIconHref > img[alt=\"Maximize\"]"))
					.click();
			driver.findElement(By.linkText("Manage ELR Activity Log")).click();
			/*
			 * driver.findElement(By.id("from_date")).clear();
			 * driver.findElement(By.id("from_date")).sendKeys("02/22/2016");
			 * driver.findElement(By.id("to_date")).clear();
			 * driver.findElement(By.id("to_date")).sendKeys("03/17/2016");
			 */
			driver.findElement(By.id("searchButton")).click();
			ObsId = driver.findElement(By.xpath("//table[@id='parent']/tbody/tr[1]/td[8]")).getText(); // get
																										// observation
																										// id
																										// of
																										// first
																										// record
																										// in
																										// the
																										// queue

			return ObsId;
		} catch (NoSuchElementException e) {
			System.out.println("no such element exception thrown:");
			e.printStackTrace();
			return ObsId;
		}
	}

	/**
	 * saveStringOnFile: save string on a file
	 * 
	 * @param hl7
	 * @param pathFolder
	 * @param name
	 */
	protected void saveStringOnFile(String hl7, String pathFile) {

		try {
			File Fileright = new File(pathFile);
			PrintWriter out = new PrintWriter(Fileright);
			out.println(hl7);
			out.close();
		} catch (FileNotFoundException e) {

		}

	}

	/**
	 * runELRBatchProcess(): run the ELRImporter batch process
	 */

	public void runELRBatchProcess(int nELR) {
		try {
			// Runtime.getRuntime().exec("sh�-c�cd
			// C:\\wildfly-8.2.0.Final\\server\\nedssdomain\\Nedss\\BatchFiles\\ELRImporter.bat")
			// ;
			Database db = new Database();
			log.write("Running ELRImporter process");
			final Process process = Runtime.getRuntime().exec(
					"cmd /c cmd.exe /K \"cd /d D:\\wildfly-10.0.0.Final\\nedssdomain\\Nedss\\BatchFiles\\ && ELRImporter.bat && exit\"");

			process.waitFor(30, TimeUnit.SECONDS);

			int elrQueued = db.getNbsInterfaceQueued(database);

			if (elrQueued != 0)
				runELRBatchProcess(nELR);

		} catch (InterruptedException e) {

			System.out.println("Error running ELRImporter.bat");

		} catch (IOException e) {

			System.out.println("Error running ELRImporter.bat");
		}

	}

	public void fileTransfer() throws InterruptedException {
		try {
			// final Process process = Runtime.getRuntime().exec("SchTasks /Run
			// /S nedss-tstappsql /TN ELRImporter /U administrator /P
			// n3d$$4dm1n");
			final Process process = Runtime.getRuntime()
					.exec("net use t: \\\\nedss-tstappsql\\HL7231 /user:tullurid Welcome2sra");
			process.waitFor();
			final Process process1 = Runtime.getRuntime().exec("xcopy /d C:\\Selenium\\MasterELRBuilder\\*.txt t:\\");
			process1.waitFor();
			final Process process2 = Runtime.getRuntime().exec("net use t: /delete");
			process2.waitFor();
			// final Process process3 = Runtime.getRuntime().exec("xcopy
			// C:\\Selenium\\InputFiles\\*.txt
			// C:\\Selenium\\InputFiles\\Backup\\");
			// process3.waitFor();
			// final Process process5 = Runtime.getRuntime().exec("del
			// C:\\Selenium\\InputFiles\\*.txt");
			// process3.waitFor();

			System.out.println("File is copied on to the remote system");

		} catch (IOException e) {

			System.out.println("Error copying files");
		}
	}

	public void runExcelMacro() throws InterruptedException {
		try {

			final Process process = Runtime.getRuntime()
					.exec("cmd /c start  C:\\Selenium\\MasterELRBuilder\\ExecuteMacroNewELR.vbs");

			process.waitFor();
			System.out.println("File created successfully");

		} catch (IOException e) {
			System.out.println(("Error creating files"));
		}

	}

}