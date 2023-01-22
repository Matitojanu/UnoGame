package ss.uno.client;

import ss.uno.Protocol;
import ss.uno.UnoGame;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.regex.Pattern;

public class Client implements Runnable {
    private String name;
    private Socket sock;
    private PrintWriter out;
    private UnoGame game;

    public Client(Socket socket) throws IOException {
        this.sock = socket;
        sock.setSoTimeout(180000);
    }

    public  boolean connect(){
        try{
            out = new PrintWriter(sock.getOutputStream());
            this.sendProtocol(Protocol.HANDSHAKE);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()))){
                String msgFromServer = in.readLine();
                if(msgFromServer.equals(Protocol.HANDSHAKE)){ //this can wait for the server infinetly so we need to find solution about it
                    new Thread(this).start();
                    return true;
                }
            } catch (SocketTimeoutException e){
                System.out.println("The server has not responded");
            }

            return false;
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
            switch (msj){
                //TO DO: all cases of input from server
            }

        }
        } catch (IOException e) {
            this.close();
        }
    }

    /**
     * This method sends the username of the client to its cleint handler
     * @param name the username of the client
     */
    public void sendUserNameForChat (String name){
        out.println("[ " + name + " ] : ");
    }

    /**
     * This method sends the move of the client to the client handler
     * @param move the move of the client
     */
    public void sendMove(int move){
        out.println(move); //TO DO: to check for protocol
    }

    public boolean sendName(String name){
        out.println(Protocol.SETPLAYERNAME + Protocol.DELIMITER + name);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()))){
            String msgFromServer = in.readLine();
            if(msgFromServer.equals(Protocol.SETPLAYERNAME + Protocol.DELIMITER + Protocol.ACCEPTED )){
                System.out.println("Successful! Loading...");
                return true;
            } else if ( msgFromServer.equals(Protocol.SETPLAYERNAME + Protocol.DELIMITER + Protocol.DENIED )){
                System.out.println("Name is already Taken. Try again");
            }
        } catch (SocketTimeoutException e){
            System.out.println("The server has not responded");
        } catch (IOException e) {
            System.out.println("IO exception");
        }
        return false;
    }

    public void sendProtocol(String mesage){
        out.println(mesage);
    }
}
