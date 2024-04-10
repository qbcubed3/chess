package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    int gameID;
    ChessMove move;

    public MakeMoveCommand(String auth, int id, ChessMove move){
        super(auth);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = id;
        this.move = move;
    }

    public int getId(){
        return gameID;
    }

    public ChessMove getMove(){
        return move;
    }
}
