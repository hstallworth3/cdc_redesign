package com.nbs.common;

import java.util.concurrent.TimeUnit;
//import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class Common_Utils {

	public WebDriver driver;
	public Log log;
	DataFile df;

	public void setUpDriver(WebDriver driver) throws Exception {
		this.driver = driver;
	}

	public void setUpLog(Log logFile) throws Exception {

		this.log = logFile;
	}

	public boolean isElementPresent(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean isElementDisplayed(By by)  {
		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS);
			if (driver.findElement(by).isDisplayed() && driver.findElement(by).isEnabled())
				return true;
			else
				return false;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}

	public boolean isAlertPresent() {
		boolean presentFlag = false;
		try {
			Alert alert = driver.switchTo().alert();
			presentFlag = true;
			alert.accept();
		} catch (NoAlertPresentException ex) {
		}
		return presentFlag;
	}

	public void waitForLoad(WebDriver driver) {
		 Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		    wait.until(new Function<WebDriver, Boolean>() {
		        public Boolean apply(WebDriver driver) {
		            System.out.println("Current Window State       : "
		                + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
		            return String
		                .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
		                .equals("complete");
		        }
		    });

		}
	
	public WebDriver getDriver() throws Exception {
		return driver;
	}

	public Log getLog() throws Exception {
		return log;
	}
}
