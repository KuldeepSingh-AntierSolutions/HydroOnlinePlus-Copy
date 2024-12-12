package tests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HydroPlusPage;
import pages.ManageNodesPage;
import pages.MyWebsitesPage;
import pages.SignInPage;
import testUtility.BaseTest;

public class ManageNodesAmountsAndRewardsTest extends BaseTest
{
	MyWebsitesPage myWebsitesPage;
	HydroPlusPage hydroPlusPage;
	ManageNodesPage manageNodesPage;
	Connection connUserService;
	Connection connReward;
	String publisherId="76513c7e-2909-4300-963d-9bfc80122c28";
	@BeforeClass
	public void signIn() throws IOException, InterruptedException, SQLException
	{
		String userName=readPropertiesFile("userName");	
		String pwd=readPropertiesFile("password");
		SignInPage signInPage=homepage.goToSignIn();
		connUserService= getInstanceHydroUserServiceDB();
		connReward= getInstanceHydroRewardDB();
		myWebsitesPage= signInPage.signIn(userName,pwd);
		hydroPlusPage=myWebsitesPage.goToHydroPlus();
		manageNodesPage= hydroPlusPage.goToManageNodesPage();
	}
	
	@Test(enabled=true)
	public void verifyHydroAmountForNodes() throws InterruptedException, SQLException
	{
		ArrayList<String> nodesList=manageNodesPage.getNodesList();
		ArrayList<Float> hydroAmounts=manageNodesPage.getHydroAmounts();
		SoftAssert sf=new SoftAssert();
		int i=0;
		for(String node:nodesList)
		{
			String queryHydroAmount="SELECT * FROM public.\"PublisherNodes\" where name='"+node+"'";
			String text=queryDatabaseColumn(connUserService, queryHydroAmount, "hydroAmount");
			float amount =((float)((int)(Float.parseFloat(text)*100)))/100;
//			System.out.println(amount);
			sf.assertEquals(hydroAmounts.get(i),amount,"Hydro amount doesn't match for node: "+node);	
			i++;
		}
		sf.assertAll();
	}
	
	@Test(enabled=true)
	public void verifySuiAmountForNodes() throws InterruptedException, SQLException
	{
		ArrayList<String> nodesList=manageNodesPage.getNodesList();
		ArrayList<Float> suiAmounts=manageNodesPage.getSuiAmounts();
		SoftAssert sf=new SoftAssert();
		int i=0;
		for(String node:nodesList)
		{
			String querySuiAmount="SELECT * FROM public.\"PublisherNodes\" where name='"+node+"'";
			String text=queryDatabaseColumn(connUserService, querySuiAmount, "suiAmount");
//			System.out.println(text);
			float amount =((float)((int)(Float.parseFloat(text)*100)))/100;
//			System.out.println(amount);
			sf.assertEquals(suiAmounts.get(i),amount,"Sui amount doesn't match for node: "+node);	
			i++;
		}
		sf.assertAll();
	}
	@Test(enabled=true)
	public void verifySuiRewardsForNodes() throws InterruptedException, SQLException
	{
		ArrayList<String> nodesList=manageNodesPage.getNodesList();
		ArrayList<Float> suiRewards=manageNodesPage.getSuiRewards();
		SoftAssert sf=new SoftAssert();
		int i=0;
		for(String node:nodesList)
		{
			String querySuiRewards="SELECT * FROM public.\"PublisherNodes\" where name='"+node+"'";
			String stakedSuiId=queryDatabaseColumn(connUserService, querySuiRewards, "stakedSuiId");
//			System.out.println(stakedSuiId);
			String queryRewards="SELECT sum(daily_reward) FROM public.staked_sui_data where staked_sui_id='"+stakedSuiId+"'";
			String text=queryDatabase(connReward, queryRewards);
			float suiReward =((float)((int)(Float.parseFloat(text)*1000000)))/1000000;
//			System.out.println(suiReward);
			sf.assertEquals(suiRewards.get(i),suiReward,"Rewards(Sui) doesn't match for node: "+node);	
			i++;
		}
		sf.assertAll();
	}
	@Test
	public void verifySuiRewardsDollarValueForNodes() throws InterruptedException, SQLException, IOException
	{
		ArrayList<String> nodesList=manageNodesPage.getNodesList();
		ArrayList<Float> suiRewardsDollarValue=manageNodesPage.getSuiRewardsDollarValue();
		SoftAssert sf=new SoftAssert();
		int i=0;
		for(String node:nodesList)
		{
			String querySuiRewards="SELECT * FROM public.\"PublisherNodes\" where name='"+node+"'";
			String stakedSuiId=queryDatabaseColumn(connUserService, querySuiRewards, "stakedSuiId");
//			System.out.println(stakedSuiId);
			String queryRewards="SELECT sum(daily_reward) FROM public.staked_sui_data where staked_sui_id='"+stakedSuiId+"'";
			String text=queryDatabase(connReward, queryRewards);
			float suiReward=Float.parseFloat(text);
			double suiPrice=getPrice(readPropertiesFile("cmcSuiApi"),"data.price");
//			System.out.println(suiPrice);
			float suiRewardDollarValue =((float)((int)((suiReward*suiPrice)*1000000)))/1000000;
//			System.out.println(suiRewardDollarValue);
			sf.assertEquals(suiRewardsDollarValue.get(i),suiRewardDollarValue,"Total rewards($) doesn't match for node: "+node);	
			i++;
		}
		sf.assertAll();
	}
	
}
