package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtility.PageUtility;

public class MyWalletPage extends PageUtility
{
	WebDriver driver;
	public MyWalletPage(WebDriver driver)
	{
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath="(//button[@class='dropdown-toggle btn btn-primary'])[1]")	
	private WebElement hydroActionButton;
	@FindBy(xpath="//h5[text()='SUI']/../../../../td[5]/div/button")
	private WebElement suiActionButton;
	@FindBy(xpath="//a[text()='Transfer']")
	private WebElement transfer;
	@FindBy(xpath="//input[@id='recipentAddress']")
	private WebElement receiverAddressInput;
	@FindBy(xpath="//input[@id='amount']")
	private WebElement amountInput;
	@FindBy(xpath="//div[@class='gasfeeSection_left']/span")
	private WebElement gasFee;
	@FindBy(xpath="//button[text()='Transfer']")
	private WebElement transferButton;
	@FindBy(xpath="(//table[@class='table'])[2]/tbody/tr[1]/td[2]/div/div/h5")
	private WebElement availableBalance;
	@FindBy(xpath="(//table[@class='table'])[2]/tbody/tr[2]/td[2]/div/div/h5")
	private WebElement lockedBalance;
	@FindBy(xpath="(//table[@class='table'])[2]/tbody/tr[3]/td[2]/div/div/h5")
	private WebElement walletBalance;
	
	//hydro
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[1]/td[2]/div/div/h5")
	private WebElement hydroAvailableAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[1]/td[2]/div/div/h6")
	private WebElement hydroAvailableDollarAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[1]/td[3]/div/div/h5")
	private WebElement hydroLockedAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[1]/td[3]/div/div/h6")
	private WebElement hydroLockedDollarAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[1]/td[4]/div/div/h5")
	private WebElement hydroTotalAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[1]/td[4]/div/div/h6")
	private WebElement hydroTotalDollarAmount;
	
	//sui
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[2]/td[2]/div/div/h5")
	private WebElement suiAvailableAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[2]/td[2]/div/div/h6")
	private WebElement suiAvailableDollarAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[2]/td[3]/div/div/h5")
	private WebElement suiLockedAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[2]/td[3]/div/div/h6")
	private WebElement suiLockedDollarAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[2]/td[4]/div/div/h5")
	private WebElement suiTotalAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[2]/td[4]/div/div/h6")
	private WebElement suiTotalDollarAmount;
	
	//usdt and eth
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[1]/td[3]/div/div/h5")
	private WebElement usdtTotalAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[1]/td[3]/div/div/h6")
	private WebElement usdtTotalDollarAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[2]/td[3]/div/div/h5")
	private WebElement ethTotalAmount;
	@FindBy(xpath="(//table[@class='table'])[1]/tbody/tr[2]/td[3]/div/div/h6")
	private WebElement ethTotalDollarAmount;
	@FindBy(xpath="//button[text()='ETH']")
	private WebElement ethWallet;
	@FindBy(xpath="//button[text()='SUI']")
	private WebElement suiWallet;
	
	//hydro crypto amount
	public double getHydroAvailableAmount()
	{
		String amount=hydroAvailableAmount.getText();
		return toDouble(amount.split(" ")[0]);
	}
	public double getHydroLockedAmount()
	{
		String amount=hydroLockedAmount.getText();
		return toDouble(amount.split(" ")[0]);
	}
	public double getHydroTotalAmount()
	{
		String amount=hydroTotalAmount.getText();
		return toDouble(amount.split(" ")[0]);
	}
	
	//hydro dollar amount
	public double getHydroAvailableDollarAmount()
	{
		String amount=hydroAvailableDollarAmount.getText();
		return toDouble(amount.replace("$",""));
	}
	public double getHydroLockedDollarAmount()
	{
		String amount=hydroLockedDollarAmount.getText();
		return toDouble(amount.replace("$",""));
	}
	public double getHydroTotalDollarAmount()
	{
		String amount=hydroTotalDollarAmount.getText();
		return toDouble(amount.replace("$",""));
	}
	
	
	//sui crypto amount
		public double getSuiAvailableAmount()
		{
			String amount=suiAvailableAmount.getText();
			return toDouble(amount.split(" ")[0]);
		}
		public double getSuiLockedAmount()
		{
			String amount=suiLockedAmount.getText();
			return toDouble(amount.split(" ")[0]);
		}
		public double getSuiTotalAmount()
		{
			String amount=suiTotalAmount.getText();
			return toDouble(amount.split(" ")[0]);
		}
		
		//sui dollar amount
		public double getSuiAvailableDollarAmount()
		{
			String amount=suiAvailableDollarAmount.getText();
			return toDouble(amount.replace("$",""));
		}
		public double getSuiLockedDollarAmount()
		{
			String amount=suiLockedDollarAmount.getText();
			return toDouble(amount.replace("$",""));
		}
		public double getSuiTotalDollarAmount()
		{
			String amount=suiTotalDollarAmount.getText();
			return toDouble(amount.replace("$",""));
		}
		
		//usdt and eth dollar and crypto values
		public void goToEthWallet() throws InterruptedException
		{
			Thread.sleep(1000);
			clickMe(ethWallet);
			Thread.sleep(1000);
		}
		public void goToSuiWallet() throws InterruptedException
		{
			Thread.sleep(1000);
			clickMe(suiWallet);
			Thread.sleep(1000);
		}
		public double getUsdtTotalAmount()
		{
			String amount=usdtTotalAmount.getText();
			return toDouble(amount.split(" ")[0]);
		}
		public double getUsdtTotalDollarAmount()
		{
			String amount=usdtTotalDollarAmount.getText();
			return toDouble(amount.replace("$",""));
		}
		public double getEthTotalAmount()
		{
			String amount=ethTotalAmount.getText();
			return toDouble(amount.split(" ")[0]);
		}
		public double getEthTotalDollarAmount()
		{
			String amount=ethTotalDollarAmount.getText();
			return toDouble(amount.replace("$",""));
		}
		//balances table
		public double getAvailableBalance()
		{
			String amount=availableBalance.getText();
			return toDouble(amount.replace("$",""));
		}
		public double getLockedBalance()
		{
			String amount=lockedBalance.getText();
			return toDouble(amount.replace("$",""));
		}
		public double getWalletBalance()
		{
			String amount=walletBalance.getText();
			return toDouble(amount.replace("$",""));
		}
		
		
	
	
	
	public void clickTransferOption() throws InterruptedException
	{
		clickMe(hydroActionButton);
		Thread.sleep(2000);
		clickMe(transfer);
	}
	public void enterReceiverAddress(String address) throws InterruptedException
	{
		Thread.sleep(500);
		receiverAddressInput.sendKeys(address);
	}
	public void enterAmount(String amount) throws InterruptedException
	{
		Thread.sleep(500);
		amountInput.sendKeys(amount);
	}
	public String getGasFeeAmount()
	{
		String text=gasFee.getText();
		String fee=text.split(" ")[0];
		return fee;
	}
	public void clickTransfer()
	{
		clickMe(transferButton);
	}
	
	
	
	

}
