package ss.uno;


import ss.uno.player.AI;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This method is responsible for running a Uno game in the terminal with a maximum ammount of 10 players, and AIs
 */
public class UnoTUI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scanner scn = new Scanner(System.in);
        ArrayList<AbstractPlayer> abstractPlayers = new ArrayList<>();

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
    }
}
