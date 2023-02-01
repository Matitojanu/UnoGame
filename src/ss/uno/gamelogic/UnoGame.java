package ss.uno.gamelogic;

import ss.uno.gamelogic.cards.AbstractCard;
import ss.uno.gamelogic.cards.Card;
import ss.uno.gamelogic.cards.Deck;
import ss.uno.gamelogic.player.AbstractPlayer;
import ss.uno.gamelogic.player.HumanPlayer;

import java.util.*;

/**
 * This class is responsible for all the actions a play can do in UnoGame,
 * such as drawing cards, playing cards, changing turns and
 * getting the points distributed.
 */
public class UnoGame implements AbstractCard.Ability {
    private Board board;
    private AbstractPlayer playersTurn;
    private List<AbstractPlayer> players;
    private HashMap<AbstractPlayer, Integer> playersPoints = new HashMap<>();

    /**
     * This method is a constructor, so it creates an object of the class UnoGame, with a board, and a list of players
     * @param abstractPlayers the players that are in this particular game
     * @ensures that the players of the game will be set as the ones given in parameters
     * @requires a non empty list of players
     */
    public UnoGame(List<AbstractPlayer> abstractPlayers) {
        this.players = abstractPlayers;
        board = new Board(new Deck());
        for (AbstractPlayer player : abstractPlayers) {
            playersPoints.put(player, 0);
        }
    }

    /**
     * This method runs the whole game, and manages the gameplay untill one player has 500 points or more
     */
    public void run() {
        while (!isGameOver()){
            boardSetUp();
            drawCardsInitial();
            playersTurn = players.get(0);
            while (!isRoundOver()) {
                if (playersTurn.existsValidMove(board)) {
                    int indexOfCard = playersTurn.determineMove(board);
                    if (indexOfCard < playersTurn.getHand().size()) {
                        Card playedCard = (Card) playersTurn.getHand().get(indexOfCard);
                        playCard(playedCard);
                        System.out.println(playersTurn.getName() + " played the card: " + playedCard.toString());
                        abilityFunction();
                    } else if (indexOfCard == playersTurn.getHand().size()) {
                        System.out.println(playersTurn.getName() + " draws a card \n");
                        drawCard();
                        Card drawnCard = (Card) playersTurn.getHand().get(playersTurn.getHand().size() - 1);
                        if (drawnCard.getColour() == board.getLastCard().getColour() || drawnCard.getColour() == AbstractCard.Colour.WILD || drawnCard.getSymbol() == board.getLastCard().getSymbol()) {
                            playDrawnCard(drawnCard);
                        }
                    }

                } else {
                    System.out.println(playersTurn.getName() + " draws a card \n");
                    drawCard();
                    Card drawnCard = (Card) playersTurn.getHand().get(playersTurn.getHand().size() - 1);
                    if (drawnCard.getColour() == board.getLastCard().getColour() || drawnCard.getColour() == AbstractCard.Colour.WILD || drawnCard.getSymbol() == board.getLastCard().getSymbol()) {
                        playDrawnCard(drawnCard);
                    }
                }
                changeTurn();
            }
            distributePoints();
            System.out.println("Player " + getRoundWinner().getName() + " won the round!");
            System.out.println("Player " + getRoundWinner().getName() + " currently has: " + playersPoints.get(getRoundWinner()) + " points.");
        }
        System.out.println("Player " + getGameWinner().getName() + " won the game!");
    }

