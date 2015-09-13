package cs425.mp1;

import cs425.mp1.PropertiesParser;
import cs425.mp1.ServerSpecs;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class implementing client for distributed grep. Multithreaded, establishes socket connection with
 * servers. One server per thread.
 */
public class grepClient {

    /**
     * client thread to fetch matching lines from server and keep line count
     */
    private static class grepClientThread extends Thread {
        ServerSpecs server_;
        grepQuery query_;

        /** Constructor
         * @param server server to connect to
         * @param query query to send
         */
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
            Scanner inputReader = null;
            try {
                inputReader = new Scanner(new InputStreamReader(socket.getInputStream()));
                inputReader.useDelimiter("\n");
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
            while (inputReader.hasNext()) {
                grepClient.out.println("["+server_.serverAddress+"]:"+inputReader.next());
                localLcount++;
            }
            totalLcount.addAndGet(localLcount);
        }
    }

    private final String configFileName;
    public static PrintStream out;
    public static AtomicInteger totalLcount;

    /** Constructor
     * @param configFile configuration file name
     */
    private grepClient(String configFile) {
        configFileName=configFile;
        out=System.out;
        totalLcount=new AtomicInteger(0);
    }

    /** Constructor
     * @param configFile configuration file name
     * @param p output stream
     */
    private grepClient(String configFile, PrintStream p) {
        configFileName=configFile;
        out=p;
        totalLcount=new AtomicInteger(0);
    }

    /** create new instance
     * @param configFile configuration file name
     * @return object of this class
     */
    public static grepClient getNewInstance(String configFile) {
        return new grepClient(configFile);
    }

    /** create new instance with output stream specified
     * @param configFile configuration file name
     * @param p
     * @return
     */
    public static grepClient getNewInstance(String configFile, PrintStream p) {
        return new grepClient(configFile,p);
    }

    /** communicate with servers to get matching lines
     * @param query query object to send
     */
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
