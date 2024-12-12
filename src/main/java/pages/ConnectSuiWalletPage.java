package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class ConnectSuiWalletPage extends PageUtility
{
	WebDriver driver;
	public ConnectSuiWalletPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	@FindBy(xpath="//button[text()='Create with Hydro']")
	private WebElement createWithHydro;
	@FindBy(xpath="//button[text()='Import existing SUI wallet']")
	private WebElement importExistingWallet;
	
	public CreateSuiWalletPage createWallet()
	{
		clickMe(createWithHydro);
		CreateSuiWalletPage createSuiWalletPage=new CreateSuiWalletPage(driver);
		return createSuiWalletPage;
	}
	

}
