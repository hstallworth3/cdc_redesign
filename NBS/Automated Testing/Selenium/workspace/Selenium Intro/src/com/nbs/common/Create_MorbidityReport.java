package com.nbs.common;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Common_Utils;

public class Create_MorbidityReport {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public String patID = "";
	public Common_Utils comUtils = new Common_Utils();

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
	public String Populate_DE_MorbReport(int col) throws Exception {
		comUtils.setUpDriver(driver);
		driver.findElement(By.linkText("Data Entry")).click();
		driver.findElement(By.linkText("Morbidity Report")).click();
		ReadMetaData newMorb_report = new ReadMetaData();
		newMorb_report.setUpDriver(driver);
		newMorb_report.setUpLog(log);
		newMorb_report.setUpDataFile(df);
		String lastName = newMorb_report.getFieldValue("Last Name", col);
		log.write("Patient Last Name: " + lastName);
		newMorb_report.ReadData(col);

		driver.findElement(By.id("Submit")).click();
		if (comUtils.isAlertPresent());			

		// Getting the Patient ID
		if (comUtils.isElementDisplayed(By.xpath("//span[@id='patient.ID']/b"))) {
			String patientID = driver.findElement(By.xpath("//span[@id='patient.ID']/b")).getText();
			String[] pID = patientID.split(":");
			String patID = pID[1].trim();
			log.writeSubSection("Morbiditity Report is created Successfully", "PatientID:" + patID, true);
			return patID;
		} else {
			log.writeSubSection("Morbiditity Report is not created Successfully", "", false);
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
}
