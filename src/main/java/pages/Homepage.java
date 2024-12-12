package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class Homepage extends PageUtility {
	WebDriver driver;

	public Homepage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void launchUrl() {
		driver.navigate().to("https://qa-dashboard.hydro.online/");
	}

	@FindBy(xpath = "//button[text()='Sign up with email']")
	private WebElement signUpEmail;
	@FindBy(xpath = "//a[text()='Sign in !']")
	private WebElement signIn;
	@FindBy(xpath = "//div[@class='spinLoader']")
	private WebElement loader;
	

	public SignInPage goToSignIn() throws InterruptedException {
		waitForElementToDisappear(loader);
//		Thread.sleep(5000);
		clickMe(signIn);
		SignInPage signInPage = new SignInPage(driver);
		return signInPage;
	}
	
	public SignUpPage goToSignUp() throws InterruptedException
	{
		Thread.sleep(5000);
		clickMe(signUpEmail);
		SignUpPage signUpPage=new SignUpPage(driver);
		return signUpPage;
	}
	
}
