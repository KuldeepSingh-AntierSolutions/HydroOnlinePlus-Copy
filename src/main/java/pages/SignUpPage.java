package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class SignUpPage extends PageUtility
{
	WebDriver driver;
	public SignUpPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	
	
	@FindBy(xpath="//input[@id='fullName']")
	private WebElement name;
	@FindBy(xpath="//input[@id='email']")
	private WebElement email;
	@FindBy(xpath="//input[@id='password']")
	private WebElement password;
	@FindBy(xpath="//input[@id='confirmPassword']")
	private WebElement confirmPassword;
	@FindBy(xpath="//input[@name='termsAndConditions']")
	private WebElement termsAndConditions;
	@FindBy(xpath="//button[@type='submit']")
	private WebElement submit;
	@FindBy(xpath="//div[@class='go3958317564']")
	private WebElement signUpToast;
	
	public void fillForm(String userName,String userEmail,String pwd) throws InterruptedException
	{	
		name.sendKeys(userName);
		email.sendKeys(userEmail);
		password.sendKeys(pwd);
		confirmPassword.sendKeys(pwd);
		Thread.sleep(1000);
		scrollPage(0, 500);
		Thread.sleep(500);
		termsAndConditions.click();
	}
	public SignInPage submitForm()
	{
		clickMe(submit);
		SignInPage signInPage=new SignInPage(driver);
		return signInPage;
	}
	public String getSignUpToast()
	{
		waitForElementToAppear(signUpToast);
		return signUpToast.getText();
	}
	
	
	
	

	

	
	
}
