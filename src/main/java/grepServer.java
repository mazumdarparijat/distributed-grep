import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class grepServer {
    public static void main(String [] args) {
        try {
            ServerSocket daemon=new ServerSocket(9090);
            System.out.println("[Server] Server started at Socket : "+
                    daemon.getInetAddress()+" Port : "+
                    daemon.getLocalPort());

            while (true) {
                Socket socket = daemon.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
                System.out.println("[Server] Socket created. Waiting for regex.");
                String regex=input.readLine();
                System.out.println("[Server] regex received : " + regex);
                regexMatcher gL = new regexMatcher(regex,output);
                gL.printMatchingLines();
                output.flush();
                System.out.println("[Server] Closing socket connection.");
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}