package ui.websocket;

import com.google.gson.Gson;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {

    Session session;


    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String s) {
                    parseMessage(s);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){}

    public void parseMessage(String message){
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()){
            case JOIN_OBSERVER -> joinObserver(command);
            case JOIN_PLAYER -> joinPlayer(command);
            case LEAVE -> leave(command);
            case RESIGN -> resign(command);
            case MAKE_MOVE -> makeMove(command);
        }
    }

    public void joinObserver(UserGameCommand command){
        return;
    }

    public void joinPlayer(UserGameCommand command){
        return;
    }

    public void makeMove(UserGameCommand command) {
        return;
    }

    public void leave(UserGameCommand command){
        return;
    }

    public void resign(UserGameCommand command){
        return;
    }
}
