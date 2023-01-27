package ss.uno;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class UnoGame implements AbstractCard.Ability {
    private Board board;
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private AbstractPlayer playersTurn;
    private ArrayList<AbstractPlayer> players;
    private HashMap<AbstractPlayer, Integer> playersPoints = new HashMap<>();


    public UnoGame(ArrayList<AbstractPlayer> abstractPlayers) {
        this.players = abstractPlayers;
        board = new Board(new Deck());
        for (AbstractPlayer player : abstractPlayers) {
            playersPoints.put(player, 0);
        }
    }

    public void run() {
        while (!isGameOver()){
            board = new Board(new Deck());
            board.setLastCard((Card) board.getDeck().getCard());
            while (true) {
                if (board.getLastCard().getColour() == AbstractCard.Colour.WILD) {
                    board = new Board(new Deck());
                } else {
                    break;
                }
            }
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < players.size(); j++) {
                    playersTurn = players.get(j);
                    drawCard();
                }
            }
            playersTurn = players.get(0);
            System.out.println("New round begins! \n");
            while (!isRoundOver()) {
                if (playersTurn.existsValidMove(board)) {
                    int indexOfCard = playersTurn.determineMove(board);
                    if (indexOfCard < playersTurn.getHand().size()) {
                        Card playedCard = (Card) playersTurn.getHand().get(indexOfCard);
                        playCard(playedCard);
                        System.out.println(playersTurn.getName() + " played the card: " + playedCard.toString());
                        abilityFunction();
                    } else if (indexOfCard == playersTurn.getHand().size()) {
                        System.out.println(playersTurn.getName() + " draws a card \n");
                        drawCard();
                        Card drawnCard = (Card) playersTurn.getHand().get(playersTurn.getHand().size() - 1);
                        if (drawnCard.getColour() == board.getLastCard().getColour() || drawnCard.getColour() == AbstractCard.Colour.WILD || drawnCard.getSymbol() == board.getLastCard().getSymbol()) {
                            playDrawnCard(drawnCard);
                        }
                    }

                } else {
                    System.out.println(playersTurn.getName() + " draws a card \n");
                    drawCard();
                    Card drawnCard = (Card) playersTurn.getHand().get(playersTurn.getHand().size() - 1);
                    if (drawnCard.getColour() == board.getLastCard().getColour() || drawnCard.getColour() == AbstractCard.Colour.WILD || drawnCard.getSymbol() == board.getLastCard().getSymbol()) {
                        playDrawnCard(drawnCard);
                    }
                }
                changeTurn();
            }
            distributePoints();
            System.out.println("Player " + getRoundWinner().getName() + " won the round!");
            System.out.println("Player " + getRoundWinner().getName() + " currently has: " + playersPoints.get(getRoundWinner()) + " points.");
        }
        System.out.println("Player " + getGameWinner().getName() + " won the game!");
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
     * Returns the player that has won the round
     * @return the player that has no more cards in their hand
     */
    public AbstractPlayer getRoundWinner(){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getHand().size()==0){
                return players.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the player that has won the game
     * @return the player that has
     */
    public AbstractPlayer getGameWinner(){
        for(AbstractPlayer player : playersPoints.keySet()){
            if(playersPoints.get(player) >= 500){
                return player;
            }
        }
        return null;
    }

    /**
     * Returns whether the round is finished or not
     * @return true if the round has finished, false otherwhise
     */
    public boolean isRoundOver(){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getHand().size()==0){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the game is finished or not
     * @return true if the game has finished, false otherwhise
     */
    public boolean isGameOver(){
        for(AbstractPlayer player : playersPoints.keySet()){
            if(playersPoints.get(player) >= 500){
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the points from a round and assigns them to the winner
     */
    public void distributePoints(){
        int totalPoints = playersPoints.get(getRoundWinner());
        for(AbstractPlayer player : playersPoints.keySet()){
            for(AbstractCard card : player.getHand()){
                switch(card.getSymbol()){
                    case ZERO -> {

                    }
                    case ONE -> {
                        totalPoints += 1;
                    }
                    case TWO -> {
                        totalPoints += 2;
                    }
                    case THREE -> {
                        totalPoints += 3;
                    }
                    case FOUR -> {
                        totalPoints += 4;
                    }
                    case FIVE -> {
                        totalPoints += 5;
                    }
                    case SIX -> {
                        totalPoints += 6;
                    }
                    case SEVEN -> {
                        totalPoints += 7;
                    }
                    case EIGHT -> {
                        totalPoints += 8;
                    }
                    case NINE -> {
                        totalPoints += 9;
                    }
                    case PLUSTWO -> {
                        totalPoints += 20;
                    }
                    case REVERSE -> {
                        totalPoints += 20;
                    }
                    case SKIPTURN -> {
                        totalPoints += 20;
                    }
                    case PLUSFOUR -> {
                        totalPoints += 50;
                    }
                    case CHANGECOLOR -> {
                        totalPoints += 50;
                    }
                }
            }
        }
        for(AbstractPlayer player : players){
            player.getHand().clear();
        }
        playersPoints.replace(getRoundWinner(), totalPoints);
    }

    public boolean isCardValid(Card card){
        Card lastCard = getBoard().getLastCard();
        if(card.getSymbol()== lastCard.getSymbol() || card.getColour()==lastCard.getColour()){
            return true;
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

    /**
     * Asks the player whether he wants to play the drawn card. AI always plays the card
     * @param drawnCard card drawn by the player that can be played
     */
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
                    if(board.getLastCard().getColour() == AbstractCard.Colour.WILD){
                        board.getLastCard().setColour(AbstractCard.Colour.RED);
                    }else {
                        board.getLastCard().setColour(board.getLastCard().getColour());
                    }
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
                    System.out.println("Player " + playersTurn.getName() + " changed the colour to " + board.getLastCard().getColour().toString() + "\n");
                }else{
                    for(AbstractCard card1 : playersTurn.getHand()) {
                        if (card1.getColour() != AbstractCard.Colour.WILD) {
                            System.out.println("Player " + playersTurn.getName() + " changed the colour to " + card1.getColour().toString() + "\n");
                            board.getLastCard().setColour(card1.getColour());
                            break;
                        }
                    }
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
