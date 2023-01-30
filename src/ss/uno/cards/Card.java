package ss.uno.cards;

/**
 * This class extends <card>AbstractCard</card> and is responsible for creating and initialising the card
 * with a color and symbol, and has a textual representation of the card
 */
public class Card extends AbstractCard{
    private Symbol symbol;

    /**
     * Creates a card with a color and it's respective symbol
     * @param colour the colour of the card
     * @param symbol the symbol of the card
     * @requires valid colour and symbol given as parameters
     * @ensures that the card has been created corectly with the color and symbol given
     */
    public Card(Colour colour, Symbol symbol){
        super(colour);
        this.symbol = symbol;
    }

    /**
     * This method returns the symbol of the card
     * @return the symbol the card has
     * @ensures the corect symbol is returned
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * This method transforms the Card into a textual interface
     * @return the string representation of the card, with the symbol and the color
     * @ensures that the textual representation of the card is accurate and coresponds to the cards' symbol and color
     */
    @Override
    public String toString(){
        return symbol.toString() + " " + getColour().toString() + "\n";
    }
}
