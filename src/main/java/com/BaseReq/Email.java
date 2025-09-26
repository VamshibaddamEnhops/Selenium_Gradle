package com.BaseReq;

import com.utils.TestSummary;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class Email {

    static String key, data, from, to, cc;
    static String[] AllToAddress;
    static String[] AllCcAddress;

    static String host = "smtp-relay.brevo.com";
    static String authUser;
    static String password;

    public void GetData() throws IOException {
        File prop = new File("src/test/resources/config/config.properties");
        FileInputStream input = new FileInputStream(prop);
        Properties props = new Properties();
        props.load(input);

        Enumeration<?> value = props.keys();
        while (value.hasMoreElements()) {
            key = (String) value.nextElement();
            data = props.getProperty(key);

            switch (key) {
                case "to":
                    to = data;
                    break;
                case "cc":
                    cc = data;
                    break;
                case "from":
                    from = data;
                    break;
                // Removed reading authuser and password from config
            }
        }

        // Load sensitive data from environment variables
        authUser = System.getenv("SMTP_AUTHUSER");
        password = System.getenv("SMTP_PASSWORD");

        if (authUser == null || password == null) {
            throw new IllegalStateException("SMTP credentials not found in environment variables.");
        }

        AllToAddress = to.split(",");
        AllCcAddress = cc.split(",");
    }

    public void mail() throws IOException {
        GetData();

        try {
            Properties props = new Properties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            // TO recipients
            for (String address : AllToAddress) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            }

            // CC recipients
            for (String address : AllCcAddress) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(address));
            }

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            message.setSubject("Execution Report - " + dateFormat.format(date));

            // Email body
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlBody =
                    "<html><body>" +
                            "<h2>Automation Report For OpenCart Web Application - Selenium Cucumber Gradle GitHub Actions</h2>" +

                            "<h3>Machine Summary</h3>" +
                            "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; text-align:left;'>" +
                            "<tr><th style='background-color:#4472c4; color:white;'>Machine Name</th><td>EH-COE-QA-VM-CI</td></tr>" +
                            "<tr><th style='background-color:#4472c4; color:white;'>OS Version</th><td>Windows 10 64bit</td></tr>" +
                            "<tr><th style='background-color:#4472c4; color:white;'>Application</th><td>OpenCart</td></tr>" +
                            "<tr><th style='background-color:#4472c4; color:white;'>Browser</th><td>Chrome Version 136.0.7103.93 (Official Build) (64-bit)</td></tr>" +
                            "</table><br>" +

                            "<h3>Execution Summary</h3>" +
                            "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; text-align:center;'>" +
                            "<tr>" +
                            "<th style='background-color:#4472c4; color:white;'>Module</th>" +
                            "<th style='background-color:#00b050; color:white;'>#Passed</th>" +
                            "<th style='background-color:#c00000; color:white;'>#Failed</th>" +
                            "<th style='background-color:#4472c4; color:white;'>Total</th>" +
                            "<th style='background-color:#4472c4; color:white;'>Pass Percentage (%)</th>" +
                            "</tr>" +
                            "<tr>" +
                            "<td>Test Suite</td>" +
                            "<td>" + TestSummary.passed + "</td>" +
                            "<td>" + TestSummary.failed + "</td>" +
                            "<td>" + TestSummary.total + "</td>" +
                            "<td>" + String.format("%.2f", TestSummary.passPercentage) + "%</td>" +
                            "</tr></table><br>" +

                            "<p style='font-style:italic;'>Note: This is confidential property of ProArch and should not be distributed outside the organization.</p>" +
                            "</body></html>";

            messageBodyPart.setContent(htmlBody, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Attach report if it exists
            File file = new File(".\\target\\opencart.html");
            if (file.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file.getAbsolutePath());
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName("Report_" + dateFormat.format(date) + ".html");
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            // Send email
            Transport transport = session.getTransport("smtp");
            transport.connect(host, authUser, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            for (String address : AllToAddress) {
                System.out.println("Mail sent to: " + address);
            }
            for (String address : AllCcAddress) {
                System.out.println("Mail CC'ed to: " + address);
            }

        } catch (AddressException ae) {
            System.out.println("Address exception: " + ae);
        } catch (MessagingException me) {
            System.out.println("Messaging exception: " + me);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}

