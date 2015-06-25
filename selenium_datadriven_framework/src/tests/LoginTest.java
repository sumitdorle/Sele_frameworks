package tests;

import java.io.IOException;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import util.TestUtil;

public class LoginTest extends TestBase{

	@Before
	public void beforeTest() throws IOException{
		initialize();
		if(TestUtil.isSkip("LoginTest"))
			Assume.assumeTrue(false);			
	}	
	
	@Test
	public void loginTest() throws InterruptedException{
		//load website
		driver.get(CONFIG.getProperty("testSiteName"));
		driver.manage().window().maximize();
		
		TestUtil.doLogin("sumitdorle@gmail.com", "Mirror@23");
		if(!isLoggedIn)
		{
			System.out.println("Test Failed");
		}		
	}
}
