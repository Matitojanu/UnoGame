package ss.uno.cards;

import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;

public class Card extends AbstractCard{
    //private static int number;
    private Symbol symbol;

    /**
     * Creates the card with the respective color and number given as parameters
     * @param colour of the card
     * @param number of the card
     */
    /*public Card(Colour colour, int number){
        super(colour);
        this.number = number;
    }*/

    /**
     * Creates a special card with the color black, and it's respective symbol
     * @param colour
     * @param symbol
     */
    public Card(Colour colour, Symbol symbol){
        super(colour);
        this.symbol = symbol;
    }

    /**
     * Returns the number of the card
     * @return number
     */
    /*public int getNumber(){
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }*/

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return symbol.toString() + "\n" + getColour().toString() + "\n";
        /*if(getSymbol()!= null){
            return symbol.toString() + "\n\n";
        }
        return "Colour: " + getColour() + "\n" + "Number: " + getNumber() + "\n\n";*/
    }
}
