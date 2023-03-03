package basic_classes;


import java.util.List;

public class Board {
    private String title;
    private List<commons.src.basic_classes.Column>columns;

    public Board(String title, List<commons.src.basic_classes.Column> columns) {
        this.title = title;
        this.columns = columns;
    }
}
