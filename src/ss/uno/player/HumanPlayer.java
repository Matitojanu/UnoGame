package ss.uno.player;

import ss.uno.Board;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.Board;
import ss.uno.UnoGame;

import java.util.Scanner;

/**
 * This class is a subclass of teh abstract class <code>AbstractPlayer</code>.
 * This class is responsible for creating the human player, with a name, and asks for input for moves, until they are valid
 */
public class HumanPlayer extends AbstractPlayer{
    private String name;

    /**
     * This method is a constructor so it creates the HumanPlayer object and sets the name o the player as the one given in parameters.
     * @param name the name the user has inputed
     */
    public HumanPlayer(String name){
        this.name = name;
    }

    /**
     * This method gets the name of the player
     * @return the name the player has
     * @ensures the name of the player is returned
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * This method sets the players' name as the one given as parameters
     * @param name the name of the user
     * @ensures the name given as parameters is set as the players' name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method checks whether there is avalid move in the players' hand
     * @param board the board of the game, on which the last played card is
     * @return true if there exists at least one valid one, false if there is no valid move
     * @requires a valid board with a valid last card
     * @ensures that true is sent if there is a valid move, and false if there are none
     */
    @Override
    public boolean existsValidMove(Board board){
        for (int index = 0; index < getHand().size(); index++) {
            if ( getHand().get(index).getColour() == board.getLastCard().getColour() ){
                return true;
            } else if ( ((Card) getHand().get(index)).getSymbol() == board.getLastCard().getSymbol() ) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns a valid move from the player
     * @return card that is wither played or drawn from the deck
     * @requires a valid board with a valid last card
     * @ensures the move that is sent is valid
     */
    @Override
    public int determineMove(Board board) {
        while (true){
            for (int i = 0; i < getHand().size(); i++) {
                System.out.println(i +" - " + getHand().get(i).toString());
            }
            System.out.println(getHand().size() +" - Draw a card" );
            System.out.println("Last card: "+board.getLastCard().toString());
            Scanner scanner =  new Scanner(System.in);
            System.out.println(getName().toString()+" please input the index of the card you want to play: ");
            int index = scanner.nextInt();
            if(index<=getHand().size()){
                if(index == getHand().size()){
                    return index;
                }
                if((getHand().get(index).getColour() == board.getLastCard().getColour() ||
                        ( (Card) getHand().get(index) ).getSymbol() == board.getLastCard().getSymbol())){
                    return index;
                }
                if(getHand().get(index).getColour() == AbstractCard.Colour.WILD) {
                    return index;
                }
            }
            System.out.println("Wrong input! Please input again");
        }

    }

}
