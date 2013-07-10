package com.mercury.util;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 */
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
public class LogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
       /* if(event.getLevel() == Level.ERROR)
            return FilterReply.NEUTRAL;*/
        
        if(event.getLevel() != Level.INFO)
            return FilterReply.DENY;
 
        if (event.getMessage().contains("Tracking mercury"))
            return FilterReply.NEUTRAL;
        return FilterReply.DENY;
    }
}
