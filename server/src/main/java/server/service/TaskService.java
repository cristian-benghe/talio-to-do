package server.service;

import commons.Card;
import commons.Task;
import org.springframework.stereotype.Service;
import server.database.CardRepository;
import server.database.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {


    private final TaskRepository repo;
    private final CardRepository cardRepo;

    /**
     * A constructor for the TaskService class that assigns a Task Repository
     * @param repo the repository reference for the Task objects
     * @param cardRepo the repository reference for the Card objects
     */
    public TaskService(TaskRepository repo, CardRepository cardRepo){
        this.repo = repo;
        this.cardRepo = cardRepo;
    }


    /**
     * A getter method that gets all the available Task objects
     * @return all the Task objects
     */
    public List<Task> getAll(){return repo.findAll();}

    /**
     * A getter method that returns the Task object, corresponding to
     * the given ID.
     * @param id the ID of the Task object to be found
     * @return the corresponding Task object, if available
     */
    public Optional<Task> getByID(long id){return repo.findById(id);}

    /**
     * Adds a new Task object to the database.
     * @param task the Card object to add
     * @return the saved Task object or null if the title is null or empty
     */
    public Task add(Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return repo.save(task);
    }


    /**
     * Deletes the Task object with the specified id.
     * @param id the identifier of the Task object to be deleted
     * @return returns true if the deletion was successful
     */
    public boolean delete(long id) {
        try {
            repo.deleteById(id);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a new Task instance, and links it to the Card instance corresponding
     * to the given identifier.
     * @param task the new Task instance to be added
     * @param cardId the identifier of the Card
     * @return the newly added task instance
     * @throws IllegalArgumentException The exception is thrown if the task instance
     * is null, or if no Card instance corresponds to the given identifier.
     */
    public Task addTaskToCard(Task task, long cardId) throws IllegalArgumentException{

        if(task == null){
            throw new IllegalArgumentException("The task instance was null.");
        }

        Optional<Card> optCard = cardRepo.findById(cardId);
        if(optCard.isEmpty()){
            throw new IllegalArgumentException("No card with such id exists");
        }

        optCard.get().getTaskList().add(task);

        cardRepo.save(optCard.get());
        return repo.save(task);

    }


    /**
     *  Updates the task instance corresponding to the given ID
     *  in the Task Repository.
     * @param newTask the updated Task instance.
     * @return the updated Task instance
     * @throws IllegalArgumentException if the given id is null, or if the Task instance does not
     * exist, an IllegalArgumentException will be thrown.
     */
    public Task updateTask(Task newTask) throws IllegalArgumentException{

        //Search for the task instance in the repository
        Optional<Task> optTask = repo.findById(newTask.getID());

        //Determine whether the instance exists
        if(optTask.isEmpty()){
            //Otherwise, throw an exception
            throw new IllegalArgumentException("The task does not exists in the DB.");
        }
        //Change the title of the instance
        Task task = optTask.get();
        task.setTitle(newTask.getTitle());
        task.setStatus(newTask.getStatus());

        //Save the instance to the repository
        repo.save(task);

        return task;
    }

    /**
     * Returns the Card instance corresponding to the given identifier
     * @param cardId the identifier of the Card
     * @return the corresponding Card instance
     */
    public Card getCardById(long cardId){
        return cardRepo.getById(cardId);
    }



}
