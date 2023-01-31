package ss.uno.networking.client;

import ss.uno.networking.Protocol;
import ss.uno.gamelogic.cards.AbstractCard;
import ss.uno.gamelogic.cards.Card;
import ss.uno.gamelogic.player.AI;
import ss.uno.gamelogic.player.HumanPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for all the textual user ineterface the user sees, taking input from said user and
 * controls the flow of the actions happening between user and client through a main() method
 */
public class ClientTUI {
    private static Scanner _scanner = new Scanner(System.in);
    public static final String WRONGINPUT = "Wrong input! Please try again.";

    /**
     * This method prints into the terminal the text representation of the player who has to make a move.
     * @param name the name of the player
     */
    public static void printCurrentPlayerText(String name){
        System.out.println("It is players' " + name + " turn!");
    }

    /**
     * This method prints into the terminal the text for updating the last played card.
     * @param card the card that will be the last one played
     */
    public static void printUpdatedFieldText(Card card){
        System.out.println("The last played card is " + card.getColour().toString() + " - " + card.getSymbol().toString());
    }

    /**
     * This method prints into the terminal the text representation of the error when the user inputs the wrong index of a server.
     */
    public static void printJoinErrorText(){
        System.out.println("The index you inputed is not valid. Please try again.");
    }

    /**
     * This method prints into the terminal the text representation of requesting a move from the user and returns it.
     * @return the index of the card/ move the user wishes to do
     * @param hand the hand with the cards the player has
     */
    public static int getMoveFromUserText(List<AbstractCard> hand){
        System.out.println("Please input the index of the card you wish to play. If you wish to draw a card, input " + (hand.size() + 1));
        _scanner = new Scanner(System.in);
        int move = Integer.parseInt(_scanner.nextLine());
        return move;
    }

    /**
     * This method prints into the terminal the text for getting the server index from the user.
     * @return the index of the server the user wishes to join.
     */
    public static int getIndexOfServerFromUserText(){
        while(true) {
            System.out.println("Please input the index of the server you wish to join. If you wish to create a game, press 0. If you do not wish to continue, input -1:");
            try {
                int i = Integer.parseInt(_scanner.nextLine());
                if ( (Integer) i instanceof Integer ) {
                    return i;
                }
            }catch (NumberFormatException e){
                System.out.println(WRONGINPUT);
            }
        }
    }

    /**
     * This method prints into the terminal the text representation of the players' hand.
     * @param hand the hand of the player, containing the cards.
     */
    public static void printShowPlayerHandText(List<AbstractCard> hand){
        for (int i = 1; i <=hand.size(); i++) {
            System.out.println(i + " - " + hand.get(i-1).getColour().toString() + " " + hand.get(i-1).getSymbol().toString());
        }
    }

    /**
     * This method prints into the terminal the text for when there are no moves left for the player to do
     */
    public static void printNoAvailableMovesText(){
        System.out.println("No moves left. Drawing a card...\n");
    }

    /**
     * This method prints into the terminal the text representation of the server list.
     * @param servers the servers the user can join
     */
    public static void printServerListText(String[] servers){
        for (int i = 0; i < servers.length; i++) {
            String[] arguments = servers[i].split(Protocol.DELIMITERINITEMS);
            String serverName = arguments[0];
            int maxPlayers = Integer.parseInt(arguments[1]);
            int playerAmmount = Integer.parseInt(arguments[2]);
            String gamemodes = "";
            if( arguments[3].contains("-")){
                gamemodes = arguments[3];
                for (int j = 4; j < arguments.length; j++) {
                    gamemodes+= "-" + arguments[i];
                }
            }else {
                gamemodes+=arguments[3];
            }
            System.out.println((i+1) + ": " + serverName + " (" + maxPlayers + " max players, " + playerAmmount + " players already in game, "+ "gamemodes: " + gamemodes + ")");
        }
    }

