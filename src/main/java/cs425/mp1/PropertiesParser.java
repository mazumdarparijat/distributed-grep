package cs425.mp1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Parse properties file
 */
public class PropertiesParser {
    private final String PROP_FILENAME;

    /** Constructor
     * @param propFileName
     */
    private PropertiesParser(String propFileName) {
        PROP_FILENAME=propFileName;
    }

    /** get new parser instance
     * @param configFileName
     * @return
     */
    public static PropertiesParser getParserInstance(String configFileName) {
        return new PropertiesParser(configFileName);
    }

    /** read config file and populates server specifications
     * @return array of server specifications
     * @throws IOException
     */
    public ArrayList<ServerSpecs> getServerSpecs() throws IOException {
        Properties pr=new Properties();
        InputStream in=new FileInputStream(PROP_FILENAME);
        if (in!=null) {
            pr.load(in);
        }
        else {
            throw new FileNotFoundException("Resource file " + this.PROP_FILENAME
                    + "not found in classpath");
        }

        String[] serverAddresses=pr.getProperty("serverAddress").split(",");
        String[] serverPorts=pr.getProperty("serverPort").split(",");
        String[] logFilePaths=pr.getProperty("logFilePath").split(",");

        if (serverAddresses.length!=serverPorts.length || serverAddresses.length!=logFilePaths.length)
            throw new AssertionError("number of serverAddresses specified should be same as the" +
                    " number of serverPorts mentioned and the number of logFilePaths mentioned.");

        ArrayList<ServerSpecs> servers=new ArrayList<ServerSpecs>(serverAddresses.length);
        for (int i=0;i<serverAddresses.length;i++) {
            servers.add(new ServerSpecs(serverAddresses[i],Integer.parseInt(serverPorts[i]),logFilePaths[i]));
        }

        return servers;
    }
}
