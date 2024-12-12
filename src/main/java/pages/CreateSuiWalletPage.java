package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class CreateSuiWalletPage extends PageUtility
{
	WebDriver driver;
	ArrayList<String> seedPhrase=new ArrayList<String>();
	public CreateSuiWalletPage(WebDriver driver)
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);		
	}
	@FindBy(xpath="//button[text()='Reveal']")
	private WebElement revealButton;
	@FindBy(xpath="//div[@class='mnemonicsStyle']/span")
	private List<WebElement> mnemonics;	
	@FindBy(xpath="//button[text()='Copy']")
	private WebElement copyButton;
	@FindBy(xpath="//button[text()='Download']")
	private WebElement downloadButton;
	@FindBy(xpath="//input[@id='default-checkbox']")
	private WebElement checkbox;
	@FindBy(xpath="//button[text()='Continue']")
	private WebElement continueButton;
	@FindBy(xpath="//button[text()='Set a password']")
	private WebElement setPassword;
	
	
	public void revealMnemonics()
	{
		clickMe(revealButton);
	}
	public ArrayList<String> getMnemonics()
	{
		for(WebElement mnemonic:mnemonics)
		{
			String text=mnemonic.getText().split(" ")[1];
//			System.out.println(text);
			seedPhrase.add(text);				
		}
		return seedPhrase;	
	}
	public void markCheckbox() throws InterruptedException
	{
		Thread.sleep(500);
		scrollPage(0, 1500);
		Thread.sleep(1000);
		clickMe(checkbox);
	}
	public void clickContinueButton()
	{
		clickMe(continueButton);
	}
	public void arrangeMnemonics()
	{
		for(String word:seedPhrase)
		{
			WebElement phrase=driver.findElement(By.xpath("//div[@class='mnemonicsStyle']/span[contains(text(),'"+word+"')]"));
			waitForElementToAppear(phrase);
			clickMe(phrase);
		}
	}
	public SetPasswordPage setPassword()
	{
		clickMe(setPassword);
		SetPasswordPage setPasswordPage=new SetPasswordPage(driver);
		return setPasswordPage;
	}
	
	
	
	
	

}
