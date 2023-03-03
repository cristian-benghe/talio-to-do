import java.util.List;

public class Board {
    private String title;
    private List<Column>columns;

    public Board(String title, List<Column> columns) {
        this.title = title;
        this.columns = columns;
    }
}
