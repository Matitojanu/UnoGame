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
        Client client = new Client(socket);
        String name;

        System.out.println("Hello!");
        while(true){
            System.out.println("To join please input your name");
            if( client.connect() ){
                client.sendProtocol(Protocol.HANDSHAKE);
                client.run();
                name = scanner.nextLine();
            }

        }


        //we can't continue the client untill we know the protocol better because we don't know how it will interact with the server




        /*System.out.println("Hello! Welcome to UNO! \nThe minimum number of players is 2 and the maximum number is 10.\n" +
                "Please input the number of players that want to play: ");
        String nrOfPlay = scanner.nextLine();
        while (true) {
            try {
                if ( Integer.parseInt(nrOfPlay) < 2 || Integer.parseInt(nrOfPlay) > 10 ) {
                    System.out.println("The input is not corect! Please input a valid number of players that want to play: ");
                    nrOfPlay = scanner.nextLine();
                } else {
                    break;
                }

            } catch(NumberFormatException e){
                System.out.println("The input is not corect! Please input a valid number of players that want to play: ");
                nrOfPlay = scanner.nextLine();
        }
    }
        int k=0;
        System.out.println("If you wish to add an AI player, you must put '-' at the begining of their name.");
        while(k<Integer.parseInt(nrOfPlay)){
            System.out.println("Please input the name of player " + (k+1) + ": ");
            String name = scn.nextLine();
            if ( name.charAt(0) == '-' ) {
                abstractPlayers.add(k, new AI(name));
            }else {
                abstractPlayers.add(k, new HumanPlayer(name));
            }
            k++;
        }
        UnoGame game = new UnoGame(abstractPlayers);
        game.run();

         */


    }
}