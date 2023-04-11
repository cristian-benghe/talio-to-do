package commons;

import org.junit.jupiter.api.Tags;
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
        Tag t1 = new Tag(0L, "tag1");
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
    void addTag() {
        Card c1 = new Card("Test Card", null, null, null);
        Tag tag = new Tag();
        HashSet<Tag> tmp = new HashSet<>();
        c1.setTags(tmp);
        tmp.add(tag);
        c1.addTag(tag);
        assertEquals(true,c1.getTags().equals(tmp));
    }

    @Test
    void testGetId() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setId((long)22);
        assertEquals(22,c1.getId());
    }

    @Test
    void testGetColumnId() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setColumnId((long)22);
        assertEquals(22,c1.getColumnId());

    }

    @Test
    void testSetColumnId() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setColumnId((long)22);
        assertEquals(22,c1.getColumnId());
    }

    @Test
    void setId() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setId((long)33);
        assertEquals(33,c1.getId());
    }

    @Test
    void testGetTitle() {
        Card c1 = new Card("Test Card", null, null, null);
        String tmp = "Test Card";
        assertEquals(true,c1.getTitle().equals(tmp));
    }

    @Test
    void testSetTitle() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setTitle("Test Card2");
        String tmp = "Test Card2";
        assertEquals(true,c1.getTitle().equals(tmp));
    }

    @Test
    void testGetDescription() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setDescription("DD");
        assertEquals(true,c1.getDescription().equals("DD"));

    }

    @Test
    void testSetDescription() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setDescription("DD");
        assertEquals(true,c1.getDescription().equals("DD"));
    }

    @Test
    void testGetTags() {
        Card c1 = new Card("Test Card", null, null, null);
        HashSet<Tag> tmp = new HashSet<>();
        Tag tag = new Tag();
        tmp.add(tag);
        c1.setTags(tmp);
        assertEquals(true,c1.getTags().equals(tmp));
    }

    @Test
    void testSetTags() {
        Card c1 = new Card("Test Card", null, null, null);
        HashSet<Tag> tmp = new HashSet<>();
        Tag tag = new Tag();
        tmp.add(tag);
        c1.setTags(tmp);
        assertEquals(true,c1.getTags().equals(tmp));
    }

    @Test
    void getGreen() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setColor(2.0,2.0,2.0);
        assertEquals(2.0,c1.getGreen());
    }

    @Test
    void getBlue() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setColor(2.0,2.0,2.0);
        assertEquals(2.0,c1.getBlue());
    }

    @Test
    void hasTagWithId() {
        Card c1 = new Card("Test Card", null, null, null);
        // Create a list of tags
        HashSet<Tag> tags = new HashSet<>();
        tags.add(new Tag(1L, "Tag1"));
        tags.add(new Tag(2L, "Tag2"));
        tags.add(new Tag(3L, "Tag3"));

        // Create a new object of class that contains the `hasTagWithId()` method and pass the list of tags
        c1.setTags(tags);

        // Test for the presence of a tag with a specific ID
        assertEquals(true,c1.hasTagWithId(2L));
        assertEquals(false,c1.hasTagWithId(4L));
    }

    @Test
    void testToString() {
        // Create a new object to test the toString() method
        Card c1 = new Card("Test Card", null, null, null);
        Tag tag = new Tag(1L, "Test Tag");

        // Check that the output of the toString() method matches the expected format
        String expectedOutput = "1.0,-1,<null>,1.0,<null>,1.0,<null>,<null>,Test Card";
        assertEquals(expectedOutput, c1.toString());
    }

    @Test
    void getRed() {
        Card c1 = new Card("Test Card", null, null, null);
        c1.setColor(2.0,2.0,2.0);
        assertEquals(2.0,c1.getRed());
    }
//
//    @Test
//    void testToString() {
//        Card c = new Card("Test Card", "basic description", null, null);
//        assertEquals(c.toString(), "-1,basic description,<null>,<null>,<null>,Test Card");
//    }
}