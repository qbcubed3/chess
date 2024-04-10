package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public int id;
    public Session session;

    public Connection(int id, Session session){
        this.id = id;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        System.out.println(msg);
        session.getRemote().sendString(msg);
    }
}
