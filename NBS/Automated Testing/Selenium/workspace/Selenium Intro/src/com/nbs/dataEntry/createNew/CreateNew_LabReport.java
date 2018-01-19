package com.nbs.dataEntry.createNew;

import com.nbs.common.*;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.openqa.selenium.WebDriver;

public class CreateNew_LabReport {
	public WebDriver driver;
	private Log log;
	DataFile df;
	public String Data_File_Path, Log_File_Path;

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		login.Setup();
		driver = login.getDriver();
		Data_File_Path = login.Data_File_Path + "dataEntry.createNew\\CreateNew_LabReport.xls";
		Log_File_Path = login.Log_File_Path;
	}

	@Test
	public void test() throws Exception {
		log = new Log("CreateNew_LabReport", Log_File_Path);
		df = new DataFile(Data_File_Path, "Lab_MetaData");
		Create_LabReport DEMRN = new Create_LabReport();
		DEMRN.setUpDriver(driver);
		DEMRN.setUpLog(log);
		DEMRN.setUpDataFile(df);
		int colCount = df.getColCount() - 3;
		for (int col = 1; col <= colCount; col++) {
			DEMRN.Populate_DE_LabReport(col);
			log.writeSubSection_RecordStatus();

		}

		driver = DEMRN.getDriver();
		log = DEMRN.getLog();
		df = DEMRN.getDataFile();
	}

	@After
	public void close() throws Exception {
		df.close();
		log.close();
		driver.close();
	}
}
