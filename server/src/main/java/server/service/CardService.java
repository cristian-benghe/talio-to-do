package server.service;
import java.util.List;
import java.util.Optional;


import commons.Task;
import org.springframework.stereotype.Service;


import commons.Card;


import server.database.CardRepository;

import javax.swing.text.html.Option;

@Service
public class CardService {

    private final CardRepository repo;


    /**
     * Constructs a new ColumnController with the specified repositories.
     * @param repo the repository for Card objects
     */
    public CardService(CardRepository repo) {

        this.repo = repo;


    }
    /**
     * Returns a list of all Card objects in the database.
     * @return the list of Card objects
     */
    public List<Card> getAll() {
        return repo.findAll();
    }

    /**
     * Returns the Card object with the specified id.
     * @param id the id of the Card object to retrieve
     * @return the Card object with the specified id or null if it does not exist
     */
    public Optional<Card> getById(long id) {
        return repo.findById(id);
    }

    /**
     * Adds a new Card object to the database.
     * @param card the Card object to add
     * @return the saved Card object or null if the title is null or empty
     */
    public Card add(Card card) {
        if (isNullOrEmpty(card.getTitle())) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return repo.save(card);
    }
    /**
     * Deletes the Card object with the specified id.
     * @param id the id of the Card object to delete
     */
    public void delete(long id) {
        repo.deleteById(id);
    }
    /**
     * This method verifies if the given string is null or not
     * @param s - a string
     * @return true/false if the string is empty or not
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public Card addTask(long id, Task task){

        Optional<Card> optCard = this.getById(id);
        if(optCard.isEmpty()){
            throw new IllegalArgumentException();
        }

        Card card = optCard.get();
        card.getTaskList().add(task);

        return repo.save(card);
    }
}

