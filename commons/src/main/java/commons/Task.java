package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Task {


    //Encapsulated Task Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long taskId;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private String title;
    private boolean status;

    /**
     * A default constructor for the Task class.
     */
    public Task(){}

    /**
     * A constructor for the Task class that initializes a newly created instance.
     * @param task  Copies the values of the attributes from
     *              this instance into the newly created instance.
     */
    public Task(Task task) {
        this.taskId = task.getID();
        this.card = task.getCard();
        this.title = task.getTitle();
        this.status = task.isComplete();
    }

    /**
     * A constructor for the task class that initializes a newly created instance
     * @param card  The card that the task corresponds to.
     * @param title The title of the task.
     * @param status    The completion status of the task.
     */
    public Task(Card card, String title, boolean status){
        this.card = card;
        this.title = title;
        this.status = status;
    }

    /**
     * A constructor for the task class that initializes a newly created instance
     * @param taskId The identifier of the task.
     * @param card  The card that the task corresponds to.
     * @param title The title of the task.
     * @param status    The completion status of the task.
     */
    public Task(long taskId, Card card, String title, boolean status){
        this.taskId = taskId;
        this.card = card;
        this.title = title;
        this.status = status;
    }

    /**
     * empty constructor
     */
    public Task() {

    }


    /**
     * A getter for the task_id attribute.
     * @return The identifier of the Task instance.
     */
    public long getID() {
        return taskId;
    }

    /**
     * A setter for the task_id attribute.
     * @param taskId The new task_id of the Task instance
     * @return The new task_id of this instance.
     */
    public long setID(long taskId) {
        this.taskId = taskId;
        return this.taskId;
    }

    /**
     * A getter for the card attribute.
     * @return The Card pointer to which this task instance corresponds.
     */
    public Card getCard() {
        return card;
    }


    /**
     * A getter for the title attribute.
     * @return The title of this task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * A setter for the title attribute.
     * @param title The new title of this Task instance.
     * @return The title of this Task instance.
     */
    public String setTitle(String title) {
        this.title = title;
        return this.title;
    }

    /**
     * A getter for the status attribute
     * @return Returns true if and only if the task is marked complete.
     */
    public boolean isComplete() {
        return status;
    }

    /**
     * Inverts the value of this instance's status attribute.
     * @param status The new status value for the instance.
     * @return The current value of this instance's status attribute.
     */
    public boolean setStatus(boolean status){
        this.status = status;
        return status;
    }


    /**
     * An equals() implementation for the Task class.
     * In order for this method to function as intended,
     * the title and card attributes of this class must not be null.
     * @param o The other object that will be compared to this object.
     * @return Returns true if and only if the attributes of the objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Task task = (Task) o;

        return getID() == task.getID()
                && isComplete() == task.isComplete()
                && getCard().equals(task.getCard())
                && getTitle().equals(task.getTitle());
    }


    /**
     * A hashCode() implementation for the task class.
     * @return the hash code for this instance of the Task class.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getID(), getCard(), getTitle(), isComplete());
    }


    /**
     * A toString implementation in the Task class. This method returns a human-friendly
     * String representation of this Task instance.
     * @return a human-friendly representation of this Task instance.
     */
    @Override
    public String toString() {
        return "Task{" +
                "task_id=" + taskId +
                ", card_id=" + card.getId() +
                ", title='" + title + '\'' +
                ", isComplete=" + status +
                '}';
    }
}
