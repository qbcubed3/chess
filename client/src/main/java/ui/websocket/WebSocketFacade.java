package ui.websocket;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler handler;

    public WebSocketFacade(String url, NotificationHandler handler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.handler = handler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String message){
                    handleMessage(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            System.out.println("ERROR ERROR");
            throw new Exception(ex.getMessage());
        }
    }

    public void sendMessage(String message) throws Exception{
        if (session.isOpen()) {
            System.out.println(message);
            session.getBasicRemote().sendText(message);
        }
        else{
            System.out.println("closed)");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){}

    public void handleMessage(String message){
        Gson gson = new Gson();
        ServerMessage tempCommand = gson.fromJson(message, ServerMessage.class);
        if (tempCommand.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            Notification command = gson.fromJson(message, Notification.class);
            handleNotification(command);
        }
        else if (tempCommand.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
            ErrorMessage command = gson.fromJson(message, ErrorMessage.class);
            handleError(command);
        }
        else if (tempCommand.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            LoadMessage command = gson.fromJson(message, LoadMessage.class);
            handleLoad(command);
        }
    }

    public void handleNotification(Notification command){
        handler.notif(command.getMessage());
    }

    public void handleError(ErrorMessage command){
        handler.error(command.getMessage());
    }

    public void handleLoad(LoadMessage command){
        handler.draw(command.getGame());
    }


}
