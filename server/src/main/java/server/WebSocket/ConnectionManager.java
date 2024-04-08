package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void add(int id, Session session) {
        var conn = new Connection(id, session);
        connections.put(id, conn);
    }

    public void remove(int id){
        connections.remove(id);
    }

    public void broadcast(int id, ServerMessage message ) throws IOException {
        var toRemove = new ArrayList<Connection>();
        for (var c: connections.values()){
            if (c.session.isOpen()) {
                if (!(c.id == id)) {
                    c.send(message.toString());
                }
            } else{
                toRemove.add(c);
            }
        }

        for (var c: toRemove) {
            connections.remove(c.id);
        }

    }

    public void broadcastOne(int id, ServerMessage message) throws IOException {
        for (var c: connections.values()) {
            if (c.session.isOpen()) {
                if (c.id == id) {
                    c.send(message.toString());
                }
            }
        }
    }
}
