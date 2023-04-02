package server.api;


import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = {"/api/tasks", "/api/tasks/"})
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructs a new TaskController with the
     * TaskService service instance
     * @param taskService the service instance for the Task repository
     */
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }


    /**
     * A GET request for all the available Task objects in the repository
     * @return all the available Task objects
     */
    @GetMapping(path={"getTask/all", "getTask/all/"})
    public ResponseEntity<List<Task>> getAll(){
        List<Task> task = taskService.getAll();
        return ResponseEntity.ok(task);
    }

    /**
     * A GET request for the Task object corresponding to the
     * given ID
     * @param id the ID of the Task
     * @return the corresponding Task object, or a BAD REQUEST
     * if the object does not exist.
     */
    @GetMapping(path={"getTask/byID", "getTask/byID/"})
    public ResponseEntity<Task> getByID(@RequestParam("id")long id){
        Optional<Task> task = taskService.getByID(id);
        if(task.isPresent()){
            return ResponseEntity.ok(task.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * A POST request for adding a new Task object
     * @param task the new Task object to be added
     * @return Status 200 message
     */
    @PostMapping(path={"addTask","addTask/"})
    public ResponseEntity<Task> add(@RequestBody Task task){
        Task addedTask = taskService.add(task);
        return ResponseEntity.ok(addedTask);
    }


    /**
     * A DELETE request for deleting a Task given an id
     * @param id the ID of teh task to be deleted
     * @return if the Task object is found and deleted,
     * it will return a response with HTTP status code 204 or
     * if the Task object is not found, it will return
     * a response with HTTP status code 404.
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id")long id){
        return ResponseEntity.ok(taskService.delete(id));
    }


    /**
     * A PUT request for updating a particular Task instance in the repository,
     * specified by the given id.
     * @param task The updated Task Instance
     * @return if the Task object is found and updated,
     * it will return a response with HTTP status code 204 or
     * if the Task object is not found, it will return
     * a response with HTTP status code 404.
     */
    @PutMapping(path={"update","update/"})
    public ResponseEntity<Task> updateTitle(@RequestBody Task task){
        taskService.updateTask(task);
        return ResponseEntity.noContent().build();
    }



}
