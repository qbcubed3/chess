package server.WebSocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.NullParameterException;
import dataAccess.UnauthorizedException;
import dataAccess.UsernameTakenException;
import model.GameDataModel;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error occurred:");
        throwable.printStackTrace();
    }

    @OnWebSocketMessage
    public void parseMessage(String message) throws IOException, NullParameterException, UnauthorizedException, UsernameTakenException {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()){
            case JOIN_OBSERVER -> {
                var fullCommand = gson.fromJson(message, JoinObserverCommand.class);
                joinObserver(fullCommand);
            }
            case JOIN_PLAYER -> {
                var fullCommand = gson.fromJson(message, JoinPlayerCommand.class);
                joinPlayer(fullCommand);
            }
            case LEAVE -> {
                var fullCommand = gson.fromJson(message, JoinObserverCommand.class);
                leave(fullCommand);
            }
            case RESIGN -> {
                var fullCommand = gson.fromJson(message, JoinObserverCommand.class);
                resign(fullCommand);
            }
            case MAKE_MOVE -> {
                var fullCommand = gson.fromJson(message, MakeMoveCommand.class);
                makeMove(fullCommand);
            }
        }
    }

    public void joinObserver(JoinObserverCommand command) throws IOException, NullParameterException, UnauthorizedException, UsernameTakenException {
        System.out.println("joining observer");
        var gameId = command.getId();
        var auth = command.getAuthString();
        GameDataModel game = GameService.getGame(auth, gameId);
        ServerMessage message = new LoadMessage(game.game());
        connections.broadcastOne(gameId, message);
    }

    public void joinPlayer(JoinPlayerCommand command) throws IOException, UnauthorizedException {
        System.out.println("joinging player");
        var gameId = command.getId();
        var auth = command.getAuthString();
        var color = command.getColor();
        GameDataModel game = GameService.getGame(auth, gameId);
        ServerMessage message = new LoadMessage(game.game());
        connections.broadcastOne(gameId, message);
    }

    public void makeMove(MakeMoveCommand command) throws IOException {
        System.out.println("making a move");
        ChessMove move = command.getMove();

    }

    public void leave(JoinObserverCommand command) throws IOException {
        var gameId = command.getId();
    }

    public void resign(JoinObserverCommand command) throws IOException {
        var gameId = command.getId();
    }
}
