package server;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import handlers.ClearServiceHandler;
import handlers.GameServiceHandler;
import handlers.UserServiceHandler;
import spark.*;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("/web");
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
