package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class AddWebsitePage extends PageUtility
{
	WebDriver driver;
	public AddWebsitePage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver,this);		
	}
	
	@FindBy(xpath="//input[@id='url']")
	private WebElement urlField;
	@FindBy(xpath="//input[@id='websiteAlias']")
	private WebElement nicknameField;
	@FindBy(xpath="//button[@type='submit']")
	private WebElement submit;
	@FindBy(xpath="//div[text()='Website has been added successfully']")
	private WebElement successToast;
	
	public void enterUrl(String url)
	{
		urlField.sendKeys(url);
	}
	public void enterNickname(String nickname)
	{
		nicknameField.sendKeys(nickname);
	}
	public SetupHydroPage submitDetail()
	{
		submit.click();
		SetupHydroPage setupHydroPage=new SetupHydroPage(driver);
		return setupHydroPage;
	
	}
	public String getSuccessToast()
	{
		waitForMe(successToast);
		successToast.isDisplayed();
		String successMessage=successToast.getText();
		return successMessage;
	}
	public void redirectedToSetupHydro()
	{
		
	}
	
	
	
	

}
