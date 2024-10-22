package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> auth;

    public MemoryAuthDAO() {
        auth = new HashMap<>();
    }

    @Override
    public AuthData createAuth(UserData userData) throws DataAccessException {
        var authToken = UUID.randomUUID().toString();
        var newAuth = new AuthData(authToken, userData.username());
        auth.put(userData.username(), newAuth);
        return newAuth;
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
