package commons;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringStyle.SIMPLE_STYLE;


@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Task> taskList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "card_tag",
            joinColumns = { @JoinColumn(name = "card_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "column_id")
    private Column column;

    public Card() {
    }

    public Card(String title, String description, List<Task> taskList, Set<Tag> tags, Column column) {
        this.title = title;
        this.description = description;
        this.taskList = taskList;
        this.tags = tags;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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
        return Objects.equals(id, card.id) && Objects.equals(title, card.title) &&
                Objects.equals(description, card.description) && Objects.equals(taskList, card.taskList) &&
                Objects.equals(tags, card.tags) && Objects.equals(column, card.column);
    }

    /**
     * hashing function
     * @return a hash code value for the card
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * human-friendly toString method, useful for debugging
     * @return a string representation of the card in a human-friendly format
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SIMPLE_STYLE);
    }
}
