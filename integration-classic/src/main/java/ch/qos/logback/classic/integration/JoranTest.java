package ch.qos.logback.classic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusUtil;

public class JoranTest {

	LoggerContext loggerContext = new LoggerContext();
	Logger logger = loggerContext.getLogger(this.getClass().getName());
	Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
	int diff = 1000; //RandomUtil.getPositiveInt();
 
	void configure(String file) throws JoranException {
		JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(loggerContext);
		loggerContext.putProperty("diff", "" + diff);
		jc.doConfigure(file);

	}
	
	@Test
	public void missingFile() throws JoranException {
		try {
			configure(IntegrationConstants.JORAN_INPUT_PREFIX + "inexistent_file9238.xml");
			fail("we should not reach here");
		} catch(JoranException e) {
			assertTrue(e.getMessage().startsWith("Could not open"));
			return;
		}
		fail("nor here");
	}
	
	
	@Test
	public void simpleList() throws JoranException {
      configure(IntegrationConstants.JORAN_INPUT_PREFIX + "simpleList.xml");
      Logger logger = loggerContext.getLogger(this.getClass().getName());
		Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
		ListAppender<ILoggingEvent> listAppender = (ListAppender<ILoggingEvent>) root.getAppender("LIST");
		assertNotNull(listAppender);
		assertEquals(0, listAppender.list.size());
		String msg = "hello world";
		logger.debug(msg);
		assertEquals(1, listAppender.list.size());
		ILoggingEvent le = (ILoggingEvent) listAppender.list.get(0);
		assertEquals(msg, le.getMessage());
	}
	
	@Test
	public void statusListener() throws JoranException {
		configure(IntegrationConstants.JORAN_INPUT_PREFIX + "statusListener.xml");
		
		StatusUtil statusUtil = new StatusUtil(loggerContext);
		assertTrue(statusUtil.isErrorFree(0));
		assertTrue(statusUtil.containsMatch(Status.WARN,
				"Please use \"level\" attribute within <logger> or <root> elements instead."));
	}
	
	
	
	// https://jira.qos.ch/browse/LOGBACK-1551
	@Test
	public void withScanAndShutdownHook() throws JoranException, InterruptedException {
		configure(IntegrationConstants.JORAN_INPUT_PREFIX + "withScanAndShutdownHook.xml");
		int REPS = 3;
		for(int i = 0; i < REPS; i++) {
			logger.debug("Hello {} "+i);
			Thread.sleep(10000);
		}
		System.out.println("Exiting test methodc called withScanAndShutdownHook");
	}
	
}
