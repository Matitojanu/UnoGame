package ss.uno.cards;

import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;

public class Card extends AbstractCard {
    private int number;

    /**
     * Creates the card with the respective color and number given as parameters
     * @param colour of the card
     * @param number of the card
     */
    public Card(Colour colour, int number){
        super(colour);
        this.number = number;
    }

    /**
     * Returns the number of the card
     * @return number
     */
    public int getNumber(){
        return number;
    }
}
