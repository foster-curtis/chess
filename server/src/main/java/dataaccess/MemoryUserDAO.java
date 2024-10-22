package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private HashMap<String, UserData> users;

    public MemoryUserDAO() {
        users = new HashMap<>();
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        return users.get(userData.username());
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        users.put(userData.username(), userData);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
