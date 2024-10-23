package service;

import dataaccess.*;
import model.*;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class ChessService {
    private final GameDAO gameAccess;
    private final UserDAO userAccess;
    private final AuthDAO authAccess;

    public ChessService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        gameAccess = gameDAO;
        userAccess = userDAO;
        authAccess = authDAO;
    }

    // Clear

    public void clear() {
        userAccess.clear();
        gameAccess.clear();
        authAccess.clear();
    }

    // Game Services

    public Collection<GameData> listGames(AuthData authData) throws DataAccessException {
        var auth = authAccess.getAuth(authData);
        if (auth == null) {
            throw new DataAccessException("unauthorized", 401);
        } else {
            return null;
        }
    }

    public GameData createGame(GameData gameData, AuthData authData) {
        return null;
    }

    public void joinGame(GameData gameData, AuthData authData) {

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
        var auth = authAccess.getAuth(authData);
        if (auth == null) {
            throw new DataAccessException("unauthorized", 401);
        } else {
            authAccess.deleteAuth(authData);
        }
    }

    public AuthData getAuth(AuthData auth) throws DataAccessException {
        return authAccess.getAuth(auth);
    }

    public UserData getUser(UserData user) throws DataAccessException {
        return userAccess.getUser(user);
    }
}
