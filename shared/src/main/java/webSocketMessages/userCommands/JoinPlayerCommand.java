package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    int gameId;
    ChessGame.TeamColor color;

    public JoinPlayerCommand(String auth, int id, ChessGame.TeamColor color){
        super(auth);
        this.gameId = id;
        this.color = color;
    }
}