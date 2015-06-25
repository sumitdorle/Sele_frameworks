package com.flipkart.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import java.util.Properties;
import org.apache.log4j.Logger;
import com.flipkart.xls.read.Xls_Reader;

public class DriverScript {

	public static Logger APP_LOGS;
	public static Xls_Reader suiteXLS;
	public static int currentSuiteId;
	public static String currentTestSuite;
	public static Xls_Reader currentSuiteXLS;
	public static int currentTestCaseId;
	public static int currentTestStepId;
	public static String currentTestCase;
	public static int currentTestDataSetId;
	public static String currentKeyword;
	public static Keywords keywords =null;
	public static Method method[];
	public static String keywordExecutionResult;
	public static ArrayList<String> resultSet;
	public static String temp[];
	public static String data;
	public static String object;
	public static Method captureScreenshotMethod;
	
	//properties
	public static Properties CONFIG;
	public static Properties OR;

	public DriverScript() throws NoSuchMethodException, SecurityException{
		keywords = new Keywords();
		method = keywords.getClass().getMethods();
		captureScreenshotMethod = keywords.getClass().getMethod("captureScreenshots", String.class, String.class);
	}	
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, NoSuchMethodException, SecurityException {
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"\\src\\com\\flipkart\\config\\config.properties");
		CONFIG = new Properties();
		CONFIG.load(fs);
		
		fs = new FileInputStream(System.getProperty("user.dir")+"\\src\\com\\flipkart\\config\\OR.properties");
		OR = new Properties();
		OR.load(fs);
		
