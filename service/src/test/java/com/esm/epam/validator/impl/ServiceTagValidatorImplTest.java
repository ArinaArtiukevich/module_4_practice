package com.esm.epam.validator.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.validator.impl.ServiceTagValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTagValidatorImpl.class)
class ServiceTagValidatorImplTest {

    @Autowired
    private ServiceTagValidatorImpl tagValidator;
    private static final Tag tag = Tag.builder().id(1L).name("tag_snow").build();

    @Test
    void validateEntity_resourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            tagValidator.validateEntity(Optional.empty(), 1L);
        });
    }

    @Test
    void validateEntity_positive() {
        tagValidator.validateEntity(Optional.ofNullable(tag), 1L);
    }

}