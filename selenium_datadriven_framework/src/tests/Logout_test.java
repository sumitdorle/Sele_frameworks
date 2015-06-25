package tests;

import java.io.IOException;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import util.TestUtil;

public class Logout_test extends TestBase{
	@Before
	public void beforeTest() throws IOException{
		initialize();
		if(TestUtil.isSkip("Logout_test"))
			Assume.assumeTrue(false);			
	}	

	@Test
	public void loginTest() throws InterruptedException{
		driver.get(CONFIG.getProperty("testSiteName"));
		driver.manage().window().maximize();
		
		TestUtil.doLogin("sumitdorle@gmail.com", "Mirror@23");
		TestUtil.doLogout();
		if(isLoggedIn){
			System.out.println("Test Failed");
		}
	}
}
