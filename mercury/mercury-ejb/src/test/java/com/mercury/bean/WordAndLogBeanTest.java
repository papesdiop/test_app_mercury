package com.mercury.bean;

import com.mercury.entity.Log;
import com.mercury.entity.Word;
import com.mercury.interceptor.WordInterceptor;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Cette classe teste les fonctionnalites d'enregistrement de mot dans la base de donnees, 
 * et de lecture de la table contenant les mots enregistres
 * Utilise Arquillian et le module ShrinkWrap qui permet de créer rapidement un archive (jar, war, ejb, etc..)
 * 
 * @author Pape S. Diop <papesdiop@gmail.com>
 */

@RunWith(Arquillian.class)
public class WordAndLogBeanTest{
    
    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "wordMercuryTest.jar")
                .addPackage("com.mercury.entity")
                .addClasses(WordBean.class, LogBean.class)//,EmailSenderBean.class)
                .addClass(WordInterceptor.class)
                .addAsResource("logback-test.xml", "logback.xml")
                .addAsManifestResource("persistence-test.xml", "persistence.xml");
    }
    
    @EJB
    private WordBean wordBean;
    @EJB
    private LogBean logBean;
    
    private Integer _id = null;
    /**
     * Test of create method, of class WordBean.
     * Teste l'enregistrement d'un mot dans la table Word de la bdd
     */
    @Test
    public void testRecordingWordInDatabase(){
        Assert.assertNotNull(wordBean);
        Word word = new Word(null,"Hello Mercury", Calendar.getInstance().getTime());
        wordBean.create(word, "179.189.18.30");
        _id = word.getId();
         Assert.assertNotNull(_id);
    }
    
    @Test
    public void simulateFailedMessage(){
        //Teste l'echec d'envoi de message 
        Word word = new Word();
        word.setMessage(null);// va lancer une exception à la persistance pour une valeur requise (Not Null)
        wordBean.create(word, "179.189.18.30");
    }

    /**
     * Teste si la table Word n'est pas vide
     */
    //@Test
    public void testIfWordTableNotEmpty() {
        if(_id == null){ testRecordingWordInDatabase();}
        List<Word> words = wordBean.findAll();        
        Assert.assertTrue("number of words must to be more than zero! ", !words.isEmpty());
    }
    
    /**
     * Teste l'enregistrement de transaction avec addresse IP du client et les details de la transaction
     */
    @Test
    public void testRecordingLogTransactionInDatabase() {
        Log log = new Log(null, "192.168.238.123", "Test Recording in Log table");
        logBean.create(log);
        Assert.assertNotNull(log.getId());
    }
}
