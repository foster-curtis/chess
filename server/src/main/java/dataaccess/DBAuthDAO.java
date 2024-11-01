package dataaccess;

import model.AuthData;
import model.UserData;

public class DBAuthDAO extends SqlConfig implements AuthDAO {

    public DBAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData createAuth(UserData userData, String authToken) throws DataAccessException {
        String statement = "INSERT INTO authentication (username, authToken) VALUES (?, ?)";
        executeUpdate(statement, userData.username(), authToken);
        return new AuthData(authToken, userData.username());
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM authentication WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var username = rs.getString("username");
                        var authToken = rs.getString("authToken");
                        return new AuthData(authToken, username);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        String statement = "DELETE FROM authentication WHERE authToken = ?";
        executeUpdate(statement, authData.authToken());
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE authentication";
        executeUpdate(statement);
    }
}
