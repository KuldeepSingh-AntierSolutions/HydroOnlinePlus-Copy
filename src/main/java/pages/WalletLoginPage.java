package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class WalletLoginPage extends PageUtility
{
	WebDriver driver;
	public WalletLoginPage(WebDriver driver)
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	@FindBy(xpath="//a[text()='Recover using seed phrase']")
	private WebElement recover;
	@FindBy(xpath="//input[@class='seed-word-input']")
	private WebElement inputPhrase;
	@FindBy(xpath="//button[@id='verifyButtonWallet']")
	private WebElement continueButton;
	@FindBy(xpath="//input[@id='password']")
	private WebElement pwd;
	@FindBy(xpath="//input[@id='confirmPassword']")
	private WebElement cnfPwd;
	@FindBy(xpath="//button[@type='submit']")
	private WebElement resetButton;
	@FindBy(xpath="(//button[@type='button']/img)[1]")
	private WebElement eyeButton1;
	@FindBy(xpath="(//button[@type='button']/img)[2]")
	private WebElement eyeButton2;
	@FindBy(xpath="//h3[@class='drawerTextHead']")
	private WebElement successMessage;
	@FindBy(xpath="//button[text()='Unlock wallet']")
	private WebElement unlockButton;
	@FindBy(xpath="//input[@type='password']")
	private WebElement inputPwd;
	@FindBy(xpath="//h3[@class='tableComp_heading_text']")
	private WebElement assets;
	
	@FindBy(xpath="//h5[text()='Hydro']/../../../../td[5]/div/button")	
	private WebElement hydroActionButton;
	
	
	
	public void goToRecoverWallet() throws InterruptedException
	{
		waitForMe(recover);
		clickMe(recover);
		Thread.sleep(1000);
	}
	public void inputSeedPhrase(String phrase) throws InterruptedException
	{
		inputPhrase.sendKeys(phrase);
		scrollPage(0, 500);
		Thread.sleep(2000);
		clickMe(continueButton);	
		Thread.sleep(1000);		
	}
	public void enterNewWalletPassword(String walletPassword) throws InterruptedException
	{
		scrollPage(0, 500);
		pwd.sendKeys(walletPassword);
		cnfPwd.sendKeys(walletPassword);
		
	}
	public void viewPassword() throws InterruptedException
	{
		Thread.sleep(500);
		clickMe(eyeButton1);
		clickMe(eyeButton2);
		Thread.sleep(2000);
		clickMe(eyeButton1);
		clickMe(eyeButton2);		
	}
	public String resetPassword() throws InterruptedException
	{
		clickMe(resetButton);
		Thread.sleep(1000);
		String message=successMessage.getText();
		return message;
	}
	public String unlockWallet(String pwd) throws InterruptedException
	{
		clickMe(unlockButton);
		Thread.sleep(2000);
		inputPwd.sendKeys(pwd);
		Thread.sleep(500);
		clickMe(eyeButton1);
		Thread.sleep(2000);
		clickMe(unlockButton);	
		Thread.sleep(5000);
		String text=assets.getText();		
		return text;
	}
	public MyWalletPage goToMyWallet()
	{
		MyWalletPage myWalletPage=new MyWalletPage(driver);
		return myWalletPage;
	}
	
	public MyWalletPage goToHydroActionButton()
	{
		clickMe(hydroActionButton);
		MyWalletPage myWalletPage=new MyWalletPage(driver);
		return myWalletPage;
	}
	
}
