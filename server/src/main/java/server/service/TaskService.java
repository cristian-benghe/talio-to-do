package server.service;

import commons.Task;
import org.springframework.stereotype.Service;
import server.database.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {


    private final TaskRepository repo;


    /**
     * A constructor for the TaskService class that assigns a Task Repository
     * @param repo the repository reference for the Task objects
     */
    public TaskService(TaskRepository repo){
        this.repo = repo;
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
        if (isNullOrEmpty(task.getTitle())) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return repo.save(task);
    }


    /**
     * Deletes the Task object with the specified id.
     * @param id the identifier of the Task object to be deleted
     */
    public boolean delete(long id) {
        try {
            repo.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }


    /**
     * This method verifies if the given string is null or not
     * @param s - a string
     * @return true/false if the string is empty or not
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     *  Updates the task instance corresponding to the given ID
     *  in the Task Repository.
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



}
