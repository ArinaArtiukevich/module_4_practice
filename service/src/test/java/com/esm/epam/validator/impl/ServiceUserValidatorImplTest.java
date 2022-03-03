package com.esm.epam.validator.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.validator.impl.ServiceUserValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceUserValidatorImpl.class)
class ServiceUserValidatorImplTest {
    private final User user = User.builder().id(1L).login("arina").budget(100).certificates(Arrays.asList(Certificate.builder()
                                    .id(1L)
                                    .name("sneakers")
                                    .description("clothing and presents")
                                    .price(200)
                                    .duration(1)
                                    .tags(Arrays.asList(Tag.builder().id(1L).name("tag_paper").build(), Tag.builder().id(2L).name("tag_name").build()))
            .build(),
                            Certificate.builder()
                                    .id(2L)
                                    .name("hockey")
                                    .description("sport")
                                    .price(120)
                                    .duration(62)
                                    .tags(Collections.singletonList(Tag.builder().id(2L).name("tag_name").build()))
            .build()))
            .build();


    @Autowired
    private ServiceUserValidatorImpl userValidator;

    @Test
    void validateUser_resourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userValidator.validateEntity(Optional.empty(), 1L);
        });
    }

    @Test
    void validateUser_positive() {
        userValidator.validateEntity(Optional.ofNullable(user), 1L);
    }

}
