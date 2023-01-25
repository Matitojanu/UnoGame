package ss.uno;

import ss.uno.cards.Card;
import ss.uno.client.Client;
import ss.uno.player.AbstractPlayer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientTUI {
    private static UnoGame game;

    public static void currentPlayerTUI(String name){
        System.out.println("It is players' " + name + " turn!");
    }

    public static void updatedFieldTUI(Card card){
        System.out.println("The last played card is " + card.getColour().toString() + " - " + card.getSymbol().toString());
    }

    public static void joinErrorTUI(){
        System.out.println("The index you inputed is not valid. Please try again.");
    }

    /**
     * The function will run the entire game
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Scanner scn = new Scanner(System.in);
        Client client = new Client();
        String name;
        List<String> functionalitiesChosen =  new ArrayList<>();

        System.out.println("Hello!");
        if( client.connect() ){
            while (true) {
                System.out.println("To join please input your name");
                name = scanner.nextLine();
                if ( client.sendName(name) ) {
                    System.out.println("Successful! Loading...");
                    while (true) {
                        System.out.println("Please input the index of the functionality you wish to add:");
                        for (int i = 0; i < Protocol.FUNCTIONALITYUSER.length; i++) {
                            System.out.println((i + 1) + " - " + Protocol.FUNCTIONALITYUSER[i]);
                        }
                        System.out.println("If you don't want any additional functionalities, press 0.");
                        int functionalityIndex = Integer.parseInt(scanner.nextLine());
                        if(functionalityIndex==0){
                            break;
                        }
                        functionalitiesChosen.add(Protocol.FUNCTIONALITYARR[functionalityIndex-1]);
                        System.out.println("If you wish add more functionalities, press 'y'. Otherwise press 'n'.");
                        String response = scanner.nextLine();
                        if ( response.toLowerCase().equals("n") ) {
                            break;
                        }
                    }
                    client.sendFunctionalities(functionalitiesChosen);
                } else {
                    System.out.println("Name is already taken. Try again.");
                }
            }
        } else {
            System.out.println("Sorry, we could not establish a connection.");
            return;
        }

        //De facut de la NEWGAME

    }
}