package ss.uno;

public abstract class AbstractCard {
    enum symbol{
        PLUSTWO,
        PLUSFOUR,
        CHANGECOLOR,
        SKIPTURN,
        REVERSE
    }

    enum Colour{
        RED,
        YELLOW,
        GREEN,
        BLUE,
        BLACK
    }
    private Colour colour;
    private Ability ability;

    public AbstractCard(Colour colour){

    }

    public Colour getColour() {
        return colour;
    }

    public interface Ability{
        public void abilityFunction();
    }
}
