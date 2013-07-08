package com.mercury.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerMercury {
	
	static final Logger LOG = LoggerFactory.getLogger(LoggerMercury.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoggerMercury console = new LoggerMercury();
		console.execute();
	}

	public LoggerMercury() {
	}
 
	public void execute() {
		
		if (LOG.isTraceEnabled()) {
			LOG.trace("Test: TRACE level message.");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Test: DEBUG level message.");
		}
		if (LOG.isInfoEnabled()) {
			LOG.info("Test: INFO level message.");
		}
		if (LOG.isWarnEnabled()) {
			LOG.warn("Test: WARN level message.");
		}
		if (LOG.isErrorEnabled()) {
			LOG.error("Test: ERROR level message.");
		}
	}

}