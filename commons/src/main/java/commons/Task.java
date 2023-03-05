package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Task {


    //Encapsulated Task Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long task_id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private String title;
    private boolean status;


    /**
     * A constructor for the Task class that initializes a newly created instance.
     * @param task  Copies the values of the attributes from this instance into the newly created instance.
     */
    public Task(Task task) {
        this.task_id = task.getTask_id();
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
     * @param task_id The identifier of the task.
     * @param card  The card that the task corresponds to.
     * @param title The title of the task.
     * @param status    The completion status of the task.
     */
    public Task(long task_id, Card card, String title, boolean status){
        this.task_id = task_id;
        this.card = card;
        this.title = title;
        this.status = status;
    }


    /**
     * A getter for the task_id attribute.
     * @return The identifier of the Task instance.
     */
    public long getTask_id() {
        return task_id;
    }

    /**
     * A setter for the task_id attribute.
     * @param task_id The new task_id of the Task instance
     * @return The new task_id of this instance.
     */
    public long setTask_id(long task_id) {
        this.task_id = task_id;
        return this.task_id;
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
     * @return The current value of this instance's status attribute.
     */
    public boolean changeCompleteState(){
        this.status = !this.status;
        return this.status;
    }


   
}
