package ss.uno;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import java.util.ArrayList;
import java.util.Collections;

import static ss.uno.cards.AbstractCard.Symbol.PLUSFOUR;
import static ss.uno.cards.AbstractCard.Symbol.REVERSE;
import static ss.uno.cards.AbstractCard.Symbol.PLUSTWO;
import static ss.uno.cards.AbstractCard.Symbol.SKIPTURN;
import static ss.uno.cards.AbstractCard.Symbol.CHANGECOLOR;

//The games still has problems checking for valid moves, other than that drawing cards, playing cards with their abilities and deciding a winner works

public class UnoGame implements AbstractCard.Ability {
    private Board board;
    private ArrayList<AbstractPlayer> players = new ArrayList<>();
    private AbstractPlayer playersTurn;

    public UnoGame(ArrayList<AbstractPlayer> playersParam){
        board = new Board(new Deck());
        this.players = playersParam;
        for(int i = 0; i < 7; i++){
            for (int j = 0; j < players.size(); j++) {
                playersTurn = players.get(j);
                drawCard();
            }
        }
        playersTurn = players.get(0);
        board.setLastCard((Card) board.getDeckCards().getCard());
    }

    public void run(){
        while(!isGameOver()){
            if(playersTurn.existsValidMove(board)){
                playCard((Card) playersTurn.getHand().get(playersTurn.determineMove(board)));
                abilityFunction();
            }else{
                System.out.println(playersTurn + "draws a card");
                drawCard();
            }
            getTurn();
        }
        System.out.println("Player "+getWinner().toString()+" won!");
    }
    /**
     * Returns the player that has to do a move
     * @return the player who's turn is it
     */
    public AbstractPlayer getTurn(){
        for (int i = 0; i < players.size()-1; i++) {
            if(playersTurn == players.get(i)){
                if(playersTurn == players.get(players.size()-1)){
                    playersTurn = players.get(0);
                }else{
                    playersTurn = players.get(i++);
                    return playersTurn;
                }
            }
        }
        return null;
    }

    /**
     * Returns the player that has won the game
     * @return the player that has no more cards in their hand
     */
    public AbstractPlayer getWinner(){
        for (int i = 0; i < players.size()-1; i++) {
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
        for (int i = 0; i < players.size()-1; i++) {
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
        Deck deck = getBoard().getDeckCards();
        for (int i = 0; i < players.size(); i++) {
            if(playersTurn == players.get(i)){
                board.setLastCard(card);
                players.get(i).getHand().remove(players.get(i).getHand().indexOf(card));
            }/*else{
                board.setLastCard(card);
                player2.getHand().remove(player2.getHand().indexOf(card));
            }*/
        }

    }

    /**
     *  This function draws a card from the deck
     * @param card that will be drawn from the deck
     */
    public void drawCard(){
        for (int i = 0; i < players.size(); i++) {
            if( playersTurn == players.get(i) ){
                players.get(i).getHand().add(getBoard().getDeckCards().getCard());
            }/*else {
                player2.getHand().add(getBoard().getDeckCards().getCard());
            }*/
        }

    }

    public void abilityFunction(){
        Deck deck = board.getDeckCards();
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
                getTurn();
            }
            case SKIPTURN -> {
                getTurn();
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

    public ArrayList<AbstractPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<AbstractPlayer> players) {
        this.players = players;
    }

}
