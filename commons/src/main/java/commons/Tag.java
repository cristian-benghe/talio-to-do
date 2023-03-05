package commons;

import javax.persistence.*;

public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tag_id;
    private String title;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;


    /**
     * Constructs a new Tag object with the specified id and title.
     * @param id - the unique identifier for the tag
     * @param title - the name of the tag
     * @param card - the Card object associated with the tag
     */
    public Tag(long id, String title, Card card) {
        this.tag_id = id;
        this.title = title;
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
    public Card getCard() {
        return card;
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
     * @param card - the value of the card
     */
    public void setCard(Card card) {
        this.card = card;
    }
}
