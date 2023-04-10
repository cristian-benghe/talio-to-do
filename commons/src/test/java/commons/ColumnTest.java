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
        Column column2 = new Column("Todo", null);
        assertEquals(new Column("Todo", null), column2);
    }

    @Test
    void testHashCode() {
        Column column1 = new Column("Todo", cards );
        assertEquals(column.hashCode(), column1.hashCode());
    }

    @Test
    void testToString() {
        Column column1 = new Column("Todo", cards );
        assertEquals(column1.toString(), "The Column Todo has the ID: null and contains the following cards: 1.0,-1,OOPP,1.0,<null>,1.0,<null>,<null>,Project_11.0,-1,OOPP,1.0,<null>,1.0,<null>,<null>,Project_11.0,-1,OOPP,1.0,<null>,1.0,<null>,<null>,Project_11.0,-1,OOPP,1.0,<null>,1.0,<null>,<null>,Project_1with position: -1");
    }

    @Test
    public void testGetIDinBoard(){
        Column column1 = new Column("Todo", cards );
        column1.setIDinBoard(2);
        assertEquals(2, column1.getIDinBoard());
    }

    @Test
    public void testGetID(){
        Column column1 = new Column();
        column1.setId(2L);
        assertEquals(2, column1.getId());
    }

    @Test
    public void testCopyCards(){
        Card kard = new Card("Project_1", "OOPP", null, null);
        Card kard1 = new Card("Project_1", "OOPP", null, null);
        Card kard2 = new Card("Project_1", "OOPP", null, null);
        Card kard3 = new Card("Project_1", "OOPP", null, null);
        List cards2 = new ArrayList<Card>();
        cards2.add(kard);
        cards2.add(kard1);
        cards2.add(kard2);
        cards2.add(kard3);
        Column column3 = new Column("Todo", cards2);
        Column column1 = new Column();
        column1.setCards(cards2);
        column1.copyCards(column1, column3);
        assertEquals(column3.getCards().get(0), column1.getCards().get(0));
        assertEquals(column3.getCards().get(1), column1.getCards().get(1));
        assertEquals(column3.getCards().get(2), column1.getCards().get(2));
    }

    @Test
    public void testUpdateColorInCard(){
        Card kard = new Card("Project_1", "OOPP", null, null);
//        kard.setColor(100.0, 150.0, 31.0);
        Card kard1 = new Card("Project_1", "OOPP", null, null);
        Card kard2 = new Card("Project_1", "OOPP", null, null);
        Card kard3 = new Card("Project_1", "OOPP", null, null);
        List cards2 = new ArrayList<Card>();
        cards2.add(kard);
        cards2.add(kard1);
        cards2.add(kard2);
        cards2.add(kard3);
        Column column3 = new Column("Todo", cards2);
        column3.updateColorInCard(0, 200.0, 105.0, 54.0);
        assertNotEquals(100.0, column3.getCards().get(0).getBlue());
        assertEquals(105.0, column3.getCards().get(0).getBlue());
        assertNotEquals(150.0, column3.getCards().get(0).getGreen());
        assertEquals(54.0, column3.getCards().get(0).getGreen());
        assertNotEquals(31.0, column3.getCards().get(0).getRed());
        assertEquals(200.0, column3.getCards().get(0).getRed());
    }
}