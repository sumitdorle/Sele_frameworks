package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

	
@RunWith(Suite.class)
@SuiteClasses({
	Register_test.class,
	LoginTest.class,
	Logout_test.class
})
	
public class TestSuite_Runner {
		
}
