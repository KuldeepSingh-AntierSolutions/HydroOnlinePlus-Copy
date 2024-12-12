package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class ManageAccountPage extends PageUtility
{
	WebDriver driver;
	public ManageAccountPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	@FindBy(xpath="//input[@id='currentPassword']")
	private WebElement currentPwd;
	@FindBy(xpath="(//form/div/div/div/button/img)[1]")
	private WebElement eyeButton1;
	@FindBy(xpath="//input[@id='newPassword']")
	private WebElement newPwd;
	@FindBy(xpath="(//form/div/div/div/button/img)[2]")
	private WebElement eyeButton2;
	@FindBy(xpath="//input[@id='confirmPassword']")
	private WebElement confirmPwd;
	@FindBy(xpath="(//form/div/div/div/button/img)[3]")
	private WebElement eyeButton3;
	@FindBy(xpath="//button[text()='Reset Password']")
	private WebElement resetButton;
	@FindBy (xpath="//div[text()='Password has been reset successfully']")
	private WebElement successToast;
	
	public void enterCurrentPassword(String pwd) throws InterruptedException
	{
		currentPwd.sendKeys(pwd);
		eyeButton1.click();
		Thread.sleep(1000);
	}
	public void enterNewPassword(String pwd) throws InterruptedException
	{
		newPwd.sendKeys(pwd);
		eyeButton2.click();
		Thread.sleep(1000);
		confirmPwd.sendKeys(pwd);
		eyeButton3.click();
		Thread.sleep(1000);		
	}
	public void submitPassword() throws InterruptedException
	{
		Thread.sleep(500);
		scrollPage(0, 500);
		Thread.sleep(500);
		clickMe(resetButton);
	}
	public String getSuccessToastMessage()
	{
		waitForElementToAppear(successToast);
		String message=successToast.getText();
		return message;
	}

}
