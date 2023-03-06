package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    Card card;
    Tag tag;
    @BeforeEach
    public void setUp() {
        card = new Card("Project_1", "OOPP", null, null, null);
        tag = new Tag(100L, "Done", card);
    }
    @Test
    void getTag_id() {
        assertEquals(100, tag.getTag_id());
    }

    @Test
    void getTitle() {
        assertEquals("Done", tag.getTitle());
    }

    @Test
    void getCard() {
        assertEquals(card, tag.getCard());
    }

    @Test
    void setTag_id() {
        tag.setTag_id(5L);
        assertEquals(tag.getTag_id(), 5L);
    }

    @Test
    void setTitle() {
        tag.setTitle("Doing");
        assertEquals(tag.getTitle(), "Doing");
    }

    @Test
    void setCard() {
        Card new_card = new Card("Project", "R&L", null, null, null);
        tag.setCard(new_card);
        assertEquals(tag.getCard(), new_card);
    }

    @Test
    void testEquals() {
        Tag tag2 = new Tag(100L, "Done", card);
        assertEquals(tag, tag2);
    }

    @Test
    void testEquals2() {
        Tag tag2 = new Tag(100L, "Passed", card);
        assertNotEquals(tag, tag2);
    }

    @Test
    void testHashCode() {
        Tag tag1 = new Tag(100L, "Done", card);
        assertEquals(tag.hashCode(), tag1.hashCode());
    }

    @Test
    void testToString() {
        // TODO: write the test properly after the toString method for the Card class has been implemented
        String answer = "Done has the ID: 100 and is part of: " + card.toString();
        assertEquals(tag.toString(), answer);
    }
}