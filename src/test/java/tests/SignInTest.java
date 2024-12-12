package tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.MyWebsitesPage;
import pages.SignInPage;
import testUtility.BaseTest;

public class SignInTest extends BaseTest
{
	MyWebsitesPage myWebsitesPage;
	SignInPage signInPage;
	String userName;
	String pwd;

	@Test
	public void toVerifyLoginUsingEmail() throws InterruptedException, IOException
	{
		userName=readPropertiesFile("userName");
		pwd=readPropertiesFile("password");
		signInPage=homepage.goToSignIn();
		myWebsitesPage= signInPage.signIn(userName,pwd);
		String profileIcon=myWebsitesPage.getProfileButton();
		Assert.assertTrue(profileIcon!=null);	
	}
	 
//	 @Test //(invocationCount = 500)  // Runs the test 500 times
//	 public void signIn() throws InterruptedException
//	 {
//		 myWebsitesPage.goToLogout();
//		 Thread.sleep(2000);
//		 signInPage.signIn(userName, pwd);
////		 String profileIcon=myWebsitesPage.getProfileButton();
////		 Assert.assertTrue(profileIcon!=null);	
//	 }
	
}
