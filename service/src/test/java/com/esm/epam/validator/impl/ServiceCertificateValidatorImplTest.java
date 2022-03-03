package com.esm.epam.validator.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.validator.impl.ServiceCertificateValidatorImpl;
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
@ContextConfiguration(classes = ServiceCertificateValidatorImpl.class)
class ServiceCertificateValidatorImplTest {

    @Autowired
    private ServiceCertificateValidatorImpl certificateValidator;
    private final Certificate certificate = Certificate.builder()
            .id(1L)
            .name("skiiing")
            .description("skiing in alps")
            .price(100)
            .duration(200)
            .build();

    @Test
    void testValidateEntity_resourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            certificateValidator.validateEntity(Optional.empty(), 1L);
        });
    }

    @Test
    void testValidateEntity_positive() {
        certificateValidator.validateEntity(Optional.ofNullable(certificate), 1L);
    }
}