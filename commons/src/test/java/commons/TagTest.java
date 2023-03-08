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
        card = new Card("Project_1", "OOPP", null, null, null);
        cards = new HashSet<>();
        cards.add(card);
        tag = new Tag(100L, "Done", cards);
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
        assertEquals(cards, tag.getCards());
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
    void setCards() {
        Card new_card = new Card("Project", "R&L", null, null, null);
        Set<Card> newSet = new HashSet<>();
        newSet.add(card);
        tag.setCards(newSet);
        assertEquals(tag.getCards(), newSet);
    }

    @Test
    void testEquals() {
        Tag tag2 = new Tag(100L, "Done", cards);
        assertEquals(tag, tag2);
    }

    @Test
    void testEquals2() {
        Tag tag2 = new Tag(100L, "Passed", cards);
        assertNotEquals(tag, tag2);
    }

    @Test
    void testHashCode() {
        Tag tag1 = new Tag(100L, "Done", cards);
        assertEquals(tag.hashCode(), tag1.hashCode());
    }

    @Test
    void testToString() {
        // TODO: write the test properly after the toString method for the Card class has been implemented
        String answer = "Done has the ID: 100 and is part of: " + cards.toString();
        assertEquals(tag.toString(), answer);
    }
}