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
        Card c1 = new Card("Test Card",null,null,null);
        c1.setId(1L);
        assertEquals(c1.getId(),1);
    }

    @Test
    void getTitle() {
        Card c1 = new Card("Test Card",null,null,null);
        assertEquals(c1.getTitle(),"Test Card");
    }

    @Test
    void setTitle() {
        Card c1 = new Card("Test Card",null,null,null);
        c1.setTitle("New Title");
        assertEquals(c1.getTitle(),"New Title");
    }

    @Test
    void getDescription() {
        Card c1 = new Card("Test Card", "desc", null, null);
        assertEquals(c1.getDescription(), "desc");
    }

    @Test
    void setDescription() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setDescription("desc");
        assertEquals(c1.getDescription(),"desc");
    }

    @Test
    void getTaskList() {
        Card card = new Card("Test Card", "description", null, null);
        assertNull(card.getTaskList());
    }

    @Test
    void getTagList() {
        Card card = new Card("Test Card", "description", null, null);
        assertNull(card.getTags());
    }

    @Test
    void testEquals() {
        Card c1 = new Card("Test Card", null, null, null);
        Card c2 = new Card("Test Card", null, null, null);
        assertEquals(c1, c2);
    }

    @Test
    void testHashCode() {
        Card c1 = new Card("Test Card", null, null, null);
        Card c2 = new Card("Test Card", null, null, null);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testSetTagList() {
        Card c = new Card("Test Card", "basic description", null, null);
        Set<Tag> tags = new HashSet<>();
        Set<Card> cards = new HashSet<>();
        cards.add(c);
        Tag t1 = new Tag(0L, "tag1", cards);
        tags.add(t1);
        c.setTags(tags);
        assertEquals(tags, c.getTags());
    }

    @Test
    void testSetTaskList() {
        Card c = new Card("Test Card", "basic description", null, null);
        List<Task> list = new ArrayList<>();
        Task t1 = new Task(0, "task1", false);
        list.add(t1);
        c.setTaskList(list);
        assertEquals(list, c.getTaskList());
    }

    @Test
    void getColumnId() {
        Card c = new Card("Test Card", "basic description", null, null);
        c.setColumnId(0L);
        assertEquals(c.getColumnId(), 0L);
    }

    @Test
    void setColumnId() {
        Card c = new Card("Test Card", "basic description", null, null);
        c.setColumnId(0L);
        assertEquals(c.getColumnId(), 0L);
    }

    @Test
    void getTags() {
        Card c = new Card("Test Card", "basic description", null, null);
        Tag tag=new Tag();
        Set<Tag>list=new HashSet<>();
        list.add(tag);
        c.setTags(list);
        assertEquals(list, c.getTags());
    }

    @Test
    void setTags() {
        Card c = new Card("Test Card", "basic description", null, null);
        Tag tag=new Tag();
        Set<Tag>list=new HashSet<>();
        list.add(tag);
        c.setTags(list);
        assertEquals(list, c.getTags());
    }

    @Test
    void testToString() {
        Card c = new Card("Test Card", "basic description", null, null);
        assertEquals(c.toString(), "-1,basic description,<null>,<null>,<null>,Test Card");
    }
}