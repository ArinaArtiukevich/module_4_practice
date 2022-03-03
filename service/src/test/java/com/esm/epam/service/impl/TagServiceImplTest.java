package com.esm.epam.service.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.repository.impl.TagDaoImpl;
import com.esm.epam.service.impl.TagServiceImpl;
import com.esm.epam.validator.impl.ServiceTagValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class TagServiceImplTest {
    @Mock
    private TagDaoImpl tagDao = Mockito.mock(TagDaoImpl.class);

    @Mock
    private ServiceTagValidatorImpl validator = Mockito.mock(ServiceTagValidatorImpl.class);

    @InjectMocks
    private TagServiceImpl tagService;

    private final Long tagId = 1L;
    private final Tag tag = Tag.builder().id(tagId).name("tag_snow").build();
    private final Tag newTag = Tag.builder().id(1L).name("tag_snow").build();
    private final List<Tag> tags = Arrays.asList(
            Tag.builder().id(2L).name("tag_outdoors").build(),
            Tag.builder().id(3L).name("tag_indoors").build(),
            Tag.builder().id(4L).name("tag_sport").build()
    );
    @Test
    void testGetAll_positive() {
        when(tagDao.getAll(0, 1000)).thenReturn(tags);
        List<Tag> actualTags = tagService.getAll(1, 1000);
        assertEquals(tags, actualTags);
    }

    @Test
    void testAdd_positive() {
        when(tagDao.add(newTag)).thenReturn(tag);
        Tag actualTag = tagService.add(newTag);
        assertEquals(tag, actualTag);
    }

    @Test
    void testGetById_positive() {
        when(tagDao.getById(1L)).thenReturn(Optional.ofNullable(tag));
        Tag actualTag = tagService.getById(1L);
        assertEquals(tag, actualTag);
    }

    @Test
    void testDeleteById_positive() {
        when(tagDao.deleteById(2L)).thenReturn(true);
        tagService.deleteById(tags.get(0).getId());
        Mockito.verify(tagDao).deleteById(tags.get(0).getId());
    }

    @Test
    void testDeleteById() {
        boolean expectedResult = false;
        long invalidTagId = -1L;
        when(tagDao.deleteById(invalidTagId)).thenReturn(false);
        Boolean actualResult = tagService.deleteById(invalidTagId);
        assertEquals(expectedResult, actualResult);

    }
}
