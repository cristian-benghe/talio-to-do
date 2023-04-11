package commons;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

@Entity
public class Column implements Serializable {
    private int idInBoard=-1;
    private Double red=1.0;
    private Double green=1.0;
    private Double blue=1.0;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    /**
     * @return id of a column in a board
     */
    public int getIDinBoard() {
        return idInBoard;
    }

    /**
     * @param idInBoard = set the boardid when adding a column
     */
    public void setIDinBoard(int idInBoard) {
        this.idInBoard = idInBoard;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<Card> cards= new ArrayList<>();

    /**
     * a default constructor for the Column class.
     */
    public Column(){

    }

    /**
     * a constructor for the Column class that initializes the title, cards,
     * and board attributes of the new instance
     * @param title the title of the new Column
     * @param cards the list of cards that are contained in the new Column
     */
    public Column(String title, List<Card> cards) {
        this.title = title;
        this.cards = cards;
    }

    //setters and getters

    /**
     * a getter for the ID attribute of the Column
     * @return the ID of this Column instance.
     */
    public Long getId() {
        return id;
    }

    /**
     * a setter for the ID attribute of the Column
     * @param id the new ID for this Column instance
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * a getter for the title attribute of the Column
     * @return the title of this Column instance
     */
    public String getTitle() {
        return title;
    }

    /**
     * a setter for the title attribute of the Column
     * @param title the new title for this Column instance
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * a getter for the cards list attribute of the Column
     * @return the cards list of this Column instance
     */
    public List<Card> getCards() { return this.cards; }

    /**
     * a setter for the cards list attribute of the Column
     * @param cards the new cards list for this Column instance
     */
    public void setCards(List<Card> cards) { this.cards = cards; }

    /**
     * A copy method that is needed for the card arrangement in the specific column.
     * @param column the source column that we will get the cards from
     * @param column2 the new column that the old column will be copied
     * @return the copied column
     */
    public Column copyCards(Column column, Column column2)
    {
        column2.cards = new ArrayList<>(column.getCards());
        return column2;
    }

    /**
     * A HashCode implementation for this Column class. If two Column instances are equivalent,
     * then there hash codes should also be equivalent
     * @return a hash code that corresponds to this Column instance
     */
    @Override
    public int hashCode(){
        return Objects.hash(getId(), getTitle(), getCards());
    }

    /**
     * An equals method implementation for the Column class. Two Column instances are equivalent
     * if and only if their attributes are equivalent.
     * @param o the other object to be compared with this instance
     * @return returns true if and only if the two objects are equivalent
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Column)) return false;

        Column column = (Column) o;

        if (idInBoard != column.idInBoard) return false;
        if (getId() != null ? !getId().equals(column.getId())
                : column.getId() != null) return false;
        if (getTitle()!=null?!getTitle().equals(column.getTitle()):column.getTitle()!=null)return false;
        return getCards()!=null?getCards().equals(column.getCards()):column.getCards()==null;
    }

    /**
     * A toString implementation for the Column class. Returns a human-friendly String
     * representation of this Column instance.
     * @return a human-friendly String representation of this Column instance.
     */
    @Override
    public String toString(){
        String cards1 = "";
        for( int i = 0; i<cards.size(); i++) { cards1 += cards.get(i).toString();}
        return "The Column "
                + this.title
                + " has the ID: "
                + this.id
                + " and contains the following cards: "
                + cards1
                + "with position: "
                + idInBoard;
    }


    /**
     * @param i index of the card
     * @param red value in the rgb
     * @param blue value in the rgb
     * @param green value in the rgb
     */
    public void updateColorInCard(int i, Double red, Double blue, Double green) {
        Card card=cards.get(i);
        card.setColor(blue, green, red);
        cards.set(i, card);
    }

    /**
     * @param red rgb value of red
     * @param green rgb value of green
     * @param blue rgb value of blue
     */
    public void updateColors(double red, double green, double blue) {
        this.red=red;
        this.green=green;
        this.blue=blue;
    }

    /**
     * @return rgb value of green
     */
    public Double getGreen() {
        return green;
    }

    /**
     * @return rgb value of blue
     */
    public Double getBlue() {
        return blue;
    }

    /**
     * @return rgb value of red
     */
    public double getRed() {
        return red;
    }
}