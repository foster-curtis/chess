package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authMap;

    public MemoryAuthDAO() {
        authMap = new HashMap<>();
    }

    @Override
    public AuthData createAuth(UserData userData, String authToken) throws DataAccessException {
        var newAuth = new AuthData(authToken, userData.username());
        authMap.put(newAuth.authToken(), newAuth);
        return newAuth;
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        if (authData == null) {
            throw new DataAccessException("unauthorized", 401);
        }
        var auth = authMap.get(authData.authToken());
        if (auth == null) {
            throw new DataAccessException("unauthorized", 401);
        } else {
            return auth;
        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        authMap.remove(authData.authToken());
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
