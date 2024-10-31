package dataaccess;

import model.UserData;

public class DBUserDAO extends SqlConfig implements UserDAO {
    public DBUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
