package model;

public record MakeMoveResponse(String username, GameData gameData, Boolean inCheck, Boolean inCheckmate,
                               Boolean inStalemate) {
}
