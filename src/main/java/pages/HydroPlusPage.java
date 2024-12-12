package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageUtility.PageUtility;

public class HydroPlusPage extends PageUtility
{
	WebDriver driver;
	public HydroPlusPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	@FindBy(xpath="//p[text()='Total nodes']/../../div[2]/h3")
	private WebElement totalNodes;
	@FindBy(xpath="//p[text()='SUI staked']/../../div[2]/h3")
	private WebElement suiStaked;
	@FindBy(xpath="//p[text()='SUI rewards ']/../../div[2]/h3")
	private WebElement suiRewards;
	@FindBy(xpath="//p[text()='SUI rewards (APR)']/../../div[2]/h3")
	private WebElement suiRewardsApr;
	@FindBy(xpath="//p[text()='Boost amount (HYD)']/../../div[2]/h3")
	private WebElement boostAmountHyd;
	@FindBy(xpath="//p[text()='Total boost %']/../../div[2]/h3")
	private WebElement totalBoostPercent;
	@FindBy(xpath="//p[text()='SUI rewards ']/../../div[3]/p")
	private WebElement lastWeekSuiRewards;
	@FindBy(xpath="//p[text()='SUI rewards (APR)']/../../div[3]/p")
	private WebElement lastWeekSuiRewardsApr;
	@FindBy(xpath="//p[text()='Boost amount (HYD)']/../../div[3]/p")
	private WebElement lastWeekBoostAmountHyd;
	@FindBy(xpath="//p[text()='Total boost %']/../../div[3]/p")
	private WebElement lastWeekTotalBoostPercent;
	@FindBy(xpath="(//div[@class='commonChartText_center']/h3/../span)[1]")
	private WebElement suiRewardsDelta;
	@FindBy(xpath="(//div[@class='commonChartText_center']/h3/../span)[2]")
	private WebElement suiRewardsAprDelta;
	@FindBy(xpath="(//div[@class='commonChartText_center']/h3/../span)[3]")
	private WebElement boostAmountHydDelta;
	@FindBy(xpath="(//div[@class='commonChartText_center']/h3/../span)[4]")
	private WebElement totalBoostPercentDelta;
	@FindBy(xpath="(//div[@class='commonChartText_center']/h3/../span)[1]/img")
	private WebElement suiRewardsDeltaArrow;
	@FindBy(xpath="(//div[@class='commonChartText_center']/h3/../span)[2]/img")
	private WebElement suiRewardsAprDeltaArrow;
	@FindBy(xpath="(//div[@class='commonChartText_center']/h3/../span)[3]/img")
	private WebElement boostAmountHydDeltaArrow;
	@FindBy(xpath="(//div[@class='commonChartText_center']/h3/../span)[4]/img")
	private WebElement totalBoostPercentDeltaArrow;   
	@FindBy(xpath="//button[text()='Manage nodes']")
	private WebElement manageNodesButton;
	
	
	
	public int getTotalNodes()
	{
		String text=totalNodes.getText();	
		return Integer.parseInt(text);
	}
	public float getSuiStaked()
	{
		String text=suiStaked.getText();
		return Float.parseFloat(text);
	}
	public float getSuiRewards()
	{
		String text=suiRewards.getText();
		return Float.parseFloat(text);
	}
	public float getSuiRewardsApr()
	{
		String text=suiRewardsApr.getText().replace("%", "");	
		return Float.parseFloat(text);
	}
	public float getBoostAmountHyd()
	{
		String text=boostAmountHyd.getText();	
		return ((float)(((int)(Float.parseFloat(text)*100)))/100);
	}
	public float getTotalBoostPercent()
	{
		String text=totalBoostPercent.getText().replace("%", "");	
		return Float.parseFloat(text);
	}
	public float getLastWeekSuiRewards()
	{
		String text=lastWeekSuiRewards.getText().split(" ")[0];
		return((float)((int)((Float.parseFloat(text))*100)))/100;
	}
	public float getLastWeekSuiRewardsApr()
	{
		String text=lastWeekSuiRewardsApr.getText().split(" ")[0];
		return((float)((int)((Float.parseFloat(text))*100)))/100;	
	}
	public float getLastWeekBoostAmountHyd()
	{
		String text=lastWeekBoostAmountHyd.getText().split(" ")[0];
		return((float)((int)((Float.parseFloat(text))*100)))/100;	
	}
	public float getLastWeekTotalBoostPercent()
	{
		String text=lastWeekTotalBoostPercent.getText().split(" ")[0];
		return((float)((int)((Float.parseFloat(text))*100)))/100;			
	}
	public int getSuiRewardsDelta()
	{
		String text=suiRewardsDelta.getText().replace("%", "");
		if(text.contains(".."))
		{
			text=suiRewardsDelta.getAttribute("title").replace("%", "");
		}
		String color=suiRewardsDelta.getAttribute("class");
		String upDown=suiRewardsDeltaArrow.getAttribute("alt");
		return getReferenceDelta(text,color,upDown);	
	}
	public int getSuiRewardsAprDelta()
	{
		String text=suiRewardsAprDelta.getText().replace("%", "");
		if(text.contains(".."))
		{
			text=suiRewardsAprDelta.getAttribute("title").replace("%", "");
		}
		String color=suiRewardsAprDelta.getAttribute("class");
		String upDown=suiRewardsAprDeltaArrow.getAttribute("alt");
		return getReferenceDelta(text,color,upDown);	
	}
	public int getBoostAmountHydDelta()
	{
		String text=boostAmountHydDelta.getText().replace("%", "");
		if(text.contains(".."))
		{
			text=boostAmountHydDelta.getAttribute("title").replace("%", "");
		}
		String color=boostAmountHydDelta.getAttribute("class");
		String upDown=boostAmountHydDeltaArrow.getAttribute("alt");
		return getReferenceDelta(text,color,upDown);
	}
	public int getTotalBoostPercentDelta()
	{
		String text=totalBoostPercentDelta.getText().replace("%", "");
		if(text.contains(".."))
		{
			text=totalBoostPercentDelta.getAttribute("title").replace("%", "");
		}
		String color=totalBoostPercentDelta.getAttribute("class");
		String upDown=totalBoostPercentDeltaArrow.getAttribute("alt");
		return getReferenceDelta(text,color,upDown);
	}
	public ManageNodesPage goToManageNodesPage() throws InterruptedException
	{
		clickMe(manageNodesButton);
		Thread.sleep(5000);
		ManageNodesPage manageNodesPage=new ManageNodesPage(driver);
		return manageNodesPage;
	}
	
	
	
	
}
