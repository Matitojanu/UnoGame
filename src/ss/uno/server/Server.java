package ss.uno.server;

import ss.uno.Protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {
    public final int port = Protocol.PORT;
    private List<ClientHandler> _handlers; //_ is best practice to diferentiate members of the class from local parameters
    private ServerSocket _serverSocket;
    private Socket _socket;


    public Server() throws IOException {
        this._handlers = new ArrayList<>();
    }

    public void start(){
        boolean running = true;
        int threadCount = 0;
        while(running){
            try {
                _socket = _serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(_socket, "thread-"+threadCount);
                _handlers.add(clientHandler);
                threadCount+=1;
                new Thread(clientHandler).start();
            } catch (IOException e){
                System.out.println("Error");
                running = false;
            }
        }
        shutDown();
        new Thread(this).start();
    }

    public void shutDown(){
        try{
            _serverSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            _serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not start server at port "+port);
        }
        System.out.println("Starting server!");
        start();
        /*_running = true;
        int threadCount = 0;
        while(_running){
            try {
                _socket = _serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(_socket, "thread-"+threadCount);
                threadCount++;
                _handlers.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e){
                System.out.println("Error");
                _running = false;
            }
        }
        shutDown();*/
    }

    public void setUp(){
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Enter port number: ");
        port = scanner.nextInt();
        try{

            System.out.println("Started server at port "+port);
            _serverSocket = new ServerSocket(port);
            //Server server = new Server(port);

        } catch (IOException e) {
            System.out.println("Could not start server at port "+port);
        }*/
    }
}
