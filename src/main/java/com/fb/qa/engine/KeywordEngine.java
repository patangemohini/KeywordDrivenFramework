package com.fb.qa.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.fb.qa.base.Base;

public class KeywordEngine {

	public static WebDriver driver;
	public static Properties prop;
	public static Workbook workbook;
	public static Sheet sheet;
	public WebElement element;
	public Base base;
	public final String SCENARIO_SHEET_PATH = "D:\\FaceBookKeywordDriven\\src\\main\\java\\com\\fb\\qa\\scenario\\fb_scenarios.xlsx";

	public void startExecution(String sheetName) {
		String locatorName = null;
		String locatorValue = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(SCENARIO_SHEET_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			workbook = WorkbookFactory.create(fileInputStream);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sheet = workbook.getSheet(sheetName);
		int k = 0;
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			try {
				String locatorColValue = sheet.getRow(i + 1).getCell(k + 1).toString().trim();
				if (!locatorColValue.equalsIgnoreCase("NA")) {
					locatorName = locatorColValue.split("=")[0].trim(); // id
					locatorValue = locatorColValue.split("=")[1].trim(); // email
				}

				String action = sheet.getRow(i + 1).getCell(k + 2).toString().trim();
				String value = sheet.getRow(i + 1).getCell(k + 3).toString().trim();

				switch (action) {
				case "open browser":
					base = new Base();
					base.init_properties();
					if (value.isEmpty() || value.equals("NA")) {
						driver = base.init_driver(prop.getProperty("browser"));
					} else {
						driver = base.init_driver(value);
					}
					break;

				case "enter url":
					if (value.isEmpty() || value.equals("NA")) {
						driver.get(prop.getProperty("url"));
					} else {
						driver.get(value);
					}
					break;
				case "quit":
					driver.quit();
					break;
				default:
					break;
				}

				switch (locatorName) {
				case "id":
					element = driver.findElement(By.id(locatorValue));
					if (action.equalsIgnoreCase("sendkeys")) {
						element.clear();
						element.sendKeys(value);
					} else if (action.equalsIgnoreCase("click")) {
						element.click();
					}
					break;

				case "name":
					element = driver.findElement(By.name(locatorValue));
					if (action.equalsIgnoreCase("click")) {
						element.clear();
						element.sendKeys(value);
					} else if (action.equalsIgnoreCase("click")) {
						element.click();
					}
					break;

				case "linkTest":
					element = driver.findElement(By.linkText(locatorValue));
					element.click();
					locatorName = null;
					break;
				default:
					break;
				}
			} catch (Exception e) {
			}
		}
	}
}
