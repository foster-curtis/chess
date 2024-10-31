package dataaccess;

import model.AuthData;
import model.UserData;

public class DBAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth(UserData userData, String authToken) throws DataAccessException {
        return null;
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
