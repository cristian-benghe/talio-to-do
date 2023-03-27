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
    @GetMapping(path={"getAll", "getAll/"})
    public List<Task> getAll(){return taskService.getAll();}

    /**
     * A GET request for the Task object corresponding to the
     * given ID
     * @param id the ID of the Task
     * @return the corresponding Task object, or a BAD REQUEST
     * if the object does not exist.
     */
    @GetMapping("getByID/{id}")
    public ResponseEntity<Task> getByID(@PathVariable("id")long id){
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
    @PostMapping(path={"add","add/"})
    public ResponseEntity add(@RequestBody Task task){
        Task addedTask = taskService.add(task);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Task> delete(@PathVariable("id")long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * A PUT request for changing the title field for a particular Task instance,
     * specified by the given id.
     * @param id the id of the Task instance to be updated
     * @param title the new title value for the Task instance
     * @return if the Task object is found and updated,
     * it will return a response with HTTP status code 204 or
     * if the Task object is not found, it will return
     * a response with HTTP status code 404.
     */
    @PutMapping(path={"update/title","update/title/"})
    public ResponseEntity<Task> updateTitle(@RequestParam long id, @RequestParam String title){
        taskService.updateTitle(id, title);
        return ResponseEntity.noContent().build();
    }

    /**
     * A PUT request for changing the status field for a particular Task instance,
     * specified by the given id.
     * @param id the id of the Task instance to be updated
     * @param status the new status value for the Task instance
     * @return if the Task object is found and updated,
     *      * it will return a response with HTTP status code 204 or
     *      * if the Task object is not found, it will return
     *      * a response with HTTP status code 404.
     */
    @PutMapping(path={"update/status","update/status/"})
    public ResponseEntity<Task> updateStatus(@RequestParam long id, @RequestParam boolean status){
        taskService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }


}
