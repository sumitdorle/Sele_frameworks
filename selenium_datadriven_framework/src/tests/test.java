package tests;

import datatable.Xls_Reader;

public class test {

	public static void main(String[] args) {
		Xls_Reader datatable = new Xls_Reader(System.getProperty("user.dir")+"//src//config//Suite.xls");
	
		
		for(int rowNum =2; rowNum< datatable.getRowCount("Test Cases"); rowNum++){
			if(datatable.getCellData("Test Cases", "TCID", rowNum).equals("Register_test")){
				if(datatable.getCellData("Test Cases", "Runmode", rowNum).equals("Y")){
					System.out.println("pass");
				}
			}
		}
	}
}
