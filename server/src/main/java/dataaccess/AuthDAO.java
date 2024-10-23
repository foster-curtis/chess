package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO extends DataAccess {

    AuthData createAuth(UserData userData, String authToken) throws DataAccessException;

    AuthData getAuth(AuthData authData) throws DataAccessException;

    void deleteAuth(AuthData authData) throws DataAccessException;
}
