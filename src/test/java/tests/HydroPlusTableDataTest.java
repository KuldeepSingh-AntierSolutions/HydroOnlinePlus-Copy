package tests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pages.HydroPlusPage;
import pages.MyWebsitesPage;
import pages.SignInPage;
import testUtility.BaseTest;

public class HydroPlusTableDataTest extends BaseTest
{
	MyWebsitesPage myWebsitesPage;
	HydroPlusPage hydroPlusPage;
	Connection connReward;
	Connection connPot;
	double hydPrice;
	String publisherId="76513c7e-2909-4300-963d-9bfc80122c28";
	@BeforeClass
	public void signIn() throws IOException, InterruptedException, SQLException
	{
		String userName=readPropertiesFile("userName");	
		String pwd=readPropertiesFile("password");
		SignInPage signInPage=homepage.goToSignIn();		
		hydPrice=getPrice(readPropertiesFile("cmcHydApi"),"data.price");
		connReward= getInstanceHydroRewardDB();
//		connPot= getInstanceQaPotDB(url+"qa_pot",dbUserName, dbPwd);
		myWebsitesPage= signInPage.signIn(userName,pwd);
		hydroPlusPage=myWebsitesPage.goToHydroPlus();
	}
	@Test(enabled=true)
	public void verifyTotalNodesAndSuiStaked() throws NumberFormatException, SQLException
	{
		int nodeCountUI=hydroPlusPage.getTotalNodes();
		float suiStakedUI=hydroPlusPage.getSuiStaked();		
		String querySuiStaked="SELECT sum(sui_amount) FROM public.sui_transactions where publisher_id='"+publisherId+"'";
		float suiStakedDB=Float.parseFloat(queryDatabase(connReward,querySuiStaked));
		float suiStaked=((float)((int)(suiStakedDB*100)))/100;		
		String queryNodeCount="SELECT count(*) FROM public.sui_transactions where publisher_id='76513c7e-2909-4300-963d-9bfc80122c28'";
		int nodeCountDB=Integer.parseInt(queryDatabase(connReward,queryNodeCount));
		
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(nodeCountUI,nodeCountDB,"Node count doesn't match");
		sf.assertEquals(suiStakedUI,suiStaked,"SUI staked amount doesn't match");
		sf.assertAll();			
	}
	
