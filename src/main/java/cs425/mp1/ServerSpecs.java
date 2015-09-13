package cs425.mp1;

/**
 * Class to store the specifications of a server : server address, port and log file local location
 */
class ServerSpecs {
    public final String serverAddress;
    public final int serverPort;
    public final String logFilePath;

    /** Constructor
     * @param serverAddress_ address
     * @param serverPort_ port
     * @param logFilePath_ log file path
     */
    public ServerSpecs(String serverAddress_,int serverPort_,String logFilePath_) {
        serverAddress = serverAddress_;
        serverPort = serverPort_;
        logFilePath = logFilePath_;
    }
}
