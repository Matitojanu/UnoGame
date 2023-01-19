package ss.uno;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;

import java.util.ArrayList;
import java.util.Collections;

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
                Card playedCard = (Card) playersTurn.getHand().get(playersTurn.determineMove(board));
                playCard(playedCard);
                System.out.println(playersTurn.getName() + " played the card: " + playedCard.toString());
                abilityFunction();
            }else{
                System.out.println(playersTurn.getName() + " draws a card \n");
                drawCard();
                if(playersTurn.existsValidMove(board)){
                    Card playedCard = (Card) playersTurn.getHand().get(playersTurn.determineMove(board));
                    playCard(playedCard);
                }
            }
            getTurn();
        }
        System.out.println("Player "+getWinner().getName()+" won!");
    }
    /**
     * Returns the player that has to do a move
     * @return the player who's turn is it
     */
    public AbstractPlayer getTurn(){
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
        for (int i = 0; i < players.size(); i++) {
            if ( playersTurn == players.get(i) ) {
                players.get(i).getHand().remove(players.get(i).getHand().indexOf(card));
                board.setLastCard((card));
            }
        }
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

    public void abilityFunction(){
        Deck deck = board.getDeck();
        Card card = (Card) board.getLastCard();
        switch (card.getSymbol()){
            case  PLUSTWO-> {
                getTurn();
                for(int i = 0; i<2; i++){
                    drawCard();
                }
            }
            case PLUSFOUR -> {
                getTurn();
                for(int i = 0; i<4; i++){
                    drawCard();
                }
                board.pickColor();
            }
            case REVERSE -> {
                Collections.reverse(players);
            }
            case SKIPTURN -> {
                getTurn();
            }
            case CHANGECOLOR -> {
                board.pickColor();
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
