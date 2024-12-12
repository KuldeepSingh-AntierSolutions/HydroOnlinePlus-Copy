package tests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.HydroPlusPage;
import pages.MyWebsitesPage;
import testUtility.BaseTest;


// get all the variabled from admin
// calculated total unpaid rewards for the publishers
// segregate the publishers as free and plus
// check for if the publisher is eligible for the reward distribution
// 

public class RewardDistributionTest extends BaseTest
{
	MyWebsitesPage myWebsitesPage;
	HydroPlusPage hydroPlusPage;
	Connection connReward;
	Connection connPot;
	double hydPrice;
	float freemiumThresholdDollar;
	float premiumThresholdDollar;
	
	@BeforeClass
	public void signIn() throws IOException, InterruptedException, SQLException
	{	
		hydPrice=getPrice(readPropertiesFile("cmcHydApi"),"data.price");
		connReward= getInstanceHydroRewardDB();
		connPot= getInstanceQaPotDB();
	}
	
	@Test
	public void rewardsDistribution()
	{
		String queryTotalUnpaidRewardsOfPublishers;
	}
}
