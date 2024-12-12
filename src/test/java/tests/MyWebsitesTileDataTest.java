package tests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pages.MyWebsitesPage;
import pages.SignInPage;
import testUtility.BaseTest;

public class MyWebsitesTileDataTest extends BaseTest
{
	MyWebsitesPage myWebsitesPage;
	Connection connUserService;
	Connection connReward;
	Connection connPot;
	double hydPrice;
	double precision=0.001;
	ArrayList<String> tagIds=new ArrayList<>();
	@BeforeClass
	public void signIn() throws IOException, InterruptedException, SQLException
	{
		String userName=readPropertiesFile("userName");	
		String pwd=readPropertiesFile("password");
		SignInPage signInPage=homepage.goToSignIn();	
//		hydPrice=getPrice(readPropertiesFile("cmcHydApi"),"data.price");
		hydPrice=0.5;
		connReward= getInstanceHydroRewardDB();
		connPot= getInstanceQaPotDB();
		connUserService=getInstanceHydroUserServiceDB();
		myWebsitesPage= signInPage.signIn(userName,pwd);
		
		String queryUserId="SELECT * FROM public.\"Users\" where email='"+userName+"'";
		String userId=queryDatabaseColumn(connUserService, queryUserId, "id");
		System.out.println(userId);
		
		ArrayList<String> websites=myWebsitesPage.getWebsiteName();
		for(String website:websites)
		{
			String queryTagId="SELECT * FROM public.\"Websites\" where \"publisherId\"='"+userId+"' and \"websiteUrl\"='"+website+"' or \"websiteAlias\"='"+website+"' and \"websiteStatus\"!='DELETED'";
			String tagId=queryDatabaseColumn(connUserService, queryTagId, "id");
			System.out.println(tagId);
			tagIds.add(tagId);
		}
	}
	
