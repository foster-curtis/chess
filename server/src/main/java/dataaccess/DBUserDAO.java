package dataaccess;

import com.google.gson.Gson;
import model.UserData;

public class DBUserDAO extends SqlConfig implements UserDAO {
    public DBUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        //try (var conn = DatabaseManager.getConnection()) {
        //var statement = "SELECT id, json FROM pet WHERE id=?";
        String statement = "SELECT * FROM users WHERE username = (username) VALUES (?)";
//            try (var ps = conn.prepareStatement(statement)) {
//                ps.setInt(1, id);
//                try (var rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        return readPet(rs);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
//        }
//        return null;
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
