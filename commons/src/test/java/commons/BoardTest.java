package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;
    Board board1;
    Board board2;
    Board board3;
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
        tag1 = new Tag("Tag 1");
        tag2 = new Tag("Tag 2");
        tagList.add(tag1);
        tagList.add(tag2);
        tagList2.add(tag1);
        tagList2.add(tag2);
        board1 = new Board("board", columnList,tagList);
        board=new Board("board", columnList2, tagList2);
        board2 = new Board(2L, "board", columnList2, tagList2);
        board3 = new Board();

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
    void testEquals1() {
        Board board1 = new Board("board", columnList, tagList);
        Board board2 = new Board("board", columnList2, tagList2);
        boolean areEqual = board1.equals(board2);
        assertTrue(areEqual);
    }

    @Test
    void testEquals2(){
        Board board1 = new Board("board", columnList, tagList);
        Board board2 = new Board("new board", columnList2, tagList2);
        assertFalse(board1.equals(board2));
    }

    @Test
    void testEquals3(){
        Board board1 = new Board("board", columnList, tagList);
        Board board2 = new Board("board", new ArrayList<>(), tagList2);
        assertFalse(board1.equals(board2));
    }

    @Test
    void testEquals4(){
        Board board1 = new Board("board", columnList, tagList);
        Board board2 = new Board("board", columnList2, new ArrayList<>());
        assertFalse(board1.equals(board2));
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
        assertEquals(board1.getTags().get(0), tag1);
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

    @Test
    void testgetId() {
        board1.setId(2L);
        assertEquals(2L, board1.getId());
    }

    @Test
    void setId() {
        board1.setId(2L);
        assertEquals(2L, board1.getId());
    }

    @Test
    void testGetTitle() {
        assertEquals("board", board1.getTitle());
    }

    @Test
    void testAddColumn() {
        Column testColumn = new Column();
        board.addColumn(testColumn);
        assertTrue(board.getColumns().contains(testColumn));
    }

    @Test
    void testSetColumn() {
        Column newColumn = new Column();
        board.addColumn(newColumn);
        List<Column> columns = board.getColumns();
        assertEquals(3, columns.size());
        assertTrue(columns.contains(newColumn));
    }

    @Test
    void testSetColumns() {
        Column column1 = new Column("Column 1", new ArrayList<>());
        Column column2 = new Column("Column 2", new ArrayList<>());
        Tag tag1 = new Tag("Tag 1");
        Tag tag2 = new Tag("Tag 2");
        List<Column> initialColumns = new ArrayList<>();
        initialColumns.add(column1);
        initialColumns.add(column2);
        List<Tag> initialTags = new ArrayList<>();
        initialTags.add(tag1);
        initialTags.add(tag2);

        Column column3 = new Column("Column 3", new ArrayList<>());
        Column column4 = new Column("Column 4", new ArrayList<>());
        List<Column> newColumns = new ArrayList<>();
        newColumns.add(column3);
        newColumns.add(column4);

        Board board = new Board("Board", initialColumns, initialTags);

        board.setColumns(newColumns);

        List<Column> actualColumns = board.getColumns();
        assertEquals(newColumns.size(), actualColumns.size());
        assertTrue(actualColumns.containsAll(newColumns));
    }


    @Test
    void testGetTags() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");

        Board board = new Board("board", new ArrayList<>(), new ArrayList<>());
        board.addTag(tag1);
        board.addTag(tag2);

        List<Tag> tags = board.getTags();

        assertTrue(tags.contains(tag1));
        assertTrue(tags.contains(tag2));
        assertEquals(2, tags.size());
    }

    @Test
    void getId() {
        Board board3 = new Board("board");
        board3.setId(3L);
        assertEquals(3L, board3.getId());
    }

    @Test
    void testGetId() {
        assertEquals(2L, board2.getId());
    }

    @Test
    void testGetId1() {
        board3.setId(2L);
        assertEquals(2L, board3.getId());
    }

    @Test
    void updateTag() {
        Tag updatedTag = new Tag(1, "Updated Tag");
        updatedTag.setFontColor(1.0, 0.0, 0.0);
        updatedTag.setHighlightColor(0.0, 1.0, 0.0);
        int index = 1;
        board.addTag(tag1);
        board.addTag(tag2);
        board.updateTag(index, updatedTag);
        assertEquals(updatedTag, board.getTags().get(index));
    }

    @Test
    public void testRemoveTag(){
        System.out.println(tagList2.toString());
        Tag tagToRemove = tagList2.get(0);
        int initialSize = board.getTags().size();

        board.removeTag(0);

        assertEquals(initialSize - 1, board.getTags().size());
        System.out.println(board.getTags());
        assertFalse(board.getTags().contains(tagToRemove));
    }

    @Test
    public void testSetTitle() {
        String newTitle = "New Title";
        board.setTitle(newTitle);
        assertEquals(newTitle, board.getTitle());
    }

    @Test
    public void testCopyBoard(){
        Board boardSource = new Board();
        boardSource.addColumn(new Column("Column 1", new ArrayList<>()));
        boardSource.addColumn(new Column("Column 2", new ArrayList<>()));
        Board boardTmp = new Board();
        boardTmp = boardTmp.copyBoard(boardSource, boardTmp);
        List<Column> columnsSource = boardSource.getColumns();
        List<Column> columnsTmp = boardTmp.getColumns();
        assertEquals(columnsSource.size(), columnsTmp.size());
        for (int i = 0; i < columnsSource.size(); i++) {
            assertEquals(columnsSource.get(i).getTitle(), columnsTmp.get(i).getTitle());
        }
    }

    @Test
    public void testUpdateColIndex(){
        Board board4 = new Board();
        List<Column> columns;
        columns = new ArrayList<>();
        columns.add(new Column("Column 1", new ArrayList<>()));
        columns.add(new Column("Column 2", new ArrayList<>()));
        columns.add(new Column("Column 3", new ArrayList<>()));
        board4.setColumns(columns);
        board4.updateColIndex(0);
        assertEquals(-2, board4.getColumns().get(0).getIDinBoard());
        assertEquals(-2, board4.getColumns().get(1).getIDinBoard());
        assertEquals(-2, board4.getColumns().get(2).getIDinBoard());
    }

}