package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String name, Session session) {
        var conn = new Connection(name, session);
        connections.put(name, conn);
    }

    public void remove(String name){
        connections.remove(name);
    }

    public void broadcast(String name, ServerMessage message ) throws IOException {
        var toRemove = new ArrayList<Connection>();
        for (var c: connections.values()){
            if (c.session.isOpen()) {
                if (!c.name.equals(name)) {
                    c.send(message.toString());
                }
            } else{
                toRemove.add(c);
            }
        }

        for (var c: toRemove) {
            connections.remove(c.name);
        }

    }
}
