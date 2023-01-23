package ss.uno.cards;

public class Card extends AbstractCard{
    private Symbol symbol;

    /**
     * Creates a special card with the color black, and it's respective symbol
     * @param colour
     * @param symbol
     */
    public Card(Colour colour, Symbol symbol){
        super(colour);
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return symbol.toString() + " " + getColour().toString() + "\n";
    }
}
