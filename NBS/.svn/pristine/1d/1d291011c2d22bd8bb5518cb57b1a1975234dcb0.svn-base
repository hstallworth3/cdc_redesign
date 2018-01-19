package com.nbs.systemManagement.pageManagement;

import org.junit.Test;
import java.awt.Robot;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class Manage_Conditions {

	public WebDriver driver;
	public Log log;
	DataFile df;
	public Robot robot = null;
	public String Data_File_Path, Log_File_Path, Screenshot_File_Path;
	public Common_Utils comUtils = new Common_Utils();
	public PageManagement_Utils pmUtils = new PageManagement_Utils();

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "systemManagement.pageManagement\\Manage_Conditions.xls";
		Log_File_Path = login.Log_File_Path;
		Screenshot_File_Path = login.Screenshot_File_Path;
	}

	@Test
	public void ManageConditions() throws Exception {
		log = new Log("PageMgmt_ManageConditions", Log_File_Path);
		comUtils.setUpDriver(driver);
		pmUtils.setUp();
		pmUtils.setUpDriver(driver);
		pmUtils.setUpLog(log);

		Add_Condition();
		Edit_Condition();
		Inactivate_Condition();
		Print_Condition();
		Download_Condition();

		pmUtils.getDriver();
		pmUtils.getLog();
	}

	private void Add_Condition() throws Exception {
		df = new DataFile(Data_File_Path, "Add");
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Navigate to Manage Conditions link under Page Management
				navigateToManage_Conditions();
				// Click on Add New
				driver.findElement(By.id("submitCr")).click();
				ReadMetaData manage_Conditions = new ReadMetaData();
				manage_Conditions.setUpDataFile(df);

				// Read the value Condition Name from excel and write it to log
				String condition_name = manage_Conditions.getFieldValue("Condition Name", col);
				log.write("Adding the record for Condition name: " + condition_name);
				manage_Conditions.setUpDriver(driver);
				manage_Conditions.setUpLog(log);

				// Read the Meta Data and populate the fields
				manage_Conditions.ReadData(col);

				// Click on Submit button
				driver.findElement(By.id("submitB")).click();
				if (driver.findElement(By.xpath("//*[@id='bd']/div[3]")).getText().contains("has been successfully added to the system"))
					log.writeSubSection("Record for Condition Name: " + condition_name + " is added successfully", "",true);
				else if (driver.findElement(By.xpath("//*[@id='bd']/div[2]")).getText().contains("already exists")) {
					log.writeSubSection("Record for Condition Name: " + condition_name + " already exists");
					log.takeSnapshot(driver);

				} else {
					log.writeSubSection("Record for Condition Name: " + condition_name + " is not added successfully",
							"", false);
					log.takeSnapshot(driver);
				}
			} catch (Exception e) {
				log.writeSubSection("There is an error adding the condition", "", false);
				log.takeSnapshot(driver);
			}
			log.writeSubSection_RecordStatus();
		}
	}

	// To Edit the Condition names
	private void Edit_Condition() throws Exception {

		String cond_name = "";
		df = new DataFile(Data_File_Path, "Edit");
		ReadMetaData manage_Conditions = new ReadMetaData();
		manage_Conditions.setUpDataFile(df);
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Navigate to Manage Conditions link under Page Management
				navigateToManage_Conditions();

				// Read the value Condition Name from excel and write it to log
				String condition_name = manage_Conditions.getFieldValue("Condition Name", col);
				log.write("Editing the record for Condition name: " + condition_name);

				// Click on condition filter
				driver.findElement(By.id("queueIcon")).click();

				int colnum = 3; // Column number of Condition
				// Get the row number of the exact Condition Name
				int rowNum = pmUtils.getRowNum(condition_name, By.id("SearchText1"), By.id("b2SearchText1"), colnum);

				System.out.println("rownum Returned: " + rowNum);

				if (rowNum != 0) {
					cond_name = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td["+colnum+"]"))
							.getText();
					System.out.println("Condition Name: " + cond_name);
					if (cond_name.equalsIgnoreCase(condition_name)) {

						// If Edit icon is enabled, click on edit icon and
						// validate
						if (comUtils.isElementDisplayed(
								By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td[2]/a/img"))) {

							// Click on Edit Icon
							driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td[2]/a/img")).click();

							// Read the Meta Data and populate the fields
							manage_Conditions.ReadData(col);

							// Click on Submit button
							driver.findElement(By.id("submitB")).click();
							Thread.sleep(1000);
							if (comUtils.isElementDisplayed(By.xpath("//*[@id='bd']/div[3]"))) {

								if (driver.findElement(By.xpath("//*[@id='bd']/div[3]")).getText()
										.contains(condition_name + " has been successfully updated in the system"))
									log.writeSubSection(
											"Record for Condition Name: " + condition_name + " is updated successfully","", true);

							} else {
								log.writeSubSection(
										"Record for Condition Name: " + condition_name + " is NOT updated successfully",
										"", false);
								log.takeSnapshot(driver);
							}
						}
					}
				} else {
					log.writeSubSection("Condition Name:" + condition_name + " is not found");
					log.takeSnapshot(driver);
				}

			} catch (Exception e) {
				log.writeSubSection("There is an error updating the condition", "", false);
				log.takeSnapshot(driver);
			}

			log.writeSubSection_RecordStatus();

		}
	}

	// To Inactivate the Conditions
	private void Inactivate_Condition() throws Exception {
		String cond_name = "";

		df = new DataFile(Data_File_Path, "Inactive");
		ReadMetaData inactive_Conditions = new ReadMetaData();
		inactive_Conditions.setUpDataFile(df);
		int colCount = df.getColCount() - 1;
		for (int col = 1; col <= colCount; col++) {
			try {
				// Navigate to Manage Conditions link under Page Management
				navigateToManage_Conditions();
				// Read the value Condition Name from excel and write it to log
				String condition_name = inactive_Conditions.getFieldValue("Condition Name", col);
				log.write("Inactivating the record for Condition name: " + condition_name);

				// Click on condition filter
				driver.findElement(By.id("queueIcon")).click();
				int colnum = 3; // Column number of Condition
				// Get the row number of the exact Condition Name
				int rowNum = pmUtils.getRowNum(condition_name, By.id("SearchText1"), By.id("b2SearchText1"), colnum);

				System.out.println("rownum Returned: " + rowNum);

				if (rowNum != 0) {
					cond_name = driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td["+colnum+"]")).getText();
					System.out.println("Condition Name: " + cond_name);
					if (cond_name.equalsIgnoreCase(condition_name)) {

						// If View icon is enabled, click on edit icon and
						// validate
						if (comUtils.isElementDisplayed(
								By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td[1]/a/img"))) {

							// Click on View Icon
							driver.findElement(By.xpath("//*[@id='parent']/tbody/tr[" + rowNum + "]/td[1]/a/img"))
							.click();

							// Click on Make Inactive button if present
							String active_Inactive = driver.findElement(By.id("submitB")).getAttribute("value");
							//if the Buttons are either Edit or Make Inactive click on Make Inactive button 							
							if (active_Inactive.equalsIgnoreCase("Edit") || active_Inactive.equalsIgnoreCase("Make Inactive")){
								if (driver.findElement(By.xpath("html/body/form/div/div/div[1]/input[2]")).getAttribute("value").contains("Make Inactive")) {
									driver.findElement(By.xpath("html/body/form/div/div/div[1]/input[2]")).click();
									Thread.sleep(1000);
									// Click OK on the Alert if it is present
									if (comUtils.isAlertPresent()) {
										Thread.sleep(1000);
										if (driver.findElement(By.xpath("//*[@id='bd']/div[3]")).getText().contains("condition has been made inactive"))
											log.writeSubSection("Condition Name: " + condition_name
													+ " is INACTIVATED successfully", "", true);
										else {
											log.writeSubSection("Condition Name: " + condition_name
													+ " is NOT INACTIVATED successfully", "", false);
											log.takeSnapshot(driver);
										}
									} else {
										log.writeSubSection("Condition Name: " + condition_name	+ " is NOT INACTIVATED successfully", "", false);
										log.takeSnapshot(driver);
									}
								}
							} else if (active_Inactive.equalsIgnoreCase("Make Active")) {
								log.writeSubSection("INACTIVE button is not present - Condition is already Inactive");
								log.takeSnapshot(driver);
							} else {
								log.writeSubSection("There is an issue. There is no Make Inactive or Make Active button");
								log.takeSnapshot(driver);
							}

						}
					}

				} else {
					log.writeSubSection("Condition Name: " + condition_name + " is NOT Present");
					log.takeSnapshot(driver);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.writeSubSection("There is an error inactivating the condition", "", false);
				log.takeSnapshot(driver);
			}

			log.writeSubSection_RecordStatus();

		}

	}

	private void Print_Condition() throws Exception {
		String winHandleBefore = driver.getWindowHandle();

		// To verify the Print option
		log.write("Checking the Print option of Condition Page");
		try {
			// Navigate to Manage Conditions link under Page Management
			navigateToManage_Conditions();

			// Check if Print button if available
			if (comUtils.isElementDisplayed(By.xpath("html/body/form/div/div[1]/div[1]/input[2]"))) {

				// Click on the Print button
				driver.findElement(By.xpath("html/body/form/div/div[1]/div[1]/input[2]")).click();
				pmUtils.setUpDriver(driver);
				pmUtils.Print_Download();
				pmUtils.getDriver();
				log.writeSubSection("Print option is working as expected", "", true);

			} else {
				log.writeSubSection("Print Button is NOT Present", "", false);
				log.takeSnapshot(driver);
			}
		} catch (Exception e) {
			log.writeSubSection("There is an issue with Print option", "", false);
			log.takeSnapshot(driver);
		}

		driver.switchTo().window(winHandleBefore);
		log.writeSubSection_RecordStatus();
	}

	private void Download_Condition() throws Exception {
		// To verify the Download option
		log.write("Checking the download option");
		try {
			// Navigate to Manage Conditions link under Page Management
			navigateToManage_Conditions();
			if (comUtils.isElementDisplayed(By.xpath("html/body/form/div/div[1]/div[1]/input[3]"))) {
				// Click on the Download button
				driver.findElement(By.xpath("html/body/form/div/div[1]/div[1]/input[3]")).click();

				pmUtils.setUpDriver(driver);
				pmUtils.Print_Download();
				pmUtils.getDriver();
				log.writeSubSection("Download option is working as expected", "", true);

			} else {
				log.writeSubSection("Download Button is NOT Present", "", false);
				log.takeSnapshot(driver);
			}
		} catch (Exception e) {
			log.writeSubSection("There is an issue with Download option", "", false);
			log.takeSnapshot(driver);
		}
		// driver.switchTo().window(winHandleBefore);
		log.writeSubSection_RecordStatus();
	}

	private void navigateToManage_Conditions() throws Exception {
		// Click on the System Management link on the Home Page
		driver.findElement(By.linkText("System Management")).click();
		// Expand Page Management
		driver.findElement(By.xpath("//*[@id='systemAdmin5']/thead/tr/th/a/img")).click();
		// Click on Manage Conditions link text
		driver.findElement(By.linkText("Manage Conditions")).click();

	}

	@After
	public void close() throws Exception {
		log.close();
		df.close();
		driver.close();
	}
}
