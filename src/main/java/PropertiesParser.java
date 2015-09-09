import com.sun.corba.se.spi.activation.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by parijatmazumdar on 09/09/15.
 */
public class PropertiesParser {
    private static final String PROP_FILENAME="config.properties";
    private PropertiesParser() {}

    private static PropertiesParser singleton=null;

    public static PropertiesParser getParserInstance() {
        if (PropertiesParser.singleton==null) {
            PropertiesParser.singleton=new PropertiesParser();
        }

        return PropertiesParser.singleton;
    }

    public ArrayList<ServerSpecs> getServerSpecs() throws IOException {
        Properties pr=new Properties();
        InputStream in=getClass().getClassLoader().getResourceAsStream(PropertiesParser.PROP_FILENAME);
        if (in!=null) {
            pr.load(in);
        }
        else {
            throw new FileNotFoundException("Resource file " + PropertiesParser.PROP_FILENAME
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
