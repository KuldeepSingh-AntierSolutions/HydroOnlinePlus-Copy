package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class AddFirstWebsitesPage extends PageUtility
{
	WebDriver driver;
	public AddFirstWebsitesPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	@FindBy(xpath="//input[@id='url']")
	private WebElement websiteUrl;
	@FindBy(xpath="//button[text()='Accept and continue']")
	private WebElement acceptAndContinue;
	@FindBy(xpath="//div[@class='go3958317564']")
	private WebElement websiteAddToast;
	
	public ConnectSuiWalletPage addWebsite(String url)
	{
		waitForElementToAppear(websiteUrl);
		websiteUrl.sendKeys(url);
		websiteUrl.submit();
		clickMe(acceptAndContinue);
		ConnectSuiWalletPage connectSuiWalletPage=new ConnectSuiWalletPage(driver);
		return connectSuiWalletPage;
	}
	public String getAddWebsiteToast()
	{
		waitForElementToAppear(websiteAddToast);
		return websiteAddToast.getText();
	}

}
