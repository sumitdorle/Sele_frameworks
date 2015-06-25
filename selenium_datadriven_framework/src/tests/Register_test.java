package tests;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;

import util.TestUtil;


@RunWith(Parameterized.class)
public class Register_test extends TestBase {
		
	public String email;
	public String pass;
	public String reppass;	
	
	public Register_test(String email, String pass, String reppass){
		this.email = email;
		this.pass = pass;
		this.reppass = reppass;
	}
		
	@Before
	public void beforeTest() throws IOException{
		initialize();
		if(TestUtil.isSkip("Register_test"))
			Assume.assumeTrue(false);		
	}	

	@Test
	public void Registration_test() throws InterruptedException{
		driver.get(CONFIG.getProperty("testSiteName"));
		driver.manage().window().maximize();
		
		driver.findElement(By.xpath(OR.getProperty("Signup_link"))).click();
		driver.findElement(By.xpath(OR.getProperty("Reg_email_input"))).sendKeys(email);
		driver.findElement(By.xpath(OR.getProperty("Reg_pass_input"))).sendKeys(pass);
		driver.findElement(By.xpath(OR.getProperty("Reg_rep_pass_input"))).sendKeys(reppass);
		driver.findElement(By.xpath(OR.getProperty("Signup_now_btn"))).click();
		
//		getObject(OR.getProperty("Signup_link")).click();
//		getObject(OR.getProperty("Reg_email_input")).sendKeys("acsd333@gmail.com");
//		getObject(OR.getProperty("Reg_pass_input")).sendKeys("pass@A1");
//		getObject(OR.getProperty("Reg_rep_pass_input")).sendKeys("pass@A1");
//		getObject(OR.getProperty("Signup_now_btn")).click();
		
		Thread.sleep(5000);		
		try{
			String actual = driver.findElement(By.xpath(OR.getProperty("Signedin_link"))).getText();			
			if (actual.equals("Hi acsd333!")){
				System.out.println("Test passed");
			} 
		}catch(Throwable t){
			System.out.println("Test failed");
		}		
	}
	
	@Parameters
	public static Collection<Object[]> getData(){
//		Object[][] data = new Object[2][3];
//		data[0][0]="Aadmcmdm@yahoo.com";
//		data[0][1]="Asp@3456";
//		data[0][2]="Asp@3456";
//		
//		data[1][0]="whatevermd@gmail.com";
//		data[1][1]="Ressc#2";
//		data[1][2]="Ressc#2";
		
		Object[][] data = TestUtil.getData("Register_test");			
		
		return Arrays.asList(data);
	}
}
