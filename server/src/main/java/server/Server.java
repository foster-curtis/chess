package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.*;
import model.*;
import service.ChessService;
import exception.ResponseException;
import spark.*;

import java.util.Collection;
import java.util.Map;

public class Server {
    private final ChessService service;

    public Server() {
        try {
            var auth = new DBAuthDAO();
            var game = new DBGameDAO();
            var user = new DBUserDAO();
            service = new ChessService(game, user, auth);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
            res.status(((DataAccessException) exception).statusCode());
        } else if (exception instanceof ResponseException) {
            res.status(((ResponseException) exception).status());
        } else {
            res.status(500);
        }
        res.body(body);
        return res;
    }

    private Object registerUserHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = service.register(user);
        return new Gson().toJson(auth);
    }

    private Object clearHandler(Request req, Response res) throws DataAccessException {
        service.clear();
        //Return empty JsonObject
        return new Gson().toJson(new JsonObject());
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = service.login(user);
        return new Gson().toJson(auth);
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        AuthData auth = new AuthData(req.headers("authorization"), null);
        service.logout(auth);
        //Return empty JsonObject
        return new Gson().toJson(new JsonObject());
    }

    private Object listGamesHandler(Request req, Response res) throws DataAccessException {
        AuthData auth = new AuthData(req.headers("authorization"), null);
        Collection<GameData> games = service.listGames(auth);
        return new Gson().toJson(Map.of("games", games));
    }

    private Object createGameHandler(Request req, Response res) throws DataAccessException {
        AuthData auth = new AuthData(req.headers("authorization"), null);
        var game = new Gson().fromJson(req.body(), GameData.class);
        int gameID = service.createGame(game, auth);
        return new Gson().toJson(gameID);
    }

    private Object joinGameHandler(Request req, Response res) throws DataAccessException {
        AuthData auth = new AuthData(req.headers("authorization"), null);
        JoinRequest request = new Gson().fromJson(req.body(), JoinRequest.class);
        service.joinGame(request, auth);
        //Return empty JsonObject
        return new Gson().toJson(new JsonObject());
    }
}
