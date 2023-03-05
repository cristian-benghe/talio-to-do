package commons;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Column> columns = new ArrayList<>();

    public Board(String title, List<Column> columns) {
        this.title = title;
        this.columns = columns;
    }

    public Board(){}

    public String getTitle() {
        return title;
    }

    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        if (getTitle() != null ? !getTitle().equals(board.getTitle()) : board.getTitle() != null) return false;
        if (getColumns() != null ? !getColumns().equals(board.getColumns()) : board.getColumns() != null) return false;
        return getColumns() != null ? getColumns().equals(board.getColumns()) : board.getColumns() == null;
    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getColumns() != null ? getColumns().hashCode() : 0);
        result = 31 * result + (getColumns() != null ? getColumns().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Board{" +
                "title='" + title + '\'' +
                ", columns=" + columns +
                ", columns=" + columns +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
