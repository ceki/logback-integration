package ch.qos.logback.classic.integration.jpl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.System.LoggerFinder;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.helpers.WithLayoutListAppender;

public class SLF4J_JPL_IntegrationTest {

	@BeforeClass
	static public void beforeClass() {
		System.setProperty(ClassicConstants.CONFIG_FILE_PROPERTY, "src/main/input/joran/simpleList2.xml");
	}

	@AfterClass
	static public void afterClass() {
		System.clearProperty(ClassicConstants.CONFIG_FILE_PROPERTY);
	}


	@Test
	public void smoke() throws IOException {

		LoggerFinder finder = System.LoggerFinder.getLoggerFinder();
		Logger systemLogger = finder.getLogger("smoke", null);
		systemLogger.log(Level.INFO, "hello");
		systemLogger.log(Level.INFO, "hello %s", "world");

		LoggerContext loggerContext = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
	    ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		
	    WithLayoutListAppender listAppender = (WithLayoutListAppender) rootLogger.getAppender("LIST");
		
		List<String> results = listAppender.list;
		results.forEach(System.out::println);
//		assertEquals(2, results.size());
//		assertEquals("INFO smoke - hello", results.get(0));
//		assertEquals("INFO smoke - hello world", results.get(1));
	}
}
