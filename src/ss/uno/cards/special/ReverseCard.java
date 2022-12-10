package ss.uno.cards.special;

import ss.uno.cards.AbstractCard;

public class ReverseCard extends AbstractCard implements AbstractCard.Ability {
    /**
     * Gives the card the ability to reverse the order of play
     */
    @Override
    public void abilityFunction() {

    }

    /**
     * Creates the card with the given color
     * @param colour of the card
     */
    public ReverseCard(Colour colour){
        super(colour);
    }
}
