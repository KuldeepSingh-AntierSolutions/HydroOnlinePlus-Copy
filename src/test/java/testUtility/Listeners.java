package testUtility;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import resources.ExtentReporter;

public class Listeners extends BaseTest implements ITestListener
{
	ExtentReports extent=ExtentReporter.getReportObject();
	ExtentTest test;
	ThreadLocal<ExtentTest> tl= new ThreadLocal<ExtentTest>();//concurrent running of test methods
														// or to make tests thread safe
	@Override
	public void onTestStart(ITestResult result) 
	{
		test=extent.createTest(result.getMethod().getMethodName());
		tl.set(test);
	}
	@Override
	public void onTestSuccess(ITestResult result) 
	{
		tl.get().log(Status.PASS, "Test Passed");
	}
	@Override
	public void onTestFailure(ITestResult result)
	{
		tl.get().fail(result.getThrowable());
		
		// Screenshot
		try {
			driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}

		String filepath = null;
		try {
			filepath = getScreenshot(result.getMethod().getMethodName(), driver);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tl.get().addScreenCaptureFromPath(filepath, result.getMethod().getMethodName());
	}
	@Override
	public void onTestSkipped(ITestResult result) 
	{
		
	}
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) 
	{
		
	}
	@Override
	public void onStart(ITestContext context) 
	{
		
	}
	@Override
	public void onFinish(ITestContext context) 
	{
		extent.flush();
	}
	
	
}