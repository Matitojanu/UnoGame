package ss.uno;

import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class UnoTUI {
    private static UnoGame game;
    /**
     * The function will run the entire game
     */

    public static void main(String[] args) {
        Scanner scanner =  new Scanner(System.in);
        boolean t = true;
        ArrayList<String> names =  new ArrayList<>();
        ArrayList<AbstractPlayer> abstractPlayers = new ArrayList<>();
        int k=0;
        Scanner numberOfPlayers = new Scanner(System.in);
        System.out.println("Please input the number of players who want to play Uno : ");
        int numOfPlay = numberOfPlayers.nextInt();
        while (k < numOfPlay){
            k++;
            System.out.println("Please input the name of player " + k + ": ");
            names.add(k-1, scanner.nextLine());
            abstractPlayers.add(k-1 ,new HumanPlayer(names.get(k-1)));
        }
        UnoGame game = new UnoGame(abstractPlayers);
        game.run();
    }
}