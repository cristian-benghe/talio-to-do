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
    public void testDefaultConstructor() {
        // Create a tag object using the default constructor
        Tag tag = new Tag();

        // Check that the properties of the object have their default values
        assertEquals("New Tag", tag.getTitle());
        assertEquals(0.0, tag.getFontRed(), 0.01);
        assertEquals(0.0, tag.getFontGreen(), 0.01);
        assertEquals(0.0, tag.getFontBlue(), 0.01);
        assertEquals(1.0, tag.getHighlightRed(), 0.01);
        assertEquals(1.0, tag.getHighlightGreen(), 0.01);
        assertEquals(1.0, tag.getHighlightBlue(), 0.01);
    }

    @Test
    public void testConstructorWithTitle() {
        // Create a tag object with a specific title
        Tag tag = new Tag("Test Tag");

        // Check that the title property of the object has the expected value
        assertEquals("Test Tag", tag.getTitle());

        // Check that the fontColor and highlightColor properties of the object have their default values
        assertEquals(0.0, tag.getFontRed(), 0.01);
        assertEquals(0.0, tag.getFontGreen(), 0.01);
        assertEquals(0.0, tag.getFontBlue(), 0.01);
        assertEquals(1.0, tag.getHighlightRed(), 0.01);
        assertEquals(1.0, tag.getHighlightGreen(), 0.01);
        assertEquals(1.0, tag.getHighlightBlue(), 0.01);
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
        assertEquals(answer, tag.toString());
    }

    @Test
    void getHighlightBlue() {
        tag.setHighlightColor(2.0,2.0,2.0);
        assertEquals(2.0,tag.getHighlightBlue());
    }

    @Test
    void getHighlightRed() {
        tag.setHighlightColor(2.0,2.0,2.0);
        assertEquals(2.0,tag.getHighlightRed());
    }

    @Test
    void getHighlightGreen() {
        tag.setHighlightColor(2.0,2.0,2.0);
        assertEquals(2.0,tag.getHighlightGreen());
    }

    @Test
    void getFontBlue() {
        tag.setFontColor(2.0,2.0,2.0);
        assertEquals(2.0,tag.getFontBlue());
    }

    @Test
    void getFontGreen() {
        tag.setFontColor(2.0,2.0,2.0);
        assertEquals(2.0,tag.getFontGreen());
    }

    @Test
    void getFontRed() {
        tag.setFontColor(2.0,2.0,2.0);
        assertEquals(2.0,tag.getFontRed());
    }

    @Test
    void getIDinBoard() {
        tag.setIDinBoard(22);
        assertEquals(22,tag.getIDinBoard());
    }

    @Test
    void setIDinBoard() {
        tag.setIDinBoard(22);
        assertEquals(22,tag.getIDinBoard());

    }
}