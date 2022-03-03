package com.esm.epam.validator.impl;

import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.validator.UserValidator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServiceUserValidatorImpl implements UserValidator {
    @Override
    public void validateEntity(Optional<User> user, Long id) {
        if (!user.isPresent()) {
            throw new ResourceNotFoundException(String.format("No such user with id = %d", id));
        }
    }

    @Override
    public void validateUserToBeUpdated(User user) {
        if (user.getCertificates().size() != 1) {
            throw new ServiceException("Enter one certificate.");
        }
    }
}
