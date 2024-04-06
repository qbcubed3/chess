package ui;

import model.GameDataModel;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessTerminalUI {
    public ServerFacade facade;
    public ChessTerminalUI(ServerFacade facade) {
        this.facade = facade;
    }

    public void runUI(){
        preLogin();
    }

    public void printGames(ArrayList<GameDataModel> games){
        for (GameDataModel model: games){
            System.out.println("game name: " + model.gameName() + ", white username: " + model.whiteUsername()
                    + ", black username: " + model.blackUsername() + ", game ID: " + model.gameID());
        }
    }

    public void preLogin(){
        System.out.println("Welcome to my CS240 chess project, type help to get started");
        while (true){
            System.out.print(">>>");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            var inputs = input.split(" ");
            if (inputs[0].equals("help")){
                System.out.println("help: shows the current commands that you can perform");
                System.out.println("register: registers you as a new user. Format is \"register <USERNAME> <PASSWORD> <EMAIL>\"");
                System.out.println("login: logs a user in. Format is \"login <USERNAME> <PASSWORD>\"");
                System.out.println("quit: quits the chess program");
            }
            else if (inputs[0].equals("register")){
                if (inputs.length < 4) {
                    System.out.println("All fields must be filled. Type help for a reminder on the syntax");
                    continue;
                }
                String username = inputs[1];
                String password = inputs[2];
                String email = inputs[3];
                try{
                    facade.register(username, password, email);
                    System.out.println(username + " is now registered and logged in.");
                    postLogin();
                    break;
                }
                catch (Exception e){
                    System.out.println("There was an error. Check you inputted everything correctly and try again.");
                }
            }
            else if (inputs[0].equals("login")){
                if (inputs.length < 3) {
                    System.out.println("All fields must be filled. Type help for a reminder on the syntax");
                    continue;
                }
                String username = inputs[1];
                String password = inputs[2];
                try{
                    facade.login(username, password);
                    System.out.println(username + " is now logged in");
                    postLogin();
                    break;
                }
                catch (Exception e){
                    System.out.println("There was an error. Check you inputted everything correctly and try again.");
                }
            }
            else if (input.equals("quit")){
                System.out.println("quitting");
                break;
            }
            else{
                System.out.println("could not understand the command");
            }
        }
    }
    public void postLogin() {
        while (true) {
            System.out.print(">>>");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            var inputs = input.split(" ");
            if (inputs[0].equals("help")) {
                System.out.println("help: shows the current commands that you can perform");
                System.out.println("logout: logs you out of the program");
                System.out.println("create game: creates a new chess game. Format is \"create game <GAME NAME>\"");
                System.out.println("join game: joins a chess game. Format is \"join game <PLAYER COLOR> <GAME ID>\". <PLAYER COLOR> can be BLACK, WHITE, or blank");
                System.out.println("list games: lists the current chess games");
                System.out.println("quit: quits the chess program");
            }
            else if (inputs[0].equals("logout")) {
                try{
                    facade.logout();
                    System.out.println("logged out");
                    break;
                }
                catch (Exception e){
                    System.out.println("There was an issue logging out. Try Again.");
                }
            }
            else if (inputs[0].equals("create") && inputs[1].equals("game")) {
                if (inputs.length < 3) {
                    System.out.println("All fields must be filled. Type help for a reminder on the syntax");
                    continue;
                }
                String gameName = inputs[2];
                try{
                    var id = facade.create(gameName);
                    System.out.println("created new game " + gameName + " with an ID of " +id);
                }
                catch (Exception e){
                    System.out.println("There was an issue with creating the game. Try Again.");
                }
            }
            else if (inputs[0].equals("join") && inputs[1].equals("game")){
                if (inputs.length == 3){
                    try{
                        facade.join("", Integer.parseInt(inputs[2]));
                        System.out.println("joined gameID: " + inputs[2] + " as a spectator");
                        gameplay();
                    }
                    catch (Exception e) {
                        System.out.println("There was an error. Check the inputs and try again.");
                    }
                }
                else if (inputs.length == 4){
                    try{
                        facade.join(inputs[2], Integer.parseInt(inputs[3]));
                        System.out.println("joined gameID: " + inputs[3] + " as " + inputs[2]);
                        gameplay();
                    }
                    catch (Exception e) {
                        System.out.println("There was an error. Check the inputs and try again.");
                    }
                }
                else{
                    System.out.println("The arguments were incorrect, check to make sure they're right and try again.");
                }
            }
            else if (inputs[0].equals("list") && inputs[1].equals("games")){
                if (inputs.length > 2){
                    System.out.println("no additional arguments needed for this command");
                }
                try{
                    var games = facade.list();
                    printGames(games);
                }
                catch (Exception e){
                    System.out.println("There was an error. Try again");
                }

            }
            else if (input.equals("quit")) {
                System.out.println("quitting");
                break;
            }
            else{
                System.out.println("could not understand the command");
            }
        }
    }

    public void printChessboard(){
        System.out.println("   " + SET_TEXT_UNDERLINE + " a   \u202f\u202fb   \u202f\u202fc\u202f\u202f  \u202fd\u202f\u202f   e\u202f\u202f   f\u202f\u202f  \u202fg\u202f   h " + RESET_TEXT_UNDERLINE);
        System.out.println("8 |"+WHITE_ROOK + "|" + WHITE_KNIGHT + "|" + WHITE_BISHOP + "|" + WHITE_QUEEN
         + "|" + WHITE_KING + "|" + WHITE_BISHOP + "|" + WHITE_KNIGHT + "|" + WHITE_ROOK + "|");
        System.out.print("7 ");
        for (int i = 0; i < 8; i++){
            System.out.print("|" + WHITE_PAWN);
        }
        System.out.print("|\n");
        for (int i = 6; i > 2; i--){
            System.out.println(i + " | \u2001\u2005\u200A | \u2001\u2005\u200A | \u2001\u2005\u200A | \u2001\u2005\u200A | " +
                    "\u2001\u2005\u200A | \u2001\u2005\u200A | \u2001\u2005\u200A | \u2001\u2005\u200A |");
        }
        System.out.print("2 ");
        for (int i = 0; i < 8; i++){
            System.out.print("|" + BLACK_PAWN);
        }
        System.out.print("|\n");
        System.out.println("1 |" + BLACK_ROOK + "|" + BLACK_KNIGHT + "|" + BLACK_BISHOP + "|" + BLACK_QUEEN
                + "|" + BLACK_KING + "|" + BLACK_BISHOP + "|" + BLACK_KNIGHT + "|" + BLACK_ROOK + "|");


        System.out.println("\n\n\n");
        System.out.println("   " + SET_TEXT_UNDERLINE + " a   \u202f\u202fb   \u202f\u202fc\u202f\u202f  \u202fd\u202f\u202f   e\u202f\u202f   f\u202f\u202f  \u202fg\u202f   h " + RESET_TEXT_UNDERLINE);
        System.out.println("1 |"+BLACK_ROOK + "|" + BLACK_KNIGHT + "|" + BLACK_BISHOP + "|" + BLACK_QUEEN
                + "|" + BLACK_KING + "|" + BLACK_BISHOP + "|" + BLACK_KNIGHT + "|" + BLACK_ROOK + "|");
        System.out.print("2 ");
        for (int i = 0; i < 8; i++){
            System.out.print("|" + BLACK_PAWN);
        }
        System.out.print("|\n");
        for (int i = 3; i < 7; i++){
            System.out.println(i + " | \u2001\u2005\u200A | \u2001\u2005\u200A | \u2001\u2005\u200A | \u2001\u2005\u200A | " +
                    "\u2001\u2005\u200A | \u2001\u2005\u200A | \u2001\u2005\u200A | \u2001\u2005\u200A |");
        }
        System.out.print("7 ");
        for (int i = 0; i < 8; i++){
            System.out.print("|" + WHITE_PAWN);
        }
        System.out.print("|\n");
        System.out.println("8 |" + WHITE_ROOK + "|" + WHITE_KNIGHT + "|" + WHITE_BISHOP + "|" + WHITE_QUEEN
                + "|" + WHITE_KING + "|" + WHITE_BISHOP + "|" + WHITE_KNIGHT + "|" + WHITE_ROOK + "|");
    }

    public void gameplay(){
        while (true){
            printChessboard();
            System.out.print(">>>");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            var inputs = input.split(" ");
            if (inputs[0].equals("help")){
                System.out.println("help: gives a list of commands you can call");
                System.out.println("back: returns back to the screen before joining the game");
                System.out.println("quit: quits the program");
            }
            else if (inputs[0].equals("back")){
                postLogin();
            }
            else if (inputs[0].equals("quit")){
                break;
            }
            else{
                System.out.println("could not understand the command. Try again");
            }
        }
    }

}
