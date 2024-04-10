package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{
    String errorMessage;

    public ErrorMessage(String message){
        super(ServerMessageType.ERROR);
        this.errorMessage = message;
    }

    public String getMessage(){
        return errorMessage;
    }
}
