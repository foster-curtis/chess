package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.lang.StringBuilder;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class BoardUI {
    private final ChessPiece[][] board;
    private final ChessGame.TeamColor playerColor;

    private static final String EMPTY = "   ";

    public BoardUI(ChessBoard board, ChessGame.TeamColor playerColor) {
        this.board = board.getBoard();
        this.playerColor = playerColor;
    }

    public String displayBoard() {
        var builder = new StringBuilder();

        builder.append(ERASE_SCREEN);

        drawHeaders(builder);
        drawChessBoard(builder);
        drawHeaders(builder);

        builder.append(SET_BG_COLOR_BLACK);
        builder.append(SET_TEXT_COLOR_WHITE);

        return builder.toString();
    }

    private void drawHeaders(StringBuilder builder) {

        builder.append(SET_BG_COLOR_DARK_GREY);
        builder.append(SET_TEXT_COLOR_BLUE);

        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        builder.append(EMPTY);
        if (this.playerColor != BLACK) {
            for (int boardCol = 0; boardCol < 8; ++boardCol) {
                builder.append(headers[boardCol]);
            }
        } else {
            for (int boardCol = 7; boardCol >= 0; --boardCol) {
                builder.append(headers[boardCol]);
            }
        }
        builder.append(EMPTY);

        setBlack(builder);

        builder.append("\n");
    }

    private void drawChessBoard(StringBuilder builder) {

        String bgColor = SET_BG_COLOR_LIGHT_GREY;
        if (this.playerColor != BLACK) {
            for (int boardRow = 7; boardRow >= 0; --boardRow) {
                drawRowPreAndPost(builder, boardRow);
                bgColor = toggleBGColor(bgColor);
                drawRowOfSquares(builder, boardRow, bgColor);
                drawRowPreAndPost(builder, boardRow);
                builder.append("\n");
            }
        } else {
            for (int boardRow = 0; boardRow < 8; ++boardRow) {
                drawRowPreAndPost(builder, boardRow);
                bgColor = toggleBGColor(bgColor);
                drawRowOfSquares(builder, boardRow, bgColor);
                drawRowPreAndPost(builder, boardRow);
                builder.append("\n");
            }
        }
    }

    private void drawRowPreAndPost(StringBuilder builder, int row) {
        builder.append(SET_BG_COLOR_DARK_GREY);
        builder.append(SET_TEXT_COLOR_BLUE);

        builder.append(" ").append(row + 1).append(" ");

        setBlack(builder);
    }

    private String toggleBGColor(String bgColor) {
        if (Objects.equals(bgColor, SET_BG_COLOR_LIGHT_GREY)) {
            return SET_BG_COLOR_DARK_GREEN;
        } else {
            return SET_BG_COLOR_LIGHT_GREY;
        }
    }

    private void drawRowOfSquares(StringBuilder builder, int row, String bgColor) {

        if (this.playerColor != BLACK) {
            for (int col = 0; col < 8; ++col) {
                bgColor = toggleBGColor(bgColor);
                drawSquare(builder, bgColor, row, col);
            }
        } else {
            for (int col = 7; col >= 0; --col) {
                bgColor = toggleBGColor(bgColor);
                drawSquare(builder, bgColor, row, col);
            }
        }

        setBlack(builder);
    }

    private void drawSquare(StringBuilder builder, String bgColor, int row, int col) {
        builder.append(bgColor);

        if (board[row][col] != null) {
            printPiece(builder, row, col);
        } else {
            builder.append(EMPTY);
        }
    }

    private static void setBlack(StringBuilder builder) {
        builder.append(SET_BG_COLOR_BLACK);
        builder.append(SET_TEXT_COLOR_BLACK);
    }

    private void printPiece(StringBuilder builder, int row, int col) {

        String symbol = board[row][col].getPieceType().toString();
        if (Objects.equals(symbol, "KNIGHT")) {
            symbol = " n ";
        } else {
            symbol = " " + symbol.charAt(0) + " ";
        }

        if (board[row][col].getTeamColor() == WHITE) {
            builder.append(SET_TEXT_COLOR_WHITE);
        } else {
            builder.append(SET_TEXT_COLOR_GREEN);
        }
        builder.append(symbol.toUpperCase());
    }
}
