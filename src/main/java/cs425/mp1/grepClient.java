package cs425.mp1;

import cs425.mp1.PropertiesParser;
import cs425.mp1.ServerSpecs;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
        	int localLcount=0;
            Socket socket=null;
            try {
                socket = new Socket(server_.serverAddress, server_.serverPort);
                socket.setSoTimeout(2000);
            } catch (IOException e) {
                System.err.println("[ERROR] Can't Connect to server " + server_.serverAddress);
                return;
            }
            BufferedReader inputReader = null;
            try {
                inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.err.println("[ERROR] Error creating input stream from socket at client side");
                return;
            }
            PrintWriter outputWriter = null;
            try {
                outputWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                System.err.println("[ERROR] Error creating input stream from socket at client side");
                return;
            }

            outputWriter.println(query_.serialize());
            outputWriter.println(server_.logFilePath);
            outputWriter.flush();
            String response;
            try {
                while ((response=inputReader.readLine())!=null) {
                    grepClient.out.println("["+server_.serverAddress+"]:"+response);
                    localLcount++;
                }
            } catch (IOException e) {
                totalLcount.addAndGet(localLcount);
                System.err.println("[ERROR] Lines from server " + server_.serverAddress + " are incomplete");
                return;
            }
            totalLcount.addAndGet(localLcount);
        }
    }

    private final String configFileName;
    public static PrintStream out;
    public static AtomicInteger totalLcount;
    private grepClient(String configFile) {
        configFileName=configFile;
        out=System.out;
        totalLcount=new AtomicInteger(0);
    }
    private grepClient(String configFile, PrintStream p) {
        configFileName=configFile;
        out=p;
        totalLcount=new AtomicInteger(0);
    }
    public static grepClient getNewInstance(String configFile) {
        return new grepClient(configFile);
    }
    public static grepClient getNewInstance(String configFile, PrintStream p) {
        return new grepClient(configFile,p);
    }
    public void executeGrep(grepQuery query) {
        ArrayList<ServerSpecs> servers=null;
        try {
            servers= PropertiesParser.getParserInstance(configFileName).getServerSpecs();
        } catch (IOException e) {
            System.err.println("[ERROR] Error parsing properties file");
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
            System.err.println("[ERROR] Client's main thread interrupted while waiting for daemons to complete.");
        }finally{
        	System.out.println("Total line(s) : "+totalLcount);
        }
    }
}
