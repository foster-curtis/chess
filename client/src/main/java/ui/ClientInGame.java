package ui;

import chess.*;
import model.AuthData;
import ui.websocketmanager.WebSocketFacade;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class ClientInGame implements Client {
    private final AuthData currentUserAuth;
    private final WebSocketFacade ws;
    private final Scanner scanner = new Scanner(System.in);
    private final Integer gameID;
    private State state = State.INGAME;
    private ChessGame chessGame;
    private final ChessGame.TeamColor playerColor;

    public ClientInGame(int port, AuthData currUserAuth, int gameID, ChessGame.TeamColor playerColor, ServerMessageObserver serverMessageObserver) {
        this.ws = new WebSocketFacade(port, serverMessageObserver, this);
        this.currentUserAuth = currUserAuth;
        this.gameID = gameID;
        this.playerColor = playerColor;

        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, currentUserAuth.authToken(), gameID);
        try {
            ws.send(command);
        } catch (IOException e) {
            System.out.println("Unhandled IO error.");
        }
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String eval(String input) {

        return switch (input) {
            case ("2") -> redrawBoard();
            case ("3") -> leave();
            case ("4") -> makeMove();
            case ("5") -> resign();
            case ("6") -> highlightMoves();
            default -> help();
        };
    }

    private String redrawBoard() {
        return new BoardUI(chessGame.getBoard(), playerColor).displayBoard(null, null);
    }

    private String leave() {
        this.state = State.LOGGEDIN;

        try {
            ws.send(new UserGameCommand(UserGameCommand.CommandType.LEAVE, currentUserAuth.authToken(), this.gameID));
        } catch (IOException e) {
            System.out.println("Unhandled IO error.");
        }
        return "";
    }

    private String makeMove() {

        ChessPosition start = null;
        ChessPosition end = null;
        ChessPiece.PieceType promotionPiece = null;

        System.out.print("""
                Input the start and end coordinates as column (letter) and then row (number).
                    Ex: d2 d4
                """);
        while (start == null || end == null) {
            System.out.print(">>> ");
            var input = scanner.nextLine();
            var moves = input.split(" ");
            if (moves.length != 2) {
                System.out.println(SET_TEXT_COLOR_RED + "Incorrect number of coordinates." + SET_TEXT_COLOR_WHITE);
            } else {
                start = checkAndSetCoordinate(moves[0]);
                end = checkAndSetCoordinate(moves[1]);
                if (start == null || end == null) {
                    continue;
                }
                if (chessGame.getBoard().getPiece(start) == null) {
                    System.out.println(SET_TEXT_COLOR_RED + "No piece at start position." + SET_TEXT_COLOR_WHITE);
                    start = null;
                    end = null;
                    continue;
                }
                var pieceType = chessGame.getBoard().getPiece(start).getPieceType();
                if (pieceType == ChessPiece.PieceType.PAWN) {
                    if (playerColor == BLACK && end.getRow() == 1) {
                        promotionPiece = getPromotionPiece();
                    } else if (playerColor == WHITE && end.getRow() == 8) {
                        promotionPiece = getPromotionPiece();
                    }
                }
            }
        }

        try {
            ws.send(new MakeMoveCommand(currentUserAuth.authToken(), this.gameID, new ChessMove(start, end, promotionPiece)));
        } catch (IOException e) {
            System.out.println("Unhandled IO error.");
        }

        return "";
    }

    private ChessPosition checkAndSetCoordinate(String move) {
        ChessPosition position = null;
        if (move.length() != 2) {
            System.out.println(SET_TEXT_COLOR_RED + "Invalid coordinate length: " + move.length() + " (Should be 2)");
            System.out.print(SET_TEXT_COLOR_WHITE);
            return null;
        } else {
            try {
                position = new ChessPosition(Integer.parseInt(String.valueOf(move.charAt(1))), switch (move.charAt(0)) {
                    case ('a') -> 1;
                    case ('b') -> 2;
                    case ('c') -> 3;
                    case ('d') -> 4;
                    case ('e') -> 5;
                    case ('f') -> 6;
                    case ('g') -> 7;
                    case ('h') -> 8;
                    default -> throw new Exception(SET_TEXT_COLOR_RED + "Invalid column.");
                });
            } catch (Exception e) {
                System.out.println(e.getMessage() + SET_TEXT_COLOR_WHITE);
            }
        }
        return position;
    }

    private ChessPiece.PieceType getPromotionPiece() {
        System.out.println("""
                Your pawn can be promoted! What piece type should it promote to?
                1. Knight
                2. Bishop
                3. Rook
                4. Queen
                """);
        var input = scanner.nextLine();
        return switch (input) {
            case ("1") -> ChessPiece.PieceType.KNIGHT;
            case ("2") -> ChessPiece.PieceType.BISHOP;
            case ("3") -> ChessPiece.PieceType.ROOK;
            default -> ChessPiece.PieceType.QUEEN;
        };
    }

    private String resign() {
        boolean done = false;
        while (!done) {
            System.out.println("Are you sure you want to resign? (Y/N)");
            var input = scanner.nextLine();
            if (Objects.equals(input.toUpperCase(), "Y")) {
                done = true;
                UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, currentUserAuth.authToken(), gameID);
                try {
                    ws.send(command);
                } catch (IOException e) {
                    System.out.println("Unhandled IO error.");
                }
            } else if (Objects.equals(input.toUpperCase(), "N")) {
                done = true;
                System.out.println("Press on! Do your best!");
            } else {
                System.out.println(SET_TEXT_COLOR_RED + "Invalid input. Please try again." + SET_TEXT_COLOR_WHITE);
            }
        }
        return "";
    }

    private String highlightMoves() {
        ChessBoard board = chessGame.getBoard();

        System.out.println("Input the coordinates for the piece you want to check.");
        System.out.println(">>> ");

        var input = scanner.nextLine();

        var position = checkAndSetCoordinate(input);
        Collection<ChessMove> moves = chessGame.validMoves(position);

        return new BoardUI(board, playerColor).displayBoard(moves, new ChessMove(position, null, null));
    }

    @Override
    public String help() {
        return """
                1. Help -> View list of available commands
                2. Redraw Chess Board -> Pretty self explanatory
                3. Leave -> Exit the game (You can come back and join later!)
                4. Make Move -> Move a piece on the board
                5. Resign -> Resign from the game. (You'll lose)
                6. Highlight Legal Moves -> Highlight all the legal moves of any piece
                """;
    }

    @Override
    public AuthData getCurrentUserAuth() {
        return this.currentUserAuth;
    }

    @Override
    public Integer getGameID() {
        return this.gameID;
    }

    @Override
    public ChessGame.TeamColor getPlayerColor() {
        return this.playerColor;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }
}
