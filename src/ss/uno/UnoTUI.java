package ss.uno;

import ss.uno.player.HumanPlayer;

import java.util.Scanner;

public class UnoTUI {
    private static UnoGame game;
    /**
     * The function will run the entire game
     */

    public static void main(String[] args) {
        Scanner scn1 = new Scanner(System.in);
        Scanner scn2 = new Scanner(System.in);
        System.out.println("Please input the first players' name of Uno: ");
        String name1 = scn1.nextLine();
        System.out.println("Please input the second players' name of Uno: ");
        String name2 = scn2.nextLine();
        HumanPlayer player1 = new HumanPlayer(name1);
        HumanPlayer player2 = new HumanPlayer(name2);
        UnoGame game = new UnoGame(player1, player2);
        game.run();
    }
}