	@Test(enabled=true, dataProvider="getData")
	public void verifyLastWeekDataOnUI(String tagId, int i) throws IOException, InterruptedException, SQLException
	{
		String[] dates=getLastWeekDateRange();
		myWebsitesPage.viewLastWeekData();
		
		float sessionsUI=myWebsitesPage.getSessions(i);
		System.out.println(sessionsUI);
		float timeOnSiteUI=myWebsitesPage.getTimeOnSite(i);
		System.out.println(timeOnSiteUI);
		float rewardsHydroUI=myWebsitesPage.getRewardsHydro(i);
		System.out.println(rewardsHydroUI);
		float rewardsDollarUI=myWebsitesPage.getRewardsDollar(i);
		System.out.println(rewardsDollarUI);
		
		// sessions in last week filter
		String querySessions="SELECT count(*) FROM public.session_dbs where tag_id='"+tagId+"' and created_at AT TIME ZONE 'UTC'>='"+dates[0]+"' and created_at AT TIME ZONE 'UTC'<='"+dates[1]+"'";
		float sessionsDB=Float.parseFloat(queryDatabase(connPot,querySessions));
		float roundedSessionsDB=roundOff(sessionsDB);
		// pot in last week filter
		String queryTimeOnSite="SELECT sum(session_time) FROM public.session_dbs where tag_id='"+tagId+"' and created_at AT TIME ZONE 'UTC'>='"+dates[0]+"' and created_at AT TIME ZONE 'UTC'<='"+dates[1]+"'";
		float timeOnSiteDB=Float.parseFloat(queryDatabase(connPot,queryTimeOnSite));
		// Rewards(Hydro) in last week filter
		String queryRewardsHydro="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.tag_id='"+tagId+"' AND r.calculation_for AT TIME ZONE 'UTC' >='"+dates[0]+"' and r.calculation_for AT TIME ZONE 'UTC'<='"+dates[1]+"'";
		float rewardsHydroDB=Math.round((Float.parseFloat(queryDatabase(connReward,queryRewardsHydro)))*100.0f)/100.0f;
		float roundedHydroDB=roundOff(rewardsHydroDB);
		// Rewards(Dollar) in last week filter
		float eqDollarRewards=Math.round(((float)(hydPrice*rewardsHydroDB))*100.0f)/100.0f;
		float roundedEqDollarRewards=roundOff(eqDollarRewards);
		
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(sessionsUI, roundedSessionsDB,precision,"Sessions are shown incorrect");
		sf.assertEquals(timeOnSiteUI, timeOnSiteDB,precision,"Time on site shown incorrect");
		sf.assertEquals(rewardsHydroUI, roundedHydroDB,precision,"Rewards(Hydro) shown incorrect");
		sf.assertEquals(rewardsDollarUI, roundedEqDollarRewards,precision,"Rewards(Dollar) shown incorrect");
		sf.assertAll();		
	}
	@Test(enabled=true, dataProvider="getData")
	public void verifyAllTimeDataOnUI(String tagId,int i) throws IOException, InterruptedException, NumberFormatException, SQLException
	{
		String curDate=getCurrentDate();
		myWebsitesPage.viewAllTimeData();
		
		float sessionsUI=myWebsitesPage.getSessions(i);
		System.out.println(sessionsUI);
		float timeOnSiteUI=myWebsitesPage.getTimeOnSite(i);
		System.out.println(timeOnSiteUI);
		float rewardsHydroUI=myWebsitesPage.getRewardsHydro(i);
		System.out.println(rewardsHydroUI);
		float rewardsDollarUI=myWebsitesPage.getRewardsDollar(i);
		System.out.println(rewardsDollarUI);
		
		// sessions in all time filter
		String querySessions="SELECT count(*) FROM public.session_dbs where tag_id='"+tagId+"' and created_at AT TIME ZONE 'UTC'<='"+curDate+"'";
		float sessionsDB=Float.parseFloat(queryDatabase(connPot,querySessions));
		float roundedSessionsDB=roundOff(sessionsDB);
		// pot in all time filter
		String queryTimeOnSite="SELECT sum(session_time) FROM public.session_dbs where tag_id='"+tagId+"' and created_at AT TIME ZONE 'UTC'<='"+curDate+"'";
		float timeOnSiteDB=Float.parseFloat(queryDatabase(connPot,queryTimeOnSite));
		// Rewards(Hydro) in all time filter
		String queryRewardsHydro="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.tag_id='"+tagId+"' AND r.calculation_for AT TIME ZONE 'UTC'<='"+curDate+"'";
		float rewardsHydroDB=Math.round((Float.parseFloat(queryDatabase(connReward,queryRewardsHydro)))*100.0f)/100.0f;
		float roundedHydroDB=roundOff(rewardsHydroDB);
		// Rewards(Dollar) in all time filter
		float eqDollarRewards=Math.round(((float)(hydPrice*rewardsHydroDB))*100.0f)/100.0f;
		float roundedEqDollarRewards=roundOff(eqDollarRewards);		
		
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(sessionsUI, roundedSessionsDB,"Sessions are shown incorrect");
		sf.assertEquals(timeOnSiteUI, timeOnSiteDB,"Time on site shown incorrect");
		sf.assertEquals(rewardsHydroUI, roundedHydroDB,"Rewards(Hydro) shown incorrect");
		sf.assertEquals(rewardsDollarUI, roundedEqDollarRewards,"Rewards(Dollar) shown incorrect");
		sf.assertAll();	
	}
	
