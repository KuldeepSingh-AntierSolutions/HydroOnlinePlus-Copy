package Extras;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailOTP 
{
	public static void main(String[] args) throws InterruptedException
	{
		String resetUrl = null;
		WebDriver driver;
		WebDriverManager.chromedriver().setup();
		driver= new ChromeDriver();	
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("https://qa-dashboard.hydro.online/");	
		Thread.sleep(10000);
		driver.findElement(By.xpath("//a[text()='Sign in !']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[text()='Forgot password?']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='email']")).sendKeys("kuldeep.singh.hydro@gmail.com");
		driver.findElement(By.xpath("//button[text()='Send reset email']")).click();
		
		Thread.sleep(30000);
		
		
		 // Your Gmail credentials
        final String username = "kuldeep.singh.hydro@gmail.com"; // Replace with your email
        final String appPassword = "gdoytmwwbeqrkgvk"; // Replace with the app password

        // Set Gmail IMAP properties
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imap.timeout", "10000");  // Timeout set to 10 seconds

        try {
            // Create a session
            Session session = Session.getInstance(props, null);

            // Connect to the Gmail IMAP server
            Store store = session.getStore();
            store.connect("imap.gmail.com", username, appPassword);

            // Open the Inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Fetch recent messages
            Message[] messages = inbox.getMessages();
            System.out.println("Total messages: " + messages.length);

            // Process the last 1 messages
            for (int i = messages.length - 1; i >= Math.max(0, messages.length - 1); i--) {
                Message message = messages[i];
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);

                // If the email contains the stated keyword, extract it
                if (message.getSubject().contains("reset")) {
                    String content = getTextFromMessage(message);
                    System.out.println("Email Content: " + content);

                    // Extract OTP from email content (adjust regex based on your OTP format)
                    resetUrl = extractResetPasswordUrl(content);
                    if (resetUrl != null) {
                        System.out.println("Extracted URL: " + resetUrl);
                    }
                }
            }

            // Close the folder and store
            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }	
        
        // Get current window handle
        String sourceHandle=driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB); 
        Set<String> handles=driver.getWindowHandles();
        for(String handle:handles)
        {
        	if(!handle.equals(sourceHandle))
        	{
        		driver.switchTo().window(handle);
        		break;
        	}     	
        }
        if (resetUrl != null && !resetUrl.isEmpty()) 
        {
            driver.get(resetUrl);
        } 
        else 
        {
            System.out.println("URL is null or empty.");
        }
	}
	
	// Method to extract plain text from an email
	private static String getTextFromMessage(Message message) throws IOException, MessagingException {
	    StringBuilder result = new StringBuilder();
	    if (message.isMimeType("text/plain")) {
	        // Plain text content
	        result.append(message.getContent().toString());
	    } else if (message.isMimeType("text/html")) {
	        // HTML content
	        result.append(message.getContent().toString());
	    } else if (message.isMimeType("multipart/*")) {
	        // Handle multipart content
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        result.append(getTextFromMimeMultipart(mimeMultipart));
	    }
	    return result.toString();
	}

	// Enhanced method to handle multipart content
	private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {
	    StringBuilder result = new StringBuilder();
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            // Append plain text content
	            result.append(bodyPart.getContent());
	        } else if (bodyPart.isMimeType("text/html")) {
	            // HTML content: you can process or strip tags if needed
	            result.append(bodyPart.getContent());
	        } else if (bodyPart.getContent() instanceof MimeMultipart) {
	            // Nested multipart: recursive call
	            result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
	        }
	    }
	    return result.toString();
	}
	
	 // Method to extract the Reset Password URL using regex
    public static String extractResetPasswordUrl(String htmlContent) {
        // Define the regex pattern for the desired URL
        String urlPattern = "https:\\/\\/qa-dashboard\\.hydro\\.online\\/reset-password#[^\\\"]+";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(urlPattern);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(htmlContent);

        // If a match is found, return the URL
        if (matcher.find()) {
            return matcher.group(); // Extracts the matched URL
        }

        // Return null if no match is found
        return null;
    }

    
//    // Method to extract OTP using regex (adjust regex to match your OTP format)
//    private static String extractOTP(String content) {
//        String otpRegex = "\\b\\d{4,6}\\b"; // Example regex for a 4-6 digit OTP
//        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(otpRegex);
//        java.util.regex.Matcher matcher = pattern.matcher(content);
//        if (matcher.find()) {
//            return matcher.group();
//        }
//        return null;
//    }
    
//    <dependency>
//    <groupId>com.sun.mail</groupId>
//    <artifactId>javax.mail</artifactId>
//    <version>1.6.2</version>
//    </dependency>
    
    // Enable IMAP by Going to settings for email >> See all settings >> Forwarding and POP/IMAP tab >> IMAP access >> Enable IMAP
    // Set App password by Going to Managae Google account >> Security >> How do sign in to Google (Enable 2 Step verification) >>
    // goto App password or directly search for app password >> name an app and get the generated 16 digit password for use.
}
