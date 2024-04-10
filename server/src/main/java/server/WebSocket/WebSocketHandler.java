package server.WebSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.NullParameterException;
import dataAccess.SQLAuthDAO;
import dataAccess.UnauthorizedException;
import dataAccess.UsernameTakenException;
import model.GameDataModel;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error occurred:");
        throwable.printStackTrace();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, NullParameterException, UnauthorizedException, UsernameTakenException {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        Connection conn;
        switch(command.getCommandType()){
            case JOIN_OBSERVER -> {
                var fullCommand = gson.fromJson(message, JoinObserverCommand.class);
                conn = makeConnection(command.getAuthString(), fullCommand.getId(), session);
                joinObserver(fullCommand, conn);
            }
            case JOIN_PLAYER -> {
                var fullCommand = gson.fromJson(message, JoinPlayerCommand.class);
                conn = makeConnection(command.getAuthString(), fullCommand.getId(), session);
                joinPlayer(fullCommand, conn);
            }
            case LEAVE -> {
                var fullCommand = gson.fromJson(message, JoinObserverCommand.class);
                conn = makeConnection(command.getAuthString(), fullCommand.getId(), session);
                leave(fullCommand, conn);
            }
            case RESIGN -> {
                var fullCommand = gson.fromJson(message, JoinObserverCommand.class);
                conn = makeConnection(command.getAuthString(), fullCommand.getId(), session);
                resign(fullCommand, conn);
            }
            case MAKE_MOVE -> {
                var fullCommand = gson.fromJson(message, MakeMoveCommand.class);
                conn = makeConnection(command.getAuthString(), fullCommand.getId(), session);
                makeMove(fullCommand, conn);
            }
        }
    }

    public void joinObserver(JoinObserverCommand command, Connection conn) throws IOException, NullParameterException, UnauthorizedException, UsernameTakenException {
        System.out.println("joining observer");
        var gameId = command.getId();
        var auth = command.getAuthString();
        var user = SQLAuthDAO.getUsername(auth);
        GameDataModel game = GameService.getGame(auth, gameId);
        ServerMessage message = new LoadMessage(game.game());
        conn.send(message.toString());
        Notification notif = new Notification("Another guy joined the game yippee")
        connections.broadcast(user, gameId, message);
    }

    public void joinPlayer(JoinPlayerCommand command, Connection conn) throws IOException, UnauthorizedException {
        var gameId = command.getId();
        var auth = command.getAuthString();
        var color = command.getColor();
        var user = SQLAuthDAO.getUsername(auth);
        GameDataModel game = GameService.getGame(auth, gameId);
        if (color.equals(ChessGame.TeamColor.BLACK)){
            if (game.blackUsername() == null){
                var message = new ErrorMessage("Spot already taken");
                conn.send(message.toString());
                return;
            }
            if (!(game.blackUsername().equals(user))){
                var message = new ErrorMessage("Spot already taken");
                conn.send(message.toString());
                return;
            }
        }
        else if (color.equals(ChessGame.TeamColor.WHITE)){
            if (game.whiteUsername() == null){
                var message = new ErrorMessage("Spot already taken");
                conn.send(message.toString());
                return;
            }
            if (!(game.whiteUsername().equals(user))){
                var message = new ErrorMessage("Spot already taken");
                conn.send(message.toString());
                return;
            }
        }
        LoadMessage message = new LoadMessage(game.game());
        conn.send(message.toString());
        Notification notif = new Notification("Dude joined the game");
        connections.broadcast(user, gameId, notif);
    }

    public void makeMove(MakeMoveCommand command, Connection conn) throws IOException {
        System.out.println("making a move");
        ChessMove move = command.getMove();

    }

    public void leave(JoinObserverCommand command, Connection conn) throws IOException {
        System.out.println("leaving uh oh");
        var gameId = command.getId();
    }

    public void resign(JoinObserverCommand command, Connection conn) throws IOException {
        System.out.println("you lose dumbass");
        var gameId = command.getId();
    }

    public Connection makeConnection(String auth, int id, Session session) throws UnauthorizedException, IOException {
        try {
            Connection conn;
            var valid = SQLAuthDAO.checkAuth(auth);
            var user = SQLAuthDAO.getUsername(auth);
            if (valid){
                conn = new Connection(id, session);
                connections.add(user, conn);
                System.out.println("conn: " + conn);
                return conn;
            }
            else{
                throw new UnauthorizedException("Bad Auth");
            }

        } catch (UnauthorizedException e){
            var conn = new Connection(id, session);
            ErrorMessage message = new ErrorMessage("Unauthorized");
            conn.send(message.toString());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
