# UNOproject

## Description 
This project allows users to play multiple games of Uno at once using a lobby system
The games are hosted on a Server that people can connect to using their Clients
The java version used is OpenJDK 19

### Additional features
- Lobby system
- Multi-game server
- Team-play (games up to 10 players)

## How to run
#### Joining the server
In order to host a server you first need to run the ServerTUI class. Then you need to
run ClientTUI in order to connect to the server as a player. The Server will ask you for 
your name and whether you will want this run to listen to your commands during the game or
to operate as an AI (write "AI" for AI, "H" for Human). Next the server will ask you whether 
you want to use any of the listed additional features (Our code doesn't support all the features, but we still 
give the option for interaction with another Server that might support these features).
#### Creating a lobby
If you are the first player to connect to the server you will most likely encounter an empty list of
lobbies. At that time your only option is to create a new lobby by inputting '0'. The server will then ask 
you to input a name for the lobby, the maximum number of players at which the game will be played and then it
will ask you to pick a gamemode that you want to play from the list. (Our current project only supports the Original gamemode).
After that you will be put in a lobby with notifications about the current number of players in the lobby.
#### Joining a lobby
If you are not the first to connect to the server, chances are there already are some lobbies
in the list of lobbies. If you don't want to create your own, you can join a lobby just by inputting it's index.
After that you will be put in a lobby with notifications about the current number of players in the lobby.
#### Gameplay loop
When a lobby gets filled by players the game will begin. The game will display the last played card and a list of cards
in your hand. When it's your turn you simply put the index of the card that you want to play. If it's valid it will be played,
it's ability(if it has one) will activate and the turn will get passed to the next player. You can also choose to draw a card instead
of playing it can be done by inputing the number that's 1 bigger the the index of the last card in your hand. If you don't have any available
moves on hand, you are forced to draw a card. Whenever you draw a card voluntarily (or forced by lack of moves) the game will check if the newly drawn
card can be played on the card on board. If it can, the game will ask you whether you want to discard it instantly.
#### Win conditions
The round ends when one of the players gets rid of all cards in their hand. That player gets declared the winner of the round
and gains points for each card in the opponets hands (Numbered cards give points equal to their number, special colored cards give +20 points
and Wildcards give +50 points) After that, a new round begins. The players keep playing until one player collects 500 points. At that point, the game
ends and the player with 500 gets declared the winner of the game.

## How to play a game with AI players
Running a game with AI players is very simple. When connecting to the server choose "AI". Then when the game in the lobby you created/joined begins, the AI
will make moves instead of you. You can play a game with multiple AIs simply by joining the game with another Client that also chooses "AI" when selecting 
player name.
