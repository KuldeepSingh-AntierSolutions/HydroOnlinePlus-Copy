package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HydroPlusPage;
import pages.MyWebsitesPage;
import testUtility.BaseTest;
import java.sql.Connection;

public class RewardsCalculationTest extends BaseTest
{
	MyWebsitesPage myWebsitesPage;
	HydroPlusPage hydroPlusPage;
	Connection connReward;
	Connection connPot;
	String date="2024-10-09";
	String publisherId="76513c7e-2909-4300-963d-9bfc80122c28";
	@BeforeClass
	public void signIn() throws IOException, InterruptedException, SQLException
	{	
		connReward= getInstanceHydroRewardDB();
		connPot= getInstanceQaPotDB();
	}
	@Test
	public void calculateRewards() throws SQLException
	{
		// tag ids having session on a particular day
		String queryTagIds="SELECT Distinct(tag_id) FROM public.session_dbs where Date(created_at)='"+date+"'";
		ArrayList<String> tagIds=new ArrayList<String>();
		tagIds=queryDatabase(connPot,queryTagIds,"tag_id");
		
		//plan pool for that particular day
		System.out.println("Reward pool for the free, plus and plus base:");
		String queryRewardPoolFree="SELECT * FROM public.pool_config_dbs where Date(created_at)='"+date+"' and plan_type='FREE'";
		String poolFree=queryDatabaseColumn(connReward, queryRewardPoolFree,"plan_pool");
		double rewardPoolFree=(Double.parseDouble(poolFree))/Math.pow(10,9);		
		System.out.println("Free: "+rewardPoolFree);		
		String queryRewardPoolPlusBase="SELECT * FROM public.pool_config_dbs where Date(created_at)='"+date+"' and plan_type='PLUS_BASE'";
		String poolPlusBase=queryDatabaseColumn(connReward, queryRewardPoolPlusBase,"plan_pool");
		double rewardPoolPlusBase=(Double.parseDouble(poolPlusBase))/Math.pow(10,9);
		System.out.println("Plus Base: "+rewardPoolPlusBase);		
		String queryRewardPoolPlus="SELECT * FROM public.pool_config_dbs where Date(created_at)='"+date+"' and plan_type='PLUS'";
		String poolPlus=queryDatabaseColumn(connReward, queryRewardPoolPlus,"plan_pool");
		double rewardPoolPlus=(Double.parseDouble(poolPlus))/Math.pow(10,9);
		System.out.println("Plus: "+rewardPoolPlus);
		
		//plan cap for the day
		String queryPlanCap="SELECT * FROM public.daily_reward_dbs where Date(calculation_for)='"+date+"' and plan_type='FREE'";
		int freePlanCap=Integer.parseInt(queryDatabaseColumn(connReward, queryPlanCap, "plan_cap"));
		System.out.println("Free plan cap = "+freePlanCap);
		
		//tag_id segregation as per free/plus
		ArrayList<String> freeTagIds=new ArrayList<String>();
		ArrayList<String> plusTagIds=new ArrayList<String>();
		for(String tagId:tagIds)
		{
			String queryTagId="SELECT * FROM public.website_dbs where tag_id='"+tagId+"'";
			String type=queryDatabaseColumn(connPot, queryTagId, "type");
			if(type.equals("FREE"))
			{
				freeTagIds.add(tagId);
			}
			if(type.equals("PLUS"))
			{
				plusTagIds.add(tagId);
			}
		}
		
		//print all free and plus websites
		System.out.println("These are free websites:");
		for(String freeTagId:freeTagIds)
		{
			System.out.println(freeTagId);
		}
		System.out.println("These are plus websites:");
		for(String plusTagId:plusTagIds)
		{
			System.out.println(plusTagId);
		}
		
		
		// total capped time for free websites
		int totalSessionTimeFree=0;
		ArrayList<Integer> sessionTimeForFreeTagIds=new ArrayList<>();
		for(String freeTagId:freeTagIds)
		{
			String queryTagIdSessionTime="SELECT sum(session_time) FROM public.session_dbs where tag_id='"+freeTagId+"' and Date(created_at AT TIME ZONE 'UTC') ='"+date+"'";
			int sessionTime=Integer.parseInt(queryDatabase(connPot,queryTagIdSessionTime));
			System.out.println("Total session time for tagId: "+freeTagId+"= "+ sessionTime);
			if(sessionTime>freePlanCap && freePlanCap!=0)
			{
				sessionTime=freePlanCap;
				System.out.println("Capped session time for tagId: "+freeTagId+"= "+ sessionTime);
			}
			sessionTimeForFreeTagIds.add(sessionTime);
			totalSessionTimeFree=totalSessionTimeFree+sessionTime;			
		}
		System.out.println("Total free session time = "+totalSessionTimeFree);	
		
		//calculate reward per second for free website
		double rpsFree=rewardPoolFree/totalSessionTimeFree;
		
		// calculate free website rewards with cap
		ArrayList<Double> rewardForFreeTagIds=new ArrayList<>();
		double freeReward;
		for(int sessionTimeForFreeTagId:sessionTimeForFreeTagIds)
		{
			freeReward=sessionTimeForFreeTagId*rpsFree;
			rewardForFreeTagIds.add(freeReward);
		}
		
		
		
		// Session time for plus websites
		int totalSessionTimePlus=0;
		ArrayList<Integer> sessionTimeForPlusTagIds=new ArrayList<>();
		for(String plusTagId:plusTagIds)
		{
			String queryTagIdSessionTime="SELECT sum(session_time) FROM public.session_dbs where tag_id='"+plusTagId+"' and Date(created_at AT TIME ZONE 'UTC') ='"+date+"'";
			int sessionTime=Integer.parseInt(queryDatabase(connPot,queryTagIdSessionTime));
			System.out.println("Total session time for plus tagId: "+plusTagId+"= "+ sessionTime);
			sessionTimeForPlusTagIds.add(sessionTime);
			totalSessionTimePlus=totalSessionTimePlus+sessionTime;			
		}
		System.out.println("Total plus session time = "+totalSessionTimePlus);
		//calculate reward per second for free website
		double rpsPlusBase=rewardPoolPlusBase/totalSessionTimePlus;
		
		// calculate plus base rewards
		ArrayList<Double> rewardForPlusBaseTagIds=new ArrayList<>();
		double plusBaseReward;
		for(int sessionTimeForPlusTagId:sessionTimeForPlusTagIds)
		{
			plusBaseReward=sessionTimeForPlusTagId*rpsPlusBase;
			rewardForPlusBaseTagIds.add(plusBaseReward);
		}

		
		
		// get number of nodes for plus websites
		ArrayList<Integer> nodeCountTagIds=new ArrayList<>();
		for(String plusTagId:plusTagIds)
		{
			String queryNodeCount="SELECT * FROM public.website_dbs where tag_id='"+plusTagId+"'";
			int nodeCount=Integer.parseInt(queryDatabaseColumn(connPot,queryNodeCount,"node_count"));
			System.out.println("Number of nodes for tagId: "+plusTagId+" is "+nodeCount);
			nodeCountTagIds.add(nodeCount);			
		}
		
		//get aggregatedTime as per node factor
		ArrayList<Integer> aggPlusBoostTime=new ArrayList<>();
		int k=0;
		int boostTime=0;
		for(int sessionTimeForPlusTagId:sessionTimeForPlusTagIds)
		{
			int aggTime=sessionTimeForPlusTagId*nodeCountTagIds.get(k);
			boostTime=boostTime+aggTime;
			aggPlusBoostTime.add(aggTime);
			k++;
		}
		System.out.println("Total agg boost time = "+boostTime);
		
		// calculate plus boost rewards
		double rpsPlusBoost=rewardPoolPlus/boostTime;//calculate reward per second for boost
		ArrayList<Double> rewardForBoostTagIds=new ArrayList<>();
		double plusBoostReward;
		for(int PlusBoostTime:aggPlusBoostTime)
		{
			plusBoostReward=PlusBoostTime*rpsPlusBoost;
			rewardForBoostTagIds.add(plusBoostReward);
		}
		
		//Assertion of Free rewards
		SoftAssert softAssert=new SoftAssert();
		int i=0;
		for(String freeTagId:freeTagIds)
		{
			String queryRewardForFreeTagId="SELECT * FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.tag_id='"+freeTagId+"' and Date(r.calculation_for)='"+date+"' and rewardee_type='FREE'";
			float rewardInDB=Float.parseFloat(queryDatabaseColumn(connReward, queryRewardForFreeTagId,"amount"));
			double rewardCalculated=rewardForFreeTagIds.get(i);
			i++;
			System.out.println("Free Reward for website: "+freeTagId+" is: "+rewardInDB+" in DB while Calculated is: "+rewardCalculated);
			softAssert.assertEquals(rewardInDB, rewardCalculated,0.01,"Free reward for tag ID: "+freeTagId+" is= "+rewardInDB+" in DB and expected calculated reward is= "+rewardCalculated);		
		}
		//Assertion of Plus_Base rewards
		int j=0;
		for(String plusTagId:plusTagIds)
		{
			String queryRewardForPlusBaseTagId="SELECT * FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.tag_id='"+plusTagId+"' and Date(r.calculation_for)='"+date+"' and rewardee_type='PLUS_BASE'";
			float rewardInDB=Float.parseFloat(queryDatabaseColumn(connReward, queryRewardForPlusBaseTagId,"amount"));
			double rewardCalculated=rewardForPlusBaseTagIds.get(j);
			j++;
			System.out.println("Plus_Base Reward for website: "+plusTagId+" is: "+rewardInDB+" in DB while Calculated is: "+rewardCalculated);
			softAssert.assertEquals(rewardInDB, rewardCalculated,0.01,"Plus Base reward for tag ID: "+plusTagId+" is= "+rewardInDB+" in DB and expected calculated reward is= "+rewardCalculated);		
		}
		//Assertion of Plus_Boost rewards
		int l=0;
		for(String plusTagId:plusTagIds)
		{
			String queryRewardForPlusBaseTagId="SELECT * FROM website_rewards_dbs AS w JOIN reward_calculations_dbs AS r ON w.id=r.website_rewards_db_id WHERE w.tag_id='"+plusTagId+"' and Date(r.calculation_for)='"+date+"' and rewardee_type='PLUS'";
			float rewardInDB=Float.parseFloat(queryDatabaseColumn(connReward, queryRewardForPlusBaseTagId,"amount"));
			double rewardCalculated=rewardForBoostTagIds.get(l);
			l++;
			System.out.println("Plus_Boost Reward for website: "+plusTagId+" is: "+rewardInDB+" in DB while Calculated is: "+rewardCalculated);
			softAssert.assertEquals(rewardInDB, rewardCalculated,0.01,"Plus Boost reward for tag ID: "+plusTagId+" is= "+rewardInDB+" in DB and expected calculated reward is= "+rewardCalculated);		
		}
		softAssert.assertAll();

		
	}
}
