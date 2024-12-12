package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class SiteSettingsPage extends PageUtility
{
	WebDriver driver;
	public SiteSettingsPage(WebDriver driver) 
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//div[@class='siteSettingImport_script_left']/ul/li[2]/span[2]")
	private WebElement tagIdText;
	
	public String getTagId()
	{
		scrollPage(0, 500);
		String tagId=extractTagId(tagIdText.getText());
		return tagId;
	}

}
