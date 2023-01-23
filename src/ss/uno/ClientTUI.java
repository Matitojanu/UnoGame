package ss.uno;

import ss.uno.client.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientTUI {
    private static UnoGame game;

    /**
     * The function will run the entire game
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Scanner scn = new Scanner(System.in);
        Socket socket =  new Socket(args[0], Integer.parseInt(args[1]));
        Client client = new Client();
        String name;

        System.out.println("Hello!");
        System.out.println("To join please input your name");
        while(true){
            if( client.connect() ){
                client.sendProtocol(Protocol.HANDSHAKE);
                client.run();
                name = scanner.nextLine();
                if(client.sendName(name)){
                    System.out.println("Successful! Loading...");
                    break;
                } else {
                    System.out.println("Name is already taken. Try again.");
                }

            } else {
                System.out.println("Sorry, we could not establish a connection.");
                return;
            }
        }
        //De facut de la NEWGAME in jos

    }
}