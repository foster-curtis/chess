package ui;

import chess.ChessBoard;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class BoardUI {
    private final ChessPiece[][] board;
    private String playerColor = "WHITE";

    private static final String EMPTY = "   ";

    public BoardUI(ChessBoard board) {
        this.board = board.getBoard();
    }

    public BoardUI(ChessBoard board, String playerColor) {
        this.board = board.getBoard();
        this.playerColor = playerColor;
    }

    public String displayBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        return "";
    }

    private void drawHeaders(PrintStream out) {

        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLUE);

        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        out.print(EMPTY);
        if (Objects.equals(this.playerColor, "WHITE")) {
            for (int boardCol = 0; boardCol < 8; ++boardCol) {
                out.print(headers[boardCol]);
            }
        } else {
            for (int boardCol = 7; boardCol >= 0; --boardCol) {
                out.print(headers[boardCol]);
            }
        }
        out.print(EMPTY);

        setBlack(out);

        out.println();
    }

    private void drawChessBoard(PrintStream out) {

        String bgColor = SET_BG_COLOR_LIGHT_GREY;
        if (Objects.equals(this.playerColor, "WHITE")) {
            for (int boardRow = 7; boardRow >= 0; --boardRow) {
                drawRowPreAndPost(out, boardRow);
                bgColor = toggleBGColor(bgColor);
                drawRowOfSquares(out, boardRow, bgColor);
                drawRowPreAndPost(out, boardRow);
                out.println();
            }
        } else {
            for (int boardRow = 0; boardRow < 8; ++boardRow) {
                drawRowPreAndPost(out, boardRow);
                bgColor = toggleBGColor(bgColor);
                drawRowOfSquares(out, boardRow, bgColor);
                drawRowPreAndPost(out, boardRow);
                out.println();
            }
        }
    }

    private void drawRowPreAndPost(PrintStream out, int row) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLUE);

        out.print(" " + (row + 1) + " ");

        setBlack(out);
    }

    private String toggleBGColor(String bgColor) {
        if (Objects.equals(bgColor, SET_BG_COLOR_LIGHT_GREY)) {
            return SET_BG_COLOR_DARK_GREEN;
        } else {
            return SET_BG_COLOR_LIGHT_GREY;
        }
    }

    private void drawRowOfSquares(PrintStream out, int row, String bgColor) {

        if (Objects.equals(this.playerColor, "WHITE")) {
            for (int col = 0; col < 8; ++col) {
                bgColor = toggleBGColor(bgColor);
                drawSquare(out, bgColor, row, col);
            }
        } else {
            for (int col = 7; col >= 0; --col) {
                bgColor = toggleBGColor(bgColor);
                drawSquare(out, bgColor, row, col);
            }
        }

        setBlack(out);
    }

    private void drawSquare(PrintStream out, String bgColor, int row, int col) {
        out.print(bgColor);

        if (board[row][col] != null) {
            printPiece(out, row, col);
        } else {
            out.print(EMPTY);
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void printPiece(PrintStream out, int row, int col) {

        String symbol = board[row][col].getPieceType().toString();
        if (Objects.equals(symbol, "KNIGHT")) {
            symbol = " n ";
        } else {
            symbol = " " + symbol.charAt(0) + " ";
        }

        if (board[row][col].getTeamColor() == WHITE) {
            out.print(SET_TEXT_COLOR_WHITE);
        } else {
            out.print(SET_TEXT_COLOR_GREEN);
        }
        out.print(symbol.toUpperCase());
    }
}
