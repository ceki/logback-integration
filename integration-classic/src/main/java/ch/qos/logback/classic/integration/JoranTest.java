package ch.qos.logback.classic.integration;

import org.junit.Test;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

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
	public void simpleList() throws JoranException {
		//configure(ClassicTestConstants.JORAN_INPUT_PREFIX + "simpleList.xml");
	}
}