    /**
     * This method draws the first 7 cards into each players' hand
     * @ensures that all the players have 7 cards in their hands
     */
    public void drawCardsInitial() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < players.size(); j++) {
                playersTurn = players.get(j);
                drawCard();
            }
        }
    }

    public void boardSetUp(){
        board = new Board(new Deck());
        while (true) {
            if (board.getLastCard().getColour() == AbstractCard.Colour.WILD) {
                board = new Board(new Deck());
            } else {
                break;
            }
        }
    }

    /**
     * Checks whose player's turn is it and changes it to the next one
     * @return the player who's turn is it
     * @ensures that the player that is returned, it is indeed the player whose turn it is
     */
    public AbstractPlayer changeTurn(){
        for (int i = 0; i < players.size(); i++) {
            if(playersTurn == players.get(i)){
                if(playersTurn == players.get(players.size()-1)){
                    playersTurn = players.get(0);
                    return playersTurn;
                }else{
                    playersTurn = players.get(i+1);
                    return playersTurn;
                }
            }
        }
        return playersTurn;

    }

    /**
     * Returns the player that has won the round
     * @return the player that has no more cards in their hand
     * @ensures that the player that is returned, has indeed won the round
     */
    public AbstractPlayer getRoundWinner(){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getHand().size()==0){
                return players.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the player that has won the game
     * @return the player that has
     * @ensures that the player who is returned is the one that won the game and has 500 points or more
     */
    public AbstractPlayer getGameWinner(){
        for(AbstractPlayer player : playersPoints.keySet()){
            if(playersPoints.get(player) >= 500){
                return player;
            }
        }
        return null;
    }

    /**
     * Returns whether the round is finished or not
     * @return true if the round has finished, false otherwhise
     * @ensures that it returns true if the round is indeed over, and false if it's not
     */
    public boolean isRoundOver(){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getHand().size()==0){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the game is finished or not
     * @return true if the game has finished, false otherwhise
     * @ensures that true is returned if the game is over, false if not
     */
    public boolean isGameOver(){
        for(AbstractPlayer player : playersPoints.keySet()){
            if(playersPoints.get(player) >= 500){
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the points from a round and assigns them to the winner
     * @ensures the points are added correctly to each player and the hands are empty after point distribution
     */
    public void distributePoints(){
        int totalPoints = playersPoints.get(getRoundWinner());
        for(AbstractPlayer player : playersPoints.keySet()){
            if(player.getHand().size() > 0) {
                for (AbstractCard card : player.getHand()) {
                    switch (card.getSymbol()) {
                        case ZERO -> {

                        }
                        case ONE -> {
                            totalPoints += 1;
                        }
                        case TWO -> {
                            totalPoints += 2;
                        }
                        case THREE -> {
                            totalPoints += 3;
                        }
                        case FOUR -> {
                            totalPoints += 4;
                        }
                        case FIVE -> {
                            totalPoints += 5;
                        }
                        case SIX -> {
                            totalPoints += 6;
                        }
                        case SEVEN -> {
                            totalPoints += 7;
                        }
                        case EIGHT -> {
                            totalPoints += 8;
                        }
                        case NINE -> {
                            totalPoints += 9;
                        }
                        case PLUSTWO -> {
                            totalPoints += 20;
                        }
                        case REVERSE -> {
                            totalPoints += 20;
                        }
                        case SKIPTURN -> {
                            totalPoints += 20;
                        }
                        case PLUSFOUR -> {
                            totalPoints += 50;
                        }
                        case CHANGECOLOR -> {
                            totalPoints += 50;
                        }
                    }
                }
                for (int i = player.getHand().size()-1; i > 0; i--) {
                    player.getHand().remove(i);
                }
            }
        }
        playersPoints.replace(getRoundWinner(), totalPoints);
    }

    /**
     * This method returns true if the card given as parameters can be played on the last card
     * @param card the card that is to be validated
     * @return true if it can be played on the last card, false otehrwhise
     * @ensures true is returned if the card is valid and it can be played on the last card, false if not
     * @requires the card to be a valid card
     */
    public boolean isCardValid(Card card){
        Card lastCard = getBoard().getLastCard();
        if(card.getSymbol().equals(lastCard.getSymbol()) || card.getColour().equals(lastCard.getColour()) || card.getColour().equals(AbstractCard.Colour.WILD)){
            return true;
        }
        return false;
    }

    /**
     * This function plays the card that is given as parameters
     * @param card that will be played
     * @ensures the card given as parameters is set as the last card and removed from the players' hand
     */
    public void playCard(Card card){
        Deck deck = board.getDeck();
        playersTurn.getHand().remove(playersTurn.getHand().indexOf(card));
        board.setLastCard(card);
    }

    /**
     *  This function draws a card from the deck
     * @ensures that a card is drawn for the player whose turn it is, and that it is removed from the deck
     */
    public void drawCard() {
        for (int i = 0; i < players.size(); i++) {
            if ( playersTurn == players.get(i) ) {
                ArrayList<AbstractCard> playersHands =  new ArrayList<>();
                if( board.deckFinished() ){
                    for (AbstractPlayer player: players) {
                        playersHands.addAll(player.getHand());
                    }
                    board.setDeck(new Deck(playersHands));
                    System.out.println("The deck has been reshuffled! \n");
                }
                players.get(i).getHand().add(board.getDeck().getCard());
            }
        }
    }

    /**
     * Asks the player whether he wants to play the drawn card. AI always plays the card
     * @param drawnCard card drawn by the player that can be played
     * @requires that the drawnCard given in the parameters is a valid card
     * @ensures that if the player wishes to play the card, it gets played and set as the last card
     */
    public void playDrawnCard(Card drawnCard){
        if(playersTurn instanceof HumanPlayer) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Drawn card: " + drawnCard.toString());
            System.out.println("Do you want to play the drawn card?  Y/N");
            String input = scanner.nextLine();
            if (input.toUpperCase().equals("Y")) {
                playCard(drawnCard);
                System.out.println(playersTurn.getName() + " played the card: " + drawnCard.toString());
                board.setLastCard(drawnCard);
                playersTurn.getHand().remove(drawnCard);
                abilityFunction();
            } else if (input.toUpperCase().equals("N")) {

            }
        }else{
            playCard(drawnCard);
            System.out.println(playersTurn.getName() + " played the card: " + drawnCard.toString());
            board.setLastCard(drawnCard);
            playersTurn.getHand().remove(drawnCard);
            abilityFunction();
        }
    }

    /**
     * Does the ability function based on the last card, and changes the turn before applying it
     * @ensures that the ability is correct and that its effects are given to the correct player
     */
    public void abilityFunction(){
        Deck deck = board.getDeck();
        Card card = (Card) board.getLastCard();
        switch (card.getSymbol()){
            case  PLUSTWO-> {
                changeTurn();
                for(int i = 0; i<2; i++){
                    drawCard();
                }
            }
            case PLUSFOUR -> {
                changeTurn();
                for(int i = 0; i<4; i++){
                    drawCard();
                }
                if(playersTurn instanceof HumanPlayer) {
                    board.pickColor();
                }else{
                    for(AbstractCard card1 : playersTurn.getHand()) {
                        if (card1.getColour() != AbstractCard.Colour.WILD) {
                            System.out.println("Player " + playersTurn.getName() + " changed the colour to " + card1.getColour().toString() + "\n");
                            board.getLastCard().setColour(card1.getColour());
                            break;
                        }
                        if(playersTurn.getHand().indexOf(card1) == playersTurn.getHand().size()-1){
                            System.out.println("Player " + playersTurn.getName() + " changed the colour to " + AbstractCard.Colour.RED + "\n");
                            board.getLastCard().setColour(AbstractCard.Colour.RED);
                            break;
                        }
                    }
                }
            }
            case REVERSE -> {
                if(players.size() == 2){
                    changeTurn();
                }else{
                    Collections.reverse(players);
                }
            }
            case SKIPTURN -> {
                changeTurn();
            }
            case CHANGECOLOR -> {
                if(playersTurn instanceof HumanPlayer) {
                    board.pickColor();
                    System.out.println("Player " + playersTurn.getName() + " changed the colour to " + board.getLastCard().getColour().toString() + "\n");
                }else{
                    for(AbstractCard card1 : playersTurn.getHand()) {
                        if (card1.getColour() != AbstractCard.Colour.WILD) {
                            System.out.println("Player " + playersTurn.getName() + " changed the colour to " + card1.getColour().toString() + "\n");
                            board.getLastCard().setColour(card1.getColour());
                            break;
                        }
                        if(playersTurn.getHand().indexOf(card1) == playersTurn.getHand().size()-1){
                            System.out.println("Player " + playersTurn.getName() + " changed the colour to " + AbstractCard.Colour.RED + "\n");
                            board.getLastCard().setColour(AbstractCard.Colour.RED);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * This method returns the board of the game
     * @return the board of the game
     * @requires a valid board
     */
    public Board getBoard() {
        return board;
    }


    /**
     * This method returns whose players' turn it is
     * @return the player who has to make a move
     * @ensures the correct player is sent
     */
    public AbstractPlayer getPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(AbstractPlayer playersTurn) {
        this.playersTurn = playersTurn;
    }

    /**
     * This method returns a map with the players as keys and the points each one has as values
     * @return the map with the players and points
     * @ensures the valid and correct map is sent
     */
    public HashMap<AbstractPlayer, Integer> getPlayersPoints() {
        return playersPoints;
    }


}
