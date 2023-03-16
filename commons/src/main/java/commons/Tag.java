package commons;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tag_id;
    private String title;

    // this joins the Tag and the Card tables together
    @ManyToMany(mappedBy = "tags")
    private Set<Card> cards = new HashSet<>();

    // this joins the Tag and the Board tables together
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;


    /**
     * Default constructor for creating an empty Tag object
     */
    public Tag() {
    }

    /**
     * Constructs a new Tag object with the specified id and title.
     * @param tag_id - the unique identifier for the tag
     * @param title - the name of the tag
     * @param cards - the set of Cards associated with the tag
     */
    public Tag(long tag_id, String title, Set<Card> cards) {
        this.tag_id = tag_id;
        this.title = title;
        this.cards = cards;
    }

    /**
     * gets the value of the tag_id
     *
     * @return the value of the tag_id
     */
    public long getTag_id() {
        return tag_id;
    }

    /**
     * gets the value of the title
     *
     * @return the value of the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * gets the value of the card
     *
     * @return the value of the card
     */
    public Set<Card> getCards() {
        return cards;
    }

    /**
     * Sets the value of tag_id
     *
     * @param tag_id - the value of the tag_id
     */
    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    /**
     * Sets the value of title
     *
     * @param title - the value of the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the value of card
     *
     * @param cards - the set of cards to be set
     */
    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    /**
     * equals method of the class
     *
     * @return true/false if this and o are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return getTag_id() == tag.getTag_id() && getTitle().equals(tag.getTitle()) && getCards().equals(tag.getCards());
    }

    /**
     * hashcode method of the class Tag
     *
     * @return the hashcode of the Tag object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getTag_id(), getTitle(), getCards());
    }

    /**
     * toString method that prints the Tag object in a human-friendly way
     *
     * @return a string which represents the Tag object in a human-friendly way
     */
    @Override
    public String toString() {
        return this.title +
                " has the ID: " + this.tag_id +
                " and is part of: " + this.cards.toString();
    }
}
