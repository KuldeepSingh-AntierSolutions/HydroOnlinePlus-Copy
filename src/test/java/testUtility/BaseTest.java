package testUtility;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import pages.Homepage;

public class BaseTest 
{
	public WebDriver driver;
	public Homepage homepage;
	static Connection connPot;
	static Connection connReward;
	static Connection connUserService;
	
	public WebDriver initializeDriver()
	{
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--auto-open-devtools-for-tabs");
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
		driver.manage().window().maximize();
		return driver;
	}
	
	@BeforeTest(alwaysRun=true)
	public Homepage launchBrowser()
	{
		initializeDriver();
		homepage=new Homepage(driver);
		homepage.launchUrl();
		return homepage;
	}
	@AfterTest
	public void closeDriver() throws InterruptedException
	{
		Thread.sleep(5000);
		driver.close();
	}
	@AfterSuite(alwaysRun = true)
	public void exitBrowser() throws InterruptedException, SQLException
	{
		if(connPot!=null)
		{
			connPot.close();
		}
		if(connPot!=null)
		{
			connReward.close();
		}
		if(connPot!=null)
		{
			connUserService.close();
		}
//		Thread.sleep(10000);
//		driver.close();
	}
	 @AfterSuite
	    public void sendReport() {
	        String reportPath = "C:\\Users\\user\\eclipse-workspace\\HydroOnlinePlus\\reports\\index.html";
	        EmailUtils.sendEmailWithAttachment("kuldeep.singh@antiersolutions.com", 
	            "Automation Test Report", 
	            "Please find the attached test report.", 
	            reportPath);
	    }
	
