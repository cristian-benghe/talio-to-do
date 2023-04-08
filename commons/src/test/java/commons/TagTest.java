package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TagTest {

    Card card;
    Set<Card> cards;
    Tag tag;
    @BeforeEach
    public void setUp() {
        tag = new Tag(100L, "Done");
    }
    @Test
    void getTag_id() {
        assertEquals(100, tag.getTagID());
    }

    @Test
    void getTitle() {
        assertEquals("Done", tag.getTitle());
    }



    @Test
    void setTag_id() {
        tag.setTagID(5L);
        assertEquals(tag.getTagID(), 5L);
    }

    @Test
    void setTitle() {
        tag.setTitle("Doing");
        assertEquals(tag.getTitle(), "Doing");
    }



    @Test
    void testEquals() {
        Tag tag2 = new Tag(100L, "Done");
        assertEquals(tag, tag2);
    }

    @Test
    void testEquals2() {
        Tag tag2 = new Tag(100L, "Passed");
        assertNotEquals(tag, tag2);
    }

    @Test
    void testHashCode() {
        Tag tag1 = new Tag(100L, "Done");
        assertEquals(tag.hashCode(), tag1.hashCode());
    }

    @Test
    void testToString() {
        // TODO: write the test properly after the toString method for the Card class has been implemented
        String answer = "Done has the ID: 100";
        assertEquals(tag.toString(), answer);
    }
}