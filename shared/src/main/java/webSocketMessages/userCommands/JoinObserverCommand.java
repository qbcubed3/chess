package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{

    int gameId;

    public JoinObserverCommand(String auth, int id, CommandType type){
        super(auth);
        this.commandType = type;
        this.gameId = id;
    }

    public int getId(){
        return gameId;
    }
}
