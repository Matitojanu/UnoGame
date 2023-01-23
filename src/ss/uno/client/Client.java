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
     * This method sends the move of the client to the client handler, according to the protocol
     * @param move the move of the client
     */
    public void sendMove(int move){
        out.println(move);//TO DO: to send it according to protocol
    }

    /**
     * This method sends the name of the player to the server for the first time and waits for the server to send whether it si accepted or denied, according to the protocol
     * @param name of the client
     * @return True if the name isn't already taken and sets the name of the client to the input, False if the name is indeed taken
     */
    public boolean sendName(String name){
        out.println(Protocol.SETPLAYERNAME + Protocol.DELIMITER + name);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()))){
            String msgFromServer = in.readLine();
            if(msgFromServer.equals(Protocol.SETPLAYERNAME + Protocol.DELIMITER + Protocol.ACCEPTED )){
                this.name = name;
                return true;
            } else if ( msgFromServer.equals(Protocol.SETPLAYERNAME + Protocol.DELIMITER + Protocol.DENIED )){
                return false;
            }
        } catch (SocketTimeoutException e){
            System.out.println("The server has not responded");
        } catch (IOException e) {
            System.out.println("IO exception");
        }
        return false;
    }

    /**
     * This method sends to the server a protocol message
     * @param mesage the protocol
     */
    public void sendProtocol(String mesage){
        out.println(mesage);
    }

    public UnoGame getGame() {
        return game;
    }

    public void setGame(UnoGame game) {
        this.game = game;
    }
}
