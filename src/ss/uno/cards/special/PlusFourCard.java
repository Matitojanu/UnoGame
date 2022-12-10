package ss.uno.cards.special;

import ss.uno.cards.AbstractCard;

public class PlusFourCard extends AbstractCard implements AbstractCard.Ability {
    /**
     * Gives the card the ability to change the current colour and to make the next player draw 4 cards
     */
    @Override
    public void abilityFunction() {

    }

    /**
     * Creates the card with the given color
     * @param colour of the card
     */
    public PlusFourCard(Colour colour){
        super(colour);
    }
}