package ss.uno.client;

import ss.uno.Protocol;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.client.Client;
import ss.uno.player.AbstractPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientTUI {
    private static UnoGame game;
    private static Scanner _scanner = new Scanner(System.in);

    /**
     * This method prints into the terminal the TUI for the player who has to make a move.
     * @param name the name of the player
     */
    public static void currentPlayerTUI(String name){
        System.out.println("It is players' " + name + " turn!");
    }

    /**
     * This method prints into the terminal the TUI what the last played card.
     * @param card the card that will be the last one played
     */
    public static void updatedFieldTUI(Card card){
        System.out.println("The last played card is " + card.getColour().toString() + " - " + card.getSymbol().toString());
    }


    public static void joinErrorTUI(){
        System.out.println("The index you inputed is not valid. Please try again.");
    }

    public static int getMoveFromUserTUI(){
        _scanner = new Scanner(System.in);
        System.out.println("Please input the index of the card you wish to play:");
        String strMove = _scanner.nextLine();
        int move = Integer.parseInt(strMove);
        return move;
    }

    public static int getIndexOfServerFromUserTUI(){
        System.out.println("Please input the index of the server you wish to join. If you wish to create a game, press 0. If you do not wish to continue, input -1:");
        String strIndex = _scanner.nextLine();
        int i = Integer.parseInt(strIndex);
        return i;
    }

    public static void showPlayerHandTUI(List<Card> hand){
        for (int i = 1; i <=hand.size(); i++) {
            System.out.println(i + hand.get(i-1).toString());
        }
    }

    public static void serverListTUI(String[] servers){
        for (int i = 0; i < servers.length; i++) {
            String[] arguments = servers[i].split(Protocol.DELIMITERINITEMS);
            String serverName = arguments[0];
            int maxPlayers = Integer.parseInt(arguments[1]);
            int playerAmmount = Integer.parseInt(arguments[2]);
            String gamemodes = "";
            if( arguments[3] !=null){
                gamemodes = arguments[3];
                for (int j = 4; j < arguments.length; j++) {
                    gamemodes+= "-" + arguments[i];
                }
            }
            System.out.println((i+1) + ": " + serverName + " (" + maxPlayers + " max players, " + playerAmmount + " players already in game, "+ "gamemodes: " + gamemodes + ")");
        }
    }

    public static String createNewGameTUI(){
        String result;
        System.out.println("Please input the name of the server:");
        String serverName  = _scanner.nextLine();
        int maxPlayers;
        int index;
        String functionalities = "";
        while(true) {
            System.out.println("Please input the maximum amount of players allowed in this game:");
            maxPlayers = Integer.parseInt(_scanner.nextLine());
            if(maxPlayers>1){
                break;
            }
            System.out.println("Wrong input. The minimum amount of players is 2. Please try again.");
        }
        while (true) {
            for (int i = 0; i < Protocol.FUNCTIONALITYUSER.length; i++) {
                System.out.println((i+1)+" - " + Protocol.FUNCTIONALITYUSER[i]);
            }
            System.out.println("Please input the index of the additional functionality you wish to add to the newly created game. If you do not wish any, press 0.");
            index = Integer.parseInt(_scanner.nextLine());
            if(index==0){
                break;
            }
            if(!functionalities.contains(Protocol.FUNCTIONALITYARR[index-1])) {
                functionalities =functionalities + Protocol.FUNCTIONALITYARR[index - 1] + "-";
            }
        }
        return result = serverName + " " + maxPlayers + " " + functionalities;
    }

    public static void waitTUI(String gameName, int maxPlayers, int nrPlayers){
        System.out.println("The game: " + gameName + " is now waiting for more players. There are currently " + nrPlayers + "/" + maxPlayers + "players waiting.");
    }

    public static void startTUI(){
        System.out.println("Everyone is connected. The game will now start!");
    }

    public static AbstractCard.Colour choseColorFromUser(){

        System.out.println("Please input the color you wish to change the last card to, from: Yellow, Red, Blue, Green");
        String userColor = _scanner.nextLine();
        for (AbstractCard.Colour colour: AbstractCard.Colour.values()){
            if(userColor.toUpperCase().equals(colour.toString().toUpperCase())){
                return colour;
            }
        }
        return null;
    }

    public static void drawnCardPrint(Card card){
        System.out.println("The card you have drawn is: " +card.toString());
    }

    /**
     * The function will run the entire game
     */
    public static void main(String[] args) throws IOException {

        Client client = new Client();
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
                synchronized (client) {
                    try {
                        client.wait(180000);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted");
                    }
                }

                if ( client.sendName(name) ) {
                    System.out.println("Successful! Loading...");
                    while (addingFunctionality) {
                        System.out.println("Please input the index of the functionality you wish to add:");
                        for (int i = 0; i < Protocol.FUNCTIONALITYUSER.length; i++) {
                            System.out.println((i + 1) + " - " + Protocol.FUNCTIONALITYUSER[i]);
                        }
                        System.out.println("If you don't want any additional functionalities, press 0.");
                        int functionalityIndex = Integer.parseInt(_scanner.nextLine());
                        if(functionalityIndex==0){
                            addingFunctionality = false;
                        }
                        functionalitiesChosen.add(Protocol.FUNCTIONALITYARR[functionalityIndex-1]);
                        System.out.println("If you wish add more functionalities, press 'y'. Otherwise press 'n'.");
                        String response = _scanner.nextLine();
                        if ( response.toLowerCase().equals("n") ) {
                            addingFunctionality = false;
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
                int index = getIndexOfServerFromUserTUI();
                if(index==-1){
                    System.out.println("You have been disconected. Goodbye!");
                    client.close();
                    return;
                } else if(index==0){
                    String[] newGameString = createNewGameTUI().split(" ");
                    String serverName = newGameString[0];
                    int maxPlayers = Integer.parseInt(newGameString[1]);
                    String[] functionalitiesString = newGameString[2].split("-");
                    List<String> features =new ArrayList<>();
                    for (int i = 0; i < functionalitiesString.length; i++) {
                        features.add(functionalitiesString[i]);
                    }
                    client.sendProtocol(Protocol.NEWGAME + Protocol.DELIMITER + serverName + Protocol.DELIMITER + maxPlayers + Protocol.DELIMITER + client.formatFunctionalities(features));
                } else {
                    client.sendProtocol(Protocol.JOINGAME + Protocol.DELIMITER + index);
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