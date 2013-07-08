package com.mercury.bean;

import com.mercury.entity.Log;
import com.mercury.entity.Word;
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
public class WordAndLogFacadeTest{
    
    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "wordMercuryTest.jar")
                .addPackage("com.mercury.entity")
                .addClasses(WordFacade.class, LogFacade.class)
                .addAsManifestResource("persistence-test.xml", "persistence.xml");
    }
    
    @EJB
    private WordFacade wordDAO;
    @EJB
    private LogFacade logDAO;
    
    private Integer _id = null;
    /**
     * Test of create method, of class WordFacade.
     * Teste l'enregistrement d'un mot dans la table Word de la bdd
     */
    @Test
    public void testRecordingWordInDatabase(){
        Assert.assertNotNull(wordDAO);
        Word word = new Word(null,"Hello", Calendar.getInstance().getTime());
        wordDAO.create(word);
        _id = word.getId();
        Assert.assertNotNull(_id);
    }

    /**
     * Teste si la table Word n'est pas vide
     */
    @Test
    public void testIfWordTableNotEmpty() {
        if(_id == null){ testRecordingWordInDatabase();}
        List<Word> words = wordDAO.findAll();        
        Assert.assertTrue("number of words must to be more than zero! ", !words.isEmpty());
    }
    
    /**
     * Teste l'enregistrement de transaction avec addresse IP du client et les details de la transaction
     */
    @Test
    public void testRecordingTransactionInDatabase() {
        Log log = new Log(null, "192.168.238.123", "Recording Hello world at " + Calendar.getInstance().getTime().toString());
        logDAO.create(log);
        Assert.assertNotNull(log.getId());
    }
}
