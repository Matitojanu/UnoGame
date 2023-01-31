package ss.uno.gamelogic.cards;

/**
 * This class holds the base of a card , with enums for all the symbols and colors, getters, setters, and the ability function
 */
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
     * @ensures the cards' color has been set to the one given as parameters
     * @requires a valid color, that is held by the enum <code>Colour</code>
     */
    public AbstractCard(Colour colour){
        this.colour =  colour;
    }

    /**
     * Returns the colour of the card object
     * @return the color of the card
     * @ensures the corect color is sent
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * This method sets the color of the card to the one given as parameters
     * @param colour the color to which the color will be set
     * @requires a valid color
     */
    public void setColour(Colour colour){
        this.colour = colour;
    }

    /**
     * This method returns the symbol of the card
     * @return the symbol the card has
     * @ensures the corect symbol has been returned
     */
    public Symbol getSymbol(){
        return symbol;
    }

    /**
     * This method sets the symbol of the card to the one given as parameters
     * @param symbol the symbol to which the card will be set to
     * @ensures the symbol given as parameters will be set as the cards' symbol
     */
    public void setSymbol(Symbol symbol){
        this.symbol = symbol;
    }

    /**
     * This interface holds the ability specifications, its functionality
     */
    public interface Ability{
        /**
         * Holds the ability of respective special card
         */
        public void abilityFunction();
    }
}
