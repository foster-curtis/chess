package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<String, GameData> gameMap;

    public MemoryGameDAO() {
        this.gameMap = new HashMap<>();
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        gameMap.put(String.valueOf(gameData.gameID()), gameData);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(gameMap.values());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gameMap.get(String.valueOf(gameID));
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        gameMap.put(String.valueOf(gameData.gameID()), gameData);
    }

    @Override
    public void clear() {
        gameMap.clear();
    }
}
