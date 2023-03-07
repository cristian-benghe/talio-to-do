package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    Board board2;
    @BeforeEach
    public void setUp() {
        board = new Board("board_title", null);
        board2=new Board("board_title", null);
    }
    @Test
    public void checkConstructor() {
        assertEquals(board.getTitle(), "board_title");
        assertNull(board.getColumns());
    }
    @Test
    void getTitle() {
        assertEquals(board.getTitle(), "board_title");
    }

    @Test
    void getColumns() {
        assertNull(board.getColumns());
    }

    @Test
    void testEquals() {
        assertEquals(board, board2);
    }

    @Test
    void testHashCode() {
        assertEquals(board, board2);
    }
    @Test
    void setTitle() {
        board.setTitle("sports");
        assertEquals(board.getTitle(), "sports");
    }

    @Test
    void setColumns() {
        board.setColumns(null);
        assertNull(board.getColumns());
    }
}