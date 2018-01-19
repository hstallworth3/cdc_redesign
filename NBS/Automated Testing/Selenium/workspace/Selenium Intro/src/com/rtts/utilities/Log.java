package com.rtts.utilities;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;

public class Log {
	// Vital fields
	private int row = 1;
	private int step = 1;
	private static final String directory = "C:\\";
	private File xlsxFile;
	private Workbook wb;
	private Sheet sheet;
	private final int STEP_COLUMN = 0;
	private final int STEP_DESC_COL = 1;
	private final int STEP_DATA_COL = 2;
	private final int STEP_PASS_FAIL = 3;
	private final int STEP_RECORD_STATUS = 4;
	private CellStyle passed;
	private CellStyle failed;
	private CellStyle info;
	private CellStyle warning;
	private final String InfoTag = "Info";

	// Color fields
	private static final short PASS_COLOR = IndexedColors.GREEN.getIndex();
	private static final short FAIL_COLOR = IndexedColors.RED.getIndex();
	private static final short WARN_COLOR = IndexedColors.ORANGE.getIndex();
	private static final short INFO_COLOR = IndexedColors.INDIGO.getIndex();

	// Counts for passes/failures/warnings
	private int passCount = 0;
	private int failCount = 0;
	private int warnCount = 0;
	private int recordsPassed = 0;
	private int recordsFailed = 0;

	// Below are the constants for text formatting
	public final static String AUTOMATIC = "AUTOMATIC";
	public final static String BLACK = "BLACK";
	public final static String WHITE = "WHITE";
	public final static String RED = "RED";
	public final static String BRIGHT_GREEN = "BRIGHT_GREEN";
	public final static String BLUE = "BLUE";
	public final static String YELLOW = "YELLOW";
	public final static String PINK = "PINK";
	public final static String TURQUOISE = "TURQUOISE";
	public final static String DARK_RED = "DARK_RED";
	public final static String GREEN = "GREEN";
	public final static String DARK_BLUE = "DARK_BLUE";
	public final static String DARK_YELLOW = "DARK_YELLOW";
	public final static String VIOLET = "VIOLET";
	public final static String TEAL = "TEAL";
	public final static String GREY_25_PERCENT = "GREY_25_PERCENT";
	public final static String GREY_50_PERCENT = "GREY_50_PERCENT";
	public final static String GREY_80_PERCENT = "GREY_80_PERCENT";
	public final static String CORNFLOWER_BLUE = "CORNFLOWER_BLUE";
	public final static String MAROON = "MAROON";
	public final static String LEMON_CHIFFON = "LEMON_CHIFFON";
	public final static String ORCHID = "ORCHID";
	public final static String CORAL = "CORAL";
	public final static String ROYAL_BLUE = "ROYAL_BLUE";
	public final static String LIGHT_CORNFLOWER_BLUE = "LIGHT_CORNFLOWER_BLUE";
	public final static String SKY_BLUE = "SKY_BLUE";
	public final static String LIGHT_TURQUOISE = "LIGHT_TURQUOISE";
	public final static String LIGHT_GREEN = "LIGHT_GREEN";
	public final static String LIGHT_YELLOW = "LIGHT_YELLOW";
	public final static String PALE_BLUE = "PALE_BLUE";
	public final static String ROSE = "ROSE";
	public final static String LAVENDER = "LAVENDER";
	public final static String TAN = "TAN";
	public final static String LIGHT_BLUE = "LIGHT_BLUE";
	public final static String AQUA = "AQUA";
	public final static String LIME = "LIME";
	public final static String GOLD = "GOLD";
	public final static String LIGHT_ORANGE = "LIGHT_ORANGE";
	public final static String ORANGE = "ORANGE";
	public final static String BLUE_GREY = "BLUE_GREY";
	public final static String GREY_40_PERCENT = "GREY_40_PERCENT";
	public final static String DARK_TEAL = "DARK_TEAL";
	public final static String SEA_GREEN = "SEA_GREEN";
	public final static String DARK_GREEN = "DARK_GREEN";
	public final static String OLIVE_GREEN = "OLIVE_GREEN";
	public final static String BROWN = "BROWN";
	public final static String PLUM = "PLUM";
	public final static String INDIGO = "INDIGO";
	private static final int NULL = 0;
	
	//Static fields for Summary of Results for Test Suite logs
	public static int summaryPassed = 0;
	public static int summaryFailed = 0;
	public static String FinalResult = "";
	public static int FinalPassed = 0;
	public static int FinalFailed = 0;
	
	

