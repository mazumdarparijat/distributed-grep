package cs425.mp1;

/**
 * Created by parijatmazumdar on 08/09/15.
 */
class ServerSpecs {
    public final String serverAddress;
    public final int serverPort;
    public final String logFilePath;

    public ServerSpecs(String serverAddress_,int serverPort_,String logFilePath_) {
        serverAddress = serverAddress_;
        serverPort = serverPort_;
        logFilePath = logFilePath_;
    }
}
