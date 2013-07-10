package com.mercury.bean;

import com.mercury.entity.Word;
import com.mercury.interceptor.WordInterceptor;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 * 
 * Cet SessionBean permet l'enregistrement de mot, et la gestion des messages asynchrone avec JMS
 * pour l'envoi d'email, un Message Driven Bean (com.mercury.bean.EmailSenderBean.class)
 * est configure pour acceder au jms/topic/sendWord pour lire les messages et envoye un email
 * 
 * ### A noter que le module Arquillian-glassfish_embedded contient un bug qui empeche l'execution propre du JMS
 * ###
 */
@Stateless
public class WordBean extends AbstractFacade<Word> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordBean.class);
    
    @PersistenceContext//(unitName = "com.mercury_mercury-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    /////@Resource(mappedName = "jms/mercuryConnectionFactory")
    private ConnectionFactory connectionFactory;
    ////@Resource(mappedName = "jms/topic/sendWord")
    private Topic destinationWord;
    private Connection connection;

    @PostConstruct
    public void openConnection() {
        /**try {
            connection = connectionFactory.createConnection();
        } catch (JMSException e) {
            throw new EJBException(e);
        }*/
    }

    @PreDestroy
    public void closeConnection() {
       /* if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                throw new EJBException(e);
            }
        }*/
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WordBean() {
        super(Word.class);
    }
    
    /**
     * Methode d'enregistrement du message dans la base de donnees,
     * annotee avec l'Interceptor
     * @param word : objet contenant les informations du message
     * @param metadata : String.. ellipse, ensemble de parametres tels que l'adresse IP du client
     */
    //@Override
    @Interceptors(WordInterceptor.class)
    public void create(Word word, String ip_address) {
        super.create(word);
        // TODO to uncomment for production environment 
        //this.sendEmailEnAsynchrone(word);
        //this.publishWord(word);//publish on jms/topic/sendWord 
    }

    private void publishWord(Word word) {
        Session session;
        try {
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destinationWord);
            ObjectMessage objectMessage = session.createObjectMessage();
            objectMessage.setObject(word);
            producer.send(objectMessage);
        } catch (JMSException e) {
            throw new EJBException(e);
        }
    }
    
    /**
     * Pour l'envoi asynchrone d'email apres l'envoi reussi de message
     */
    @Resource(name = "mail/mercuryGmailSession")
    private javax.mail.Session session;
    @Asynchronous
    public void sendEmailEnAsynchrone(Word word){
        try {
            // Create email and headers.  
            Message msg = new MimeMessage(session);
            msg.setSubject("Send Message from mercury with id :" + word.getId());
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress("papesdiop@gmail.com", "Pape"));
            msg.setFrom(new InternetAddress("psdnoreply@gmail.com", "Mercury"));
            // text body
            msg.setText("The message labelled " + word.getMessage() + " is sent!!!");
            // Send email.  
            Transport.send(msg);
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
