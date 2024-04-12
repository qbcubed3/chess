package server;

import handlers.ClearServiceHandler;
import handlers.GameServiceHandler;
import handlers.UserServiceHandler;
import server.WebSocket.WebSocketHandler;
import spark.*;

import java.net.http.WebSocket;

import static spark.Spark.webSocket;

public class Server {
    public int run(int desiredPort){
        Spark.port(desiredPort);
        Spark.staticFiles.location("/web");

        webSocket("/connect", WebSocketHandler.class);

        Spark.delete("/db", ClearServiceHandler::callClearService);
        Spark.post("/user", UserServiceHandler::callRegisterService);
        Spark.post("/session", UserServiceHandler::callLoginService);
        Spark.delete("/session", UserServiceHandler::callLogoutService);
        Spark.get("/game", GameServiceHandler:: callListService);
        Spark.post("/game", GameServiceHandler::callCreateGameService);
        Spark.put("/game", GameServiceHandler::callJoinGameService);
        // Register your endpoints and handle exceptions here.
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
