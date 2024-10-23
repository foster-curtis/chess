package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import service.ChessService;
import service.ResponseException;
import spark.*;

import javax.xml.crypto.Data;
import java.util.Map;

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
        //Login
        Spark.post("/session", this::loginHandler);
        //Logout
        Spark.delete("/session", this::logoutHandler);
        //List Games
        Spark.get("/game", this::listGamesHandler);
        //Create Game
        Spark.post("/game", this::createGameHandler);
        //Join Game
        Spark.put("/game", this::joinGameHandler);

        //Exceptions
        Spark.exception(DataAccessException.class, this::exceptionHandler);
        Spark.notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return exceptionHandler(new ResponseException(msg, 400), req, res);
        });

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object exceptionHandler(Exception exception, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", exception.getMessage())));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

    private Object registerUserHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = service.register(user);
        return new Gson().toJson(auth);
    }

    private Object clearHandler(Request req, Response res) {
        service.clear();
        return new Gson().toJson("{}");
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = service.login(user);
        return new Gson().toJson(auth);
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        var auth = new Gson().fromJson(req.body(), AuthData.class);
        service.logout(auth);
        return new Gson().toJson("{}");
    }

    private Object listGamesHandler(Request req, Response res) {
        return null;
    }

    private Object createGameHandler(Request req, Response res) {
        return null;
    }

    private Object joinGameHandler(Request req, Response res) {
        return null;
    }
}
