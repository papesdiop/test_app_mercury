package com.mercury.bean;

import com.mercury.entity.Word;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 * Cette classe permet l'envoi d'email de façon asynchrone en utilisant l'API JMS.
 * C'est la methode sendEmail() de l'Entite Word.java avec l'annotation Listener @PostPersit 
 * qui sera utilise pour envoye les emails pour chaque message valide avec succes.
 * 
 */
@MessageDriven(mappedName = "jms/topic/sendWord")
public class EmailSenderBean implements MessageListener{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderBean.class);
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String USER = "psdnoreply@gmail.com";
    private static final String PASSWORD = "papesdiop";

    @Override
    public void onMessage(Message message) {
        LOGGER.debug("###################Message Listener for sending email ########################");
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage msg = (ObjectMessage) message;
                Word word = (Word) msg.getObject();
                this.sendEmail(word);
            }
        } catch (JMSException e) {
            LOGGER.error("mercury, Exception JMS : " + e.getMessage());
        } 
    }
    
    @Resource(name = "mail/mercuryGmailSession")
    private Session session;

    /**
     * Implementation de Listener pour l'envoi d'email apres enregistrement de
     * message reussi par le client. ### A noter que la solution (JMS) de
     * message asynchrone avec Le Message Driven Bean peut etre utilise pour une
     * meilleure separation (loosely coupling)
     *
     */
    public void sendEmail(Word word) {
        try {
            // Create email and headers.  
            javax.mail.Message msg = new MimeMessage(session);
            msg.setSubject("Send Message from mercury with id :" + word.getId());
            msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress("papesdiop@gmail.com", "Pape"));
            msg.setFrom(new InternetAddress("psdnoreply@gmail.com", "Mercury"));
            // text body
            msg.setText("The message labelled " + word.getMessage() + " is sent!!!");
            // Send email.  
            Transport.send(msg);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("UnsupportedEncodingException in sending Email {}", ex.getMessage());
        } catch (MessagingException ex) {
            LOGGER.error("MessagingException in sending Email {}", ex.getMessage());
        }
    }

    public static void sendEMailStandalone(Word word) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USER, PASSWORD);
                    }
                });
        javax.mail.Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("psdnoreply@gmail.com"));
        String emailTo = "papesdiop@gmail.com";
        msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(emailTo));
        msg.setSentDate(new Date());
        msg.setSubject("[Mercury test] Recording word id :" + word.getId());
        msg.setText("A word with labeled *** " + word.getId() + " *** is sent to database. Mercury test app");
        Transport transport = session.getTransport("smtp");
        transport.connect(SMTP_HOST, USER, PASSWORD);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }
}
