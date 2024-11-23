package model;

public record MakeMoveResponse(String username, Boolean inCheck, Boolean inCheckmate, Boolean inStalemate) {
}
