package ss.uno.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {
    public static int port;
    private List<ClientHandler> handlers;
    private ServerSocket serverSocket;
    private boolean running = true;

    public Server(ServerSocket serverSocket){
        this.handlers = new ArrayList<>();
        this.serverSocket = serverSocket;
    }

    public void start(){
        new Thread(this).start();
    }

    public void shutDown(){
        running = false;
        try{
            serverSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        setUp();
        running = true;
        int threadCount = 0;
        while(running){
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, "thread-"+threadCount);
                threadCount++;
                handlers.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e){
                System.out.println("Error");
                running = false;
            }
        }
        shutDown();
    }

    public void setUp(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter port number: ");
        port = scanner.nextInt();
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Started server at port "+port);
            Server server = new Server(serverSocket);

        } catch (IOException e) {
            System.out.println("Could not start server at port "+port);
        }
    }
}