	@Test(enabled=true)
	public void verifyTableDataInWeekSoFar() throws NumberFormatException, SQLException, InterruptedException
	{
		String mondayDate=getWeekSoFarDate();
		if (mondayDate.equalsIgnoreCase("Note: No data and delta will be shown on dashboard on monday"))
		{
			throw new SkipException("Skipping the test case as: No data and delta will be shown on dashboard on monday");
		}
		myWebsitesPage.viewWeekSoFarData();
		
		float suiRewardsUI=hydroPlusPage.getSuiRewards();
		System.out.println(suiRewardsUI);
		float suiRewardsAprUI=hydroPlusPage.getSuiRewardsApr();
		System.out.println(suiRewardsAprUI);
		float boostAmountHydUI=hydroPlusPage.getBoostAmountHyd();
		System.out.println(boostAmountHydUI);
		float totalBoostPercentUI=hydroPlusPage.getTotalBoostPercent();	
		System.out.println(totalBoostPercentUI);
		
		// SUI rewards in week so far filter
		String querySuiRewards="SELECT sum(daily_reward) FROM staked_sui_data AS ssd JOIN sui_transactions AS st ON ssd.staked_sui_id=st.staked_sui_id WHERE st.publisher_id='"+publisherId+"' and calculation_for AT TIME ZONE 'UTC'>='"+mondayDate+"'";
		float suiRewardsDB=Float.parseFloat(queryDatabase(connReward,querySuiRewards));
		float suiRewards=((float)((int) (suiRewardsDB*100)))/100;		
		System.out.println(suiRewards);
		// SUI rewards(APR) in week so far filter
		String querySuiRewardsApr="SELECT AVG(apr_percentage) FROM staked_sui_data AS ssd JOIN sui_transactions AS st ON ssd.staked_sui_id=st.staked_sui_id WHERE st.publisher_id='"+publisherId+"'and calculation_for AT TIME ZONE 'UTC'>='"+mondayDate+"'";
		float suiRewardsAprDB=Float.parseFloat(queryDatabase(connReward,querySuiRewardsApr));
		float suiRewardsApr=(float)((int) (suiRewardsAprDB*100))/100;	
		System.out.println(suiRewardsApr);
		// Boost amount (HYD) in week so far filter
		String queryBoostAmountHyd="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND r.calculation_for AT TIME ZONE 'UTC'>='"+mondayDate+"' AND rewardee_type in ('PLUS','PLUS_BASE')";
		float boostAmountHydroDB=Float.parseFloat(queryDatabase(connReward,queryBoostAmountHyd));
		float boostAmountHydro=((float)(((int)(boostAmountHydroDB*100)))/100);
		System.out.println(boostAmountHydro);
		// Total boost % in week so far filter
		String queryPlusBaseAmount="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND r.calculation_for AT TIME ZONE 'UTC'>='"+mondayDate+"' AND rewardee_type='PLUS_BASE'";
		float plusBaseAmount=Float.parseFloat(queryDatabase(connReward,queryPlusBaseAmount)); 
		String queryPlusBoostAmount="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND r.calculation_for AT TIME ZONE 'UTC'>='"+mondayDate+"' AND rewardee_type='PLUS'";
		float plusBoostAmount=Float.parseFloat(queryDatabase(connReward,queryPlusBoostAmount));
		float totalBoostPercent=((float)(((int)(((plusBoostAmount/plusBaseAmount)*100)*100)))/100);
		System.out.println(totalBoostPercent);
				
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(suiRewardsUI, suiRewardsDB,"Sui rewards shown incorrect");
		sf.assertEquals(suiRewardsApr, suiRewardsAprDB,"Sui rewards (APR) shown incorrect");
		sf.assertEquals(boostAmountHydUI, boostAmountHydro,"Boost amount (HYD) shown incorrect");
		sf.assertEquals(totalBoostPercentUI,totalBoostPercent ,"Total boost % shown incorrect");
		sf.assertAll();
	}
	@Test(enabled=true)
	public void verifyTableDataInLastWeek() throws NumberFormatException, SQLException, InterruptedException
	{
		String[] dates=getLastWeekDateRange();
		myWebsitesPage.viewLastWeekData();
		
		float suiRewardsUI=hydroPlusPage.getSuiRewards();
		System.out.println(suiRewardsUI);
		float suiRewardsAprUI=hydroPlusPage.getSuiRewardsApr();
		System.out.println(suiRewardsAprUI);
		float boostAmountHydUI=hydroPlusPage.getBoostAmountHyd();
		System.out.println(boostAmountHydUI);
		float totalBoostPercentUI=hydroPlusPage.getTotalBoostPercent();	
		System.out.println(totalBoostPercentUI);
		
		// SUI rewards in week so far filter
		String querySuiRewards="SELECT sum(daily_reward) FROM staked_sui_data AS ssd JOIN sui_transactions AS st ON ssd.staked_sui_id=st.staked_sui_id WHERE st.publisher_id='"+publisherId+"' and calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' and calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"'";
		float suiRewardsDB=Float.parseFloat(queryDatabase(connReward,querySuiRewards));
		float suiRewards=(float)((int) (suiRewardsDB*100))/100;		
		System.out.println(suiRewards);
		// SUI rewards(APR) in week so far filter
		String querySuiRewardsApr="SELECT AVG(apr_percentage) FROM staked_sui_data AS ssd JOIN sui_transactions AS st ON ssd.staked_sui_id=st.staked_sui_id WHERE st.publisher_id='"+publisherId+"' and calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' and calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"'";
		float suiRewardsAprDB=Float.parseFloat(queryDatabase(connReward,querySuiRewardsApr));
		float suiRewardsApr=(float)((int) (suiRewardsAprDB*100))/100;	
		System.out.println(suiRewardsApr);
		// Boost amount (HYD) in week so far filter
		String queryBoostAmountHyd="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' AND calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"' AND rewardee_type in ('PLUS','PLUS_BASE')";
		float boostAmountHydroDB=Float.parseFloat(queryDatabase(connReward,queryBoostAmountHyd));
		float boostAmountHydro=((float)(((int)(boostAmountHydroDB*100)))/100);
		System.out.println(boostAmountHydro);
		// Total boost % in week so far filter
		String queryPlusBaseAmount="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' AND calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"' AND rewardee_type='PLUS_BASE'";
		float plusBaseAmount=Float.parseFloat(queryDatabase(connReward,queryPlusBaseAmount)); 
		String queryPlusBoostAmount="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' AND calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"' AND rewardee_type='PLUS'";
		float plusBoostAmount=Float.parseFloat(queryDatabase(connReward,queryPlusBoostAmount));
		float totalBoostPercent=((float)(((int)(((plusBoostAmount/plusBaseAmount)*100)*100)))/100);
		System.out.println(totalBoostPercent);
					
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(suiRewardsUI, suiRewardsDB,"Sui rewards shown incorrect");
		sf.assertEquals(suiRewardsApr, suiRewardsAprDB,"Sui rewards (APR) shown incorrect");
		sf.assertEquals(boostAmountHydUI, boostAmountHydro,"Boost amount (HYD) shown incorrect");
		sf.assertEquals(totalBoostPercentUI,totalBoostPercent ,"Total boost % shown incorrect");
		sf.assertAll();
	}
	@Test(enabled=true)
	public void verifyTableDataInAllTime() throws NumberFormatException, SQLException, InterruptedException
	{
		String curDate=getCurrentDate();
		myWebsitesPage.viewAllTimeData();
		
		float suiRewardsUI=hydroPlusPage.getSuiRewards();
		System.out.println(suiRewardsUI);
		float suiRewardsAprUI=hydroPlusPage.getSuiRewardsApr();
		System.out.println(suiRewardsAprUI);
		float boostAmountHydUI=hydroPlusPage.getBoostAmountHyd();
		System.out.println(boostAmountHydUI);
		float totalBoostPercentUI=hydroPlusPage.getTotalBoostPercent();	
		System.out.println(totalBoostPercentUI);
		
		// SUI rewards in all time filter
		String querySuiRewards="SELECT sum(daily_reward) FROM staked_sui_data AS ssd JOIN sui_transactions AS st ON ssd.staked_sui_id=st.staked_sui_id WHERE st.publisher_id='"+publisherId+"' and calculation_for AT TIME ZONE 'UTC'<='"+curDate+"'";
		float suiRewardsDB=Float.parseFloat(queryDatabase(connReward,querySuiRewards));
		float suiRewards=(float)((int) (suiRewardsDB*100))/100;		
		System.out.println(suiRewards);
		// SUI rewards(APR) in all time filter
		String querySuiRewardsApr="SELECT AVG(apr_percentage) FROM staked_sui_data AS ssd JOIN sui_transactions AS st ON ssd.staked_sui_id=st.staked_sui_id WHERE st.publisher_id='"+publisherId+"'and calculation_for AT TIME ZONE 'UTC'<='"+curDate+"'";
		float suiRewardsAprDB=Float.parseFloat(queryDatabase(connReward,querySuiRewardsApr));
		float suiRewardsApr=(float)((int) (suiRewardsAprDB*100))/100;	
		System.out.println(suiRewardsApr);
		// Boost amount (HYD) in all time filter
		String queryBoostAmountHyd="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND r.calculation_for AT TIME ZONE 'UTC'<='"+curDate+"' AND rewardee_type in ('PLUS','PLUS_BASE')";
		float boostAmountHydroDB=Float.parseFloat(queryDatabase(connReward,queryBoostAmountHyd));
		float boostAmountHydro=((float)(((int)(boostAmountHydroDB*100)))/100);
		System.out.println(boostAmountHydro);
		// Total boost % in all time filter
		String queryPlusBaseAmount="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND r.calculation_for AT TIME ZONE 'UTC'<='"+curDate+"' AND rewardee_type='PLUS_BASE'";
		float plusBaseAmount=Float.parseFloat(queryDatabase(connReward,queryPlusBaseAmount)); 
		String queryPlusBoostAmount="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND r.calculation_for AT TIME ZONE 'UTC'<='"+curDate+"' AND rewardee_type='PLUS'";
		float plusBoostAmount=Float.parseFloat(queryDatabase(connReward,queryPlusBoostAmount));
		float totalBoostPercent=((float)(((int)(((plusBoostAmount/plusBaseAmount)*100)*100)))/100);
		System.out.println(totalBoostPercent);
				
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(suiRewardsUI, suiRewardsDB,"Sui rewards shown incorrect");
		sf.assertEquals(suiRewardsApr, suiRewardsAprDB,"Sui rewards (APR) shown incorrect");
		sf.assertEquals(boostAmountHydUI, boostAmountHydro,"Boost amount (HYD) shown incorrect");
		sf.assertEquals(totalBoostPercentUI,totalBoostPercent ,"Total boost % shown incorrect");
		sf.assertAll();
	}
	@Test(enabled=true)
	public void verifyTableLastWeekComparisonDataOnUI() throws NumberFormatException, SQLException, InterruptedException
	{
		String[] dates=getRespectiveDateRangeForLastWeek();
		myWebsitesPage.viewWeekSoFarData();
		
		float suiRewardsUI=hydroPlusPage.getLastWeekSuiRewards();
		System.out.println(suiRewardsUI);
		float suiRewardsAprUI=hydroPlusPage.getLastWeekSuiRewardsApr();
		System.out.println(suiRewardsAprUI);
		float boostAmountHydUI=hydroPlusPage.getLastWeekBoostAmountHyd();
		System.out.println(boostAmountHydUI);
		float totalBoostPercentUI=hydroPlusPage.getLastWeekTotalBoostPercent();	
		System.out.println(totalBoostPercentUI);
		
		// SUI rewards in week so far filter
		String querySuiRewards="SELECT sum(daily_reward) FROM staked_sui_data AS ssd JOIN sui_transactions AS st ON ssd.staked_sui_id=st.staked_sui_id WHERE st.publisher_id='"+publisherId+"' and calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' and calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"'";
		float suiRewardsDB=Float.parseFloat(queryDatabase(connReward,querySuiRewards));
		float suiRewards=(float)((int) (suiRewardsDB*100))/100;		
		System.out.println(suiRewards);
		// SUI rewards(APR) in week so far filter
		String querySuiRewardsApr="SELECT AVG(apr_percentage) FROM staked_sui_data AS ssd JOIN sui_transactions AS st ON ssd.staked_sui_id=st.staked_sui_id WHERE st.publisher_id='"+publisherId+"' and calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' and calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"'";
		float suiRewardsAprDB=Float.parseFloat(queryDatabase(connReward,querySuiRewardsApr));
		float suiRewardsApr=(float)((int) (suiRewardsAprDB*100))/100;	
		System.out.println(suiRewardsApr);
		// Boost amount (HYD) in week so far filter
		String queryBoostAmountHyd="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' AND calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"' AND rewardee_type in ('PLUS','PLUS_BASE')";
		float boostAmountHydroDB=Float.parseFloat(queryDatabase(connReward,queryBoostAmountHyd));
		float boostAmountHydro=((float)(((int)(boostAmountHydroDB*100)))/100);
		System.out.println(boostAmountHydro);
		// Total boost % in week so far filter
		String queryPlusBaseAmount="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' AND calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"' AND rewardee_type='PLUS_BASE'";
		float plusBaseAmount=Float.parseFloat(queryDatabase(connReward,queryPlusBaseAmount)); 
		String queryPlusBoostAmount="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.publisher_id='"+publisherId+"' AND calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' AND calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"' AND rewardee_type='PLUS'";
		float plusBoostAmount=Float.parseFloat(queryDatabase(connReward,queryPlusBoostAmount));
		float totalBoostPercent=((float)(((int)(((plusBoostAmount/plusBaseAmount)*100)*100)))/100);
		System.out.println(totalBoostPercent);
					
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(suiRewardsUI, suiRewardsDB,"Last week Sui rewards shown incorrect");
		sf.assertEquals(suiRewardsApr, suiRewardsAprDB,"Last weekSui rewards (APR) shown incorrect");
		sf.assertEquals(boostAmountHydUI, boostAmountHydro,"Last week Boost amount (HYD) shown incorrect");
		sf.assertEquals(totalBoostPercentUI,totalBoostPercent ,"Last week Total boost % shown incorrect");
		sf.assertAll();
	}
	@Test(enabled=true)
	public void verifyTableDeltaPercentage() throws NumberFormatException, SQLException, InterruptedException
	{
		String mondayDate=getWeekSoFarDate();
		if (mondayDate.equalsIgnoreCase("Note: No data and delta will be shown on dashboard on monday"))
		{
			throw new SkipException("Skipping the test case as: No data and delta will be shown on dashboard on monday");
		}
		myWebsitesPage.viewWeekSoFarData();
		
		int suiRewardsDeltaUI=hydroPlusPage.getSuiRewardsDelta();
		System.out.println(suiRewardsDeltaUI);
		int suiRewardsAprDeltaUI=hydroPlusPage.getSuiRewardsAprDelta();
		System.out.println(suiRewardsAprDeltaUI);
		int boostAmountHydDeltaUI=hydroPlusPage.getBoostAmountHydDelta();
		System.out.println(boostAmountHydDeltaUI);
		int totalBoostPercentDeltaUI=hydroPlusPage.getTotalBoostPercentDelta();	
		System.out.println(totalBoostPercentDeltaUI);
		
		float suiRewardsUI=hydroPlusPage.getSuiRewards();
		System.out.println(suiRewardsUI);
		float suiRewardsAprUI=hydroPlusPage.getSuiRewardsApr();
		System.out.println(suiRewardsAprUI);
		float boostAmountHydUI=hydroPlusPage.getBoostAmountHyd();
		System.out.println(boostAmountHydUI);
		float totalBoostPercentUI=hydroPlusPage.getTotalBoostPercent();	
		System.out.println(totalBoostPercentUI);
		
		float lastWeekSuiRewardsUI=hydroPlusPage.getLastWeekSuiRewards();
		System.out.println(suiRewardsUI);
		float lastWeekSuiRewardsAprUI=hydroPlusPage.getLastWeekSuiRewardsApr();
		System.out.println(suiRewardsAprUI);
		float lastWeekBoostAmountHydUI=hydroPlusPage.getLastWeekBoostAmountHyd();
		System.out.println(boostAmountHydUI);
		float lastWeekTotalBoostPercentUI=hydroPlusPage.getLastWeekTotalBoostPercent();	
		System.out.println(totalBoostPercentUI);
					
		int calculatedSuiRewardsDelta=calculateDelta(suiRewardsUI,lastWeekSuiRewardsUI);	
		int calculatedSuiRewardsAprDelta=calculateDelta(suiRewardsAprUI,lastWeekSuiRewardsAprUI);	
		int calculatedBoostAmountHydDelta=calculateDelta(boostAmountHydUI,lastWeekBoostAmountHydUI);
		int calculatedTotalBoostPercentDelta=calculateDelta(totalBoostPercentUI,lastWeekTotalBoostPercentUI);
				
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(suiRewardsDeltaUI, calculatedSuiRewardsDelta,"Sui rewards delta doesn't match");
		sf.assertEquals(suiRewardsAprDeltaUI, calculatedSuiRewardsAprDelta,"Sui rewards (APR) delta doesn't match");
		sf.assertEquals(boostAmountHydDeltaUI, calculatedBoostAmountHydDelta,"Boost amount(HYD) delta doesn't match");
		sf.assertEquals(totalBoostPercentDeltaUI, calculatedTotalBoostPercentDelta,"Total boost % delta doesn't match");
		sf.assertAll();	
	}
}
