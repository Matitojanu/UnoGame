package ss.uno.cards;

public abstract class AbstractCard {
    /**
     * All the possibilities of symbols a special card has
     */
    public enum Symbol{
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        PLUSTWO,
        PLUSFOUR,
        CHANGECOLOR,
        SKIPTURN,
        REVERSE,
        ZERO
    }

    /**
     * All the possiblities of colors that can be on a card
     */
    public enum Colour{
        RED,
        YELLOW,
        GREEN,
        BLUE,
        WILD
    }

    private Colour colour;
    private Symbol symbol;
    private Ability ability;

    /**
     * Creates the Card with the colour given as parameter
     * @param colour the color that the card has
     */
    public AbstractCard(Colour colour){
        this.colour =  colour;
    }

    /**
     * Returns the colour of the card object
     * @return the color of the card
     */
    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour){
        this.colour = colour;
    }
    public Symbol getSymbol(){
        return symbol;
    }

    public void setSymbol(Symbol symbol){
        this.symbol = symbol;
    }


    public interface Ability{
        /**
         * Holds the ability of respective special card
         */
        public void abilityFunction();
    }
}
