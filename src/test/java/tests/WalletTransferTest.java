package tests;

import java.io.IOException;

import org.testng.annotations.Test;

import pages.MyWalletPage;
import pages.MyWebsitesPage;
import pages.SignInPage;
import pages.WalletLoginPage;
import testUtility.BaseTest;

public class WalletTransferTest extends BaseTest
{
	@Test
	public void toVerifySendAmount() throws IOException, InterruptedException
	{
		String userName=readPropertiesFile("userName");
		String pwd=readPropertiesFile("password");
		SignInPage signInPage=homepage.goToSignIn();
		MyWebsitesPage myWebsitesPage= signInPage.signIn(userName,pwd);
		WalletLoginPage walletLoginPage = myWebsitesPage.goToMyWallet();
		MyWalletPage myWalletPage= walletLoginPage.goToHydroActionButton();
		myWalletPage.clickTransferOption();
		myWalletPage.enterReceiverAddress("kljhgfxcfgvhbjn");
		myWalletPage.enterAmount("5");
		myWalletPage.clickTransfer();
		
	}
}
