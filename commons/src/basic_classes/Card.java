package basic_classes;


import java.util.ArrayList;
import java.util.List;

public class Card {
    private String title;
    private String description;
    private List<Task> taskList;
    private List<Tag> tagList;

    public Card(String title, String description) {
        this.title=title;
        this.description = description;
        this.taskList = new ArrayList<>();
        this.tagList = new ArrayList<>();
    }
}
