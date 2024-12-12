package tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.Homepage;
import pages.ManageAccountPage;
import pages.MyWebsitesPage;
import pages.SignInPage;
import testUtility.BaseTest;

public class ChangePasswordTest extends BaseTest
{
	MyWebsitesPage myWebsitesPage;
	SignInPage signInPage;
	String old;
	
	@Test
	public void toVerifySetNewPassword() throws IOException, InterruptedException
	{
		String userName=readPropertiesFile("userName");
		String pwd=readPropertiesFile("password");
		old=pwd;
		String expected=readPropertiesFile("pwdResetSuccessToast");
		String newPwd=generateNewPassword(10);
		signInPage=homepage.goToSignIn();
		myWebsitesPage=signInPage.signIn(userName,pwd);
		ManageAccountPage manageAccountPage=myWebsitesPage.goToManageAccount();
		manageAccountPage.enterCurrentPassword(readPropertiesFile("password"));
		manageAccountPage.enterNewPassword(newPwd);
		manageAccountPage.submitPassword();
		String actual=manageAccountPage.getSuccessToastMessage();
		Assert.assertEquals(actual, expected);
		updatePropertiesFile("password", newPwd);		
	}
	
	@Test(dependsOnMethods = "toVerifySetNewPassword")
	public void toVerifyLoginUsingOldPassword() throws InterruptedException, IOException
	{
		String userName=readPropertiesFile("userName");
		String expected=readPropertiesFile("invalidLoginCredToast");
		
		Homepage homepage=myWebsitesPage.goToLogout();
		homepage.goToSignIn();
		signInPage.signIn(userName,old);
		String actual=signInPage.getInvalidCredsToast();
		Assert.assertEquals(actual, expected);
	}
	
	@Test(dependsOnMethods = {"toVerifySetNewPassword","toVerifyLoginUsingOldPassword"})
	public void toVerifyNewPassword() throws InterruptedException, IOException
	{
		String userName=readPropertiesFile("userName");
		String pwd=readPropertiesFile("password");
		signInPage.signIn(userName,pwd);
		String icon=myWebsitesPage.getProfileButton();
	 	Assert.assertTrue(icon!=null);
	}
	
}
