package ss.uno.cards.special;

import ss.uno.cards.AbstractCard;

public class PlusTwoCard extends AbstractCard implements AbstractCard.Ability {
    /**
     * Gives the card the ability to make the next player draw 2 cards
     */
    @Override
    public void abilityFunction() {

    }

    /**
     * Creates the card with the given color
     * @param colour of the card
     */
    public PlusTwoCard(Colour colour){
        super(colour);
    }
}