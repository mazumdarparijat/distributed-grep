package cs425.mp1;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class grepServer {
    private static class grepServerThread extends Thread {
        Socket socket_;
        public grepServerThread(Socket socket) {
            socket_=socket;
        }

        private void closeSocket() {
            try {
                socket_.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            BufferedReader input;
            PrintWriter output=null;
            grepQuery regex=null;
            String logFilePath=null;

            try {
                input = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
                output = new PrintWriter(socket_.getOutputStream(),true);

                System.out.println("[Server] Socket created.");

                // client sends regex as 1st line
                String serializedRegex = input.readLine();
                regex = grepQuery.deserialize(serializedRegex);

                // client sends log file path as 2nd line
                logFilePath =input.readLine();
            } catch (IOException e) {
                closeSocket();
                e.printStackTrace();
            }

            regexMatcher gL = null;
            try {
                gL = regexMatcher.getNewInstance(regex,logFilePath,output);
            } catch (FileNotFoundException e) {
                closeSocket();
                e.printStackTrace();
            }

            try {
                gL.printMatchingLines();
            } catch (IOException e) {
                e.printStackTrace();
            }

            output.flush();

            System.out.println("[Server] Closing socket connection.");
            closeSocket();
        }
    }

    private final int portNumber;
    private grepServer(int port) {
        portNumber=port;
    }

    public static grepServer getNewInstance(int port) {
        return new grepServer(port);
    }

    public void runServer() {
        ServerSocket mainThread= null;
        try {
            mainThread = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Server] Server started at Socket : " +
                mainThread.getInetAddress() + " Port : " +
                mainThread.getLocalPort());

        while (true) {
            grepServerThread gS=null;
            try {
                gS = new grepServerThread(mainThread.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }

            gS.start();

            try {
                gS.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}