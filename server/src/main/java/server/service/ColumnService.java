package server.service;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import commons.Column;
import commons.Card;

import server.database.ColumnRepository;
import server.database.CardRepository;
@Service
public class ColumnService {

    private final CardRepository cardrepo;
    private final ColumnRepository repo;

    /**
     * Constructs a new ColumnController with the specified repositories.
     * @param repo the repository for Column objects
     * @param cardrepo the repository for Card objects
     */
    public ColumnService(ColumnRepository repo, CardRepository cardrepo) {

        this.repo = repo;
        this.cardrepo=cardrepo;
       
    }
    /**
     * Returns a list of all Column objects in the database.
     * @return the list of Column objects
     */
    public List<Column> getAll() {
        return repo.findAll();
    }

    /**
     * Returns the Column object with the specified id.
     * @param id the id of the Column object to retrieve
     * @return the Column object with the specified id or null if it does not exist
     */
    public Optional<Column> getById(long id) {
        return repo.findById(id);
    }

    /**
     * Adds a new Column object to the database.
     * @param column the Column object to add
     * @return the saved Column object or null if the title is null or empty
     */
    public Column add(Column column) {
        if (column.getTitle() == null || column.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return repo.save(column);
    }
    /**
     * Updates an existing column with new information.
     * @param existing the existing column object
     * @param updated the updated column object
     * @return the updated column object saved in the database
     */
    public Column update(Column existing, Column updated) {
        existing.setTitle(updated.getTitle());
        existing.setCards(updated.getCards());
        return repo.save(existing);
    }
    /**
     * Deletes the Column object with the specified id and all cards contained by that column.
     * @param id the id of the Column object to delete
     * @return a response indicating success or failure
     */
    public boolean delete(long id) {
        Optional<Column> existing = repo.findById(id);
        if (existing.isPresent()) {
            Column column = existing.get();
            List<Card> cards = column.getCards();
            repo.deleteById(id);
            for (Card card : cards) {
                repo.deleteById(card.getId());
            }
            return true;
        } else {
            return false;
        }
    }

}

