package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;
    Board board1;
    Board board2;
    Column column1;
    Column column2;
    Tag tag1;
    Tag tag2;
    List<Column>columnList;
    List<Column>columnList2;
    List<Tag>tagList;
    List<Tag>tagList2;
    @BeforeEach
    public void setUp() {
        columnList=new ArrayList<>();
        tagList=new ArrayList<>();
        columnList2=new ArrayList<>();
        tagList2=new ArrayList<>();
        columnList.add(column1);
        columnList.add(column2);
        columnList2.add(column1);
        columnList2.add(column2);
        tagList.add(tag1);
        tagList.add(tag2);
        tagList2.add(tag1);
        tagList2.add(tag2);
        board1 = new Board("board", columnList,tagList);
        board=new Board("board", columnList2, tagList2);
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
        assertEquals(columnList, board.getColumns());
    }

    @Test
    void setColumns() {
        board.setColumns(null);
        assertNull(board.getColumns());
    }

    @Test
    void getTags() {
        assertEquals(board.getTags(), tagList);
    }

    @Test
    void setTags() {
        board.setTags(tagList2);
        assertEquals(tagList2, board.getTags());
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

    @Test
    void addColumn() {
        board.addColumn(column1);
        assertEquals(board.getColumns().size(), 3);
    }

    @Test
    void setColumn() {
        board.setColumn(0, column1);
        assertEquals(board.getColumns().get(0), column1);
    }

    @Test
    void addTag() {
         board1.addTag(tag1);
         assertEquals(board1.getTags().size(), 3);
    }

    @Test
    void setTag() {
        board1.setTag(0, tag1);
        assertEquals(board1.getTags().get(0), tag1);
    }


    @Test
    void toStringShort() {
        assertEquals("board -- null", board1.toStringShort());
    }

    @Test
    void deleteColumn() {
        board1.deleteColumn(0);
        assertEquals(board1.getTags().get(0), tag2);
    }
    

    @Test
    void setColor() {
        board1.setColor((double) 0, (double) 0, (double) 0);
        assertEquals(board1.getBlue(), 0);
        assertEquals(board1.getRed(), 0);
        assertEquals(board1.getGreen(), 0);
    }

    @Test
    void getRed() {
        board1.setColor((double) 0, (double) 0, (double) 0);
        assertEquals(board1.getRed(), 0);
    }

    @Test
    void getGreen() {
        board1.setColor((double) 0, (double) 0, (double) 0);
        assertEquals(board1.getGreen(), 0);
    }

    @Test
    void getBlue() {
        board1.setColor((double) 0, (double) 0, (double) 0);
        assertEquals(board1.getBlue(), 0);
    }

    @Test
    void deleteTag() {
        board1.deleteTag(0);
        assertEquals(board1.getTags().get(0), tag2);
    }
}