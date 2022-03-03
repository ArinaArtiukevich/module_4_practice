package com.esm.epam.validator;

import com.esm.epam.entity.User;

public interface UserValidator extends ServiceValidator<User> {
    /**
     * validates certificates list in user entity
     *
     * @param user  is parameter to be validated
     */
    void validateUserToBeUpdated(User user);

}
