package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTest {

    Column column;
    Board board;
    List<Card> cards;
    @BeforeEach
    public void setUp() {
        cards = new ArrayList<>();
        Card card = new Card("Project_1", "OOPP", null, null);
        Card card1 = new Card("Project_1", "OOPP", null, null);
        Card card2 = new Card("Project_1", "OOPP", null, null);
        Card card3 = new Card("Project_1", "OOPP", null, null);
        cards.add(card);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        List<Column> columns = new ArrayList<>();
        board = new Board("Project", columns, null);
        column= new Column("Todo", cards);
    }
    @Test
    void getId() {
        column.setId(100);
        assertEquals(100, column.getId());
    }

    @Test
    void getTitle() {
        assertEquals("Todo", column.getTitle());
    }

    @Test
    void getCards() {
        column.setCards(cards);
        assertEquals(cards, column.getCards());
    }

    @Test
    void setId() {
        column.setId(5L);
        assertEquals(column.getId(), 5L);
    }

    @Test
    void setTitle() {
        column.setTitle("Doing");
        assertEquals(column.getTitle(), "Doing");
    }

    @Test
    void setCards() {
        List<Card> newCards = new ArrayList<>();
        column.setCards(newCards);
        assertEquals(column.getCards(), newCards);
    }


    @Test
    void testEquals() {
        Column column1 = new Column("Todo", cards);
        assertEquals(column, column1);
    }

    @Test
    void testEquals2() {
        Column column2 = new Column("Todo", null );
        assertEquals(column, column2);
    }

    @Test
    void testHashCode() {
        Column column1 = new Column("Todo", cards );
        assertEquals(column.hashCode(), column1.hashCode());
    }

    @Test
    void testToString() {
        Column column1 = new Column("Todo", cards );
        assertEquals(column1.toString(), "The Column Todo has the ID: null and contains the following cards: with position: -1");
    }
}