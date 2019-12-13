package com.company;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import java.io.IOException;
import java.util.Properties;


class MailAPI {


    static void sendMail(ErasmusStudents s, String proofOfPaymentPath) {

        final String username = Main.emailUsername ;
        final String password = Main.emailPassword;

        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        MimeMessage msg = new MimeMessage(session);

        try {
            msg.setFrom(username);
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s.getEmail()));
            msg.setSubject("Proof of Payment");

            Multipart emailContent = new MimeMultipart();


            MimeBodyPart proofOfPaymentAttachment = new MimeBodyPart();
            proofOfPaymentAttachment.attachFile(proofOfPaymentPath);

            emailContent.addBodyPart(proofOfPaymentAttachment);

            msg.setContent(emailContent);

            Transport.send(msg);

            System.out.println("Mail sended");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Something went wrong while sending the emails");
            System.exit(0);
        }
    }
}
