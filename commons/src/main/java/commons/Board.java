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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();
    public Board(String title, List<Column> columns, List<Tag>tags) {
        this.title = title;
        this.columns = columns;
        this.tags=tags;
    }

    public Board(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        if (getId() != null ? !getId().equals(board.getId()) : board.getId() != null) return false;
        if (getTitle() != null ? !getTitle().equals(board.getTitle()) : board.getTitle() != null) return false;
        if (getColumns() != null ? !getColumns().equals(board.getColumns()) : board.getColumns() != null) return false;
        return getTags() != null ? getTags().equals(board.getTags()) : board.getTags() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getColumns() != null ? getColumns().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        return result;
    }
}