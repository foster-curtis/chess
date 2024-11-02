package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class DBGameDAO extends SqlConfig implements GameDAO {
    public DBGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE games";
        executeUpdate(statement);
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        String statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";
        var white = gameData.whiteUsername();
        var black = gameData.blackUsername();
        var name = gameData.gameName();
        if (gameData.game() == null) {
            throw new DataAccessException("ChessGame was null but is marked as NOT NULL", 500);
        }
        var game = new Gson().toJson(gameData.game());
        return executeUpdate(statement, white, black, name, game);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    ArrayList<GameData> games = new java.util.ArrayList<>();
                    while (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var white = rs.getString("whiteUsername");
                        var black = rs.getString("blackUsername");
                        var name = rs.getString("gameName");
                        var game = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
                        games.add(new GameData(gameID, white, black, name, game));
                    }
                    return games;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM games WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var id = rs.getInt("gameID");
                        var white = rs.getString("whiteUsername");
                        var black = rs.getString("blackUsername");
                        var name = rs.getString("gameName");
                        var game = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
                        return new GameData(id, white, black, name, game);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }
}
