package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.player.AI;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnoGameTest {
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private AbstractPlayer player3;
    private AbstractPlayer player4;
    UnoGame game;

    @BeforeEach
    public void setUp(){
        ArrayList<AbstractPlayer> players =  new ArrayList<>();
        players.add(player1 =  new HumanPlayer("Ami"));
        players.add(player2 =  new HumanPlayer("Matuesz"));
        players.add(player3 =  new AI("-AliceBot"));
        players.add(player4 =  new AI("-SandyBot"));
        game =  new UnoGame(players);
        game.drawCardsInitial();
        while (true){
            if(game.getPlayersTurn()!=player1){
                game.changeTurn();
            }else{
                break;
            }
        }
    }

    @Test
    public void initialDrawCardTest(){
        assertTrue(player1.getHand().size()==7);
        assertTrue(player2.getHand().size()==7);
        assertTrue(player3.getHand().size()==7);
        assertTrue(player4.getHand().size()==7);
    }

    @Test
    public void testDrawCard(){
        game.drawCard();
        assertTrue(player1.getHand().size() == 8);
    }

    @Test
    public void testPlayCard(){
        game.drawCard();
        Card card = (Card) player1.getHand().get(0);
        game.playCard(card);
        assertTrue(game.getBoard().getLastCard() == card );

    }
    @Test
    public void testCardAbilityPlusTwo(){
        Card card = new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.PLUSTWO);
        Card lastCard =  new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.ONE);
        ArrayList<AbstractCard> hand = new ArrayList<>();
        hand.add(card);
        player1.setHand(hand);
        game.getBoard().setLastCard(lastCard);
        game.playCard(card);
        game.getBoard().setLastCard(card);
        game.abilityFunction();
        assertTrue(player2.getHand().size()==9);
    }

    @Test
    public void testCardAbilityReverse(){
        Card card = new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.REVERSE);
        Card lastcard = new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.ONE);
        ArrayList<AbstractCard> hand = player1.getHand();
        hand.add(card);
        game.getBoard().setLastCard(lastcard);
        game.playCard(card);
        game.getBoard().setLastCard(card);
        game.abilityFunction();
        game.changeTurn();
        assertTrue((game.getPlayersTurn())==player4);

    }

    @Test
    public void testCardAbilitySkipTurn(){
        Card card = new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.SKIPTURN);
        Card lastcard = new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.ONE);
        ArrayList<AbstractCard> hand = player1.getHand();
        hand.add(card);
        game.getBoard().setLastCard(lastcard);
        game.playCard(card);
        game.getBoard().setLastCard(card);
        game.abilityFunction();
        game.changeTurn();
        assertTrue((game.getPlayersTurn())==player3);
    }

}
