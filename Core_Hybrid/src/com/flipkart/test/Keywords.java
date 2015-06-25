package com.flipkart.test;
import static com.flipkart.test.DriverScript.APP_LOGS;
import static com.flipkart.test.DriverScript.CONFIG;
import static com.flipkart.test.DriverScript.OR;
import static com.flipkart.test.DriverScript.currentSuiteXLS;
import static com.flipkart.test.DriverScript.currentTestCase;
import static com.flipkart.test.DriverScript.currentTestDataSetId;
import static com.flipkart.test.DriverScript.keywordExecutionResult;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.apache.commons.io.FileUtils;


public class Keywords{
	
	public WebDriver driver;	
	
	public String openBrowser(String object, String data){		
		APP_LOGS.debug("Opening " + data + " browser");
		if(CONFIG.getProperty(data).equals("Mozilla")){
			driver= new FirefoxDriver();
		}else if(CONFIG.getProperty(data).equals("IE")){
			driver= new InternetExplorerDriver();
		}else if(CONFIG.getProperty(data).equals("Chrome")){
			driver= new ChromeDriver();
		}
		
		//implicit wait
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return Constants.KEYWORD_PASS;
	}
	
	public String navigate(String object, String data){
		APP_LOGS.debug("Navigating to url " + data);
		try{
			driver.navigate().to(CONFIG.getProperty(data));
		} catch(Exception e){
			return Constants.KEYWORD_FAIL + " not able to navigate";
		}
		
		return Constants.KEYWORD_PASS;
	}
	
