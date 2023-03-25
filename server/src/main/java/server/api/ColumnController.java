package server.api;

import java.util.List;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Column;

import server.service.ColumnService;


@RestController
@RequestMapping("/api/columns")
public class ColumnController {


    private final ColumnService columnservice;

    /**
     * Constructs a new ColumnController with the specified service.
     * @param columnservice the service for Column operation
     */

    public ColumnController(ColumnService columnservice) {

        this.columnservice=columnservice;
    }
    /**
     * Returns a list of all Column objects in the database.
     * @return the list of Column objects
     */
    @GetMapping(path = { "", "/" })
    public List<Column> getAll() {
        return columnservice.getAll();
    }

    /**
     * Returns the Column object with the specified id.
     * @param id the id of the Column object to retrieve
     * @return a response containing the Column object,or a bad request response
     * if the id is invalid
     */
    @GetMapping("/{id}")
    public ResponseEntity<Column> getById(@PathVariable("id") long id) {
        Optional<Column> column = columnservice.getById(id);
        if (column.isPresent()) {
            return ResponseEntity.ok(column.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a new Column object to the database.
     * @param column the Column object to add to the database
     * @return a response containing the saved Column object,or a bad request response
     * if the title is null or empty
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Column> add(@RequestBody Column column) {
        Column saved = columnservice.add(column);
        return ResponseEntity.ok(saved);
    }


    /**
     * Updates an existing Column object with the specified id.
     * @param id the id of the Column object to update
     * @param column the updated Column object
     * @return a response containing the updated Column object,or a not found response
     * if the id is invalid
     */

    @PutMapping("/{id}")
    public ResponseEntity<Column> update(@PathVariable("id") long id, @RequestBody Column column) {
        Optional<Column> existing = columnservice.getById(id);
        if (existing.isPresent()) {
            Column saved = columnservice.update(existing.get(),column);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes the Column object with the specified id and all cards contained by that column.
     * @param id the id of the Column object to delete
     * @return a response indicating success or failure
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        boolean deleted = columnservice.delete(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }




}