	/**
	 * Constructor to specify the name of the log file and its path
	 * 
	 * @param logName
	 *            Name of the log file
	 * @param path
	 *            Path for the log file
	 */
	public Log(String logName, String path) {

		// Ensure you have rights to write to that directory
		File dir = new File(path);
		//Record Status
		if (dir.exists()) {
			if (!dir.canWrite()) {
				dir.setWritable(true);
			}
		}

		// Using a date & time stamp for the name and keep the file unique
		SimpleDateFormat date = new SimpleDateFormat("EEE, MMM d ''yy hh_mm_ss a");
		String formatedDate = date.format(new Date());
		xlsxFile = new File(dir, logName + "-" + formatedDate + ".xlsx");
		try {
			xlsxFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Define the workbook objects
		this.wb = new XSSFWorkbook();
		this.sheet = wb.createSheet("Log");

		// Adjust the column widths
		sheet.setColumnWidth(STEP_COLUMN, 7 * 256);
		sheet.setColumnWidth(STEP_DESC_COL, 60 * 256);
		sheet.setColumnWidth(STEP_DATA_COL, 44 * 256);
		sheet.setColumnWidth(STEP_PASS_FAIL, 10 * 256);
		sheet.setColumnWidth(STEP_RECORD_STATUS, 15 * 256);// added

		// Define header style
		CellStyle headerStyle = wb.createCellStyle();
		Font headerFont = wb.createFont();

		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		// Create the header row
		Row headerRow = sheet.createRow(0);

		// Create the column headers
		Cell stepCol = headerRow.createCell(STEP_COLUMN);
		Cell descCol = headerRow.createCell(STEP_DESC_COL);
		Cell dataCol = headerRow.createCell(STEP_DATA_COL);
		Cell pfCol = headerRow.createCell(STEP_PASS_FAIL);
		Cell rsCol = headerRow.createCell(STEP_RECORD_STATUS);

		// Populate Line #
		stepCol.setCellValue("Line #");
		stepCol.setCellStyle(headerStyle);

		// Populate Description
		descCol.setCellValue("Description");
		descCol.setCellStyle(headerStyle);

		// Populate Test Data
		dataCol.setCellValue("Test Data");
		dataCol.setCellStyle(headerStyle);

		// Populate Pass/Fail
		pfCol.setCellValue("Pass/Fail");
		pfCol.setCellStyle(headerStyle);

		// Populate Record Status //added
		rsCol.setCellValue("Record Status");
		rsCol.setCellStyle(headerStyle);

		// Define Passed Style
		passed = wb.createCellStyle();
		Font fontPassed = wb.createFont();
		fontPassed.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontPassed.setColor(PASS_COLOR);
		passed.setFont(fontPassed);

		// Define Failed Style
		failed = wb.createCellStyle();
		Font fontFailed = wb.createFont();
		fontFailed.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontFailed.setColor(FAIL_COLOR);
		failed.setFont(fontFailed);

		// Define Warning Style
		warning = wb.createCellStyle();
		Font fontWarn = wb.createFont();
		fontWarn.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontWarn.setColor(WARN_COLOR);
		warning.setFont(fontWarn);

		// Define Info style
		info = wb.createCellStyle();
		Font infoFont = wb.createFont();
		infoFont.setColor(INFO_COLOR);
		info.setFont(infoFont);
	}

	/**
	 * Constructor that allows the log file name to be specified.
	 * 
	 * @param logName
	 *            Name of the log file
	 */
	public Log(String logName) {
		this(logName, directory);
	}

	/**
	 * Default constructor
	 */
	public Log() {
		this("testLog");
	}
	
	/**
	 * Constructor for Suites
	 */
	public Log(String logName, String path,String x) {

		// Ensure you have rights to write to that directory
		File dir = new File(path);
		//Record Status
		if (dir.exists()) {
			if (!dir.canWrite()) {
				dir.setWritable(true);
			}
		}

		// Using a date & time stamp for the name and keep the file unique
		SimpleDateFormat date = new SimpleDateFormat("EEE, MMM d ''yy hh_mm_ss a");
		String formatedDate = date.format(new Date());
		xlsxFile = new File(dir, logName + "-" + formatedDate + ".xlsx");
		try {
			xlsxFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Define the workbook objects
		this.wb = new XSSFWorkbook();
		this.sheet = wb.createSheet("Dash Board");

		// Adjust the column widths
		sheet.setColumnWidth(STEP_COLUMN, 7 * 256);
		sheet.setColumnWidth(STEP_DESC_COL, 60 * 256);
		sheet.setColumnWidth(STEP_DATA_COL, 20 * 256);
		sheet.setColumnWidth(STEP_PASS_FAIL, 20 * 256);
		sheet.setColumnWidth(STEP_RECORD_STATUS, 15 * 256);// added

		// Define header style
		CellStyle headerStyle = wb.createCellStyle();
		Font headerFont = wb.createFont();

		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);
		headerStyle.setAlignment(CellStyle.ALIGN_LEFT);//Changed from CENTER to LEFT

		// Create the header row
		Row headerRow = sheet.createRow(0);

		// Create the column headers
		Cell stepCol = headerRow.createCell(STEP_COLUMN);
		Cell descCol = headerRow.createCell(STEP_DESC_COL);
		Cell dataCol = headerRow.createCell(STEP_DATA_COL);
		Cell pfCol = headerRow.createCell(STEP_PASS_FAIL);
		Cell rsCol = headerRow.createCell(STEP_RECORD_STATUS);

		// Populate Line #
		stepCol.setCellValue("Line #");
		stepCol.setCellStyle(headerStyle);

		// Populate Description
		descCol.setCellValue("Script Name");
		descCol.setCellStyle(headerStyle);

		// Populate Test Data
		dataCol.setCellValue("TestCases Passed");
		dataCol.setCellStyle(headerStyle);

		// Populate Pass/Fail
		pfCol.setCellValue("TestCases Failed");
		pfCol.setCellStyle(headerStyle);

		// Populate Record Status //added
		rsCol.setCellValue("Final Results");
		rsCol.setCellStyle(headerStyle);

		// Define Passed Style
		passed = wb.createCellStyle();
		Font fontPassed = wb.createFont();
		fontPassed.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontPassed.setColor(PASS_COLOR);
		passed.setFont(fontPassed);

		// Define Failed Style
		failed = wb.createCellStyle();
		Font fontFailed = wb.createFont();
		fontFailed.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontFailed.setColor(FAIL_COLOR);
		failed.setFont(fontFailed);

		// Define Warning Style
		warning = wb.createCellStyle();
		Font fontWarn = wb.createFont();
		fontWarn.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontWarn.setColor(WARN_COLOR);
		warning.setFont(fontWarn);

		// Define Info style
		info = wb.createCellStyle();
		Font infoFont = wb.createFont();
		infoFont.setColor(INFO_COLOR);
		info.setFont(infoFont);
	}


	/**
	 * Writes a generic Info message to the log.
	 * 
	 * @param text
	 *            Message to write to the log.
	 */
	public void write(String text) {
		this.writeInfo(text, this.info, this.InfoTag, null);
	}

	/**
	 * Method for writing to the log file, including a step name and a pass or
	 * fail for the step
	 * 
	 * @param description
	 *            Log item description
	 * @param testData
	 *            The test data
	 * @param passed
	 *            Passing or failing for this test step
	 */
	public void write(String description, String testData, Boolean passed) throws IOException {
		// String for determining the text to be entered or the Pass/Fail field
		String pfValue;
		CellStyle style;
		if (passed.booleanValue()) {
			pfValue = "Pass";
			style = this.passed;
			this.passCount++;
		} else {
			pfValue = "Fail";
			style = this.failed;
			this.failCount++;
		}

		// Create the row and populate the data
		Row stepRow = this.sheet.createRow(row);

		// Line number
		Cell lineNum = stepRow.createCell(STEP_COLUMN);
		lineNum.setCellType(Cell.CELL_TYPE_NUMERIC);
		lineNum.setCellValue(this.step);

		// Description
		Cell desc = stepRow.createCell(STEP_DESC_COL);
		desc.setCellValue(description);
		desc.setCellStyle(style);

		// Test data
		Cell tData = stepRow.createCell(STEP_DATA_COL);
		tData.setCellValue(testData);
		tData.setCellStyle(style);

		// Pass/Fail
		Cell pfFlag = stepRow.createCell(STEP_PASS_FAIL);
		pfFlag.setCellValue(pfValue);
		pfFlag.setCellStyle(style);

		this.row++;
		this.step++;
	}

	
public void writeToSuite(String description, String TCPassed,String TCFailed, String Final) throws IOException {
		// String for determining the text to be entered or the Pass/Fail field
		String pfValue;
		CellStyle style;
		
	if (Final.equalsIgnoreCase("PASSED")) {
			pfValue = "Pass";
			style = this.passed;
			this.passCount++;
			FinalPassed++;
			this.recordsPassed++;
		} else {
			pfValue = "Fail";
			style = this.failed;
			this.failCount++;
			FinalPassed++;
			this.recordsFailed++;
		}

		// Create the row and populate the data
		Row stepRow = this.sheet.createRow(row);

		// Line number
		Cell lineNum = stepRow.createCell(STEP_COLUMN);
		lineNum.setCellType(Cell.CELL_TYPE_NUMERIC);
		lineNum.setCellValue(this.step);

		// Description
		Cell desc = stepRow.createCell(STEP_DESC_COL);
		desc.setCellValue(description);
		desc.setCellStyle(style);

		// Test Case Passed
		Cell TCpassed = stepRow.createCell(STEP_DATA_COL);
		TCpassed.setCellValue(TCPassed);
		TCpassed.setCellStyle(style);

		// Test Case Failed
		Cell pfFlag = stepRow.createCell(STEP_PASS_FAIL);
		pfFlag.setCellValue(TCFailed);
		pfFlag.setCellStyle(style);
		
		// Final Results
		Cell FResults = stepRow.createCell(STEP_RECORD_STATUS);
		FResults.setCellValue(Final);
		FResults.setCellStyle(style);

		this.row++;
		this.step++;
	}

	/**
	 * Custom info message to be written to the console. The available colors
	 * are: <br>
	 * BLACK, WHITE, RED, BRIGHT_GREEN, BLUE, YELLOW, PINK, TURQUOISE, DARK_RED,
	 * GREEN, DARK_BLUE, DARK_YELLOW, VIOLET, TEAL, GREY_25_PERCENT,
	 * GREY_50_PERCENT, CORNFLOWER_BLUE, MAROON, LEMON_CHIFFON, ORCHID, CORAL,
	 * ROYAL_BLUE, LIGHT_CORNFLOWER_BLUE, SKY_BLUE, LIGHT_TURQUOISE,
	 * LIGHT_GREEN, LIGHT_YELLOW, PALE_BLUE, ROSE, LAVENDER, TAN, LIGHT_BLUE,
	 * AQUA, LIME, GOLD, LIGHT_ORANGE, ORANGE, BLUE_GREY, GREY_40_PERCENT,
	 * DARK_TEAL, SEA_GREEN, DARK_GREEN, OLIVE_GREEN, BROWN, PLUM, INDIGO,
	 * GREY_80_PERCENT, and AUTOMATIC
	 * 
	 * @param text
	 *            Message to be written to the log
	 * @param textFontColor
	 *            The color of the text in the log
	 * @param bgColor
	 *            The color of the cell background of this log message
	 * @param bold
	 *            {@code true} for bold, {@code false} otherwise
	 */
	public void write(String text, String textFontColor, String bgColor, boolean bold) {
		// Define the style to pass to writeInfo
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();

		// Define the font to be bold if requested
		if (bold) {
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}

		// Define the text color if possible
		try {
			Class<?> c1 = Class.forName("org.apache.poi.ss.usermodel.IndexedColors");
			Field fontColor = c1.getDeclaredField(textFontColor.toUpperCase());
			font.setColor(((IndexedColors) fontColor.get(null)).getIndex());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Define background color
		try {
			Class<?> c1 = Class.forName("org.apache.poi.ss.usermodel.IndexedColors");
			Field fontColor = c1.getDeclaredField(bgColor.toUpperCase());
			style.setFillForegroundColor(((IndexedColors) fontColor.get(null)).getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		} catch (Exception e) {
			e.printStackTrace();
		}

		style.setFont(font);
		//this.writeInfo(text, style, InfoTag, null); // InfoTag replaced by ""
		this.writeInfo(text, style, null);
	}

	/**
	 * Performs the task of writing information to the log.
	 * 
	 * @param text
	 *            Text to be entered into the info tag
	 * @param style
	 *            The {@link CellStyle} to format the cell
	 * @param tag
	 *            Text to be entered as the pass/fail tag
	 * @param tagStyle
	 *            The {@link CellStyle} defining the format for the Pass/Fail
	 *            column. For the default format, set to {@code null}
	 */
	private void writeInfo(String text, CellStyle style, String tag, CellStyle tagStyle) {
		Row infoRow = sheet.createRow(row);
			
		infoRow.createCell(STEP_COLUMN).setCellValue(step); 

		Cell tagCell = infoRow.createCell(this.STEP_PASS_FAIL);
		tagCell.setCellValue(tag);
		if (tagStyle != null) {
			tagCell.setCellStyle(tagStyle);
		}

		Cell infoCell = infoRow.createCell(STEP_DESC_COL);
		infoCell.setCellValue(text);
		infoCell.setCellStyle(style);

		sheet.addMergedRegion(new CellRangeAddress(row, row, STEP_DESC_COL, STEP_DATA_COL));
		row++;
		step++;
	}
	
	//For Summary of Results
	
	private void writeInfo(String text, CellStyle style, CellStyle tagStyle) {
				
		Row infoRow = sheet.createRow(row);
			
		infoRow.createCell(STEP_COLUMN).setCellValue(""); 

		Cell tagCell = infoRow.createCell(this.STEP_PASS_FAIL);
		//tagCell.setCellValue(tag);
		if (tagStyle != null) {
			tagCell.setCellStyle(tagStyle);
		}

		Cell infoCell = infoRow.createCell(STEP_DESC_COL);
		infoCell.setCellValue(text);
		infoCell.setCellStyle(style);

		sheet.addMergedRegion(new CellRangeAddress(row, row, STEP_DESC_COL, STEP_DATA_COL));
		row++;
		//step++;
	}

	public void writeSubSection(String description, String testData, Boolean passed) throws IOException {
		String pfValue;
		CellStyle style;
		if (passed.booleanValue()) {
			pfValue = "Pass";
			style = this.passed;
			this.passCount++;
		} else {
			pfValue = "Fail";
			style = this.failed;
			this.failCount++;
		}		

		// Create the row and populate the data
		Row stepRow = this.sheet.createRow(row);

		// Description
		Cell desc = stepRow.createCell(STEP_DESC_COL);
		desc.setCellValue("          " + description);
		desc.setCellStyle(style);

		// Test data
		Cell tData = stepRow.createCell(STEP_DATA_COL);
		tData.setCellValue(testData);
		tData.setCellStyle(style);

		// Pass/Fail
		Cell pfFlag = stepRow.createCell(STEP_PASS_FAIL);
		pfFlag.setCellValue(pfValue);
		pfFlag.setCellStyle(style);

		this.row++;
	}

	public void writeSubSection(String text) {
		this.writeInfoSubSection(text, this.info, this.InfoTag, null);
	}

	private void writeInfoSubSection(String text, CellStyle style, String tag, CellStyle tagStyle) {
		Row infoRow = sheet.createRow(row);
		// infoRow.createCell(STEP_COLUMN).setCellValue(step);

		Cell tagCell = infoRow.createCell(this.STEP_PASS_FAIL);
		tagCell.setCellValue(tag);
		if (tagStyle != null) {
			tagCell.setCellStyle(tagStyle);
		}

		Cell infoCell = infoRow.createCell(STEP_DESC_COL);
		infoCell.setCellValue("          " + text);
		infoCell.setCellStyle(style);

		sheet.addMergedRegion(new CellRangeAddress(row, row, STEP_DESC_COL, STEP_DATA_COL));
		row++;
		// step++;
	}

	public void writeSubSection_RecordStatus() throws IOException {

		String pfValue;
		CellStyle style;

		if (this.failCount > 0) {
			pfValue = "Fail";
			style = this.failed;
			this.recordsFailed++;
		} else {
			pfValue = "Pass";
			style = this.passed;
			this.recordsPassed++;
		}

		// Create the row and populate the data
		Row stepRow = this.sheet.createRow(row);

		// Description
		Cell desc = stepRow.createCell(STEP_DESC_COL);
		desc.setCellValue("          Record Status");
		desc.setCellStyle(style);

		// Record Status
		Cell pfFlag = stepRow.createCell(STEP_RECORD_STATUS);
		pfFlag.setCellValue(pfValue);
		pfFlag.setCellStyle(style);

		this.row++;

		this.failCount = 0;
		this.passCount = 0;
	}

	/**
	 * Writes a warning message to the log
	 * 
	 * @param text
	 *            {@link String} containing text to write in the warning tag
	 */
	public void writeWarning(String text) {
		this.warnCount++;
		this.writeInfo(text, this.warning, "Warning", this.warning);
	}

	/**
	 * Captures a snapshot of the entire desktop and stores the image in the log
	 * 
	 * @param filePath
	 *            {@link String} argument specifying the temporary location of
	 *            the snapshot image.
	 * @param fileName
	 *            {@link String} argument specifying the base name of the
	 *            temporary image. This file will be deleted once the image is
	 *            added to the Excel log
	 */
	public void takeSnapshot(String filePath, String fileName) {

		// Define the Date & Time stamp for the image
		SimpleDateFormat date = new SimpleDateFormat("EEE, MMM d ''yy hh_mm_ss a");
		String formattedDate = date.format(new Date());

		try {
			// Use the Robot class to capture the snapshot
			Robot robot = new Robot();
			File imgFile = new File(filePath + "\\" + fileName + "_" + formattedDate + ".jpeg");

			// Write the snapshot out to a file
			BufferedImage screenShot = robot
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "jpeg", imgFile);

			int pictureIdx = 0;

			// add picture data to this workbook.
			InputStream is;
			try {
				is = new FileInputStream(imgFile.getAbsolutePath());
				byte[] bytes = IOUtils.toByteArray(is);
				pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
				is.close();
				imgFile.delete(); // Uncomment this if you want the image to be
									// deleted
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();

			// Set to the top-left corner and define the size of the picture
			anchor.setCol1(STEP_DESC_COL);
			anchor.setRow1(row);
			anchor.setCol2(STEP_PASS_FAIL);
			anchor.setRow2(row + 30);

			drawing.createPicture(anchor, pictureIdx);

			row += 30;
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Captures an image of the webpage currently loaded in the
	 * {@link WebDriver} and inserts it into the log.
	 * 
	 * @param driver
	 *            {@link WebDriver} containing page for image capture
	 */
	public void takeSnapshot(WebDriver driver) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		int pictureIdx = 0;
		try {
			InputStream is = new FileInputStream(scrFile);
			byte[] bytes = IOUtils.toByteArray(is);
			pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();

		// Set to the top-left corner and define the size of the picture
		anchor.setCol1(STEP_DESC_COL);
		anchor.setRow1(row);
		anchor.setCol2(STEP_PASS_FAIL);
		anchor.setRow2(row + 30);

		drawing.createPicture(anchor, pictureIdx);

		row += 30;
	}

	/**
	 * Closes and saves the log
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		summaryPassed = this.recordsPassed;
		summaryFailed = this.recordsFailed;
		String finalRslt ="";
		++row;  //For inserting blank row before Summary of Test Results
		// Write the counts to the log
		this.write("Summary of Test Results", WHITE, BLUE, true);
		//this.writeInfoSummary("Records Passed:  " + this.recordsPassed, this.passed, "", null);
		//this.writeInfoSummary("Records Failed:  " + this.recordsFailed, this.failed, "", null);
		//this.writeInfoSummary("Warning Count: " + this.warnCount, this.warning,"", null);
		this.writeInfo("Records Passed:  " + this.recordsPassed, this.passed, null);
		this.writeInfo("Records Failed:  " + this.recordsFailed, this.failed, null);
		this.writeInfo("Warning Count: " + this.warnCount, this.warning, null);
		// Line Number
		Row stepRow = this.sheet.createRow(row);

		// Description Column
		Cell desc = stepRow.createCell(this.STEP_DESC_COL);
		desc.setCellValue("Final Result: ");

		CellStyle descStyle = wb.createCellStyle();
		descStyle.cloneStyleFrom(this.info);
		Font bold = wb.createFont();
		bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		descStyle.setFont(bold);
		desc.setCellStyle(descStyle);

		// Test Data
		Cell testData = stepRow.createCell(this.STEP_DATA_COL);

		if (this.recordsFailed > 0) {
			finalRslt = "FAILED";
			testData.setCellValue("FAILED");
			testData.setCellStyle(this.failed);
		} else if (this.warnCount > 0) {
			finalRslt = "WARNING";
			testData.setCellValue("WARNING");
			testData.setCellStyle(this.warning);
		} else {
			finalRslt = "PASSED";
			testData.setCellValue("PASSED");
			testData.setCellStyle(this.passed);
		}
		FinalResult = finalRslt;
		FileOutputStream out = new FileOutputStream(xlsxFile);
		this.wb.write(out);
		out.close();
	}
}
