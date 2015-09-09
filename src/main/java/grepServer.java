import java.io.*;
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
            String regex=null;
            String logFilePath=null;

            try {
                input = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
                output = new PrintWriter(socket_.getOutputStream(),true);

                System.out.println("[Server] Socket created.");

                // client sends regex as 1st line
                regex = input.readLine();
                // client sends log file path as 2nd line
                logFilePath =input.readLine();
            } catch (IOException e) {
                closeSocket();
                e.printStackTrace();
            }

            System.out.println("[Server] regex received : " + regex);
            System.out.println("[Server] log path received : " + logFilePath);
            regexMatcher gL = null;
            try {
                gL = new regexMatcher(regex,logFilePath,output);
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
    public static void main(String [] args) {
        ServerSocket daemon= null;
        try {
            daemon = new ServerSocket(9090);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Server] Server started at Socket : "+
                    daemon.getInetAddress()+" Port : "+
                    daemon.getLocalPort());

        while (true) {
            grepServerThread gS=null;
            try {
                gS = new grepServerThread(daemon.accept());
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