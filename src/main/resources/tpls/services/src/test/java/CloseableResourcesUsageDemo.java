package ${groupId}.services.test;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CloseableResourcesUsageDemo extends TestCase {
    private final static Logger logger = LoggerFactory.getLogger($.CloseableResourcesUsageDemo.class);

    public void testCloseableResourcesUsage() {
        String path = "pom.xml";

        try (BufferedReader b = new BufferedReader(new FileReader(path))) {
            String line = b.readLine();
            while (line != null) {
                logger.info("{}", line);
                line = b.readLine();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
