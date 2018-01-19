package com.nbs.common;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;
import com.nbs.common.Common_Utils;

public class ReadMetaData {

	public WebDriver driver;
	public int rowNum;
	public Robot robot = null;
	boolean iselementdisplayed, iselementpopulated;
	public Log log;
	DataFile df;
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

	// @Test
	public void ReadData(int col) throws Exception {
		
		comUtils.setUpDriver(driver);
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		rowNum = df.getRowCount();
		System.out.println("number of rows" + rowNum);
		String Field_Value_Header = df.getDataFromColumn("Field_Value" + col, 0);
		if (Field_Value_Header != null) {
			for (int row = 1; row < rowNum; row++) {
				try {
					String Field_Label = df.getDataFromColumn("Field_Label", row);
					String Field_Id = df.getDataFromColumn("Field_Id", row);
					String Field_ID_Type = df.getDataFromColumn("Field_ID_Type", row);
					String Field_Value = df.getDataFromColumn("Field_Value" + col, row);
					System.out.println(row);
					System.out.println("Field label" + Field_Label);
					System.out.println("Field ID " + Field_Id);
					System.out.println("Field ID TYpe " + Field_ID_Type);
					System.out.println("Field_Value " + Field_Value);

					switch (Field_ID_Type) {
					case "text_data":
						iselementdisplayed = comUtils.isElementDisplayed(By.id(Field_Id));
						if (iselementdisplayed) {
							try {
								driver.findElement(By.id(Field_Id)).clear();
								driver.findElement(By.id(Field_Id)).sendKeys(Field_Value);
							} catch (Exception e) {
								String msg = e.getMessage();
								if (msg.contains("Element is read-only"))
									break;
							}
						
						}
						break;
					case "coded_data":
						if (Field_Value != null) {
							String Field_Id_updated = Field_Id + "_textbox";
							iselementdisplayed = comUtils.isElementDisplayed(By.name(Field_Id_updated));
							if (iselementdisplayed) {
								driver.findElement(By.name(Field_Id_updated)).clear();
								driver.findElement(By.name(Field_Id_updated)).sendKeys(Field_Value);
								robot.keyPress(KeyEvent.VK_TAB);
							}
						}

						break;

					case "name":
						if (Field_Value != null) {
							iselementdisplayed = comUtils.isElementDisplayed(By.name(Field_Id));
							if (iselementdisplayed) {
								driver.findElement(By.name(Field_Id)).clear();
								driver.findElement(By.name(Field_Id)).sendKeys(Field_Value);
								robot.keyPress(KeyEvent.VK_TAB);
							}
						}

						break;

					case "xpath":
						
						iselementdisplayed = comUtils.isElementDisplayed(By.xpath(Field_Id));
						if (iselementdisplayed) {
							driver.findElement(By.xpath(Field_Id)).clear();
							driver.findElement(By.xpath(Field_Id)).sendKeys(Field_Value);
						}
						
						break;
					case "Tab":
						iselementdisplayed = comUtils.isElementDisplayed(By.xpath(Field_Id));
						if (iselementdisplayed) {
							driver.findElement(By.xpath(Field_Id)).click();
						}
						break;
					case "Click":
						iselementdisplayed = comUtils.isElementDisplayed(By.name(Field_Id));
						if (iselementdisplayed)
							driver.findElement(By.name(Field_Id)).click();
						else if (comUtils.isElementDisplayed(By.id(Field_Id)))
							driver.findElement(By.id(Field_Id)).click();

						break;
					case "ClickIfYes":                            
                        iselementdisplayed =  comUtils.isElementDisplayed(By.name(Field_Id));
                        if (iselementdisplayed){
                               if (Field_Value != null){
                                      if(Field_Value.equalsIgnoreCase("Yes"))
                                            driver.findElement(By.name(Field_Id)).click();
                               }
                        }
                        break;

					case "participation_data":
						String Field_Id_Updated = Field_Id + "Text";
						iselementdisplayed = comUtils.isElementDisplayed(By.id(Field_Id_Updated));
						if (iselementdisplayed) {
							driver.findElement(By.id(Field_Id_Updated)).clear();
							driver.findElement(By.id(Field_Id_Updated)).sendKeys(Field_Value);
							String Field_ID_Button = Field_Id + "CodeLookupButton";
							driver.findElement(By.id(Field_ID_Button)).click();
						}
						break;
					case "participation_data2":
						String Field_Id_Updated2 = Field_Id + "[1]";
						iselementdisplayed = comUtils.isElementDisplayed(By.xpath(Field_Id_Updated2));
						if (iselementdisplayed) {
							driver.findElement(By.xpath(Field_Id_Updated2)).clear();
							driver.findElement(By.xpath(Field_Id_Updated2)).sendKeys(Field_Value);
							String Field_ID_Button = Field_Id + "[2]";
							driver.findElement(By.xpath(Field_ID_Button)).click();
						}
						break;
					case "Repeating_Block":
						iselementdisplayed = comUtils.isElementDisplayed(By.xpath(Field_Id));
						if (iselementdisplayed) {
							driver.findElement(By.xpath(Field_Id)).click();
						}
						break;
					case "Multiple_Select":
						if (Field_Value != null) {
							iselementdisplayed = comUtils.isElementDisplayed(By.id(Field_Id));
							if (iselementdisplayed) {
								String multipleSel[] = Field_Value.split(";");
								for (String valueToBeSelected : multipleSel) {
									valueToBeSelected = valueToBeSelected.trim();
									new Select(driver.findElement(By.id(Field_Id)))
											.selectByVisibleText(valueToBeSelected);
									driver.findElement(By.id(Field_Id)).sendKeys(Keys.CONTROL);
								}

							}
						}
						break;
					case "Named":
						List<WebElement> optionCount = driver.findElements(By.xpath("//*[@id='CON143']/option"));
						int count = optionCount.size();
						driver.findElement(By.xpath("//*[@id='GA17103']/tbody/tr[5]/td[2]/img")).click();
						String named = driver.findElement(By.xpath("//*[@id='CON143']/option[" + count + "]"))
								.getText();
						driver.findElement(By.name("CON143_textbox")).sendKeys(named);
						break;

					case "prepopulated":
						break;
					case "populate":
						break;
					case "Race":
						String[] race = Field_Value.split(",");
						int numberOfCkBoxes = race.length;

						for (int i = 0; i < numberOfCkBoxes; i++) {
							switch (race[i].trim()) {
							case "American Indian or Alaska Native":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.americanIndianAlskanRace")))
									driver.findElement(By.name("pageClientVO.americanIndianAlskanRace")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.americanIndianAlskanRace")))
									driver.findElement(By.name("cTContactClientVO.americanIndianAlskanRace")).click();
								else if (comUtils.isElementDisplayed(By.id("DEM153")))
									driver.findElement(By.id("DEM153")).click();
								else if (comUtils.isElementDisplayed(By.id("americanIndianRace")))
									driver.findElement(By.id("americanIndianRace")).click();
								break;
							case "Asian":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.asianRace")))
									driver.findElement(By.name("pageClientVO.asianRace")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.asianRace")))
									driver.findElement(By.name("cTContactClientVO.asianRace")).click();
								else if (comUtils.isElementDisplayed(By.id("DEM154")))
									driver.findElement(By.id("DEM154")).click();
								else if (comUtils.isElementDisplayed(By.id("asianRace")))
									driver.findElement(By.id("asianRace")).click();
								break;
							case "Black or African American":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.africanAmericanRace")))
									driver.findElement(By.name("pageClientVO.africanAmericanRace")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.africanAmericanRace")))
									driver.findElement(By.name("cTContactClientVO.africanAmericanRace")).click();
								else if (comUtils.isElementDisplayed(By.id("DEM155")))
									driver.findElement(By.id("DEM155")).click();
								else if (comUtils.isElementDisplayed(By.id("africanRace")))
									driver.findElement(By.id("africanRace")).click();
								break;
							case "Native Hawaiian or Other Pacific Islander":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.hawaiianRace")))
									driver.findElement(By.name("pageClientVO.hawaiianRace")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.hawaiianRace")))
									driver.findElement(By.name("cTContactClientVO.hawaiianRace")).click();
								else if (comUtils.isElementDisplayed(By.id("DEM156")))
									driver.findElement(By.id("DEM156")).click();
								else if (comUtils.isElementDisplayed(By.id("hawaiianRace")))
									driver.findElement(By.id("hawaiianRace")).click();
								break;
							case "White":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.whiteRace")))
									driver.findElement(By.name("pageClientVO.whiteRace")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.whiteRace")))
									driver.findElement(By.name("cTContactClientVO.whiteRace")).click();
								else if (comUtils.isElementDisplayed(By.id("DEM157")))
									driver.findElement(By.id("DEM157")).click();
								else if (comUtils.isElementDisplayed(By.id("whiteRace")))
									driver.findElement(By.id("whiteRace")).click();
								break;
							case "Other":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.otherRace")))
									driver.findElement(By.name("pageClientVO.otherRace")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.otherRace")))
									driver.findElement(By.name("cTContactClientVO.otherRace")).click();
								else if (comUtils.isElementDisplayed(By.id("otherRace")))
									driver.findElement(By.id("otherRace")).click();
								break;
							case "Refused to answer":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.refusedToAnswer")))
									driver.findElement(By.name("pageClientVO.refusedToAnswer")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.refusedToAnswer")))
									driver.findElement(By.name("cTContactClientVO.refusedToAnswer")).click();
								else if (comUtils.isElementDisplayed(By.id("refusedToAnswer")))
									driver.findElement(By.id("refusedToAnswer")).click();
								break;
							case "Not Asked":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.notAsked")))
									driver.findElement(By.name("pageClientVO.notAsked")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.notAsked")))
									driver.findElement(By.name("cTContactClientVO.notAsked")).click();
								else if (comUtils.isElementDisplayed(By.id("notAsked")))
									driver.findElement(By.id("notAsked")).click();
								break;
							case "Unknown":
								if (comUtils.isElementDisplayed(By.name("pageClientVO.unKnownRace")))
									driver.findElement(By.name("pageClientVO.unKnownRace")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.unKnownRace")))
									driver.findElement(By.name("cTContactClientVO.unKnownRace")).click();
								else if (comUtils.isElementDisplayed(By.id("DEM152")))
									driver.findElement(By.id("DEM152")).click();
								else if (comUtils.isElementDisplayed(By.id("unknownRace")))
									driver.findElement(By.id("unknownRace")).click();
								break;
							default:
								if (comUtils.isElementDisplayed(By.name("pageClientVO.unKnownRace")))
									driver.findElement(By.name("pageClientVO.unKnownRace")).click();
								else if (comUtils.isElementDisplayed(By.name("cTContactClientVO.unKnownRace")))
									driver.findElement(By.name("cTContactClientVO.unKnownRace")).click();
								else if (comUtils.isElementDisplayed(By.id("DEM152")))
									driver.findElement(By.id("DEM152")).click();
								else if (comUtils.isElementDisplayed(By.id("unknownRace")))
									driver.findElement(By.id("unknownRace")).click();
								break;
							}
						}
						break;
					case "Check_Box":
						iselementdisplayed = comUtils.isElementDisplayed(By.id(Field_Id));
						if (iselementdisplayed) {
							if (Field_Value != null) {
								if ((Field_Value).equalsIgnoreCase("yes")) {
									if (!(driver.findElement(By.id(Field_Id)).isSelected()))
										driver.findElement(By.id(Field_Id)).click();
								} else if ((Field_Value).equalsIgnoreCase("no")) {
									if ((driver.findElement(By.id(Field_Id)).isSelected()))
										driver.findElement(By.id(Field_Id)).click();
								}
							}
						
						}
						break;
					case "Display":
						break;
					case "Sleep":
						Thread.sleep(1000);
					default:
						break;
					}

				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}
	}

	public String getFieldValue(String Field_Name, int col) {
		int rowNum = df.getRowCount();
		String Field_Value = "";

		for (int row = 1; row < rowNum; row++) {
			String Field_Label = df.getDataFromColumn("Field_Label", row);
			if (Field_Label.equals(Field_Name)) {
				Field_Value = df.getDataFromColumn("Field_Value" + col, row);
				break;
			}
		}
		if (Field_Value != null)
		return Field_Value;
		else 
			return "";
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
