package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import service.ChessService;
import service.ResponseException;
import spark.*;

import javax.xml.crypto.Data;
import java.util.Collection;
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
        if (exception instanceof DataAccessException) {
            res.status(((DataAccessException) exception).StatusCode());
        } else if (exception instanceof ResponseException) {
            res.status(((ResponseException) exception).status());
        } else {
            res.status(500);
        }
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
        JsonObject empty = new JsonObject();
        return new Gson().toJson(empty);
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = service.login(user);
        return new Gson().toJson(auth);
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        AuthData auth = new AuthData(req.headers("authorization"), null);
        service.logout(auth);
        JsonObject empty = new JsonObject();
        return new Gson().toJson(empty);
    }

    private Object listGamesHandler(Request req, Response res) throws DataAccessException {
        AuthData auth = new AuthData(req.headers("authorization"), null);
        Collection<GameData> games = service.listGames(auth);
        return new Gson().toJson(Map.of("games", games));
    }

    private Object createGameHandler(Request req, Response res) {
        return null;
    }

    private Object joinGameHandler(Request req, Response res) {
        return null;
    }
}
