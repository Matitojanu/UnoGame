package ss.uno.cards;

public abstract class AbstractCard {
    /**
     * All the possibilities of symbols a special card has
     */
    public enum Symbol{
        PLUSTWO,
        PLUSFOUR,
        CHANGECOLOR,
        SKIPTURN,
        REVERSE
    }

    /**
     * All the possiblities of colors that can be on a card
     */
    public enum Colour{
        RED,
        YELLOW,
        GREEN,
        BLUE,
        BLACK
    }

    private Colour colour;
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


    public interface Ability{
        /**
         * Holds the ability of respective special card
         */
        public void abilityFunction();
    }
}
