
package server.api;

import java.util.List;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Card;

import server.service.CardService;


@RestController
@RequestMapping("/api/cards")
public class CardController {


    private final CardService cardservice;

    /**
     * Constructs a new CardController with the specified service.
     * @param cardservice the service for Card operation
     */

    public CardController(CardService cardservice) {

        this.cardservice=cardservice;
    }

    /**
     * Returns a list of all Card objects in the database.
     * @return the list of Card objects
     */
    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return cardservice.getAll();
    }

    /**
     * Returns the Card object with the specified id.
     * @param id the id of the Card object to retrieve
     * @return a response containing the Card object, or a bad request response if the id is invalid
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        Optional<Card> card = cardservice.getById(id);
        if (card.isPresent()) {
            return ResponseEntity.ok(card.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Adds a new Card object to the database.
     * @param card the Card object to add
     * @return a response containing the saved Card object,
     * or a bad request response if the title is null or empty
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Card> add(@RequestBody Card card) {

        if (card.getTitle() == null || card.getTitle().isEmpty()){

            return ResponseEntity.badRequest().build();
        }

        Card saved = cardservice.add(card);
        return ResponseEntity.ok(saved);
    }



//    /**
//     * Updates an existing Card object with the specified id.
//     * @param id the id of the Card object to update
//     * @param card the updated Card object
//     * @return a response containing the updated card object,
//     * or a not found response if the id is invalid
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Card> update(@PathVariable("id") long id, @RequestBody Card card) {
//        Optional<Card> existing = repo.findById(id);
//        if (existing.isPresent()) {
//            Card updated = existing.get();
//            updated.setTitle(card.getTitle());
//            updated.setDescription(card.getDescription());
//            updated.setTaskList(card.getTaskList());
//            updated.setTags(card.getTags());
//            updated.setColumn(card.getColumn());
//            Card saved = repo.save(updated);
//            return ResponseEntity.ok(saved);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//    /**
//     * Update the column for a specific card.
//     * @param id the ID of the card to update
//     * @param columnId the ID of the new column for the card
//     * @return a ResponseEntity containing the updated card object,
//     * or a not found response if the card or column ID is invalid
//     */
//    @PutMapping("/{id}/column")
//    public ResponseEntity<Card> updateColumn(@PathVariable("id") long id,
//                                             @RequestParam("columnId") long columnId) {
//        Optional<Card> existingCard = repo.findById(id);
//        Optional<Column> existingColumn = columnrepo.findById(columnId);
//
//        if (existingCard.isPresent() && existingColumn.isPresent()) {
//            Card card = existingCard.get();
//            Column column = existingColumn.get();
//            card.setColumn(column);
//            Card saved = repo.save(card);
//            return ResponseEntity.ok(saved);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }




    /**
     * Deletes the Card object with the specified id.
     *
     * @param id the id of the Card object to delete
     * @return a response indicating success or failure
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        Optional<Card> existing = cardservice.getById(id);
        if (existing.isPresent()) {
            cardservice.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}

