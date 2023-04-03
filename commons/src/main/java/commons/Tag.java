package commons;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag {

    private int idInBoard=-1;
    private Double fontRed;
    private Double fontGreen;
    private Double fontBlue;

    public Double getHighlightBlue() {
        return highlightBlue;
    }

    public Double getHighlightRed() {
        return highlightRed;
    }

    public Double getHighlightGreen() {
        return highlightGreen;
    }

    private Double highlightBlue;
    private Double highlightRed;
    private Double highlightGreen;

    public Double getFontBlue() {
        return fontBlue;
    }

    public Double getFontGreen() {
        return fontGreen;
    }

    public Double getFontRed() {
        return fontRed;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tagID;
    private String title;

    // this joins the Tag and the Card tables together
    @ManyToMany(mappedBy = "tags")
    private Set<Card> cards = new HashSet<>();

    // this joins the Tag and the Board tables together
//    @ManyToOne
//    @JoinColumn(name = "board_id")
//    private Board board;


    /**
     * Default constructor for creating an empty Tag object
     */
    public Tag() {
        this.setFontColor(0.0, 0.0, 0.0);
        this.setHighlightColor(1.0, 1.0, 1.0);
    }

    /**
     * Constructs a new Tag object with the specified id and title.
     * @param tagID - the unique identifier for the tag
     * @param title - the name of the tag
     * @param cards - the set of Cards associated with the tag
     */
    public Tag(long tagID, String title, Set<Card> cards){
        this.tagID = tagID;
        this.title = title;
        this.cards = cards;
        this.setFontColor(0.0, 0.0, 0.0);
        this.setHighlightColor(1.0, 1.0, 1.0);
    }
    

    /**
     * @param title of the tag
     * @param cards that are associated to a tag
     */
    public Tag(String title, Set<Card>cards){
        this.title=title;
        this.cards=cards;
        this.setFontColor(0.0, 0.0, 0.0);
        this.setHighlightColor(1.0, 1.0, 1.0);
    }

    public void addCard(Card newCard) {
        cards.add(newCard);
    }

    /**
     * @return id of a tag in a board
     */
    public int getIDinBoard() {
        return idInBoard;
    }

    /**
     * @param idInBoard = set the boardid when adding a tag
     */
    public void setIDinBoard(int idInBoard) {
        this.idInBoard = idInBoard;
    }

    /**
     * gets the value of the tagID
     *
     * @return the value of the tagID
     */
    public long getTagID() {
        return tagID;
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
     * Sets the value of tagID
     *
     * @param tagID - the value of the tagID
     */
    public void setTagID(long tagID) {
        this.tagID = tagID;
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
        return getTagID() == tag.getTagID() && getTitle().equals(tag.getTitle()) &&
                getCards().equals(tag.getCards());
    }

    /**
     * hashcode method of the class Tag
     *
     * @return the hashcode of the Tag object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getTagID(), getTitle(), getCards());
    }

    /**
     * toString method that prints the Tag object in a human-friendly way
     *
     * @return a string which represents the Tag object in a human-friendly way
     */
    @Override
    public String toString() {
        return this.title +
                " has the ID: " + this.tagID +
                " and is part of: " + this.cards.toString();
    }
    public void setFontColor(Double red, Double green, Double blue){
        this.fontRed=red;
        this.fontGreen=green;
        this.fontBlue=blue;
    }
    public void setHighlightColor(Double highlightBlue, Double highlightGreen, Double highlightRed){
        this.highlightBlue=highlightBlue;
        this.highlightGreen=highlightGreen;
        this.highlightRed=highlightRed;
    }
}
