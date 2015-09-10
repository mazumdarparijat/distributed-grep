package cs425.mp1;

import cs425.mp1.PropertiesParser;
import cs425.mp1.ServerSpecs;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class grepClient {

    private static class grepClientThread extends Thread {
        ServerSpecs server_;
        grepQuery query_;

        public grepClientThread(ServerSpecs server, grepQuery query) {
            server_=server;
            query_=query;
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

            System.out.println("Client Socket created!" + query_.serialize());
            outputWriter.println(query_.serialize());
            outputWriter.println(server_.logFilePath);
            outputWriter.flush();
//            System.out.println("regexString passed " + regexString_);
//            System.out.println("logFile passed " + regexString_);
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

    private final String configFileName;
    private grepClient(String configFile) {
        configFileName=configFile;
    }

    public static grepClient getNewInstance(String configFile) {
        return new grepClient(configFile);
    }
    public void executeGrep(grepQuery query) {
        ArrayList<ServerSpecs> servers=null;
        try {
            servers= PropertiesParser.getParserInstance(configFileName).getServerSpecs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<grepClientThread> threads=new ArrayList<grepClientThread>(servers.size());
        for (ServerSpecs s : servers) {
            grepClientThread gT = new grepClientThread(s,query);
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
