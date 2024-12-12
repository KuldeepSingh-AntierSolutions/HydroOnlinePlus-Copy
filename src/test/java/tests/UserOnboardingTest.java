package tests;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddFirstWebsitesPage;
import pages.ConnectSuiWalletPage;
import pages.CreateSuiWalletPage;
import pages.MyWebsitesPage;
import pages.SetPasswordPage;
import pages.SignInPage;
import pages.SignUpPage;
import testUtility.BaseTest;

public class UserOnboardingTest extends BaseTest
{
	Connection connUserService;
	SignInPage signInPage;
	AddFirstWebsitesPage addFirstWebsitePage;
	ConnectSuiWalletPage connectSuiWalletPage;
	CreateSuiWalletPage createSuiWalletPage;
	SetPasswordPage setPasswordPage;
	MyWebsitesPage myWebsitesPage;
	
	@Test
	public void toVerifySignUpUsingEmail() throws IOException, InterruptedException, SQLException
	{
		String user=getRandomString();
		String email=user+"@yopmail.com";
		updatePropertiesFileNew("user", email);
		String pwd=readPropertiesFileNew("pwd");
		SignUpPage signUpPage=homepage.goToSignUp();
		signUpPage.fillForm(user, email, pwd);
		signInPage= signUpPage.submitForm();
		String message=signUpPage.getSignUpToast();
		Thread.sleep(20000);
		connUserService=getInstanceHydroUserServiceDB();
		verifyUserEmail(connUserService, email);
		String query="Select \"isEmailVerified\" From public.\"Users\" where email='"+email+"'";
		Assert.assertEquals((queryDatabase(connUserService, query)),"t","Unable to verify email from the database");
		Assert.assertEquals(message,readPropertiesFileNew("signUpSuccessToast"),"Sign up using email failed: "+message);
	}
	@Test(dependsOnMethods = "toVerifySignUpUsingEmail")
	public void toVerifyAddWebsite() throws IOException, InterruptedException, AWTException
	{
		String email=readPropertiesFileNew("user");
		String pwd=readPropertiesFileNew("pwd");
		addFirstWebsitePage=signInPage.signInOnboarding(email, pwd);
//		String websiteUrl=generateWebsite();
		String websiteUrl="https://netlify.com";
		connectSuiWalletPage=addFirstWebsitePage.addWebsite(websiteUrl);	
		String message=addFirstWebsitePage.getAddWebsiteToast();
		Assert.assertEquals(message, readPropertiesFileNew("addWebsiteSuccessToast"));		
	}
	@Test(dependsOnMethods = "toVerifyAddWebsite")
	public void toVerifyCreateHydroWallet() throws InterruptedException
	{
		createSuiWalletPage=connectSuiWalletPage.createWallet();
		createSuiWalletPage.revealMnemonics();
		createSuiWalletPage.getMnemonics();
		createSuiWalletPage.markCheckbox();
		createSuiWalletPage.clickContinueButton();
		createSuiWalletPage.arrangeMnemonics();
		createSuiWalletPage.clickContinueButton();
	 	setPasswordPage=createSuiWalletPage.setPassword();
	 	myWebsitesPage= setPasswordPage.setPassword("Admin@123");		
	 	String icon=myWebsitesPage.getProfileButton();
	 	Assert.assertTrue(icon!=null);
	}
	
	
	
}
