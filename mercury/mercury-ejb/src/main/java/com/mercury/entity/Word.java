package com.mercury.entity;

import com.mercury.bean.WordBean;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 */
/*
 Cette entite permet l'enregistrement des mots envoyes qui devront etre enregistres dans la base de donnees
 */
@Entity
@Table(name = "word")
@XmlRootElement
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    // @NotNull
    @Column(name = "version")
    @Temporal(TemporalType.TIMESTAMP)
    private Date version;

    public Word() {
    }

    @PrePersist
    public void checkVersionIfNotNull() {
        if (this.version == null) {
            this.version = Calendar.getInstance().getTime();
        }
    }
    
    @Transient
    @Resource(name = "mail/mercuryGmailSession")
    private Session session;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WordBean.class);
    /**
     * Implementation de Listener pour l'envoi d'email apres  enregistrement de message reussi par le client.
     * ### A noter que la solution (JMS) de message asynchrone avec Le Message Driven Bean peut etre utilise
     * pour une meilleure separation (loosely coupling)
     * 
     */
    @PostPersist // commented for development test
    public void sendEmail() {
        try {
            // Create email and headers.  
            Message msg = new MimeMessage(session);
            msg.setSubject("Send Message from mercury with id :" + this.id);
            msg.setRecipient(RecipientType.TO, new InternetAddress("papesdiop@gmail.com", "Pape"));
            msg.setFrom(new InternetAddress("psdnoreply@gmail.com", "Mercury"));
            // text body
            msg.setText("The message labelled " + this.message + " is sent!!!");
            // Send email.  
            Transport.send(msg);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("UnsupportedEncodingException in sending Email {}",ex.getMessage());
        } catch (MessagingException ex) {
            LOGGER.error("MessagingException in sending Email {}",ex.getMessage());
        }
    }

    public Word(Integer id) {
        this.id = id;
    }

    public Word(Integer id, String libelle, Date version) {
        this.id = id;
        this.message = libelle;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String libelle) {
        this.message = libelle;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Word)) {
            return false;
        }
        Word other = (Word) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mercury.entity.Words[ id=" + id + " ]";
    }
}
