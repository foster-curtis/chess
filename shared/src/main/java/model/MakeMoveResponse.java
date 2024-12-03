package model;

public record MakeMoveResponse(String username, String opponentUsername, GameData gameData, Boolean inCheck,
                               Boolean inCheckmate,
                               Boolean inStalemate) {
}
