package com.esm.epam.validator.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.validator.ServiceValidator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServiceCertificateValidatorImpl implements ServiceValidator<Certificate> {
    @Override
    public void validateEntity(Optional<Certificate> certificate, Long id) {
        if (!certificate.isPresent()) {
            throw new ResourceNotFoundException(String.format("Requested certificate resource not found id = %d ", id));
        }
    }
}
