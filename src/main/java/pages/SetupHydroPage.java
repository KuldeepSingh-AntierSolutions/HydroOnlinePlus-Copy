package pages;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import pageUtility.PageUtility;

public class SetupHydroPage extends PageUtility
{
	WebDriver driver;
	String status;
	public SetupHydroPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	

	@FindBy(xpath="//*[@id=\"root\"]/div[1]/div[2]/div[2]/div/h3")
	private WebElement websiteName;
	@FindBy(xpath="//button[@fdprocessedid='d6nz0m']")
	private WebElement copyButton;
	@FindBy(xpath="//span[contains(text(),'window.Hydro_tagId')]")
	private WebElement tagId;
	@FindBy(xpath="//button[@class='customButton    ']")
	private WebElement verify;
	@FindBy(xpath="//button[text()='Go to dashboard']")
	private WebElement dashboard;
	@FindBy(xpath="//h3[@class='drawerTextHead']")
	private WebElement sideWindow;
	@FindBy(xpath="//button[text()='Try again']")
	private WebElement tryAgain;
	@FindBy(xpath="//button[text()='Get help']")
	private WebElement getHelp;
	@FindBy(xpath="(//button[@class='drawerCloseIcon'])[4]")
	private WebElement closeSideWindow;
	public String getWebsiteName() throws InterruptedException
	{
		Thread.sleep(1000);
		waitForMe(websiteName);
		String name=websiteName.getText();
		String[] names=name.split(" ");
		String website=names[1];
		return website;
	}
	
	public String getTagId() throws InterruptedException
	{
		waitForMe(tagId);
		String id=tagId.getText();
		return id;
	}
	public MyWebsitesPage clickVerifyInstallation() throws InterruptedException
	{
		Thread.sleep(5000);
		clickMe(verify);
		waitForMe(sideWindow);
		Thread.sleep(4000);
		if(dashboard.isDisplayed())
		{
			clickMe(dashboard);
		}
		else
		{
			Assert.assertTrue(false,"Website verification failed");
		}
		MyWebsitesPage myWebsitesPage=new MyWebsitesPage(driver);
		return myWebsitesPage;
//		status=sideWindow.getText();
//		goToSideWindow();
	}
//	public void goToSideWindow() throws InterruptedException
//	{	
//		if(status=="Your dashboard is ready!")
//		{
//			clickMe(dashboard);
//		}
//		else
//		{
//			clickMe(tryAgain);
//			Thread.sleep(1000);
//			clickMe(verify);
//			waitForMe(sideWindow);
//			Thread.sleep(2000);
//			status=sideWindow.getText();
//				if(status=="Your dashboard is ready!")
//				{
//					clickMe(dashboard);
//				}
//				else
//				{
//					clickMe(closeSideWindow);
//					Assert.assertTrue(false, "Website verification failed");
//				}
//		}
//	}
	

}
