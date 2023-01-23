package ss.uno;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class UnoGame implements AbstractCard.Ability {
    private Board board;
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private AbstractPlayer playersTurn;
    private ArrayList<AbstractPlayer> players;

    public UnoGame(ArrayList<AbstractPlayer> abstractPlayers){
        this.players = abstractPlayers;
        board = new Board(new Deck());
        board.setLastCard((Card) board.getDeck().getCard());
        while(true) {
            if ( board.getLastCard().getColour() == AbstractCard.Colour.WILD ) {
                board = new Board(new Deck());
            } else {
                break;
            }
        }
        for(int i = 0; i < 7; i++){
            for (int j = 0; j < players.size(); j++) {
                playersTurn = players.get(j);
                drawCard();
            }
        }
        playersTurn= players.get(0);
    }

    public void run(){
        while(!isGameOver()){
            if(playersTurn.existsValidMove(board)){
                int indexOfCard = playersTurn.determineMove(board);
                if(indexOfCard < playersTurn.getHand().size()){
                    Card playedCard = (Card) playersTurn.getHand().get(indexOfCard);
                    playCard(playedCard);
                    System.out.println(playersTurn.getName() + " played the card: " + playedCard.toString());
                    abilityFunction();
                }else if(indexOfCard == playersTurn.getHand().size()){
                    System.out.println(playersTurn.getName() + " draws a card \n");
                    drawCard();
                    Card drawnCard = (Card) playersTurn.getHand().get(playersTurn.getHand().size()-1);
                    if(drawnCard.getColour() == board.getLastCard().getColour() || drawnCard.getColour() == AbstractCard.Colour.WILD || drawnCard.getSymbol() == board.getLastCard().getSymbol()) {
                        playDrawnCard(drawnCard);
                    }
                }

            }else{
                System.out.println(playersTurn.getName() + " draws a card \n");
                drawCard();
                Card drawnCard = (Card) playersTurn.getHand().get(playersTurn.getHand().size()-1);
                if(drawnCard.getColour() == board.getLastCard().getColour() || drawnCard.getColour() == AbstractCard.Colour.WILD || drawnCard.getSymbol() == board.getLastCard().getSymbol()) {
                    playDrawnCard(drawnCard);
                }
            }
            changeTurn();
        }
        System.out.println("Player "+getWinner().getName()+" won!");
    }
    /**
     * Checks whose player's turn is it and changes it to the next one
     * @return the player who's turn is it
     */
    public AbstractPlayer changeTurn(){
        for (int i = 0; i < players.size(); i++) {
            if(playersTurn == players.get(i)){
                if(playersTurn == players.get(players.size()-1)){
                    playersTurn = players.get(0);
                    return playersTurn;
                }else{
                    playersTurn = players.get(i+1);
                    return playersTurn;
                }
            }
        }
        return playersTurn;

    }

    /**
     * Returns the player that has won the game
     * @return the player that has no more cards in their hand
     */
    public AbstractPlayer getWinner(){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getHand().size()==0){
                return players.get(i);
            }
        }
        return null;
    }

    /**
     * Returns whether the game is finished or not
     * @return true if the game has finished, false otherwhise
     */
    public boolean isGameOver(){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getHand().size()==0){
                return true;
            }
        }
        return false;
    }

    /**
     * This function plays the card that is given as parameters
     * @param card that will be played
     */
    public void playCard(Card card){
        Deck deck = board.getDeck();
        playersTurn.getHand().remove(playersTurn.getHand().indexOf(card));
        board.setLastCard(card);
    }

    /**
     *  This function draws a card from the deck
     * @param card that will be drawn from the deck
     */
    public void drawCard() {
        for (int i = 0; i < players.size(); i++) {
            if ( playersTurn == players.get(i) ) {
                ArrayList<AbstractCard> playersHands =  new ArrayList<>();
                if( board.deckFinished() ){
                    for (AbstractPlayer player: players) {
                        playersHands.addAll(player.getHand());
                    }
                    board.setDeck(new Deck(playersHands));
                    System.out.println("The deck has been reshuffled! \n");
                }
                players.get(i).getHand().add(board.getDeck().getCard());
            }
        }
    }

    public void playDrawnCard(Card drawnCard){
        if(playersTurn instanceof HumanPlayer) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Drawn card: " + drawnCard.toString());
            System.out.println("Do you want to play the drawn card?  Y/N");
            String input = scanner.nextLine();
            if (input.toUpperCase().equals("Y")) {
                playCard(drawnCard);
                System.out.println(playersTurn.getName() + " played the card: " + drawnCard.toString());
                board.setLastCard(drawnCard);
                playersTurn.getHand().remove(drawnCard);
                abilityFunction();
            } else if (input.toUpperCase().equals("N")) {

            }
        }else{
            playCard(drawnCard);
            System.out.println(playersTurn.getName() + " played the card: " + drawnCard.toString());
            board.setLastCard(drawnCard);
            playersTurn.getHand().remove(drawnCard);
            abilityFunction();
        }
    }

    /**
     * Does the ability function based on the last card, and changes the turn before applying it
     */
    public void abilityFunction(){
        Deck deck = board.getDeck();
        Card card = (Card) board.getLastCard();
        switch (card.getSymbol()){
            case  PLUSTWO-> {
                changeTurn();
                for(int i = 0; i<2; i++){
                    drawCard();
                }
            }
            case PLUSFOUR -> {
                changeTurn();
                for(int i = 0; i<4; i++){
                    drawCard();
                }
                if(playersTurn instanceof HumanPlayer) {
                    board.pickColor();
                }else{
                    board.getLastCard().setColour(board.getLastCard().getColour());
                }
            }
            case REVERSE -> {
                if(players.size() == 2){
                    changeTurn();
                }else{
                    Collections.reverse(players);
                }
            }
            case SKIPTURN -> {
                changeTurn();
            }
            case CHANGECOLOR -> {
                if(playersTurn instanceof HumanPlayer) {
                    board.pickColor();
                }else{
                    board.getLastCard().setColour(board.getLastCard().getColour());
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public AbstractPlayer getPlayersTurn() {
        return playersTurn;
    }
}
