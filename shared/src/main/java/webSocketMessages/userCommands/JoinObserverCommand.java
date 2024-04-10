package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{

    int gameID;

    public JoinObserverCommand(String auth, int id, CommandType type){
        super(auth);
        this.commandType = type;
        this.gameID = id;
    }

    public int getId(){
        return gameID;
    }
}
