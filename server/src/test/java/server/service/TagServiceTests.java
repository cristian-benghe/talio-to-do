package server.service;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TagServiceTests {

    @Mock
    private TagRepository tagRepository;

    private TagService tagService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagService(tagRepository);
    }

    @Test
    void testGetAllTags() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag("Tag1");
        Tag tag2 = new Tag("Tag2");
        tags.add(tag1);
        tags.add(tag2);

        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> result = tagService.getAll();

        assertEquals(result.size(), 2);
        assertTrue(result.contains(tag1));
        assertTrue(result.contains(tag2));
    }

    @Test
    void testGetTagById() {
        long id = 1L;
        Tag tag = new Tag("Tag1");
        tag.setTagID(id);
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));

        Optional<Tag> result = tagService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(result.get(), tag);
    }

    @Test
    void testAddTag() {
        Tag tag = new Tag("Tag1");

        when(tagRepository.save(tag)).thenReturn(tag);

        Tag result = tagService.add(tag);

        assertEquals(result, tag);
    }

    @Test
    void testAddTagWithNullOrEmptyTitle() {
        Tag tag = new Tag(null);

        assertThrows(IllegalArgumentException.class, () -> {
            tagService.add(tag);
        });

        tag.setTitle("");

        assertThrows(IllegalArgumentException.class, () -> {
            tagService.add(tag);
        });
    }

    @Test
    void testDeleteTag() {
        long id = 1L;
        tagService.delete(id);

        verify(tagRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdateTag() {
        long id = 1L;
        Tag existingTag = new Tag("Existing Tag");
        existingTag.setTagID(id);
        existingTag.setFontColor(255.0, 0.0, 0.0);
        existingTag.setHighlightColor(0.0, 0.0, 255.0);

        Tag updatedTag = new Tag("Updated Tag");
        updatedTag.setTagID(id);
        updatedTag.setFontColor(0.0, 255.0, 0.0);
        updatedTag.setHighlightColor(255.0, 255.0, 0.0);

        when(tagRepository.findById(id)).thenReturn(Optional.of(existingTag));
        when(tagRepository.save(existingTag)).thenReturn(existingTag);

        Tag result = tagService.update(existingTag, updatedTag);

        assertEquals(result.getTitle(), updatedTag.getTitle());
        assertEquals(result.getFontRed(), updatedTag.getFontRed());
        assertEquals(result.getFontGreen(), updatedTag.getFontGreen());
        assertEquals(result.getFontBlue(), updatedTag.getFontBlue());
        assertEquals(result.getHighlightRed(), updatedTag.getHighlightRed());
        assertEquals(result.getHighlightGreen(), updatedTag.getHighlightGreen());
        assertEquals(result.getHighlightBlue(), updatedTag.getHighlightBlue());
    }


}