package ss.uno;

import ss.uno.client.Client;
import ss.uno.player.AI;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class UnoTUI {
    private static UnoGame game;

    /**
     * The function will run the entire game
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scanner scn = new Scanner(System.in);
        ArrayList<AbstractPlayer> abstractPlayers = new ArrayList<>();
        Socket socket;
        Client client;

        System.out.println("Hello!");
        while(true){
            System.out.println("To join Uno! please input the IP adress: ");
            String adress = scanner.nextLine();
            System.out.println("Please input the port: ");
            String port = scanner.nextLine();
            int portNr = Integer.parseInt(port);
            try{
                socket = new Socket(adress, portNr);
                client = new Client(socket);
                break;
            } catch (UnknownHostException e) {
                System.out.println("Sorry, The IP adress is invalid. Please try again");
            } catch (IOException e) {
                System.out.println("Sorry, an error has occured while trying to connect to the server. Please try again");
            } catch (IllegalArgumentException e){
                System.out.println("Sorry, the port is invalid. Please try again.");
            }
        }

        System.out.println("input name");
        String name = scanner.nextLine();
        HumanPlayer player = new HumanPlayer(name);
        client.sendUserName(name);
        client.run();

        //we can't continue the client untill we know the protocol better because we don't know how it will interact with the server



/*
        System.out.println("Hello! Welcome to UNO! \nThe minimum number of players is 2 and the maximum number is 10.\n" +
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