    /**
     * This method prints into the terminal the text for the creation of a new game, which means
     * it requests for the name of the game server, the maximum amount of players allowed in it and the functionalities,
     * with which the game is created
     * @return a string containing the name of the game server, the max players number and the functionalities.
     */
    public static String createNewGameText(){
        System.out.println("Please input the name of the server:");
        String serverName  = _scanner.nextLine();
        int maxPlayers;
        int index;
        String functionalities = "";
        while(true) {
            try {
                System.out.println("Please input the maximum amount of players allowed in this game:");
                maxPlayers = Integer.parseInt(_scanner.nextLine());
                if ( maxPlayers > 1 ) {
                    break;
                }
            } catch (NumberFormatException e ) {
                System.out.println("Wrong input. The minimum amount of players is 2. Please try again.");
            }
        }
        while (true) {
            for (int i = 0; i < Protocol.FUNCTIONALITYUSER.length; i++) {
                System.out.println((i+1)+" - " + Protocol.FUNCTIONALITYUSER[i]);
            }
            System.out.println("Please input the index of the additional functionality you wish to add to the newly created game. " +
                    "\nIf you do not wish any, press 0.");
            try {
                index = Integer.parseInt(_scanner.nextLine());
                if ( index == 0 ) {
                    functionalities = Protocol.ORIGINAL;
                    break;
                }
                if ( !functionalities.contains(Protocol.FUNCTIONALITYARR[index - 1]) ) {
                    functionalities = functionalities + Protocol.FUNCTIONALITYARR[index - 1] + "-";
                }
                break;
            } catch (NumberFormatException e){
                System.out.println("Wrong input. Please input the number of the functionality you wish to add.");
            }
        }
        return  serverName + " " + maxPlayers + " " + functionalities;
    }

    /**
     * This method prints into the terminal the text representation of waiting, until there are enough players to start the game
     * @param gameName the game servers' name
     * @param maxPlayers the maximum amount of players allowed in that game
     * @param nrPlayers how many players have currently joined the game and are waiting for it to start
     */
    public static void printWaitText(String gameName, int maxPlayers, int nrPlayers){
        System.out.println("The game: '" + gameName + "' is now waiting for more players. There are currently " + nrPlayers + "/" + maxPlayers + " players waiting.");
    }

    /**
     * This method prints into the terminal the text representation of the start of the game.
     */
    public static void printStartText(){
        System.out.println("Everyone is connected. The game will now start!\n");
    }

    /**
     * This method prints into the terminal the text for requesting of a color from the user
     * @return the color the user chose as an instance of <code>AbstractCard.Colour</code>
     */
    public static AbstractCard.Colour choseColorFromUserText(){
        while (true) {
            System.out.println("Please input the color you wish to change the last card to, from: Yellow, Red, Blue, Green");
            String userColor = _scanner.nextLine();
            if ( Protocol.COLORSPOSSIBLE.contains(userColor.toUpperCase()) ) {
                for (AbstractCard.Colour colour : AbstractCard.Colour.values()) {
                    if ( userColor.toUpperCase().equals(colour.toString().toUpperCase()) ) {
                        return colour;
                    }
                }
            } else {
                System.out.println(WRONGINPUT);
            }
        }
    }

    /**
     * This methods prints into the terminal the text representation of the drawn card
     * @param card the card that has been drawn
     */
    public static void printDrawnCardText(Card card){
        System.out.println("The card you have drawn is: " +card.toString());
    }

    /**
     * This method prints into the terminal the text for a new round.
     */
    public static void printNewRoundText(){
        System.out.println("A new round has started!\n");
    }

    /**
     * This method prints into the terminat the text for the list of players and the ammount of points they have.
     * @param playersPointsList a string array that contains teh name of the player and how many points they have
     */
    public static void printResultsText(List<String> playersPointsList){
        for (int i = 0; i < playersPointsList.size(); i++) {
            String[] playersArguments = playersPointsList.get(i).split(Protocol.DELIMITERINITEMS);
            String playerName = playersArguments[0].toString();
            String points = playersArguments[1].toString();
            System.out.println(playerName + " : " + points + " points");
        }
    }

    /**
     * This method prints into the terminal the text for asking the user of what they wish to do with the drawn card, if they want to play it or not.
     * @return an empty string if the user doesn't wish to play the card, 'Y' if the player wishes to play it
     */
    public static String askForChoiceToPlayDrawnCardText(){
        String result = "";
        while (true) {
            System.out.println("Do you wish to play this card? If yes press 'Y', if no, press 'N'.");
            String response = _scanner.nextLine();
            if ( response.equalsIgnoreCase("Y") ) {
                result = "Y";
                break;
            } else if (response.equalsIgnoreCase("N")){
                result = "N";
                break;
            } else {
                System.out.println(WRONGINPUT);
            }
        }
        return result;
    }

    /**
     * This method prints into the terminal the text for requesting if the user wishes to challange the player who placed a +4 card
     * @return true if the user wants to challange the other player, false otherwhise
     */
    public static boolean askUserIfChallangeText(){
        while (true) {
            System.out.println("Do you wish to challange the player that placed this card? Press 'Y' if yes and 'N' if no.");
            String response = _scanner.nextLine();
            if ( response.equalsIgnoreCase("y") ) {
                return true;
            } else if ( response.equalsIgnoreCase("N") ){
                return false;
            } else {
                System.out.println(WRONGINPUT);
            }
        }
    }

