package dataaccess;

import model.AuthData;
import model.UserData;

public class DBAuthDAO extends SqlConfig implements AuthDAO {
    public DBAuthDAO() throws DataAccessException {
        configureDatabase();
    }

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
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE authentication";
        executeUpdate(statement);
    }
}
