package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class MyWebsitesPage extends PageUtility
{
	WebDriver driver;
	public MyWebsitesPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	
	@FindBy(xpath="//button[text()='Add website']")
	private WebElement addWebsite;
//	@FindBy(xpath="//div[@class='myWebsiteCards_card_leftWrapper_topText']")
//	private WebElement websiteTile;
	@FindBy(xpath="//div[@class='myWebsiteCards_card_leftWrapper_topText']")
	private WebElement websiteNickname;
	@FindBy(xpath="//a[text()='Upgrade to Plus']")
	private WebElement upgrade;
	@FindBy(xpath="//div[@class='header_rightNav_profileBtn']")
	private WebElement profileButton; 
	@FindBy(xpath="//a[text()='Manage Account']")
	private WebElement manageAccount;
	@FindBy(xpath="//a[text()='Log out']")
	private WebElement logout;
	@FindBy(xpath="//span[text()='My Wallet']")
	private WebElement myWallet;
	@FindBy(xpath="//span[text()='Hydro Plus']")
	private WebElement hydroPlus;
	@FindBy(xpath="//div[text()='Sessions']/../div[2]")
	private List<WebElement> sessions;
	@FindBy(xpath="//div[text()='Time on site']/../div[2]")
	private List<WebElement> timeOnSite;
	@FindBy(xpath="//div[text()='Rewards (Hydro)']/../div[2]")
	private List<WebElement> rewardsHydro;
	@FindBy(xpath="//div[text()='Earned ($)']/../div[2]")
	private List<WebElement> rewardsDollar;
	@FindBy(xpath="//div[@aria-label='Dropdown select']")
	private WebElement dropdown;	
	@FindBy(xpath="//span[text()='Week so far']")
	private WebElement weekSoFar;
	@FindBy(xpath="//span[text()='Last week']")
	private WebElement lastWeek;
	@FindBy(xpath="//span[text()='All time']")
	private WebElement allTime;
	@FindBy(xpath="//div[text()='Sessions']/../div[3]")
	private List<WebElement> lastWeekSessions;
	@FindBy(xpath="//div[text()='Time on site']/../div[3]")
	private List<WebElement> lastWeekTimeOnSite;
	@FindBy(xpath="//div[text()='Rewards (Hydro)']/../div[3]")
	private List<WebElement> lastWeekRewardsHydro;
	@FindBy(xpath="//div[text()='Earned ($)']/../div[3]")
	private List<WebElement> lastWeekRewardsDollar;
	@FindBy(xpath="//div[text()='Sessions']/../div[2]/span")
	private List<WebElement> sessionsDeltaUI;
	@FindBy(xpath="//div[text()='Time on site']/../div[2]/span")
	private List<WebElement> timeOnSiteDeltaUI;
	@FindBy(xpath="//div[text()='Rewards (Hydro)']/../div[2]/span")
	private List<WebElement> rewardsHydroDeltaUI;
	@FindBy(xpath="//div[text()='Earned ($)']/../div[2]/span")
	private List<WebElement> rewardsDollarDeltaUI;
	@FindBy(xpath="//div[text()='Sessions']/../div[2]/span/img")
	private List<WebElement> sessionsDeltaArrow;
	@FindBy(xpath="//div[text()='Time on site']/../div[2]/span/img")
	private List<WebElement> timeOnSiteDeltaArrow;
	@FindBy(xpath="//div[text()='Rewards (Hydro)']/../div[2]/span/img")
	private List<WebElement> rewardsHydroDeltaArrow;
	@FindBy(xpath="//div[text()='Earned ($)']/../div[2]/span/img")
	private List<WebElement> rewardsDollarDeltaArrow;
//	@FindBy(xpath="//div[@class='myWebsiteCards_card  ']")
//	private List<WebElement> websiteTiles;
	@FindBy(xpath="//a[text()='Site settings']")
	private WebElement siteSettings;
	@FindBy(xpath="//div[@class='myWebsiteCards_card_leftWrapper_topText']")
	private List<WebElement> websiteTiles;
	
	
	
			
	
	public AddWebsitePage goToAddWebsite() throws InterruptedException
	{
//		Thread.sleep(5000);
		clickMe(addWebsite);
		AddWebsitePage addWebsitePage=new AddWebsitePage(driver);
		return addWebsitePage;
	}
	public String getWebsiteNickname()
	{
		String nickname=websiteNickname.getText();
		return nickname;
	}
	public ArrayList<String> getWebsiteName()
	{
		ArrayList<String> websites=new ArrayList<>();
		for(WebElement tile:websiteTiles)
		{
			String text=tile.getText();
			String website = null;
			if(text.contains("Free"))
			{
				website=text.replace("Free Plan", "");
			}
			if(text.contains("Hydro"))
			{
				website=text.replace("Hydro Plus", "");
			}
			websites.add(website);			
			System.out.println(website);
		}
		return websites;
	}
	public void goToUpgrade()
	{
		clickMe(upgrade);
	}
	public ManageAccountPage goToManageAccount() throws InterruptedException
	{
//		Thread.sleep(5000);
		clickMe(profileButton);
		clickMe(manageAccount);
		ManageAccountPage manageAccountPage=new ManageAccountPage(driver);
		return manageAccountPage;
	}
	public String getProfileButton()
	{
		waitForElementToAppear(profileButton);
		String profileIcon=profileButton.getText();
		return profileIcon;
	}
	public Homepage goToLogout() throws InterruptedException
	{
		Thread.sleep(5000);
		clickMe(profileButton);
		clickMe(logout);
		Homepage homepage=new Homepage(driver);
		return homepage;
	}
	public WalletLoginPage goToMyWallet() throws InterruptedException
	{
//		Thread.sleep(5000);
		clickMe(myWallet);
		WalletLoginPage walletLoginPage=new WalletLoginPage(driver);
		return walletLoginPage;
	}
	
	public void loginToWallet()
	{
		clickMe(myWallet);	
	}
	public void viewWeekSoFarData() throws InterruptedException
	{
		clickMe(dropdown);
		Thread.sleep(1000);
		clickMe(weekSoFar);
		Thread.sleep(3000);
	}
	public void viewLastWeekData() throws InterruptedException
	{
		clickMe(dropdown);
		Thread.sleep(1000);
		clickMe(lastWeek);
		Thread.sleep(3000);
	}
	public void viewAllTimeData() throws InterruptedException
	{
		clickMe(dropdown);
		Thread.sleep(1000);
		clickMe(allTime);
		Thread.sleep(3000);
	}
	public float getSessions(int i)
	{
		
		String text=sessions.get(i).getText();
		String sessions=text.split("\\r?\\n")[0]; //Unix-style or Windows-style line breaks
		float multiplier=1;
		if(sessions.endsWith("K"))
		{
			multiplier=1000;
			sessions=sessions.substring(0, sessions.length()-1);
		}
		if(sessions.endsWith("M"))
		{
			multiplier=1000000;
			sessions=sessions.substring(0, sessions.length()-1);
		}
		if(sessions.endsWith("B"))
		{
			multiplier = 1000000000;
			sessions=sessions.substring(0, sessions.length()-1);
		}
		return (Float.parseFloat(sessions)*multiplier);	
	}
	public float getTimeOnSite(int i)
	{
		String text=timeOnSite.get(i).getText();
		String pot=text.split("\\r?\\n")[0];
		return convertTimeToSeconds(pot);							
	}
	public float getRewardsHydro(int i)
	{
		String text=rewardsHydro.get(i).getText();
		String reward=text.split("\\r?\\n")[0];
		float multiplier=1;
		if(reward.endsWith("K"))
		{
			multiplier=1000;
			reward=reward.substring(0, reward.length()-1);
		}
		if(reward.endsWith("M"))
		{
			multiplier=1000000;
			reward=reward.substring(0, reward.length()-1);
		}
		if(reward.endsWith("B"))
		{
			multiplier = 1000000000;
			reward=reward.substring(0, reward.length()-1);
		}
		return (Float.parseFloat(reward)*multiplier);
	}
	public float getRewardsDollar(int i)
	{	
		String text=rewardsDollar.get(i).getText().replace("$","");
		String reward=text.split("\\r?\\n")[0];
		float multiplier=1;
		if(reward.endsWith("K"))
		{
			multiplier=1000;
			reward=reward.substring(0, reward.length()-1);
		}
		if(reward.endsWith("M"))
		{
			multiplier=1000000;
			reward=reward.substring(0, reward.length()-1);
		}
		if(reward.endsWith("B"))
		{
			multiplier = 1000000000;
			reward=reward.substring(0, reward.length()-1);
		}
		return (Float.parseFloat(reward)*multiplier);
	}
	public float getLastWeekSessions(int i)
	{
		String text=lastWeekSessions.get(i).getText();
		String sessions=text.replace(" last week","");
		float multiplier=1;
		if(sessions.endsWith("K"))
		{
			multiplier=1000;
			sessions=sessions.substring(0, sessions.length()-1);
		}
		if(sessions.endsWith("M"))
		{
			multiplier=1000000;
			sessions=sessions.substring(0, sessions.length()-1);
		}
		if(sessions.endsWith("B"))
		{
			multiplier = 1000000000;
			sessions=sessions.substring(0, sessions.length()-1);
		}
		return (Float.parseFloat(sessions)*multiplier);		
	}
	public float getLastWeekTimeOnSite(int i)
	{
		String text=lastWeekTimeOnSite.get(i).getText();
		return convertTimeToSeconds(text.replace(" last week",""));					
	}
	public float getLastWeekRewardsHydro(int i)
	{
		String text=lastWeekRewardsHydro.get(i).getText();
		return Float.parseFloat(text.replace(" last week",""));	
	}
	public float getLastWeekRewardsDollar(int i)
	{	
		String text=lastWeekRewardsDollar.get(i).getText().replace("$","");
		return Float.parseFloat(text.replace(" last week",""));
	}
	public int getSessionsDelta(int i)
	{
		String text=sessionsDeltaUI.get(i).getText().replace("%", "");
		if(text.contains(".."))
		{
			text=sessionsDeltaUI.get(i).getAttribute("title").replace("%", "");
		}
		String color=sessionsDeltaUI.get(i).getAttribute("class");
		String upDown=sessionsDeltaArrow.get(i).getAttribute("alt");
		return getReferenceDelta(text,color,upDown);
	}
	public int getTimeOnSiteDelta(int i)
	{
		String text=timeOnSiteDeltaUI.get(i).getText().replace("%", "");
		if(text.contains(".."))
		{
			text=timeOnSiteDeltaUI.get(i).getAttribute("title").replace("%", "");
		}
		String color=timeOnSiteDeltaUI.get(i).getAttribute("class");
		String upDown=timeOnSiteDeltaArrow.get(i).getAttribute("alt");
		return getReferenceDelta(text,color,upDown);
	}
	public int getRewardsHydroDelta(int i)
	{
		String text=rewardsHydroDeltaUI.get(i).getText().replace("%", "");
		if(text.contains(".."))
		{
			text=rewardsHydroDeltaUI.get(i).getAttribute("title").replace("%", "");
		}		
		String color=rewardsHydroDeltaUI.get(i).getAttribute("class");
		String upDown=rewardsHydroDeltaArrow.get(i).getAttribute("alt");
		return getReferenceDelta(text,color,upDown);
	}
	public int getRewardsDollarDelta(int i)
	{
		String text=rewardsDollarDeltaUI.get(i).getText().replace("%", "");
		if(text.contains(".."))
		{
			text=rewardsDollarDeltaUI.get(i).getAttribute("title").replace("%", "");
		}
		String color=rewardsDollarDeltaUI.get(i).getAttribute("class");
		String upDown=rewardsDollarDeltaArrow.get(i).getAttribute("alt");
		return getReferenceDelta(text,color,upDown);
	}
	public int getWebsitesCount()
	{
		int sitesCount=websiteTiles.size();
		return sitesCount;
	}
	public SiteSettingsPage goToSiteSettings(int sequence) throws InterruptedException
	{
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//div[@class='myWebsiteCards_card_leftWrapper_bottomText'])["+sequence+"]")).click();
		Thread.sleep(1000);
		clickMe(siteSettings);			
		SiteSettingsPage siteSettingsPage=new SiteSettingsPage(driver);
		return siteSettingsPage;
	}
	
}