		DriverScript test = new DriverScript();
		test.start();
	}
	
	public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		APP_LOGS = Logger.getLogger("devpinoyLogger");
		suiteXLS = new Xls_Reader(System.getProperty("user.dir")+ "\\src\\com\\flipkart\\xls\\Suite.xlsx");
		
		for (currentSuiteId=2; currentSuiteId<= suiteXLS.getRowCount(Constants.TEST_SUITE_SHEET); currentSuiteId++){
			//current suite
			currentTestSuite = suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.TEST_SUITE_ID, currentSuiteId);					
			if (suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.TEST_SUITE_RUNMODE, currentSuiteId).equals(Constants.RUNMODE_YES)){
				APP_LOGS.debug("********Executing the suite " + suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.TEST_SUITE_ID, currentSuiteId) + "**********");
				currentSuiteXLS = new Xls_Reader(System.getProperty("user.dir")+ "\\src\\com\\flipkart\\xls\\" + currentTestSuite +".xlsx");
				//To access and reference the same variable from keywords.java
				//keywords.currentSuiteXLS=currentSuiteXLS;
				
				//for xls results
				resultSet = new ArrayList<String>();
				//iterating tests
				for (currentTestCaseId=2; currentTestCaseId <= currentSuiteXLS.getRowCount(Constants.TEST_CASE_SHEET); currentTestCaseId++){
					if(currentSuiteXLS.getCellData(Constants.TEST_CASE_SHEET, Constants.TEST_SUITE_RUNMODE, currentTestCaseId).equals(Constants.RUNMODE_YES)){
						APP_LOGS.debug("********Executing the test " + currentSuiteXLS.getCellData(Constants.TEST_CASE_SHEET, Constants.TCID, currentTestCaseId) + "**********");						
						currentTestCase = currentSuiteXLS.getCellData(Constants.TEST_CASE_SHEET, Constants.TCID, currentTestCaseId);
						//keywords.currentTestCase = currentTestCase;
						
						//if the test data is included.
						if(currentSuiteXLS.isSheetExist(currentTestCase)){						
							for (currentTestDataSetId =2; currentTestDataSetId<= currentSuiteXLS.getRowCount(currentTestCase); currentTestDataSetId++){
								if(currentSuiteXLS.getCellData(currentTestCase, Constants.TEST_SUITE_RUNMODE, currentTestDataSetId).equals(Constants.RUNMODE_YES)){
									Execute_keyword();
									CreateXLSReport();
								}
							}
						}
						else
						{	
							Execute_keyword();
							CreateXLSReport();
						}
					}
				}				
			}
		}		
	}
	
	public void Execute_keyword() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for (currentTestStepId=2; currentTestStepId<= currentSuiteXLS.getRowCount(Constants.TEST_STEPS); currentTestStepId++){
			//collecting data corresponding to the variable.
			data = currentSuiteXLS.getCellData(Constants.TEST_STEPS, Constants.DATA, currentTestStepId);
			//if data has to be read from the separate test case level data sheet
			if(data.startsWith("col")){				
				data = currentSuiteXLS.getCellData(currentTestCase, data.split("\\|")[1], currentTestDataSetId);
			}
			
			object = currentSuiteXLS.getCellData(Constants.TEST_STEPS, Constants.OBJECT, currentTestStepId);
			
			if(currentSuiteXLS.getCellData(Constants.TEST_STEPS, Constants.TCID, currentTestStepId).equals(currentTestCase)){
				APP_LOGS.debug("Executing the keyword " + currentSuiteXLS.getCellData(Constants.TEST_STEPS, Constants.KEYWORD, currentTestStepId));
				currentKeyword = currentSuiteXLS.getCellData(Constants.TEST_STEPS, Constants.KEYWORD, currentTestStepId);
				
				Method method[] = keywords.getClass().getMethods();
				for (int i=0; i<method.length; i++){
					if (method[i].getName().equals(currentKeyword)){		
						keywordExecutionResult = (String) method[i].invoke(keywords, object, data);
						System.out.println(currentKeyword + " == " + keywordExecutionResult);
						resultSet.add(keywordExecutionResult);
						captureScreenshotMethod.invoke(keywords, 
								currentTestSuite + "_" + currentTestCase + "_TS" + currentTestStepId + "_" + currentTestDataSetId,
								keywordExecutionResult);						
					}					
				}
			}							 
		}
	}
	
	public void CreateXLSReport(){
		
		String colName=Constants.RESULT +(currentTestDataSetID-1);
		boolean isColExist=false;
		
		for(int c=0;c<currentSuiteXLS.getColumnCount(Constants.TEST_STEPS);c++){
			System.out.println(currentSuiteXLS.getCellData(Constants.TEST_STEPS,c , 2));
			if(currentSuiteXLS.getCellData(Constants.TEST_STEPS,c , 1).equals(colName)){
				isColExist=true;
				break;
			}
		}
		
		if(!isColExist)
			currentSuiteXLS.addColumn(Constants.TEST_STEPS, colName);
		int index=0;
		for(int i=2;i<=currentSuiteXLS.getRowCount(Constants.TEST_STEPS);i++){
			
			if(currentTestCase.equals(currentSuiteXLS.getCellData(Constants.TEST_STEPS, Constants.TCID, i))){
				if(resultSet.size()==0)
					currentSuiteXLS.setCellData(Constants.TEST_STEPS, colName, i, Constants.KEYWORD_SKIP);
				else	
					currentSuiteXLS.setCellData(Constants.TEST_STEPS, colName, i, resultSet.get(index));
				index++;
			}
			
			
		}
		
		if(resultSet.size()==0){
			// skip
			currentSuiteXLS.setCellData(currentTestCase, Constants.RESULT, currentTestDataSetID, Constants.KEYWORD_SKIP);
			return;
		}else{
			for(int i=0;i<resultSet.size();i++){
				if(!resultSet.get(i).equals(Constants.KEYWORD_PASS)){
					currentSuiteXLS.setCellData(currentTestCase, Constants.RESULT, currentTestDataSetID, Constants.KEYWORD_FAIL);
					return;
				}
			}
		}
		currentSuiteXLS.setCellData(currentTestCase, Constants.RESULT, currentTestDataSetID, Constants.KEYWORD_PASS);		
	}
}
