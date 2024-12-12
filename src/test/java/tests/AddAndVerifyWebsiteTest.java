package tests;

import java.awt.AWTException;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddWebsitePage;
import pages.MyWebsitesPage;
import pages.SetupHydroPage;
import pages.SignInPage;
import testUtility.BaseTest;

public class AddAndVerifyWebsiteTest extends BaseTest
{
	SetupHydroPage setupHydroPage;
	MyWebsitesPage myWebsitesPage;
	String nickname;
	
	@Test
	public void toCreateAndAddWebsite() throws InterruptedException, AWTException, IOException
	{
		String userName=readPropertiesFile("userName");
		String pwd=readPropertiesFile("pwd");
		String websiteUrl=generateWebsite();
		nickname=getRandomString();
		SignInPage signInPage=homepage.goToSignIn();
		MyWebsitesPage myWebsitesPage=signInPage.signIn(userName,pwd);
		AddWebsitePage addWebsitePage=myWebsitesPage.goToAddWebsite();
		addWebsitePage.enterUrl(websiteUrl);
		addWebsitePage.enterNickname(nickname);
		setupHydroPage=addWebsitePage.submitDetail();
		String toast=addWebsitePage.getSuccessToast();
		Assert.assertEquals("Website has been added successfully", toast);
		String website=setupHydroPage.getWebsiteName();
		Assert.assertEquals(website, nickname);	
	}
	
	@Test (dependsOnMethods = "toCreateAndAddWebsite")
	public void toVerifyWebsite() throws AWTException, InterruptedException
	{
		String filePath=System.getProperty("user.dir")+"\\src\\main\\resources\\jsCode";
		String tagId=setupHydroPage.getTagId();
		addTagId(tagId, filePath);
		myWebsitesPage= setupHydroPage.clickVerifyInstallation();
	}
	@Test(enabled=false)
	public void toVerifySiteNicknameOnMyWebsites()
	{
		String actualNickname=myWebsitesPage.getWebsiteNickname();
		Assert.assertEquals(actualNickname, nickname);
	}
	
	
	
	
	
}
