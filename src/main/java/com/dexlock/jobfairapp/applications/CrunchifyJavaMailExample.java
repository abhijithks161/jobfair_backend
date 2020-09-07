package com.dexlock.jobfairapp.applications;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Crunchify.com
 */

public class CrunchifyJavaMailExample {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

//    public static void main(String args[]) throws AddressException, MessagingException {
//        generateAndSendEmail(email);
//        System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
//    }

    public static boolean generateAndSendEmail(String email, String id) throws AddressException, MessagingException {

        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("nithya@dexlock.com"));
        generateMailMessage.setSubject("Greetings from JobFair App");
        String emailBody = "This is to verify your email, please click the link " +
                "http://10.10.1.203:8080/user/verification?id=" + id +
                " to verify" + "<br><br> Regards, <br>JobFair App";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", "heartecg62@gmail.com", "Password123$");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();

        return true;
    }
}