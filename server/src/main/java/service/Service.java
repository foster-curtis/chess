package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;

public class Service {
    public final GameDAO gameAccess;
    public final UserDAO userAccess;
    public final AuthDAO authAccess;

    public Service(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        gameAccess = gameDAO;
        userAccess = userDAO;
        authAccess = authDAO;
    }

    // Clear

    public void clear() throws DataAccessException {
        userAccess.clear();
        gameAccess.clear();
        authAccess.clear();
    }

    public AuthData authenticate(AuthData authData) throws DataAccessException {
        if (authData == null) {
            throw new DataAccessException("bad request", 400);
        }
        var auth = authAccess.getAuth(authData);
        if (auth == null) {
            throw new DataAccessException("unauthorized", 401);
        }
        return auth;
    }
}
