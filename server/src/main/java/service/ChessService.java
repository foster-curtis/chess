package service;

import dataaccess.*;
import model.*;

import java.util.Collection;
import java.util.Objects;

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

    public Collection<GameData> listGames(AuthData authData) {
        return null;
    }

    public GameData createGame(GameData gameData, AuthData authData) {
        return null;
    }

    public void joinGame(GameData gameData, AuthData authData) {

    }

    // User Services

    public AuthData register(UserData user) throws DataAccessException {
        if (userAccess.getUser(user) != null) {
            throw new DataAccessException("already taken");
        } else {
            userAccess.createUser(user);
        }
        return authAccess.createAuth(user);
    }

    public AuthData login(UserData user) {
        return null;
    }

    public void logout(AuthData auth) {
    }
}
