package ss.uno;

public abstract class AbstractCard {
    /**
     * All the possibilities of symbols a special card has
     */
    enum symbol{
        PLUSTWO,
        PLUSFOUR,
        CHANGECOLOR,
        SKIPTURN,
        REVERSE
    }

    /**
     * All the possiblities of colors that can be on a card
     */
    enum Colour{
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

    }

    /**
     * Returns the colour of the card object
     * @return the color of the card
     */
    public Colour getColour() {
        return colour;
    }

    /**
     *
     */
    public interface Ability{
        public void abilityFunction();
    }
}
