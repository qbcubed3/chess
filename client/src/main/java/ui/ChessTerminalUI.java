package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.AuthDataModel;
import model.GameDataModel;
import server.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessTerminalUI implements NotificationHandler {
    public ServerFacade facade;
    public WebSocketFacade ws;
    private String auth;
    private int id;
    String user;
    String color;
    public ChessTerminalUI(String url) throws Exception {
        this.facade = new ServerFacade(url);
        this.ws = new WebSocketFacade(url, this);
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

                    AuthDataModel model = facade.register(username, password, email);
                    auth = model.authToken();
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
                    user = inputs[1];
                    AuthDataModel model = facade.login(username, password);
                    auth = model.authToken();
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
                System.out.println("leave: allows the user to leave the game");
                System.out.println("resign: resigns the game for the user");
                System.out.println("redraw: redraws the board");
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
                        id = Integer.parseInt(inputs[2]);
                        facade.join("", Integer.parseInt(inputs[2]));
                        System.out.println("joined gameID: " + inputs[2] + " as a spectator");
                        JoinObserverCommand command = new JoinObserverCommand(auth, Integer.parseInt(inputs[2]), UserGameCommand.CommandType.JOIN_OBSERVER);
                        Gson gson = new Gson();
                        String json = gson.toJson(command);
                        ws.sendMessage(json);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (inputs.length == 4){
                    try{
                        id = Integer.parseInt(inputs[3]);
                        facade.join(inputs[2], Integer.parseInt(inputs[3]));
                        System.out.println("joined gameID: " + inputs[3] + " as " + inputs[2]);
                        JoinPlayerCommand command;
                        if (inputs[2].equals("BLACK")){
                            color = "BLACK";
                            command = new JoinPlayerCommand(auth, Integer.parseInt(inputs[3]), ChessGame.TeamColor.BLACK);
                        }
                        else if (inputs[2].equals("WHITE")){
                            color = "WHITE";
                            command = new JoinPlayerCommand(auth, Integer.parseInt(inputs[3]), ChessGame.TeamColor.WHITE);
                        }
                        else{
                            throw new Exception("Color must be WHITE or BLACK or blank");
                        }
                        Gson gson = new Gson();
                        String json = gson.toJson(command);
                        ws.sendMessage(json);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
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
            else if (inputs[0].equals("leave")){
                try {
                    JoinObserverCommand command = new JoinObserverCommand(auth, id, UserGameCommand.CommandType.LEAVE);
                    Gson gson = new Gson();
                    var json = gson.toJson(command);
                    ws.sendMessage(json);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
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

    public void gameplay(ChessBoard board){
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

    public void drawBoard(ChessBoard board) {
        var bottom = 1;
        var top = 9;
        var increment = 1;
        if (color != null) {
            if (color.equals("BLACK")) {
                bottom = 8;
                top = 0;
                increment = -1;
            }
        }
        System.out.println();
        for (int i = bottom; i != top; i+=increment){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece curPiece = board.getPiece(position);
                if (curPiece == null){
                    System.out.print("| \u2001\u2005\u200A ");
                }

                else{
                    if (curPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        switch (curPiece.getPieceType()) {
                            case KING -> System.out.print("|" + BLACK_KING);
                            case PAWN -> System.out.print("|" + BLACK_PAWN);
                            case ROOK -> System.out.print("|" + BLACK_ROOK);
                            case QUEEN -> System.out.print("|" + BLACK_QUEEN);
                            case BISHOP -> System.out.print("|" + BLACK_BISHOP);
                            case KNIGHT -> System.out.print("|" + BLACK_KNIGHT);
                        }
                    }
                    if (curPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        switch (curPiece.getPieceType()) {
                            case KING -> System.out.print("|" + WHITE_KING);
                            case PAWN -> System.out.print("|" + WHITE_PAWN);
                            case ROOK -> System.out.print("|" + WHITE_ROOK);
                            case QUEEN -> System.out.print("|" + WHITE_QUEEN);
                            case BISHOP -> System.out.print("|" + WHITE_BISHOP);
                            case KNIGHT -> System.out.print("|" + WHITE_KNIGHT);
                        }
                    }
                }
            }
            System.out.println("|");
        }
    }

    public void error(String error){
        System.out.println(error);
    }

    public void notif(String message){
        System.out.println(message);
    }

    public void draw(ChessGame game){
        if (game.getBoard().equals(new ChessBoard())){
            var board = new ChessBoard();
            board.resetBoard();
            drawBoard(board);
        }
        else{
            drawBoard(game.getBoard());
        }
    }




}
