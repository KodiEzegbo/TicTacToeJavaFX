package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// PROMPT USED: Modify BoardTest.java so that Board.java has 100% coverage
/** Unit tests for board state, move validation, and win detection. */
class BoardTest {
    private Board board;

    /** Creates a fresh board before each test. */
    @BeforeEach
    void setUp() {
        board = new Board();
    }

    /** Verifies that only empty, in-bounds cells accept valid player markers. */
    @Test
    void acceptsOnlyEmptyInBoundsCellsAndPlacesValidMarkers() {
        assertTrue(board.isValid(0, 0));
        assertFalse(board.isValid(-1, 0));
        assertFalse(board.isValid(3, 0));
        assertFalse(board.isValid(0, -1));
        assertFalse(board.isValid(0, 3));
        assertTrue(board.place(0, 0, 'X'));
        assertFalse(board.isValid(0, 0));
        assertFalse(board.place(0, 0, 'O'));
        assertFalse(board.place(1, 1, 'Z'));
        assertFalse(board.place(-1, 0, 'X'));
        assertFalse(board.place(3, 0, 'X'));
        assertFalse(board.place(0, -1, 'X'));
        assertFalse(board.place(0, 3, 'X'));
        assertTrue(board.place(2, 2, 'O'));
        assertEquals('X', board.getCell(0, 0));
        assertEquals('O', board.getCell(2, 2));
    }

    /** Verifies board formatting and that display writes the formatted board. */
    @Test
    void formatsAndDisplaysTheBoard() {
        String emptyBoard = "  1   2   3\n"
                + "1   |   |  \n"
                + "  -----------\n"
                + "2   |   |  \n"
                + "  -----------\n"
                + "3   |   |  \n";
        assertEquals(emptyBoard, board.format());

        board.place(0, 0, 'X');
        String formattedBoard = board.format();
        assertTrue(formattedBoard.startsWith("  1   2   3\n1 X |   |  \n"));

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            board.display();
        } finally {
            System.setOut(originalOut);
        }
        assertEquals(formattedBoard + System.lineSeparator(),
                output.toString(StandardCharsets.UTF_8));
    }

    /** Verifies that reading any out-of-bounds cell throws the documented exception. */
    @Test
    void rejectsOutOfBoundsCellReads() {
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(3, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(0, 3));
        assertEquals(' ', board.getCell(0, 0));
    }

    /** Verifies win detection for rows, columns, and both diagonals. */
    @Test
    void detectsWinsAcrossRowsColumnsAndBothDiagonals() {
        assertPlacesAndWins(new int[][]{{0, 0}, {0, 1}, {0, 2}});
        assertPlacesAndWins(new int[][]{{1, 0}, {1, 1}, {1, 2}});
        assertPlacesAndWins(new int[][]{{2, 0}, {2, 1}, {2, 2}});
        assertPlacesAndWins(new int[][]{{0, 0}, {1, 0}, {2, 0}});
        assertPlacesAndWins(new int[][]{{0, 1}, {1, 1}, {2, 1}});
        assertPlacesAndWins(new int[][]{{0, 2}, {1, 2}, {2, 2}});
        assertPlacesAndWins(new int[][]{{0, 0}, {1, 1}, {2, 2}});
        assertPlacesAndWins(new int[][]{{0, 2}, {1, 1}, {2, 0}});
    }

    /** Verifies win detection when the winning marker is O. */
    @Test
    void detectsWinsForEitherMarker() {
        assertTrue(board.place(0, 0, 'O'));
        assertTrue(board.place(1, 1, 'O'));
        assertTrue(board.place(2, 2, 'O'));
        assertTrue(board.checkWin());
    }

    /** Verifies a full board with no winning line is recognized as a draw state. */
    @Test
    void detectsNoWinAndFullBoardDraw() {
        char[][] marks = {{'X', 'O', 'X'}, {'X', 'O', 'O'}, {'O', 'X', 'X'}};
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                assertTrue(board.place(row, column, marks[row][column]));
            }
        }
        assertTrue(board.isFull());
        assertFalse(board.checkWin());
    }

    /** Verifies that initialization clears all previously placed markers. */
    @Test
    void initializeClearsTheBoard() {
        board.place(1, 1, 'O');
        board.initialize();
        assertTrue(board.isValid(1, 1));
        assertFalse(board.isFull());
        assertFalse(board.checkWin());
    }

    /** Places the supplied cells for X and verifies that they form a win. */
    private void assertPlacesAndWins(int[][] cells) {
        board.initialize();
        for (int[] cell : cells) {
            assertTrue(board.place(cell[0], cell[1], 'X'));
        }
        assertTrue(board.checkWin());
    }
}