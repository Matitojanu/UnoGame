package ss.uno.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client implements Runnable {
    private Socket sock;
    private PrintWriter out;

    public Client() throws IOException {
        this.sock = null;
    }

    public  boolean connect(InetAddress address, int port){
        try{
            sock = new Socket(address, port);
            new Thread(this).start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void close(){
        try {
            sock.close();
        } catch (SocketException e){
            System.out.println("Socket was closed");
        } catch (IOException e) {
            System.out.println("Unknown IO exception!");
        }
    }


    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()))) {
        String msj;
        } catch (IOException e) {

        }
    }
}
