package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;
    Board board1;
    @BeforeEach
    public void setUp() {
        board1 = new Board("board", null,null);
        board=new Board("board", null, null);
    }
    @Test
    void getTitle() {
        assertEquals(board.getTitle(), "board");
    }

    @Test
    void setTitle() {
        board.setTitle("anapoda");
        assertEquals(board.getTitle(), "anapoda");
    }

    @Test
    void getColumns() {
        assertNull(board.getColumns());
    }

    @Test
    void setColumns() {
        board.setColumns(null);
        assertNull(board.getColumns());
    }

    @Test
    void getTags() {
        assertNull(board.getTags());
    }

    @Test
    void setTags() {
        board.setTags(null);
        assertNull(board.getTags());
    }

    @Test
    void testEquals() {
        assertTrue(board1.equals(board));
        assertEquals(board.hashCode(), board1.hashCode());
    }

    @Test
    void testHashCode() {
        assertEquals(board.hashCode(), board1.hashCode());
    }
}