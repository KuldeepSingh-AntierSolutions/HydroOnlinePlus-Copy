package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class SetPasswordPage extends PageUtility
{
	WebDriver driver;
	public SetPasswordPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//input[@name='password']")
	private WebElement password;
	@FindBy(xpath="//input[@name='confirmPassword']")
	private WebElement confirmPassword;
	@FindBy(xpath="//button[@type='submit']")
	private WebElement createWalletPassword;
	@FindBy(xpath="//button[text()='Continue to final step']")
	private WebElement continueToFinalStep;
	
	public MyWebsitesPage setPassword(String pwd)
	{
		password.sendKeys(pwd);
		confirmPassword.sendKeys(pwd);
		clickMe(createWalletPassword);	
		clickMe(continueToFinalStep);
		MyWebsitesPage myWebsitesPage=new MyWebsitesPage(driver);
		return myWebsitesPage;
	}

}
