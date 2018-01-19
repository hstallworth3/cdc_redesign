package com.nbs.common;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Common_Utils;

public class Create_LabReport {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public String patID = "";
	public Common_Utils comUtils = new Common_Utils();
	
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
	}

	public void setUpDriver(WebDriver driver) throws Exception {
		this.driver = driver;
	}

	public void setUpLog(Log logFile) throws Exception {

		this.log = logFile;
	}

	public void setUpDataFile(DataFile df) throws Exception {
		this.df = df;
	}

	@Test
	public String Populate_DE_LabReport(int col) throws Exception {
		
		comUtils.setUpDriver(driver);

		driver.findElement(By.linkText("Data Entry")).click();
		driver.findElement(By.linkText("Lab Report")).click();

		ReadMetaData newSTD_Patient = new ReadMetaData();

		newSTD_Patient.setUpDriver(driver);
		newSTD_Patient.setUpLog(log);
		newSTD_Patient.setUpDataFile(df);

		// Read Patient Last Name
		String lastName = newSTD_Patient.getFieldValue("Last Name", col);
		log.write("Patient Last Name: " + lastName);

		// Add Lab Report and Click Submit Button
		newSTD_Patient.ReadData(col);

		driver.findElement(By.id("Submit")).click();
		if (comUtils.isAlertPresent())
			;

		// Log the Lab Report Results
		if (comUtils.isElementDisplayed(By.xpath("//span[@id='patient.ID']/b"))) {
			String patientID = driver.findElement(By.xpath("//span[@id='patient.ID']/b")).getText();
			String[] pID = patientID.split(":");
			String patID = pID[1].trim();
			log.writeSubSection("Lab Report is created Successfully", "PatientID:" + patID, true);

			return patID;
		} else {
			log.writeSubSection("Lab Report is not created Successfully", "", false);
			log.takeSnapshot(driver);
		}
		return patID;
	}

	public WebDriver getDriver() throws Exception {
		return driver;
	}

	public Log getLog() throws Exception {
		return log;
	}

	public DataFile getDataFile() throws Exception {
		return df;
	}

	public void close() throws Exception {
		log.close();
		df.close();
	}
}
