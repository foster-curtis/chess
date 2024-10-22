package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import service.ChessService;
import spark.*;

public class Server {
    private final ChessService service;

    public Server() {
        var auth = new MemoryAuthDAO();
        var game = new MemoryGameDAO();
        var user = new MemoryUserDAO();
        service = new ChessService(game, user, auth);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //Clear
        Spark.delete("/db", this::clearHandler);
        //Register User
        Spark.post("/user", this::registerUserHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUserHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = service.register(user);
        return new Gson().toJson(auth);
    }

    private Object clearHandler(Request req, Response res) {
        service.clear();
        return new Gson().toJson(res);
    }
}
