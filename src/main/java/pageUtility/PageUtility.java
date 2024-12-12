package pageUtility;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.HydroPlusPage;

public class PageUtility {
	WebDriver driver;

	public PageUtility(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath="//span[text()='My Websites']")
	private WebElement myWebsites;
	@FindBy(xpath="//span[text()='Hydro Plus']")
	private WebElement hydroPlus;
	
	public void waitForElementToAppear(WebElement ele) 
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(ele));
	}
	public void waitForElementToDisappear(WebElement ele) 
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOf(ele));
	}

	public void clickMe(WebElement ele)
	{
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(ele));
		wait.until(ExpectedConditions.elementToBeClickable(ele));
		ele.click();
	}
	public void waitForMe(WebElement ele)
	{
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(ele));
		wait.until(ExpectedConditions.elementToBeClickable(ele));
	}
	public void scrollPage(int x,int y)
	{
		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("window.scrollBy("+x+","+y+");");
	}
	public float convertTimeToSeconds(String potTime)
	{
		float potSeconds = 0;

        // Regular expression to match the time string with optional day, hour, minute, second parts
        Pattern pattern = Pattern.compile("(?:(\\d+)d)?\\s*(?:(\\d+)h)?\\s*(?:(\\d+)m)?\\s*(?:(\\d+)s)?");
        Matcher matcher = pattern.matcher(potTime);

        if (matcher.matches()) 
        {
        	String days = matcher.group(1);
        	String hours = matcher.group(2);
        	String minutes = matcher.group(3);
        	String seconds = matcher.group(4);

        	// Convert each matched part to seconds and sum them up
        	if (days != null) 
        	{
        		potSeconds += Long.parseLong(days) * 86400; // 1 day = 86400 seconds
        	}
        	if (hours != null) 
        	{
        		potSeconds += Long.parseLong(hours) * 3600; // 1 hour = 3600 seconds
        	}
        	if (minutes != null) 
        	{
        		potSeconds += Long.parseLong(minutes) * 60;  // 1 minute = 60 seconds
        	}
        	if (seconds != null) 
        	{
        		potSeconds += Long.parseLong(seconds);        // Seconds
        	}
        }
        return potSeconds;   
	}
	public int getReferenceDelta(String text,String color,String upDown)
	{
		int refDelta;
		int delta=Integer.parseInt(text);
		if(color.equals("green") && upDown.equals("UpwordArrow"))
		{
			refDelta=delta;
		}
		else
		{
			refDelta=0-delta;
		}
		return refDelta;
	}
	public String extractTagId(String text)
	{
		String regex = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
		String extractedTagId="";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Create a matcher to find the pattern in the text
        Matcher matcher = pattern.matcher(text);
       // Check if the matcher finds the UUID and extract it
        if (matcher.find()) 
        {
            extractedTagId = matcher.group();
            System.out.println("Extracted tagId: " + extractedTagId);
        } 
        else 
        {
            System.out.println("No tagId found.");
        }
        return extractedTagId;
	}
	public double toDouble(String amount)
	{
		return Double.parseDouble(amount);
	}

	public void goToMyWebsites()
	{
		myWebsites.click();
	}
	public HydroPlusPage goToHydroPlus() throws InterruptedException
	{
		Thread.sleep(5000);
		clickMe(hydroPlus);
		Thread.sleep(5000);
		HydroPlusPage hydroPlusPage=new HydroPlusPage(driver);
		return hydroPlusPage;
	}
}
