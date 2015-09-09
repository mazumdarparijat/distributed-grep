import com.sun.corba.se.spi.activation.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class grepClient {

    private static class grepClientThread extends Thread {
        ServerSpecs server_;
        String regexString_;

        public grepClientThread(ServerSpecs server, String regexString) {
            server_=server;
            regexString_=regexString;
        }

        @Override
        public void run() {
            Socket socket=null;
            try {
                socket = new Socket(server_.serverAddress, server_.serverPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader inputReader = null;
            try {
                inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter outputWriter = null;
            try {
                outputWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Client Socket created!");
            outputWriter.println(regexString_);
            outputWriter.println(server_.logFilePath);
            outputWriter.flush();
            System.out.println("regexString passed " + regexString_);
            System.out.println("logFile passed " + regexString_);
            String response;
            try {
                while ((response=inputReader.readLine())!=null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String [] args) {
        if ((args.length != 1)) throw new AssertionError("Expecting 1 argument. Received " + args.length);
        String regexString=args[0];

        ArrayList<ServerSpecs> servers=null;
        try {
            servers=PropertiesParser.getParserInstance().getServerSpecs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<grepClientThread> threads=new ArrayList<grepClientThread>(servers.size());
        for (ServerSpecs s : servers) {
            grepClientThread gT = new grepClientThread(s,regexString);
            threads.add(gT);
            gT.start();
        }

        try {
            for (grepClientThread g : threads) {
                g.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
