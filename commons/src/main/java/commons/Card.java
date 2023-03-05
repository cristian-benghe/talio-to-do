package commons;

import javax.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) && Objects.equals(title, card.title) && Objects.equals(description, card.description) && Objects.equals(taskList, card.taskList) && Objects.equals(tagList, card.tagList) && Objects.equals(column, card.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, taskList, tagList, column);
    }
}
