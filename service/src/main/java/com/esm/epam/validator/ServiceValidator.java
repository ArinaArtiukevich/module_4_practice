package com.esm.epam.validator;

import java.util.Optional;

/**
 * @param <T> describes type parameter
 */
public interface ServiceValidator<T> {
    /**
     * validates element with id
     *
     * @param t  is parameter to be validated
     * @param id is id of parameter
     */
    void validateEntity(Optional<T> t, Long id);

}
