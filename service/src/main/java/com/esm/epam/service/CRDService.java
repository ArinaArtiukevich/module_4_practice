package com.esm.epam.service;

import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * @param <T> describes type parameter
 */
public interface CRDService<T> {
    /**
     * gets all values
     *
     * @param actualPage is started element
     * @param size the number of items to be returned
     * @return List with values
     */
    List<T> getAll(int actualPage, int size);

    /**
     * gets filtered values
     *
     * @param params collection that contains {@link String} as
     *               key and {@link Object} as value
     * @param actualPage   is started element
     * @param size   the number of items to be returned
     * @return List with sorted values
     */
    default List<T> getFilteredList(MultiValueMap<String, Object> params, int actualPage, int size) {
        return getAll(actualPage, size);
    }

    /**
     * adds new element
     *
     * @param t the type of element to be added
     * @return element
     */
    T add(T t);

    /**
     * finds element by id
     *
     * @param id is required element id
     * @return required element
     */
    T getById(long id);

    /**
     * deletes element by id
     *
     * @param id is required element id
     * @return true when element was deleted
     */
    boolean deleteById(long id);
}
