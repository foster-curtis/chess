package dataaccess;

import model.UserData;

public interface UserDAO extends DataAccess {

    UserData getUser(UserData userData) throws DataAccessException;

    UserData createUser(UserData userData) throws DataAccessException;
}
