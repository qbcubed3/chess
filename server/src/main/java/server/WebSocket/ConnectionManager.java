package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final HashMap<String, Connection> connections = new HashMap<>();

    public void add(String user, Connection conn) {
        connections.put(user, conn);
    }

    public void remove(String user){
        connections.remove(user);
    }

    public void broadcast(String user, int id, ServerMessage message ) throws IOException {
        for (Map.Entry<String, Connection> c: connections.entrySet()) {
            String tempUser = c.getKey();
            Connection connection = c.getValue();
            if (!(tempUser.equals(user))) {
                if (connection.id == id){
                    connection.send(message.toString());
                }
            }
        }
    }
}
