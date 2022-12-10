package ss.uno.cards.special;

import ss.uno.cards.AbstractCard;

public class SkipTurnCard extends AbstractCard implements AbstractCard.Ability {
    /**
     * Gives the card the ability to skip the next player's turn
     */
    @Override
    public void abilityFunction() {

    }

    /**
     * Creates the card with the given color
     * @param colour of the card
     */
    public SkipTurnCard(Colour colour){
        super(colour);
    }
}
