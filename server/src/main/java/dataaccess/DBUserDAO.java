package dataaccess;

import com.google.gson.Gson;
import model.UserData;

public class DBUserDAO extends SqlConfig implements UserDAO {
    public DBUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM users WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, userData.username());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var username = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        return new UserData(username, password, email);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE users";
        executeUpdate(statement);
    }
}
