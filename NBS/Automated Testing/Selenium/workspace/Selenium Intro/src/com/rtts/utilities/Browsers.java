package com.rtts.utilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public enum Browsers {
	FIREFOX(1), INTERNETEXPLORER(2), CHROME(3), UNIT(4), SAFARI(5);

	public static List<Browsers[]> chooseBrowsersForExectution() {
		Browsers[][] data = { { Browsers.FIREFOX }, { Browsers.CHROME }// ,
				// {Browsers.INTERNETEXPLORER }
		};

		return Arrays.asList(data);
	}

	private int driver;

	/**
	 * Constructor create appropriate {@link WebDriver}
	 * 
	 * @param driverNum
	 *            {@code int} that determines which WebDriver is created.
	 */
	private Browsers(int driverNum) {
		this.driver = driverNum;
	}

	/**
	 * Returns the {@linkplain WebDriver} referenced by the Enum.
	 * 
	 * @return {@link WebDriver} referenced by the selected {@link Enum}
	 */
	public WebDriver getDriver() {
		switch (driver) {
		case 1:
			return new FirefoxDriver();
		case 2:
			System.setProperty("webdriver.ie.driver", "C:\\resources\\IEDriverServer.exe");// "<PATH
																							// TO
																							// LOCATION>\\IEDriverServer.exe");
			return new InternetExplorerDriver();
		case 3:
			System.setProperty("webdriver.chrome.driver", "C:\\resources\\chromedriver.exe");// "<PATH
																								// TO
																								// LOCATION>\\chromedriver.exe");
			return new ChromeDriver();
		// case 4:break;
		// return new HtmlUnitDriver(true);
		case 5:
			return new SafariDriver();
		default:
			return null;
		}
	}

	public WebDriver getRemoteDriver() {
		switch (driver) {
		case 1:
			try {
				DesiredCapabilities capability = DesiredCapabilities.firefox();
				return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		case 2:
			try {
				DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
				System.setProperty("webdriver.chrome.driver", "C:\\resources\\IEServerDriver.exe");// "<PATH
																									// TO
																									// LOCATION>\\chromedriver.exe");
				return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		case 3:
			try {

				DesiredCapabilities capability = DesiredCapabilities.chrome();
				System.setProperty("webdriver.chrome.driver", "C:\\resources\\chromedriver.exe");// "<PATH
																									// TO
																									// LOCATION>\\chromedriver.exe");
				return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// case 4:
			// break;//return new HtmlUnitDriver(true);
		case 5:
			return new SafariDriver();
		default:
			return null;
		}

	}

}
