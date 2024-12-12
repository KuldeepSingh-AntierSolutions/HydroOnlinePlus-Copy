package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class ManageNodesPage extends PageUtility
{
	WebDriver driver;
	
	public ManageNodesPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(xpath="//tbody/tr/td[2]/span/span")
	private List<WebElement> nodeNames;
	@FindBy(xpath="//tbody/tr/td[4]")
	private List<WebElement> hydroAmount;
	@FindBy(xpath="//tbody/tr/td[5]")
	private List<WebElement> suiAmount;
	@FindBy(xpath="//tbody/tr/td[7]")
	private List<WebElement> rewardSui;
	@FindBy(xpath="//tbody/tr/td[8]")
	private List<WebElement> rewardDollar;
	
	public ArrayList<String> getNodesList() throws InterruptedException
	{
		ArrayList<String> nodesList=new ArrayList<String>();
		for(WebElement node:nodeNames)
		{
			String nodeName=node.getText();
//			System.out.println(nodeName);
			nodesList.add(nodeName);
		}
		return nodesList;
	}
	public ArrayList<Float> getHydroAmounts() throws InterruptedException
	{
		ArrayList<Float> hydroAmounts=new ArrayList<Float>();
		for(WebElement amount:hydroAmount)
		{
			String text=amount.getText();
			Float hydroAmount=Float.parseFloat(text.split(" ")[0]);		
//			System.out.println(hydroAmount);
			hydroAmounts.add(hydroAmount);
		}
		return hydroAmounts;
	}
	public ArrayList<Float> getSuiAmounts() throws InterruptedException
	{
		ArrayList<Float> suiAmounts=new ArrayList<Float>();
		for(WebElement amount:suiAmount)
		{
			String text=amount.getText();
			Float suiAmount=Float.parseFloat(text.split(" ")[0]);		
//			System.out.println(suiAmount);
			suiAmounts.add(suiAmount);
		}
		return suiAmounts;
	}
	public ArrayList<Float> getSuiRewards() throws InterruptedException
	{
		ArrayList<Float> suiRewards=new ArrayList<Float>();
		for(WebElement amount:rewardSui)
		{
			String text=amount.getText();
			Float suiReward=Float.parseFloat(text);		
//			System.out.println(suiReward);
			suiRewards.add(suiReward);
		}
		return suiRewards;
	}
	public ArrayList<Float> getSuiRewardsDollarValue() throws InterruptedException
	{
		ArrayList<Float> suiRewardsDollarValue=new ArrayList<Float>();
		for(WebElement amount:rewardDollar)
		{
			String text=amount.getText();
			Float rewardDollar=Float.parseFloat(text);		
//			System.out.println(rewardDollar);
			suiRewardsDollarValue.add(rewardDollar);
		}
		return suiRewardsDollarValue;
	}
	
	

}
