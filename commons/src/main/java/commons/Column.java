package commons;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

@Entity
public class Column {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL)
    private List<Card> cards= new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

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
     * @param board the board instance to which the new Column belongs to
     */
    public Column(String title, List<Card> cards, Board board) {
        this.title = title;
        this.cards = new ArrayList<>();
        this.board= board;
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
     * a getter for the board attribute of the Column
     * @return the board instance that contains this Column instance
     */
    public Board getBoard() {
        return board;
    }

    /**
     * a setter for the board attribute of the Column
     * @param board the new Board instance that contains this Column instance
     */
    public void setBoard(Board board) {this.board = board; }

    /**
     * A HashCode implementation for this Column class. If two Column instances are equivalent,
     * then there hash codes should also be equivalent
     * @return a hash code that corresponds to this Column instance
     */
    @Override
    public int hashCode(){
        return Objects.hash(getId(), getTitle(), getCards(), getBoard());
    }

    /**
     * An equals method implementation for the Column class. Two Column instances are equivalent
     * if and only if their attributes are equivalent.
     * @param o the other object to be compared with this instance
     * @return returns true if and only if the two objects are equivalent
     */
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Column) || o == null) return false;
        Column column = (Column) o;
        return (getId() == column.getId() && getTitle().equals(column.getTitle())
                && getBoard().equals(column.getBoard()  ) && getCards().equals(column.getCards()));

    }

    /**
     * A toString implementation for the Column class. Returns a human-friendly String
     * representation of this Column instance.
     * @return a human-friendly String representation of this Column instance.
     */
    @Override
    public String toString(){
        String cards1 = "";
        for( int i = 0; i<cards.size(); i++) {
            cards1 += cards.get(i).toString();
        }
        return "The Column "
                + this.title
                + " has the ID: "
                + this.id
                + " is part of Board "
                + this.board.getTitle()
                + " and contains the following cards: "
                + cards1;
    }





}
