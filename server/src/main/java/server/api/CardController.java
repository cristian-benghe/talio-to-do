
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Card;
import commons.Column;
import server.database.CardRepository;
import server.database.ColumnRepository;

@RestController
@RequestMapping("/api/cards")
public class CardController {


    private final CardRepository repo;
    private final ColumnRepository columnrepo;

    public CardController(CardRepository repo, ColumnRepository columnrepo) {

        this.repo = repo;
        this.columnrepo = columnrepo;
    }

    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return repo.findAll();
    }

    //GET endpoint that retrieves a specific Card object by its id from the database using the CardRepository.
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    // saves the Card object using the repository and returns a response with the saved Card object in the body.
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Card> add(@RequestBody Card card) {

        if (isNullOrEmpty(card.getTitle())){

            return ResponseEntity.badRequest().build();
        }

        Card saved = repo.save(card);
        return ResponseEntity.ok(saved);
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    //update an existing Card object with the given id
    @PutMapping("/{id}")
    public ResponseEntity<Card> update(@PathVariable("id") long id, @RequestBody Card card) {
        Optional<Card> existing = repo.findById(id);
        if (existing.isPresent()) {
            Card updated = existing.get();
            updated.setTitle(card.getTitle());
            updated.setDescription(card.getDescription());
            updated.setTaskList(card.getTaskList());
            updated.setTags(card.getTags());
            updated.setColumn(card.getColumn());
            Card saved = repo.save(updated);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
     //update the column of a card
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateColumn(@PathVariable("id") long id, @RequestParam("columnId") long columnId) {
        Optional<Card> existingCard = repo.findById(id);
        Optional<Column> existingColumn = columnrepo.findById(columnId);

        if (existingCard.isPresent() && existingColumn.isPresent()) {
            Card card = existingCard.get();
            Column column = existingColumn.get();
            card.setColumn(column);
            Card saved = repo.save(card);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    //delete a Card object from the database.

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        Optional<Card> existing = repo.findById(id);
        if (existing.isPresent()) {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}

