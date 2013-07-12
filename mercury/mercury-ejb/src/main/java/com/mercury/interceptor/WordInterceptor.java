package com.mercury.interceptor;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 */
import com.mercury.bean.LogBean;
import com.mercury.entity.Log;
import com.mercury.entity.Word;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordInterceptor.class);
    @EJB
    private LogBean logBean;

    @AroundInvoke
    public Object tryToSendWordToDatabaseAndDoLog(InvocationContext context) {
        boolean error = false;
        Word word = (Word) context.getParameters()[0];

        String sent_message = word.getMessage();
        String ip_address = context.getParameters()[1] != null ? context.getParameters()[1].toString() : null;
        LOGGER.debug("***** Interceptor method (mercury app): **** sending-message {} by ip address client {}", sent_message, ip_address);
        try {
            //Si une erreur est rencontree lors d'envoi du mot, cette partie permet l'enregistrement
            // de l'erreur dans le fichier log par l'Appender File configure dans logBack
            LOGGER.error(sent_message);
            //la partie qui enregistre le mot dans la base et publie le message 
            //dans le topic jms/topic/sendWord pour un envoi asynchrone d'email
            return context.proceed();
        } catch (Exception e) {
            error = true;
            sent_message = e.getMessage();
            LOGGER.error("*************Error in sending word****************** " + e.getMessage());
            return e;
        } finally {
            //Enregistre l'adresse IP du client et les details de la transaction dans la base
            //On pouvait aussi seul utiliser l'Appender DB de logBack pour faire le tracking
            // le mot compose "Tracking mercury" permet d'utiliser le filtre LogFilter.class pour l'Appender DB de LogBack
            String msg = error ? " Message failed " : " Message succedded ";
            LOGGER.info("**** Tracking mercury **** " + msg + " {} by ip address client {}", sent_message, ip_address);
            Log log = new Log(null, ip_address, msg + sent_message + " at " + Calendar.getInstance().getTime());
            logBean.create(log);
        }
    }
}