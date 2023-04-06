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
import static org.mockito.Mockito.when;

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

}