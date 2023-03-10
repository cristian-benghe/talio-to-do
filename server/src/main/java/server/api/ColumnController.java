
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
import server.database.ColumnRepository;

@RestController
@RequestMapping("/api/columns")
public class ColumnController {


    private final ColumnRepository repo;

    public ColumnController(ColumnRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Column> getAll() {
        return repo.findAll();
    }

    //GET endpoint that retrieves a specific Column object by its id from the database using the ColumnRepository.
    @GetMapping("/{id}")
    public ResponseEntity<Column> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    // saves the Column object using the repository and returns a response with the saved Column object in the body.
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Column> add(@RequestBody Column column) {

        if (isNullOrEmpty(column.getTitle())){

            return ResponseEntity.badRequest().build();
        }

        Column saved = repo.save(column);
        return ResponseEntity.ok(saved);
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    //update an existing Column object with the given id

    @PutMapping("/{id}")
    public ResponseEntity<Column> update(@PathVariable("id") long id, @RequestBody Column column) {
        Optional<Column> existing = repo.findById(id);
        if (existing.isPresent()) {
            Column updated = existing.get();
            updated.setTitle(column.getTitle());
            updated.setBoard(column.getBoard());
            updated.setCards(column.getCards());
            Column saved = repo.save(updated);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete a Column object from the database.

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        Optional<Column> existing = repo.findById(id);
        if (existing.isPresent()) {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}

