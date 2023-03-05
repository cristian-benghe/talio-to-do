package basic_classes;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "card_id", cascade = CascadeType.ALL)
    private List<Task> taskList;

    @OneToMany(mappedBy = "card_id", cascade = CascadeType.ALL)
    private List<Tag> tagList;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private Column column;

    public Card() {
    }

    public Card(String title, String description, List<Task> taskList, List<Tag> tagList, Column column) {
        this.title = title;
        this.description = description;
        this.taskList = taskList;
        this.tagList = tagList;
        this.column = column;
    }

    // getters and setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