	public String clickLink(String object, String data){		
		APP_LOGS.debug("Clicking on the link");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).click();			
		} catch(Exception e){
			return Constants.KEYWORD_FAIL + " not able to click on link " + object;				
		}		
		
		return Constants.KEYWORD_PASS;
	}
	
	public String verifyLinkText(String object, String data){
		APP_LOGS.debug("Verifying link text");
		try{
			if(driver.findElement(By.xpath(OR.getProperty(object))).getText().trim().equals(OR.getProperty(data).trim())){
				return Constants.KEYWORD_PASS;
			}
			else {
				return Constants.KEYWORD_FAIL;
			}
		}catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}		
	}
	
	public String clickButton(String object, String data){
		APP_LOGS.debug("Clicking button");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}		
		
		return Constants.KEYWORD_PASS;
	}
	
	public String verifyButtonText(String object, String data){
		APP_LOGS.debug("Verifying button text");
		try{
			if(driver.findElement(By.xpath(OR.getProperty(object))).getText().equals(OR.getProperty(data))){
				return Constants.KEYWORD_PASS;
			} else {
				return Constants.KEYWORD_FAIL;
			}
		}catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}		
	}
	
	public String writeInInput(String object, String data){
		APP_LOGS.debug("Writing " + data + "in input box");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		}catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}	
		
		return Constants.KEYWORD_PASS;
	}
	
	public String verifyText(String object, String data){
		APP_LOGS.debug("Verifying text" + data);
		try{
			if(driver.findElement(By.xpath(object)).getText().equals(OR.getProperty(data))){
				return Constants.KEYWORD_PASS;
			}
			else{
				return Constants.KEYWORD_FAIL;
			}				
		} catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}		
	}
	
	public String closeBroswer(String object, String data){
		APP_LOGS.debug("Closing bowser");
		try{
			driver.quit();
		} catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}
		return Constants.KEYWORD_PASS;
	}
	
	public String selectList(String object, String data){
		APP_LOGS.debug("Selecting " + data + " from the list dropdown.");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		}catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}	
		
		return Constants.KEYWORD_PASS;
	}
	
	public String verifyListSelection(String object, String data){
		APP_LOGS.debug("Verifying selected option in the list drop down");
		try{
			List<WebElement> listOptions = driver.findElement(By.xpath(OR.getProperty(object))).findElements(By.tagName("option"));
			for(int i=0; i< listOptions.size(); i++){
				if(listOptions.get(i).getText().equals(OR.getProperty(data)) && listOptions.get(i).isSelected()){
					return Constants.KEYWORD_PASS;				
				}
			}
		} catch(Exception e){
			return Constants.KEYWORD_FAIL;
		}
		return Constants.KEYWORD_FAIL;
	}
	
	public String verifyAllListElements(String object, String data){
		APP_LOGS.debug("Verifying all the elements present in the list");
		boolean flag = false;
	    String expectedListElements[] = (OR.getProperty(data)).split(",");
		try{
			List<WebElement> listOptions = driver.findElement(By.xpath(OR.getProperty(object))).findElements(By.tagName("option"));
			if(listOptions.size() != expectedListElements.length){
				return Constants.KEYWORD_FAIL;
			}
			
			for (int i =0; i<=listOptions.size(); i++){
				for (int j=0; j<= expectedListElements.length; j++){
					if(listOptions.get(i).getText().equals(expectedListElements[j])){
						flag = true;
					}
				}
				if (!flag){
					return Constants.KEYWORD_FAIL;
				}
				flag = false;
			}			
		} catch(Exception e){
			return Constants.KEYWORD_FAIL;
		}
		return Constants.KEYWORD_PASS;
	}	
		
	public String selectRadio(String object, String data){
		APP_LOGS.debug("Selecting radio button");
		boolean checked;
		try{
			checked = driver.findElement(By.xpath(OR.getProperty(object))).isSelected();
			if(checked)
				driver.findElement(By.xpath(object)).click();
		} catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}		
		return Constants.KEYWORD_PASS;
	}
	
	public String verifyRadioSelected(String object, String data){
		APP_LOGS.debug("Verifying selected radio button");
		boolean checked;
		try{
			checked = driver.findElement(By.xpath(OR.getProperty(object))).isSelected();
		}catch (Exception e){
        	return Constants.KEYWORD_FAIL;
        }
        if (checked)
        	return Constants.KEYWORD_PASS;
        else
        	return Constants.KEYWORD_FAIL;
	}

	public String checkCheckBox(String object, String data){
		APP_LOGS.debug("Selecting checkbox");
		boolean checked;
		try{
			checked = driver.findElement(By.xpath(OR.getProperty(object))).isSelected();
			if(!checked)
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}		
		return Constants.KEYWORD_PASS;
	}
	
	public String unCheckCheckBox(String object, String data){
		APP_LOGS.debug("Deselecting checkbox");
		boolean checked;
		try{
			checked = driver.findElement(By.xpath(OR.getProperty(object))).isSelected();
			if (checked)
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch (Exception e){
        	return Constants.KEYWORD_FAIL;
        }
       	return Constants.KEYWORD_PASS;
	}
	
	public String pause(String object, String data) throws InterruptedException{
		APP_LOGS.debug("Pausing for " +  object + "sec");
		long time = (long)Double.parseDouble(object);
		Thread.sleep(time*1000L);
		return Constants.KEYWORD_PASS;		
	}
	
	public String click(String object, String data){
		APP_LOGS.debug("Clicking on web element");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}
		return Constants.KEYWORD_PASS;
	}
	
	public String verifyTitle(String object, String data){
		APP_LOGS.debug("Verifying title of the page");
		try{
			driver.getTitle().equals(OR.getProperty(data));
		} catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}
		return Constants.KEYWORD_PASS;
	}
	
	public String exist(String object, String data){
		APP_LOGS.debug("Verifying link text");
		try{
			driver.findElement(By.xpath(OR.getProperty(object)));
		} catch (Exception e){
			return Constants.KEYWORD_FAIL;
		}
		return Constants.KEYWORD_PASS;
	}
	
	//to be used in the situation where we are sure that the page is going to open successfully.
	public String synchronize(String object, String data){
		APP_LOGS.debug("Waiting till page loads completely");
		((JavascriptExecutor) driver).executeScript(
				"function pageloadingtime()"+
				"{"+
				"return 'Page has loaded completely.'" +
				"}"+				
				"return (window.onload=pageloadingtime());");
		return Constants.KEYWORD_PASS;
	}	
	
	//**************Application Specific keywords****************
	
	public String validateLogin(String object, String data){
		APP_LOGS.debug("Validating login");
		String dataFlag =currentSuiteXLS.getCellData(currentTestCase, Constants.DATA_CORRECTNESS, currentTestDataSetId);
		try{
			while(driver.findElements(By.xpath(OR.getProperty(object))).size() !=0){
				String visibility = driver.findElement(By.xpath(object)).getAttribute("style");
				if(visibility.indexOf("hidden") != -1){
					String result = verifyText(OR.getProperty("Error_login"), OR.getProperty("Error_login_text"));
					if(result.equals(Constants.KEYWORD_PASS)){
						if (dataFlag.equals("Y")){
							return Constants.KEYWORD_FAIL;
						} else {
							return Constants.KEYWORD_PASS;	
						}
						
					}
				}
			} 
		} catch (Exception e){

		}
		
		if (dataFlag.equals("Y")){
			return Constants.KEYWORD_PASS;
		} else {
			return Constants.KEYWORD_FAIL;	
		}	
	}
	
	public String verifySearchResults(String object, String data){
		APP_LOGS.debug("validating search results");
		System.out.println(data);
		for(int i=1; i<=3; i++){
			String x = OR.getProperty("Search_result_link_start") + i + OR.getProperty("Search_result_link_end");
			if(!(driver.findElement(By.xpath(x)).getText().indexOf(data) != -1)){
				return Constants.KEYWORD_FAIL;	
			}
		}		
		return Constants.KEYWORD_PASS;
	}
	
	//not a keyword to be executed from the test steps.
	public void captureScreenshots(String filename, String data) throws IOException{
		if(CONFIG.getProperty("screenshot_everystep").equals("Y")){
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"\\screenshots\\" + filename + ".jpg"));
						
		} else if (keywordExecutionResult.startsWith(Constants.KEYWORD_FAIL) && CONFIG.getProperty("screenshot_error").equals("Y")){
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"\\screenshots\\" + filename + ".jpg"));
		}		
	}
}

