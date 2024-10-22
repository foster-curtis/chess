package service;

import dataaccess.*;
import model.*;

import java.util.Collection;
import java.util.Objects;

public class ChessService {
    private final GameDAO gameAccess;
    private final UserDAO userAccess;
    private final AuthDAO authAccess;
    private final UserService userService;
    private final GameService gameService;

    public ChessService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        gameAccess = gameDAO;
        userAccess = userDAO;
        authAccess = authDAO;
        userService = new UserService();
        gameService = new GameService();
    }

    //Game Services

    public Collection<GameData> listGames(AuthData authData) {
        return null;
    }

    public GameData createGame(GameData gameData, AuthData authData) {
        return null;
    }

    public void joinGame(GameData gameData, AuthData authData) {

    }

    //User Services

    public AuthData registerUser(UserData user) throws DataAccessException {
        if (Objects.equals(userAccess.getUser(user).username(), user.username())) {
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
