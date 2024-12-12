package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class SignInPage extends PageUtility
{
	WebDriver driver;

	public SignInPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//input[@id='email']")
	private WebElement email;
	@FindBy(xpath="//input[@id='password']")
	private WebElement password;
	@FindBy(xpath="//button[@type='submit']")
	private WebElement signIn;
	@FindBy(xpath="//div[@class='customInput-password_wrapper']/button")
	private WebElement eyeButton;
	@FindBy(xpath="//div[text()='Invalid credentials']")
	private WebElement invalidCreds;
	
	public MyWebsitesPage signIn(String username, String pwd) throws InterruptedException
	{		
		driver.navigate().refresh();
		email.sendKeys(username);
		password.sendKeys(pwd);
		Thread.sleep(500);
		eyeButton.click();
		Thread.sleep(2000);
		eyeButton.click();
		Thread.sleep(500);
		scrollPage(0, 500);
		Thread.sleep(500);
		signIn.click();
		Thread.sleep(5000);
		MyWebsitesPage myWebsitesPage=new MyWebsitesPage(driver);
		return myWebsitesPage;		
	}
	public AddFirstWebsitesPage signInOnboarding(String userName,String pwd) throws InterruptedException
	{
		email.sendKeys(userName);
		password.sendKeys(pwd);
		Thread.sleep(500);
		scrollPage(0, 500);
		Thread.sleep(500);
		signIn.click();
		Thread.sleep(5000);
		AddFirstWebsitesPage addFirstWebsitesPage=new AddFirstWebsitesPage(driver);
		return addFirstWebsitesPage;	
	}

	
	public String getInvalidCredsToast()
	{
		waitForElementToAppear(invalidCreds);
		String toast=invalidCreds.getText();
		return toast;		
	}
	
	
}
