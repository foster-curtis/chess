package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class DBGameDAO extends SqlConfig implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }
}
