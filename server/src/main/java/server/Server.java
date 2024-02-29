package server;

import handlers.ClearServiceHandler;
import handlers.UserServiceHandler;
import spark.*;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("/web");
        Spark.delete("/db", ClearServiceHandler::callClearService);
        Spark.post("/user", UserServiceHandler::callUserService);
        // Register your endpoints and handle exceptions here.
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
