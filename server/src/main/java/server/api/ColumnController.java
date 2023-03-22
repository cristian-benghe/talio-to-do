
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
import commons.Card;
import server.database.ColumnRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/columns")
public class ColumnController {


    private final ColumnRepository repo;
    private final CardRepository cardrepo;

    /**
     * Constructs a new ColumnController with the specified repositories.
     * @param repo the repository for Column objects
     * @param cardrepo the repository for Card objects
     */
    public ColumnController(ColumnRepository repo, CardRepository cardrepo) {

        this.repo = repo;
        this.cardrepo = cardrepo;
    }
    /**
     * Returns a list of all Column objects in the database.
     * @return the list of Column objects
     */
    @GetMapping(path = { "", "/" })
    public List<Column> getAll() {
        return repo.findAll();
    }

    /**
     * Returns the Column object with the specified id.
     * @param id the id of the Column object to retrieve
     * @return a response containing the Column object,or a bad request response
     * if the id is invalid
     */
    @GetMapping("/{id}")
    public ResponseEntity<Column> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds a new Column object to the database.
     * @param column the Column object to add to the database
     * @return a response containing the saved Column object,or a bad request response
     * if the title is null or empty
     */
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

    /**
     * Updates an existing Column object with the specified id.
     * @param id the id of the Column object to update
     * @param column the updated Column object
     * @return a response containing the updated Column object,or a not found response
     * if the id is invalid
     */

    @PutMapping("/{id}")
    public ResponseEntity<Column> update(@PathVariable("id") long id, @RequestBody Column column) {
        Optional<Column> existing = repo.findById(id);
        if (existing.isPresent()) {
            Column updated = existing.get();
            updated.setTitle(column.getTitle());
            updated.setCards(column.getCards());
            Column saved = repo.save(updated);
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
        Optional<Column> existing = repo.findById(id);
        if (existing.isPresent()) {
            Column column = existing.get();
            List<Card> cards = column.getCards();
            repo.deleteById(id);
            for(Card card : cards){
                cardrepo.deleteById(card.getId());
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }





}

