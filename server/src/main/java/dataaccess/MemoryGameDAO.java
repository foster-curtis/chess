package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<String, GameData> gameMap;

    public MemoryGameDAO() {
        this.gameMap = new HashMap<>();
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        int gameID = Math.abs(new Random().nextInt());
        var newGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        gameMap.put(String.valueOf(gameID), newGame);
        return gameID;
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
