package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        Card c1 = new Card("Test Card", "desc", null, null, null);
        assertEquals(c1.getDescription(), "desc");
    }

    @Test
    void setDescription() {
        Card c1 = new Card("Test Card", null, null, null, null);
        c1.setDescription("desc");
        assertEquals(c1.getDescription(),"desc");
    }

    @Test
    void getTaskList() {
        Card card = new Card("Test Card", "description", null, null, null);
        assertNull(card.getTaskList());
    }

    @Test
    void getTagList() {
        Card card = new Card("Test Card", "description", null, null, null);
        assertNull(card.getTags());
    }

    @Test
    void getColumn() {
        Card card = new Card("Test Card", "description", null, null, null);
        assertNull(card.getColumn());
    }

    @Test
    void testToString() {
        Card c1 = new Card("Test Card", "basic description", null, null, null);
        assertEquals("<null>,basic description,<null>,<null>,<null>,Test Card", c1.toString());
    }

    @Test
    void testEquals() {
        Card c1 = new Card("Test Card", null, null, null, null);
        Card c2 = new Card("Test Card", null, null, null, null);
        assertEquals(c1, c2);
    }

    @Test
    void testHashCode() {
        Card c1 = new Card("Test Card", null, null, null, null);
        Card c2 = new Card("Test Card", null, null, null, null);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testSetTagList() {
        Card c = new Card("Test Card", "basic description", null, null, null);
        Set<Tag> tags = new HashSet<>();
        Set<Card> cards = new HashSet<>();
        cards.add(c);
        Tag t1 = new Tag(0L, "tag1", cards);
        tags.add(t1);
        c.setTags(tags);
        assertEquals(tags, c.getTags());
    }

    @Test
    void testSetColumn() {
        Card c = new Card("Test Card", "basic description", null, null, null);
        Column col = new Column("column1", null);
        c.setColumn(col);
        assertEquals(col, c.getColumn());
    }

    @Test
    void testSetTaskList() {
        Card c = new Card("Test Card", "basic description", null, null, null);
        List<Task> list = new ArrayList<>();
        Task t1 = new Task(c, "task1", false);
        list.add(t1);
        c.setTaskList(list);
        assertEquals(list, c.getTaskList());
    }
}