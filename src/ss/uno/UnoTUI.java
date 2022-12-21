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
    /*public void start() {
    boolean continueGame = true;
        while (continueGame) {
        reset();
        play();
        System.out.println("\n> Play another time? (y/n)?");
        continueGame = TextIO.getBoolean();
    }*/
    /*public Game(Player s0, Player s1) {
        board = new Board();
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }*/
    /*private void play() {
        System.out.println(board.toString());
        boolean gameRunning = true;
        while (gameRunning) {
            board.setField(players[0].determineMove(board), players[0].getMark());
            update();
            if ( board.gameOver()){
                break;
            }
            board.setField(players[1].determineMove(board), players[1].getMark());
            update();
            if ( board.gameOver() ){
                break;
            }
        }
        printResult();
    }*/
}