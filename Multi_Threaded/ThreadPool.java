import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.FileReader;

public class ThreadPool {
    private final ExecutorService threadPool;

    public ThreadPool(int poolSize){
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket clientSocket) {
        try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
            toSocket.println("Hello from ThreadPool " + clientSocket.getInetAddress() + "\nYourFile\n");
            toSocket.flush();
            String filePath = "lorem.txt";
            try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while((line = br.readLine()) != null){
                    System.out.println("Sending line: " + line);
                    toSocket.println(line);
                    toSocket.flush();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8010;
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
