package ss.uno.cards.special;

import ss.uno.cards.AbstractCard;

public class PlusTwoCard extends AbstractCard implements AbstractCard.Ability {

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