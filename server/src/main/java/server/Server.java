package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;
import service.ChessService;
import spark.*;

public class Server {
    private final ChessService service;

    public Server(ChessService service) {
        this.service = service;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //Register User
        Spark.post("/user", this::registerUserHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUserHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = service.registerUser(user);
        return new Gson().toJson(auth);
    }

}
