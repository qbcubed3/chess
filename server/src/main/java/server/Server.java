package server;

import spark.*;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        Spark.init();
        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void registerUser(){
        return;
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
