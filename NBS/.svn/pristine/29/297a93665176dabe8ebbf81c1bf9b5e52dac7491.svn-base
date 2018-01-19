package com.rtts.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class DataFile {

	private FileInputStream inFile;
	private FileOutputStream outFile;
	private Workbook wb;
	private Sheet sheet;
	private Hashtable<String, Integer> headers;
	public String filePath;

	/**
	 * Constructor for creating a proxy to the Excel Worksheet
	 */
	public DataFile(String filePath, String sheetName) {
		try {

			// Open the input stream to the file
			inFile = new FileInputStream(filePath);

			// Open the workbook from the InputStream and open the Worksheet
			wb = WorkbookFactory.create(inFile);
			sheet = wb.getSheet(sheetName);

			// Keep track of the column headers and their indexes
			headers = new Hashtable<String, Integer>();
			Row row = sheet.getRow(0);
			for (int col = 0; col < row.getLastCellNum(); col++) {
				String curr = row.getCell(col).getStringCellValue();
				headers.put(curr, new Integer(col));
				this.filePath = filePath;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Hashtable<String, String> Headers(String sheetName) {

		// To get the numeric value from the cells
		DataFormatter formatter = new DataFormatter(Locale.US);
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		// Open the workbook from the InputStream and open the Worksheet and
		// refresh the formulas in the excel sheet so that unique records are
		// generated
		sheet = wb.getSheet(sheetName);
		XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) wb);

		// Keep track of the column headers and their indexes
		Row heading = sheet.getRow(0);
		Row value = sheet.getRow(1);
		Hashtable<String, String> header = new Hashtable<String, String>();
		for (int col = 0; col < (heading.getLastCellNum()); col++) {
			String title = heading.getCell(col).getStringCellValue();
			String actual = formatter.formatCellValue(value.getCell(col), evaluator);
			header.put(title, actual);
		}
		return header;
	}

	public Hashtable<String, String> HeadersBatchFiles(String sheetName, int row) {// throws
																					// InvalidFormatException,
																					// IOException{
		DataFormatter formatter = new DataFormatter(Locale.US); // To get the
																// numeric value
																// from the
																// cells
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		// Open the workbook from the InputStream and open the Worksheet
		// wb = WorkbookFactory.create(inFile);
		sheet = wb.getSheet(sheetName);
		XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) wb); // to
																			// refresh
																			// the
																			// formulas
																			// in
																			// the
																			// excel
																			// sheet
																			// so
																			// that
																			// unique
																			// records
																			// are
																			// generated
		// Keep track of the column headers and their indexes

		Row heading = sheet.getRow(0);
		Row value = sheet.getRow(row);
		Hashtable<String, String> header = new Hashtable<String, String>();
		for (int col = 0; col < (heading.getLastCellNum()); col++) {
			String title = heading.getCell(col).getStringCellValue();
			// Cell actual_value = value.getCell(col).getStringCellValue();
			String actual = formatter.formatCellValue(value.getCell(col), evaluator);
			System.out.println(title + " " + actual);
			header.put(title, actual);

		}
		return header;

	}

	public Hashtable<String, String> Headers_Conditions(String sheetName, int col) {

		// To refresh the formulas in the excel sheet so that unique records are
		// generated
		sheet = wb.getSheet(sheetName);
		XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) wb);

		// Keep track of the column headers and their indexes
		int noOfRows = sheet.getLastRowNum();
		Hashtable<String, String> header = new Hashtable<String, String>();
		for (int j = 1; j <= noOfRows; j++) {
			String title = sheet.getRow(j).getCell(0).getStringCellValue();
			String value = sheet.getRow(j).getCell(col).getStringCellValue();
			header.put(title, value);
		}
		return header;
	}

	/**
	 * Retrieves data from the data file at the specified column and row
	 * 
	 * @param columnHeader
	 *            The name of the column where the desired data resides
	 * @param rowNumber
	 *            Row number where the data resides
	 */
	public String getDataFromColumn(String columnHeader, int rowNumber) {

		// Get the column number
		Integer colNum = headers.get(columnHeader);
		if (colNum == null) {
			return null;
		} else if (rowNumber > sheet.getLastRowNum()) {
			return null;
		}
		try {
			// Return the data from the specified cell
			Cell cell = sheet.getRow(rowNumber).getCell(colNum.intValue());
			if (cell != null) {
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					return cell.getStringCellValue();
				case Cell.CELL_TYPE_NUMERIC:
					return Double.toString(cell.getNumericCellValue());
				case Cell.CELL_TYPE_BOOLEAN:
					return Boolean.toString(cell.getBooleanCellValue());
				case Cell.CELL_TYPE_BLANK:
					return "";
				case Cell.CELL_TYPE_FORMULA:
					FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();

					switch (eval.evaluateFormulaCell(cell)) {
					case Cell.CELL_TYPE_STRING:
						return cell.getStringCellValue();
					case Cell.CELL_TYPE_NUMERIC:
						return Double.toString(cell.getNumericCellValue());
					case Cell.CELL_TYPE_BOOLEAN:
						return Boolean.toString(cell.getBooleanCellValue());
					case Cell.CELL_TYPE_BLANK:
						return "";
					default:
						break;
					}
				}

			} else
				return null;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * This is to increment the value in the increment column to generate unique
	 * patient records
	 */
	public int setCellValue(String columnHeader, int rowNumber) {
		String Incr = getDataFromColumn(columnHeader, rowNumber);

		if (Incr.contains("."))
			Incr = Incr.substring(0, Incr.length() - 2);
		int incr_value = Integer.parseInt(Incr);
		incr_value = incr_value + 1;
		return incr_value;
	}

	public void updateCellValue(String columnHeader, int rowNumber, int cellValue) {
		Integer colNum = headers.get(columnHeader);
		Cell cell = sheet.getRow(rowNumber).getCell(colNum.intValue());
		cell.setCellValue(cellValue);

	}

	/**
	 * Returns the number of rows
	 * 
	 * @return The number of rows populated in the Excel sheet, including the
	 *         column headers
	 */
	public int getRowCount() {
		// This is for backward compatibility
		return this.sheet.getLastRowNum() + 1;
	}

	public int getColCount() {
		return this.sheet.getRow(0).getLastCellNum();
	}

	/**
	 * Saves the Excel sheet
	 */
	public void SaveFile(String columnHeader, int rowNumber, int cellValue) {
		try {

			// Open the output stream to the file
			String outfile = this.filePath;
			outFile = new FileOutputStream(outfile);
			Integer colNum = headers.get(columnHeader);
			Cell cell = sheet.getRow(rowNumber).getCell(colNum.intValue());
			cell.setCellValue(Integer.toString(cellValue));
			wb.write(outFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the Excel sheet
	 */
	public void close() throws IOException {
		this.inFile.close();
	}
}
