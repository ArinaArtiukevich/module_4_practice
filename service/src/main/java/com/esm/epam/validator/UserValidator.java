package com.esm.epam.validator;

import com.esm.epam.entity.User;

public interface UserValidator extends ServiceValidator<User> {
    /**
     * validates certificates list in user entity
     *
     * @param user is parameter to be validated
     */
    void validateUserToBeUpdated(User user);

    /**
     * validates if user has access to data
     *
     * @param currentUser  is current user
     * @param requiredId is id of user with required data
     */
    void validateUserId(User currentUser, Long requiredId);
}
