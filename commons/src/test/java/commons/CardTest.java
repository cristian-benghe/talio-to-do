package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void getId() {
        Card c1 = new Card("Test Card",null,null,null,null);
        c1.setId(1L);
        assertEquals(c1.getId(),1);
    }

    @Test
    void getTitle() {
        Card c1 = new Card("Test Card",null,null,null,null);
        assertEquals(c1.getTitle(),"Test Card");
    }

    @Test
    void setTitle() {
        Card c1 = new Card("Test Card",null,null,null,null);
        c1.setTitle("New Title");
        assertEquals(c1.getTitle(),"New Title");
    }

    @Test
    void getDescription() {
        Card c1 = new Card("Test Card","desc",null,null,null);
        assertEquals(c1.getDescription(),"desc");
    }

    @Test
    void setDescription() {
        Card c1 = new Card("Test Card",null,null,null,null);
        c1.setDescription("desc");
        assertEquals(c1.getDescription(),"desc");
    }

    @Test
    void getTaskList() {
        Card card = new Card("Test Card","description",null,null,null);
        assertEquals(card.getTaskList(),null);
    }

    @Test
    void setTaskList() {
        Card card = new Card("Test Card","description",null,null,null);
    }

    @Test
    void getTagList() {
        Card card = new Card("Test Card","description",null,null,null);
        assertEquals(card.getTagList(),null);
    }

    @Test
    void setTagList() {
        Card card = new Card("Test Card","description",null,null,null);
    }

    @Test
    void getColumn() {
        Card card = new Card("Test Card","description",null,null,null);
        assertEquals(card.getColumn(),null);
    }

    @Test
    void setColumn() {
        Card card = new Card("Test Card","description",null,null,null);
    }

    @Test
    void testEquals() {
        Card c1 = new Card("Test Card",null,null,null,null);
        Card c2 = new Card("Test Card",null,null,null,null);
        assertEquals(c1,c2);
    }

    @Test
    void testHashCode() {
        Card c1 = new Card("Test Card",null,null,null,null);
        Card c2 = new Card("Test Card",null,null,null,null);
        assertEquals(c1.hashCode(),c2.hashCode());
    }
}