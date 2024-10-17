package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO extends DataAccess {

    GameData createGame(GameData gameData) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;
}
