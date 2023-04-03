package commons;

//import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringStyle.SIMPLE_STYLE;


@Entity
@Table(name = "card")
public class Card implements Serializable {
    private Long columnid = (long)-1;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> taskList;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Tag> tags;


    /**
     * empty constructor
     */
    public Card() {
    }

    /**
     *
     * @param title = title of the card
     * @param description = description of a card for advanced feat
     * @param taskList = a list of tasks for a card, also advanced feat
     * @param tags = list of tags for a card
     */

    public Card(String title, String description,
                List<Task> taskList, Set<Tag> tags) {
        this.title = title;
        this.description = description;
        this.taskList = taskList;
        this.tags = tags;
    }


    // getters and setters

    /**
     * id of the card for the database
     * @return the id of the card
     */

    public Long getId() {
        return id;
    }

    /**
     * to get column id
     * @return columnid
     */
    public Long getColumnId() {
        return columnid;
    }

    /**
     * set column id
     * @param id a value to set columnid
     */
    public void setColumnId(Long id) {
        columnid = id;
    }

    /**
     * set the id of a card
     * @param id = id of the card for the db
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * gets the title of the card
     * @return the title of the card
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the title of the board to a new title
     * @param title = new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the description of the card
     * @return description of the card
     */
    public String getDescription() {
        return description;
    }
    /**
     * sets the description of the card to a new title
     * @param description = new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return the list of tasks
     */
    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * @param taskList the list of tasks in a card
     */
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * @return the list of tags
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * @param tags set the taglist to a new list of tags
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


    /**
     * @param o = object to be compared
     * @return if objects are equal or not
     */

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) &&
                Objects.equals(title, card.title) &&
                Objects.equals(description, card.description) &&
                Objects.equals(taskList, card.taskList) &&
                Objects.equals(tags, card.tags);
    }



    /**
     * hashing function
     * @return a hash code value for the card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, taskList, tags);
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