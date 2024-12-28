package Multi_Threaded;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private final ExecutorService threadPool;

    public ThreadPool(int poolSize){
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket clientSocket) {
        try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
            toSocket.println("Hello from ThreadPool " + clientSocket.getInetAddress());
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        int poolSize = 50;
        ThreadPool server = new ThreadPool(poolSize);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000);
            System.out.println("ThreadPool is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                server.threadPool.execute(() -> server.handleClient(clientSocket));
            }
        } catch (IOException ex){
            ex.printStackTrace();
        } finally {
            server.threadPool.shutdown();
        }
    }
}