    /**
     * The method contains the initialization of the client with a name, and list of functionalities, and then manages if the user wishes to continue playing, exit or join a gameserver
     *
     */
    public static void main(String[] args) throws IOException {

        ClientInterface client = new Client();
        String name;
        List<String> functionalitiesChosen =  new ArrayList<>();
        boolean addingFunctionality = true;
        boolean wishToPlay= true;
        boolean userInitialization =  true;

        System.out.println("Hello!");
        if( client.connect() ){
            while (userInitialization) {
                System.out.println("To join please input your name");
                name = _scanner.nextLine();

                while (true) {
                    System.out.println("Please write 'AI' if you wish to play as an AI, or 'H' if you wish to play as a regular player.");
                    String userTypeChoice = _scanner.nextLine();
                    if ( userTypeChoice.equalsIgnoreCase("AI") ) {
                        client.set_playerClient(new AI(name));
                        break;
                    } else if ( userTypeChoice.equalsIgnoreCase("h") ){
                        client.set_playerClient(new HumanPlayer(name));
                        break;
                    } else {
                        System.out.println("Wrong input! Please try again!");
                    }
                }
                if ( client.sendName(name) ) {
                    System.out.println("Successful! Loading...");
                    while (addingFunctionality) { //TODO: to check for valid move
                        while (true) {
                            System.out.println("Please input the index of the functionality you wish to add:");
                            for (int i = 0; i < Protocol.FUNCTIONALITYUSER.length; i++) {
                                System.out.println((i + 1) + " - " + Protocol.FUNCTIONALITYUSER[i]);
                            }
                            System.out.println("If you don't want any or you finished adding all the " +
                                    "additional functionalities, press 0.");
                            try {
                                int functionalityIndexStr = Integer.parseInt(_scanner.nextLine());
                                if ( functionalityIndexStr == 0 ) {
                                    addingFunctionality = false;
                                    functionalitiesChosen.add(Protocol.ORIGINAL);
                                } else {
                                    functionalitiesChosen.add(Protocol.FUNCTIONALITYARR[functionalityIndexStr - 1]);
                                    System.out.println("If you wish add more functionalities, press 'y'. Otherwise press 'n'.");
                                    String response = _scanner.nextLine();
                                    if ( response.toLowerCase().equals("n") ) {
                                        addingFunctionality = false;
                                    }
                                }
                                break;
                            } catch (NumberFormatException e){
                                System.out.println(WRONGINPUT);
                            }
                        }
                    }
                    client.sendFunctionalities(functionalitiesChosen);
                    new Thread(client).start(); //TODO: hopefully servers are shown here
                    userInitialization = false;
                } else {
                    System.out.println("Name is already taken. Try again.");
                }
            }
        } else {
            System.out.println("Sorry, we could not establish a connection.");
            return;
        }


        while(wishToPlay) {
            while(!client.is_inGame()){
                int index = getIndexOfServerFromUserText();

                if(index==-1){
                    System.out.println("You have been disconected. Goodbye!");
                    client.close();
                    return;
                } else if(index==0){
                    String[] newGameString = createNewGameText().split(" ");
                    String serverName = newGameString[0];
                    int maxPlayers = Integer.parseInt(newGameString[1]);
                    List<String> features = new ArrayList<>();
                    if(newGameString[2].contains("-")) {
                        String[] functionalitiesString;
                         functionalitiesString = newGameString[2].split("-");

                        for (int i = 0; i < functionalitiesString.length; i++) {
                            features.add(functionalitiesString[i]);
                        }
                    } else {
                        features.add(newGameString[2]);
                    }
                    client.sendProtocol(Protocol.NEWGAME + Protocol.DELIMITER + serverName + Protocol.DELIMITER + maxPlayers + Protocol.DELIMITER + client.formatFunctionalities(features));
                    client.set_inGame(true);
                } else {
                    client.sendProtocol(Protocol.JOINGAME + Protocol.DELIMITER + index);
                    client.set_inGame(true);
                }

            }
            synchronized (client){
                try {
                    client.wait();
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted!!!");
                }
            }
            System.out.println("This game is over. Do you wish to play and choose another game server to join or create? Press Y for Yes or N for No");
            String response = _scanner.nextLine();
            if(response.equalsIgnoreCase("N")){
                wishToPlay=false;
                System.out.println("You have been disconected! Goodbye!");
            }


        }
        //TODO: from WAIT down

    }
}