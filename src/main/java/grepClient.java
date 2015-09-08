import java.io.*;
import java.net.Socket;

public class grepClient {
    public static final String serverAddress="0.0.0.0";
    public static final int serverPort=9090;
    public static void main(String [] args) {
        String regexString=args[0];

        try {
            Socket socket = new Socket(serverAddress,serverPort);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Client Socket created!");
            outputWriter.println(regexString);
            outputWriter.flush();
            System.out.println("regexString passed " + regexString);
            String response;
            while ((response=inputReader.readLine())!=null) {
                System.out.println(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
