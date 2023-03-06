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
    @JoinColumn( name = "board_id")
    private Board board;

    public Column(){

    }

    public Column(String title, List<Card> cards, Board board) {
        this.title = title;
        this.cards = new ArrayList<>();
        this.board= board;
    }

    //setters and getters
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Card> getCards() { return this.cards; }

    public void setCards(List<Card> cards) { this.cards = cards; }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {this.board = board; }

    //return the hashcode of the Column object
    @Override
    public int hashCode(){
        return Objects.hash(getId(), getTitle(), getCards(), getBoard());
    }
    //equals method for Column Object
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Column) || o == null) return false;
        Column column = (Column) o;
        return (getId() == column.getId() && getTitle().equals(column.getTitle())
                && getBoard().equals(column.getBoard()  ) && getCards().equals(column.getCards()));

    }
    @Override
    public String toString(){
        String cards1 = "";
        for( int i = 0; i<cards.size(); i++) {
            cards1 += cards.get(i).toString();
        }
        return "The Column " + this.title + " has the ID: " + this.id +
                " is part of Board " + this.board.getTitle() + " and contains the following cards: " + cards1;
    }





}
