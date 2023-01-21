package ss.uno.client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Pattern;

public class Client implements Runnable {
    private String name;
    private Socket sock;
    private PrintWriter out;

    public Client(Socket socket) throws IOException {
        this.sock = socket;
    }

    public  boolean connect(){
        try{
            out = new PrintWriter(sock.getOutputStream());
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
        while ((msj = in.readLine()) != null){

        }
        } catch (IOException e) {
            this.close();
        }
    }

    /**
     * This method sends the username of the client to its cleint handler
     * @param name the username of the client
     */
    public void sendUserName(String name){
        out.write(name);
    }

    /**
     * This method sends the move of the client to the client handler
     * @param move the move of the client
     */
    public void sendMove(int move){

    }
}
