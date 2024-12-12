package testUtility;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.activation.*;
import java.io.File;


public class EmailUtils {

    public static void sendEmailWithAttachment(String to, String subject, String body, String filepath) {
        // Sender's email ID
        final String from = "rajavatkuldeepsingh1063@gmail.com";
        final String password = "wztxfecnxhlzaamy";

        // Get system properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Get the default Session object
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set From
            message.setFrom(new InternetAddress(from));

            // Set To
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is the attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filepath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(new File(filepath).getName());
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}