	public String generateWebsite() throws AWTException, InterruptedException
	{
		String website=goToNetlify(System.getProperty("user.dir")+"\\src\\main\\resources\\jsCode");
		return website;
	}
	public void addTagId(String tagId, String filePath2) throws AWTException, InterruptedException
	{
		 String filePath =System.getProperty("user.dir")+"\\src\\main\\resources\\jsCode\\index.html";
         int lineNumberToUpdate = 8; // Example line number to update
         
         // Update content at the specified line
         updateLineInFile(filePath, lineNumberToUpdate,tagId);
         deployWebsite(filePath2);
         switchToTab(0);
         
	}
	private void updateLineInFile(String filePath, int lineNumber, String newContent) {
        File fileToBeModified = new File(filePath);
        StringBuilder oldContent = new StringBuilder();
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            String line;
            int lineCount = 1;

            // Read the content of the file line by line
            while ((line = reader.readLine()) != null) {
                if (lineCount == lineNumber) {
                    // Update the content at the specified line
                    oldContent.append(newContent).append(System.lineSeparator());
                } else {
                    oldContent.append(line).append(System.lineSeparator());
                }
                lineCount++;
            }

            // Write the modified content back to the file
            writer = new FileWriter(fileToBeModified);
            writer.write(oldContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	public String getScreenshot(String testcaseName, WebDriver driver) throws IOException
	{
		TakesScreenshot ts= (TakesScreenshot) driver;
		File src= ts.getScreenshotAs(OutputType.FILE);
		File dest=new File(System.getProperty("user.dir")+"//reports//"+testcaseName+".png");
		FileUtils.copyFile(src,dest);
		return System.getProperty("user.dir")+"//reports//"+testcaseName+".png";
	}
	public void switchToTab(int i)
	{
		ArrayList<String> handles=new ArrayList<>(driver.getWindowHandles());
	    driver.switchTo().window(handles.get(i));
	}
	public String goToNetlify(String filePath) throws AWTException, InterruptedException
	{
		driver.switchTo().newWindow(WindowType.TAB);
	    driver.get("https://app.netlify.com/");
	    driver.findElement(By.xpath("//button[text()='Log in with email']")).click();
	    driver.findElement(By.xpath("//input[@type='email']")).sendKeys("nishant.mitra@antiersolutions.com");
	    driver.findElement(By.xpath("//input[@type='password']")).sendKeys("lotus@TOP123");
	    Thread.sleep(1000);
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    Thread.sleep(5000);
	    driver.findElement(By.xpath("//button[@title='Add new site']")).click();
	    driver.findElement(By.xpath("//button[text()='Deploy manually']")).click();
	    driver.findElement(By.xpath("(//label[@for='dropzone-upload'])[1]")).click();
	    uploadFile(filePath);
	    Thread.sleep(5000);
	    driver.findElement(By.xpath("//span[text()='Site overview']")).click();
	    String website=driver.findElement(By.xpath("//a[@class='status success']")).getAttribute("href");
	    switchToTab(0);
	    return website;
	}
	public void deployWebsite(String filePath) throws AWTException, InterruptedException
	{
		switchToTab(1);
		driver.findElement(By.xpath("//span[text()='Deploys']")).click();
		driver.findElement(By.xpath("(//label[@for='dropzone-upload'])[1]")).click();
		uploadFile(filePath);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[text()='Site overview']")).click();
		driver.findElement(By.xpath("//a[@class='status success']")).click();
		Thread.sleep(10000);
		switchToTab(0);		
	}
	
	public void uploadFile(String filePath) throws AWTException
	{
		StringSelection sel=new StringSelection(filePath);
		Clipboard clip= Toolkit.getDefaultToolkit().getSystemClipboard();
		clip.setContents(sel, null);
		
		Robot robot=new Robot();
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.delay(500);
	}
	public String readPropertiesFile(String key) throws IOException
	{
		FileInputStream fis=new FileInputStream(new File(System.getProperty("user.dir")+"\\existingUser.properties"));
		Properties prop=new Properties();
		prop.load(fis);
		String value=prop.getProperty(key);
		return value;		
	}
	public String readPropertiesFileNew(String key) throws IOException
	{
		FileInputStream fis=new FileInputStream(new File(System.getProperty("user.dir")+"\\newUser.properties"));
		Properties prop=new Properties();
		prop.load(fis);
		String value=prop.getProperty(key);
		return value;		
	}
	public void updatePropertiesFile(String key,String value) throws IOException
	{
		FileInputStream fis=new FileInputStream(new File(System.getProperty("user.dir")+"\\existingUser.properties"));
		Properties prop=new Properties();
		prop.load(fis);
		prop.setProperty(key,value);	
		FileOutputStream fos=new FileOutputStream(new File(System.getProperty("user.dir")+"\\existingUser.properties"));
		prop.store(fos, "Configuration Settings");
	}
	public void updatePropertiesFileNew(String key,String value) throws IOException
	{
		FileInputStream fis=new FileInputStream(new File(System.getProperty("user.dir")+"\\newUser.properties"));
		Properties prop=new Properties();
		prop.load(fis);
		prop.setProperty(key,value);	
		FileOutputStream fos=new FileOutputStream(new File(System.getProperty("user.dir")+"\\newUser.properties"));
		prop.store(fos, "Configuration Settings");
	}
	public String getRandomString()
	{
		String nickname = RandomStringUtils.randomAlphabetic(5, 10);
		return nickname.toLowerCase();
	}
	public String generateNewPassword(int length)
	{
		 final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		 final String lower = "abcdefghijklmnopqrstuvwxyz";
		 final String digits = "0123456789";
		 final String special = "!@#&$";

		 final String all=upper+lower+digits+special;

		 final SecureRandom random = new SecureRandom();

		        StringBuilder password = new StringBuilder(length);

		        // Ensure at least one character from each set is included
		        password.append(upper.charAt(random.nextInt(upper.length())));
		        password.append(lower.charAt(random.nextInt(lower.length())));
		        password.append(digits.charAt(random.nextInt(digits.length())));
		        password.append(special.charAt(random.nextInt(special.length())));

		        // Fill the remaining length with random characters from all sets
		        for (int i = 4; i < length; i++) {
		            password.append(all.charAt(random.nextInt(all.length())));
		        }
		        return password.toString();
	}
	
	public String getApiResponse(String apiEndpoint,String token,String key)
	{
		Response apiResponse=RestAssured
				.given()
				.header("Authorization", "Bearer " + token)
				.get(apiEndpoint);
		String apiData=apiResponse.jsonPath().getString(key);
		return apiData;
		
	}
	
	public int getPingResponse() throws Exception 
	 {
	        HttpClient client = HttpClient.newHttpClient();
	        HttpRequest request = HttpRequest
	        							.newBuilder(URI.create("https://qaapi-analytics.hydro.online/hydro-ping"))
	                                    .build();

	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	        System.out.println(response);

	        int statusCode = response.statusCode();
	        System.out.println("Response code: " + statusCode);
	        return statusCode;
	    }
	public double getPrice(String api,String key)
	{	String price="";
		String resp="";
		Response response=RestAssured.get(api);
		 if (response.getStatusCode() == 200) 
		 {
			 resp=response.asString(); 
		 }
		 else
		 {
			 System.out.println("Unable to fetch Hydro price due to api");
		 }
		JsonPath jsonPath=new JsonPath(resp);
		price=jsonPath.getString(key);
		return Double.parseDouble(price);
	}
	public Connection getInstanceQaPotDB() throws SQLException, IOException
	{
		String url=readPropertiesFile("dbUrl");
		String dbUserName=readPropertiesFile("dbUser");
		String dbPwd=readPropertiesFile("dbPwd");
		if(connPot==null)
		{
		connPot=DriverManager.getConnection(url+"qa_pot",dbUserName,dbPwd);
		}
		return connPot;
	}
	public Connection getInstanceHydroRewardDB() throws SQLException, IOException
	{
		String url=readPropertiesFile("dbUrl");
		String dbUserName=readPropertiesFile("dbUser");
		String dbPwd=readPropertiesFile("dbPwd");
		if(connReward==null)
		{
			connReward=DriverManager.getConnection(url+"hydro_reward",dbUserName,dbPwd);
		}
		return connReward;
	}
	public Connection getInstanceHydroUserServiceDB() throws SQLException, IOException
	{
		String url=readPropertiesFile("dbUrl");
		String dbUserName=readPropertiesFile("dbUser");
		String dbPwd=readPropertiesFile("dbPwd");
		if(connUserService==null)
		{
			connUserService =DriverManager.getConnection(url+"hydro_user_service",dbUserName,dbPwd);
		}
		return connUserService;
	}
	public String queryDatabase(Connection conn,String query) throws SQLException
	{
		String result = null;
		try
		{
			//object of statement class help to execute queries
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(query);
			if(rs.next())//setting pointer to specific row (each .next moves the focus to next row)						
			result=rs.getString(1);//to get the value in int columnindex 	
			if (result==null)
			{
				result="0";
			}
			rs.close();
			st.close();
		}
		catch (SQLException e) 
		{
            e.printStackTrace();
        } 
		return result;				
	}
	public ArrayList<String> queryDatabase(Connection conn,String query,String columnName) throws SQLException
	{
		ArrayList<String> result=new ArrayList<String>();
		try
		{
			//object of statement class help to execute queries
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(query);
			while(rs.next())//setting pointer to specific row (each .next moves the focus to next row)								
			{
				result.add(rs.getString(columnName));//to get the value in int columnindex 	
			}
			rs.close();
			st.close();
		}
		catch (SQLException e) 
		{
            e.printStackTrace();
        } 
		return result;				
	}
	public String queryDatabaseColumn(Connection conn,String query,String columnName) throws SQLException
	{
		String result = null;
		try
		{
			//object of statement class help to execute queries
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(query);
			if(rs.next())//setting pointer to specific row (each .next moves the focus to next row)				
			result=rs.getString(columnName);//to get the value in int columnindex 
			if (result==null)
			{
				result="0";
			}
			rs.close();
			st.close();
		}
		catch (SQLException e) 
		{
            e.printStackTrace();
        } 
		return result;				
	}
	public void verifyUserEmail(Connection conn,String email) throws SQLException
	{	
		String query="UPDATE public.\"Users\" SET \"isEmailVerified\" = TRUE WHERE email = '"+email+"'";
		//object of statement class help to execute queries
		int res=conn.createStatement().executeUpdate(query);	
		if(res==0)
		{
			System.out.println("No email address found to update isEmailVerified");
		}
	}
	public float roundOff(float rewards)
	{
		float result;
		if (rewards>=999.995)
		{
			int rounded=Math.round(rewards/100)*100;//round off to nearest hundred
			result=Float.parseFloat(String.valueOf(rounded));
		}
		else
		{
			result=rewards;
		}
		return result;		
	}	
	public String[] getLastWeekDateRange()
	{
		LocalDate today=LocalDate.now();
		LocalDate previousWeek=today.minusWeeks(1);
		String dateMonday=previousWeek.with(DayOfWeek.MONDAY).toString();
		String dateSunday=previousWeek.with(DayOfWeek.SUNDAY).toString();
		String[] arr= {dateMonday,dateSunday};
		return arr;
	}
	public String getCurrentDate()
	{
		LocalDate today=LocalDate.now();
		return today.toString();
	}
	public String getWeekSoFarDate()
	{
		String date;
		LocalDate today=LocalDate.now();
		// Find the Monday of the current week by adjusting the current date
		LocalDate monday=today.with(DayOfWeek.MONDAY);
		if(today.equals(monday))
		{
			date="Note: No data and delta will be shown on dashboard on monday";
		}
		else
		{
			date=monday.toString();
		}
		return date;
	}
	public String[] getRespectiveDateRangeForLastWeek()
	{
		String previousMonday;
		LocalDate today=LocalDate.now();
		LocalDate currentMonday=today.with(DayOfWeek.MONDAY);
		if(today.equals(currentMonday))
		{
			previousMonday="Note: No delta and last week data will be shown on Monday";
		}
		else
		{
			previousMonday=currentMonday.minusWeeks(1).toString();
		}
		String currentDayPreviousWeek=today.minusWeeks(1).toString();
		String[] arr= {previousMonday,currentDayPreviousWeek};
		return arr;
	}
	public int calculateDelta(float current,float previous)
	{
		float Delta=((current-previous)/previous)*100;
		return Math.round(Delta);
	}
	
	
}
