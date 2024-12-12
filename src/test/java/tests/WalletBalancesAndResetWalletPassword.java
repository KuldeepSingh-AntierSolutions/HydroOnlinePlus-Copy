package tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pages.WalletLoginPage;
import pages.MyWalletPage;
import pages.MyWebsitesPage;
import pages.SignInPage;
import testUtility.BaseTest;


public class WalletBalancesAndResetWalletPassword extends BaseTest
{
	WalletLoginPage walletLoginPage;
	MyWalletPage myWalletPage;
	double availableHydro,lockedHydro,totalHydro;
	double availableHydroDollar,lockedHydroDollar,totalHydroDollar;
	double availableSui,lockedSui,totalSui;
	double availableSuiDollar,lockedSuiDollar,totalSuiDollar;
	double totalUsdt,totalUsdtDollar;
	double totalEth,totalEthDollar;
	double availableBalance,lockedBalance,walletBalance;
	double suiPrice,hydPrice,ethPrice,usdtPrice;
	double precision=0.02;
	
	
	@Test
	public void toVerifyResetWalletPassword() throws IOException, InterruptedException
	{
		String expected=readPropertiesFile("walletPasswordSetSuccessMessage");
		String userName=readPropertiesFile("walletUser");
		String pwd=readPropertiesFile("walletUserPwd");
		String walletPassword=generateNewPassword(10);
		SignInPage signInPage=homepage.goToSignIn();
		MyWebsitesPage myWebsitesPage= signInPage.signIn(userName,pwd);
		walletLoginPage= myWebsitesPage.goToMyWallet();
		walletLoginPage.goToRecoverWallet();
		walletLoginPage.inputSeedPhrase(readPropertiesFile("publisherSeedPhrase"));
		walletLoginPage.enterNewWalletPassword(walletPassword);
		walletLoginPage.viewPassword();
		String actual=walletLoginPage.resetPassword();
		Assert.assertEquals(actual, expected);	
		updatePropertiesFile("walletPassword", walletPassword);
	}
	@Test(dependsOnMethods = "toVerifyResetWalletPassword")
	public void toVerifyUnlockWalletUsingNewPassword() throws InterruptedException, IOException
	{
		String text=walletLoginPage.unlockWallet(readPropertiesFile("walletPassword"));
		myWalletPage= walletLoginPage.goToMyWallet();
		Assert.assertEquals(text, "Assets");
	}
	@Test (dependsOnMethods ="toVerifyUnlockWalletUsingNewPassword" )
	public void getWalletAmounts() throws InterruptedException, IOException
	{
		suiPrice=getPrice(readPropertiesFile("cmcSuiApi"),"data.price");
		hydPrice=getPrice(readPropertiesFile("cmcHydApi"),"data.price");
		ethPrice=getPrice(readPropertiesFile("cmcEthApi"),"data.price");
		usdtPrice=getPrice(readPropertiesFile("cmcUsdtApi"),"data.price");
		
		availableHydro=myWalletPage.getHydroAvailableAmount();
		lockedHydro=myWalletPage.getHydroLockedAmount();
		totalHydro=myWalletPage.getHydroTotalAmount();
		availableHydroDollar=myWalletPage.getHydroAvailableDollarAmount();
		lockedHydroDollar=myWalletPage.getHydroLockedDollarAmount();
		totalHydroDollar=myWalletPage.getHydroTotalDollarAmount();
		availableSui=myWalletPage.getSuiAvailableAmount();
	    lockedSui=myWalletPage.getSuiLockedAmount();
	    totalSui=myWalletPage.getSuiTotalAmount();
	    availableSuiDollar=myWalletPage.getSuiAvailableDollarAmount();
	    lockedSuiDollar=myWalletPage.getSuiLockedDollarAmount();
	    totalSuiDollar=myWalletPage.getSuiTotalDollarAmount();
	    availableBalance=myWalletPage.getAvailableBalance();
	    lockedBalance=myWalletPage.getLockedBalance();
	    walletBalance=myWalletPage.getWalletBalance();
	    myWalletPage.goToEthWallet();
		totalUsdt=myWalletPage.getUsdtTotalAmount();
		totalUsdtDollar=myWalletPage.getUsdtTotalDollarAmount();
		totalEth=myWalletPage.getEthTotalAmount();
		totalEthDollar=myWalletPage.getEthTotalDollarAmount();
		myWalletPage.goToSuiWallet();
	}	
	@Test (dependsOnMethods ="getWalletAmounts" )
	public void verifyHydroTotalAmount()
	{
		double actualTotalHydro=availableHydro+lockedHydro;
		Assert.assertEquals(totalHydro,actualTotalHydro,precision);		
	}
	@Test (dependsOnMethods ="getWalletAmounts" )
	public void verifyHydroTotalDollarAmount()
	{
		myWalletPage= walletLoginPage.goToMyWallet();
		double actualTotalHydroDollar=availableHydroDollar+lockedHydroDollar;
		Assert.assertEquals(totalHydroDollar,actualTotalHydroDollar, precision);	
	}
	@Test (dependsOnMethods ="getWalletAmounts" )
	public void verifySuiTotalAmount()
	{
		double actualTotalSui=availableSui+lockedSui;
		Assert.assertEquals(totalSui,actualTotalSui,precision);		
	}
	@Test (dependsOnMethods ="getWalletAmounts" )
	public void verifySuiTotalDollarAmount()
	{
		double actualTotalSuiDollar=availableSuiDollar+lockedSuiDollar;
		Assert.assertEquals(totalSuiDollar,actualTotalSuiDollar, precision);	
	}
	@Test (dependsOnMethods ="getWalletAmounts" )
	public void verifyAvailableBalance() throws InterruptedException
	{
		double actualAvailableBalance=availableSuiDollar+availableHydroDollar+totalUsdtDollar+totalEthDollar;
		Assert.assertEquals(availableBalance,actualAvailableBalance,precision);
	}
	@Test (dependsOnMethods ="getWalletAmounts" )
	public void verifyLockedBalance() throws InterruptedException
	{
		double actualLockedBalance=lockedHydroDollar+lockedSuiDollar;
		Assert.assertEquals(lockedBalance,actualLockedBalance, precision,"Locked balance doesn't match");
	}
	@Test (dependsOnMethods ="getWalletAmounts" )
	public void verifyWalletBalance() throws InterruptedException
	{
		double actualTotalBalance=totalSuiDollar+totalHydroDollar+totalUsdtDollar+totalEthDollar;
		Assert.assertEquals(walletBalance,actualTotalBalance,precision);	
	}
	@Test(dependsOnMethods ="getWalletAmounts" )
	public void verifyDollarValuesForTheCryptoAssets() throws IOException
	{
		SoftAssert sf=new SoftAssert();
		//Hydro available,locked,total balance dollar value
		sf.assertEquals(availableHydroDollar, ((float)((int)((hydPrice*availableHydro)*100)))/100,precision,"Available Hydro dollar value doesn't match");
		sf.assertEquals(lockedHydroDollar,((float)((int)((hydPrice*lockedHydro)*100)))/100 ,precision,"Locked Hydro dollar value doesn't match");
		sf.assertEquals(totalHydroDollar, ((float)((int)((hydPrice*totalHydro)*100)))/100,precision,"Total Hydro dollar value doesn't match");
		//Sui available,locked,total balance dollar value
		sf.assertEquals(availableSuiDollar, ((float)((int)((suiPrice*availableSui)*100)))/100,precision,"Available Sui dollar value doesn't match");
		sf.assertEquals(lockedSuiDollar, ((float)((int)((suiPrice*lockedSui)*100)))/100,precision,"Locked Sui dollar value doesn't match");
		sf.assertEquals(totalSuiDollar, ((float)((int)((suiPrice*totalSui)*100)))/100,precision,"Total Sui dollar value doesn't match");
		//Eth and Usdt total balance dollar value
		sf.assertEquals(totalUsdtDollar, ((float)((int)((usdtPrice*totalUsdt)*100)))/100,precision,"Total Usdt dollar value doesn't match");
		sf.assertEquals(totalEthDollar, ((float)((int)((ethPrice*totalEth)*100)))/100,precision,"Total Eth dollar value doesn't match");
		sf.assertAll();
		
	}

}
