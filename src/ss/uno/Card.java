package ss.uno;

public class Card extends AbstractCard {
    private int number;

    public Card(AbstractCard.Colour colour, int number){
        super(colour);
        this.number = number;

    }
    public int getNumber(){
        return number;
    }
}
