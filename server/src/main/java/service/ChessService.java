package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class ChessService extends Service {

    public ChessService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        super(gameDAO, userDAO, authDAO);
    }

    // Game Services

    public Collection<GameData> listGames(AuthData authData) throws DataAccessException {
        authenticate(authData);
        return gameAccess.listGames();
    }

    public int createGame(GameData gameData, AuthData authData) throws DataAccessException {
        if (gameData.gameName() == null) {
            throw new DataAccessException("bad request", 400);
        }
        authenticate(authData);
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(0, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        return gameAccess.createGame(newGame);
    }

    public void joinGame(JoinRequest req, AuthData authData) throws DataAccessException {
        var auth = authenticate(authData);
        if (req.playerColor() == null || req.gameID() == null) {
            throw new DataAccessException("bad request", 400);
        }
        GameData game = gameAccess.getGame(req.gameID());
        if (game == null) {
            throw new DataAccessException("game not found", 500);
        }
        if (req.playerColor().equals("BLACK")) {
            if (!Objects.equals(game.blackUsername(), auth.username()) && game.blackUsername() != null) {
                throw new DataAccessException("already taken", 403);
            } else {
                var newGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
                gameAccess.updateGame(newGame);
            }
        } else {
            if (!Objects.equals(game.whiteUsername(), auth.username()) && game.whiteUsername() != null) {
                throw new DataAccessException("already taken", 403);
            } else {
                var newGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
                gameAccess.updateGame(newGame);
            }
        }
    }

    // User Services

    public AuthData register(UserData user) throws DataAccessException {
        if (userAccess.getUser(user) != null) {
            throw new DataAccessException("already taken", 403);
        } else if (user.password() == null || user.username() == null || user.email() == null) {
            throw new DataAccessException("bad request", 400);
        } else {
            userAccess.createUser(user);
        }
        return authAccess.createAuth(user, UUID.randomUUID().toString());
    }

    public AuthData login(UserData user) throws DataAccessException {
        var data = userAccess.getUser(user);
        if ((data == null) || (!Objects.equals(data.password(), user.password()))) {
            throw new DataAccessException("unauthorized", 401);
        } else {
            return authAccess.createAuth(user, UUID.randomUUID().toString());
        }
    }

    public void logout(AuthData authData) throws DataAccessException {
        authenticate(authData);
        authAccess.deleteAuth(authData);
    }

    public AuthData getAuth(AuthData auth) throws DataAccessException {
        return authAccess.getAuth(auth);
    }

    public UserData getUser(UserData user) throws DataAccessException {
        return userAccess.getUser(user);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return gameAccess.getGame(gameID);
    }
}
