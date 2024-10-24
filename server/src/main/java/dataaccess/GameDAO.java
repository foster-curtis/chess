package dataaccess;

import model.GameData;
import model.UserData;

import java.util.Collection;

public interface GameDAO extends DataAccess {

    void createGame(GameData gameData) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData gameData, UserData user) throws DataAccessException;
}
