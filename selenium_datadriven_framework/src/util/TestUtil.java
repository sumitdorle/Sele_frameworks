package util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import datatable.Xls_Reader;
import tests.TestBase;

public class TestUtil extends TestBase {
	
	public static void doLogin(String username, String password) throws InterruptedException{
		if (isLoggedIn)
			return;		
		
		//click on login link	
		getObject("Signin_link").click();
		getObject("Email_input").sendKeys(username);
		getObject("Password_input").sendKeys(password);
		getObject("Login_btn").click();	
		
		Thread.sleep(10000);
		try{
			String actual = driver.findElement(By.xpath(OR.getProperty("Signedin_link"))).getText();
			
			if (actual.equals("Hi sumitdorle!")){
				isLoggedIn = true;
			} else {
				isLoggedIn = false;
			}
		}catch(Throwable t){
			isLoggedIn = false;
		}		
	}
	
	public static void doLogout() throws InterruptedException{
		if(!isLoggedIn)
			return;		
		
		WebElement Signedin = driver.findElement(By.xpath("//div[@class='header-links unitExt']/ul/li[7]/a"));
		Actions act = new Actions(driver);
		act.moveToElement(Signedin).build().perform();;
		
		Thread.sleep(2000);
		
		getObject(OR.getProperty("Logout_link")).click();
		
		try{
			String actual = driver.findElement(By.xpath(OR.getProperty("Signedin_link"))).getText();			
			if (actual.equals("Hi sumitdorle!")){
				isLoggedIn = true;
			} 
		}catch(Throwable t){
			isLoggedIn = false;
		}		
	}

	public static boolean isSkip(String testCase) {	
		if (datatable == null){
		    datatable = new Xls_Reader(System.getProperty("user.dir")+"//src//config//Suite.xls");
		}
		
		for(int rowNum =2; rowNum< datatable.getRowCount("Test Cases"); rowNum++){
			if(datatable.getCellData("Test Cases", "TCID", rowNum).equals(testCase)){
				if(datatable.getCellData("Test Cases", "Runmode", rowNum).equals("N"))
					return true;
				else
					return false;
			}
		}
		return false;	
	}
	
	public static Object[][] getData(String testCase){
		if (datatable == null){
		    datatable = new Xls_Reader(System.getProperty("user.dir")+"//src//config//Suite.xls");
		}
		
		int rows = datatable.getRowCount(testCase)-1;
		if (rows<0){
			Object[][] testData = new Object[1][0];
			return testData;
		}
		
		int cols = datatable.getColumnCount(testCase);
		Object data[][] = new Object[rows][cols];
		
		rows = datatable.getRowCount(testCase);
		for(int rowNum=2; rowNum<= rows; rowNum++){
			for (int colNum=0; colNum<cols; colNum++){
				data[rowNum-2][colNum] = datatable.getCellData(testCase, colNum, rowNum);
			}
		}
		return data;		
	}
	
}
