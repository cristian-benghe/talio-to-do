package server.api;


import commons.Card;
import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.service.TaskService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@RequestMapping(path = {"/api/tasks", "/api/tasks/"})
public class TaskController {

    private final TaskService taskService;

    private HashMap<Object, Consumer<Card>> taskListners = new HashMap<Object, Consumer<Card>>();


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
     * @param cardId the identifier of the parent Card
     * @param task The updated Task Instance.
     * @return if the Task object is found and updated,
     * it will return a response with HTTP status code 204 or
     * if the Task object is not found, it will return
     * a response with HTTP status code 404.
     */
    @PutMapping(path={"update","update/"})
    public ResponseEntity<Task> updateTask(@RequestParam("id") long cardId, @RequestBody Task task){
        var updatedTask = taskService.updateTask(task);
        taskListners.forEach((k,l) -> l.accept(taskService.getCardById(cardId)));
        return ResponseEntity.noContent().build();
    }


    /**
     * Handles the POST request that adds a new Task instance and links it
     * to its corresponding Card instance.
     * @param cardId the identifier of the Card instance
     * @param task the new Task instance
     * @return the newly created Task instance
     */
    @PostMapping(path={"addTask/byCard","addTask/byCard/"})
    public ResponseEntity<Task> addTask(@RequestParam("id") long cardId, @RequestBody Task task){

        try{
            var addedTask =  taskService.addTaskToCard(task,cardId);
            var res = ResponseEntity.ok(task);
            taskListners.forEach((k,l) -> l.accept(taskService.getCardById(cardId)));
            return res;
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

    }


    /**
     * A GET request that handles the registration for Long Polling updates
     * @param id the id of the card whose updates will be listened to.
     * @return the list of tasks of the registered card.
     */
    @GetMapping(path={"getTask/updates", "getTask/updates/"})
    public DeferredResult<ResponseEntity<List<Task>>> getTaskUpdates(@RequestParam("id")long id){

        var results = new DeferredResult<ResponseEntity<List<Task>>>(10000L,
                ResponseEntity.noContent().build());

        var key = new Object();
        taskListners.put(key, card -> {


            if(id == -1 || id == card.getId()) {
                results.setResult(ResponseEntity.ok(card.getTaskList()));

            }
        });

        results.onCompletion(() ->{
            taskListners.remove(key);
        });


        return results;
    }


}