	@Test(enabled=true, dataProvider="getData")
	public void verifyWeekSoFarDataOnUI(String tagId, int i) throws IOException, InterruptedException, NumberFormatException, SQLException
	{
		String mondayDate=getWeekSoFarDate();
		if (mondayDate.equalsIgnoreCase("Note: No data and delta will be shown on dashboard on monday"))
		{
			throw new SkipException("Skipping the test case as: No data and delta will be shown on dashboard on monday");
		}
		myWebsitesPage.viewWeekSoFarData();
		
		float sessionsUI=myWebsitesPage.getSessions(i);
		System.out.println(sessionsUI);
		float timeOnSiteUI=myWebsitesPage.getTimeOnSite(i);
		System.out.println(timeOnSiteUI);
		float rewardsHydroUI=myWebsitesPage.getRewardsHydro(i);
		System.out.println(rewardsHydroUI);
		float rewardsDollarUI=myWebsitesPage.getRewardsDollar(i);
		System.out.println(rewardsDollarUI);
		
		// sessions in week so far filter
		String querySessions="SELECT count(*) FROM public.session_dbs where tag_id='"+tagId+"' and created_at AT TIME ZONE 'UTC'>='"+mondayDate+"'";
		float sessionsDB=Float.parseFloat(queryDatabase(connPot,querySessions));
		float roundedSessionsDB=roundOff(sessionsDB);
		// pot in week so far filter
		String queryTimeOnSite="SELECT sum(session_time) FROM public.session_dbs where tag_id='"+tagId+"' and created_at AT TIME ZONE 'UTC'>='"+mondayDate+"'";
		float timeOnSiteDB=Float.parseFloat(queryDatabase(connPot,queryTimeOnSite));
		// Rewards(Hydro) in week so far filter
		String queryRewardsHydro="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.tag_id='"+tagId+"' AND r.calculation_for AT TIME ZONE 'UTC'>='"+mondayDate+"'";
		float rewardsHydroDB=Math.round((Float.parseFloat(queryDatabase(connReward,queryRewardsHydro)))*100.0f)/100.0f;
		float roundedHydroDB=roundOff(rewardsHydroDB);
		// Rewards(Dollar) in week so far filter
		float eqDollarRewards=Math.round(((float)(hydPrice*rewardsHydroDB))*100.0f)/100.0f;	
		
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(sessionsUI, roundedSessionsDB,"Sessions are shown incorrect");
		sf.assertEquals(timeOnSiteUI, timeOnSiteDB,"Time on site shown incorrect");
		sf.assertEquals(rewardsHydroUI, roundedHydroDB,"Rewards(Hydro) shown incorrect");
		sf.assertEquals(rewardsDollarUI, eqDollarRewards,"Rewards(Dollar) shown incorrect");
		sf.assertAll();	
	}
	@Test(enabled=true, dataProvider="getData")
	public void verifyLastWeekComparisonDataOnUI(String tagId, int i) throws IOException, InterruptedException, NumberFormatException, SQLException
	{
		String[] dates=getRespectiveDateRangeForLastWeek();
		if (dates[0].equalsIgnoreCase("Note: No delta and last week data will be shown on Monday"))
		{
			throw new SkipException("Skipping the test case as: No data and delta will be shown on dashboard on Monday");
		}
		myWebsitesPage.viewWeekSoFarData();
		
		float lastWeekSessionsUI=myWebsitesPage.getLastWeekSessions(i);
		System.out.println(lastWeekSessionsUI);
		float lastWeekTimeOnSiteUI=myWebsitesPage.getLastWeekTimeOnSite(i);
		System.out.println(lastWeekTimeOnSiteUI);
		float lastWeekRewardsHydroUI=myWebsitesPage.getLastWeekRewardsHydro(i);
		System.out.println(lastWeekRewardsHydroUI);
		float lastWeekRewardsDollarUI=myWebsitesPage.getLastWeekRewardsDollar(i);	
		System.out.println(lastWeekRewardsDollarUI);
		
		// sessions in last week
		String querySessions="SELECT count(*) FROM public.session_dbs WHERE tag_id='"+tagId+"' AND created_at AT TIME ZONE 'UTC' >= '"+dates[0]+"' AND created_at AT TIME ZONE 'UTC' < '"+dates[1]+"'";
		float lastWeekSessionsDB=Float.parseFloat(queryDatabase(connPot,querySessions));
		// pot in last week
		String queryTimeOnSite="SELECT sum(session_time) FROM public.session_dbs where tag_id='"+tagId+"' and created_at AT TIME ZONE 'UTC'>='"+dates[0]+"' and created_at AT TIME ZONE 'UTC'<'"+dates[1]+"'";
		float lastWeekTimeOnSiteDB=Float.parseFloat(queryDatabase(connPot,queryTimeOnSite));
		// Rewards(Hydro) in last week
		String queryRewardsHydro="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.tag_id='"+tagId+"' AND r.calculation_for AT TIME ZONE 'UTC'>='"+dates[0]+"' and r.calculation_for AT TIME ZONE 'UTC'<'"+dates[1]+"'";
		float lastWeekRewardsHydroDB=Math.round((Float.parseFloat(queryDatabase(connReward,queryRewardsHydro)))*100.0f)/100.0f;
		// Rewards(Dollar) in last week
		float lastWeekEqDollarRewards=Math.round(((float)(hydPrice*lastWeekRewardsHydroDB))*100.0f)/100.0f;	
		
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(lastWeekSessionsUI, lastWeekSessionsDB,"Last week Sessions doesn't match");
		sf.assertEquals(lastWeekTimeOnSiteUI, lastWeekTimeOnSiteDB,"Last week Time on Site doesn't match");
		sf.assertEquals(lastWeekRewardsHydroUI, lastWeekRewardsHydroDB,"Last week Rewards(Hydro) doesn't match");
		sf.assertEquals(lastWeekRewardsDollarUI, lastWeekEqDollarRewards,"Last week Rewards(Dollar) doesn't match");
		sf.assertAll();
	}
	@Test (enabled = true, dataProvider="getData")
	public void verifyDeltaPercentage(String tagId,int i) throws IOException, InterruptedException, NumberFormatException, SQLException
	{
		String mondayDate=getWeekSoFarDate();
		if (mondayDate.equalsIgnoreCase("Note: No data and delta will be shown on dashboard on monday"))
		{
			throw new SkipException("Skipping the test case as: No data and delta will be shown on dashboard on monday");
		}
		myWebsitesPage.viewWeekSoFarData();
		
		int sessionDeltaUI=myWebsitesPage.getSessionsDelta(i);
		System.out.println(sessionDeltaUI);
		int timeOnSiteDeltaUI=myWebsitesPage.getTimeOnSiteDelta(i);
		System.out.println(timeOnSiteDeltaUI);
		int rewardsHydroDeltaUI=myWebsitesPage.getRewardsHydroDelta(i);
		System.out.println(rewardsHydroDeltaUI);
		int rewardsDollarDeltaUI=myWebsitesPage.getRewardsDollarDelta(i);
		System.out.println(rewardsDollarDeltaUI);
		
		// sessions in week so far filter
		String querySessions="SELECT count(*) FROM public.session_dbs where tag_id='"+tagId+"' and created_at AT TIME ZONE 'UTC'>='"+mondayDate+"'";
		float sessionsDB=Float.parseFloat(queryDatabase(connPot,querySessions));
		// Rewards(Hydro) in week so far filter
		String queryRewardsHydro="SELECT SUM(w.amount) FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.tag_id='"+tagId+"' AND r.calculation_for AT TIME ZONE 'UTC'>='"+mondayDate+"'";
		float rewardsHydroDB=Math.round((Float.parseFloat(queryDatabase(connReward,queryRewardsHydro)))*100.0f)/100.0f;
		// Rewards(Dollar) in week so far filter
		float eqDollarRewards=Math.round(((float)(hydPrice*rewardsHydroDB))*100.0f)/100.0f;
		
		int calculatedSessionsDelta=calculateDelta(sessionsDB,myWebsitesPage.getLastWeekSessions(i));	
		int calculatedTimeOnSiteDelta=calculateDelta(myWebsitesPage.getTimeOnSite(i),myWebsitesPage.getLastWeekTimeOnSite(i));	
		int calculatedRewardsHydroDelta=calculateDelta(rewardsHydroDB,myWebsitesPage.getLastWeekRewardsHydro(i));
		int calculatedRewardsDollarDelta=calculateDelta(eqDollarRewards,myWebsitesPage.getLastWeekRewardsDollar(i));
				
		SoftAssert sf=new SoftAssert();
		sf.assertEquals(sessionDeltaUI, calculatedSessionsDelta,"Sessions delta doesn't match");
		sf.assertEquals(timeOnSiteDeltaUI, calculatedTimeOnSiteDelta,"Time on Site delta doesn't match");
		sf.assertEquals(rewardsHydroDeltaUI, calculatedRewardsHydroDelta,"Rewards(Hydro) delta doesn't match");
		sf.assertEquals(rewardsDollarDeltaUI, calculatedRewardsDollarDelta,"Rewards(Dollar) delta doesn't match");
		sf.assertAll();		
	}
	
	@DataProvider
	public Object[][] getData()
	{
		Object[][] data=new Object[tagIds.size()][2];
		for(int i=0;i<tagIds.size();i++)
		{
			data[i][0]=tagIds.get(i);
			data[i][1]=i;
		}
		return data;
	}
	
	
	
	
	
	
	
	
	
//	@Test(enabled =false)
//	public void verifyLastWeekDataInApi() throws IOException
//	{
//		String api=readPropertiesFile("lastWeek");
//		String token=readPropertiesFile("token");
//		String sessionsApi=getApiResponse(api,token,"data[0].total_session");
//		System.out.println(sessionsApi);
//		String potApi=getApiResponse(api, token, "data[0].total_time");
//		System.out.println(potApi);
//		String hydroRewardApi=getApiResponse(api, token,"data[0].hydro_reward");
//		System.out.println(hydroRewardApi);
//		String dollarRewardApi=getApiResponse(api, token,"data[0].total_reward");
//		System.out.println(dollarRewardApi);
//	}
	
	
	
	
	
}
