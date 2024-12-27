package Single_Threaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    public void run() throws IOException, UnknownHostException {
        int port = 8080;
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        socket.setSoTimeout(10000);
        while (true) {
            System.out.println("Server is listening on port: "+ port);
            Socket acceptedConnection = socket.accept();
            System.out.println("Connected to : "+ acceptedConnection.getRemoteSocketAddress());
            PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(), true);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()));
            toClient.println("Hello world from the server");
        }

    }
    public static void main(String [] args){
        Server server = new Server();
        try{
            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
