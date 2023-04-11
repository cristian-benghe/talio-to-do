package server.service;


import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository repo;


    /**
     * Constructs a new TagController with the specified repositories.
     * @param repo the repository for Card objects
     */
    public TagService(TagRepository repo) {

        this.repo = repo;


    }
    /**
     * Returns a list of all Tag objects in the database.
     * @return the list of Tag objects
     */
    public List<Tag> getAll() {
        return repo.findAll();
    }

    /**
     * Returns the Tag object with the specified id.
     * @param id the id of the Tag object to retrieve
     * @return the Tag object with the specified id or null if it does not exist
     */
    public Optional<Tag> getById(long id) {
        return repo.findById(id);
    }

    /**
     * Adds a new Tag object to the database.
     * @param tag the Tag object to add
     * @return the saved tag object or null if the title is null or empty
     */
    public Tag add(Tag tag) {
        if (tag.getTitle() == null || tag.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return repo.save(tag);
    }


    /**
     * @param existing the initial tag
     * @param updated the tag after update
     * @return the updated tag
     */
    public Tag update(Tag existing, Tag updated) {
        existing.setTitle(updated.getTitle());
        existing.setFontColor(updated.getFontRed(), updated.getFontGreen(), updated.getFontBlue());
        existing.setHighlightColor(updated.getHighlightBlue(),
                updated.getHighlightGreen(), updated.getHighlightRed());
//        Set<Card> existingCards = existing.getCards();
//        Set<Card> updatedCards = updated.getCards();
//        existingCards.clear();
//        existingCards.addAll(updatedCards);
        return repo.save(existing);
    }
    /**
     * Deletes the Tag object with the specified id.
     * @param id the id of the Tag object to delete
     */
    public void delete(long id) {
        repo.deleteById(id);
    }

}


