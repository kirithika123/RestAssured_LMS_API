package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {

    public static Properties readPropertiesFile(String API) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        String FilePath = null;
        if (API=="LMS") {
        	FilePath="./src/test/resources/PropertyFiles/config.properties";
        }
        if (API=="Jobs") {
        	FilePath="./src/test/resources/PropertyFiles/config_jobs.properties";
        }
        try {

            fis = new FileInputStream(FilePath);
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            fis.close();
        }
        return prop;
    }
}
