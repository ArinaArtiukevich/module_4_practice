package com.esm.epam.repository;

import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

/**
 * @param <T> describes type parameter
 */
public interface CRDDao<T> {
    /**
     * gets all values
     *
     * @param page is started element
     * @param size the number of items to be returned
     * @return List with values
     */
    List<T> getAll(int page, int size);

    /**
     * gets filtered values
     *
     * @param params collection that contains {@link String} as
     *               key and {@link Object} as value
     * @param page   is started element
     * @param size   the number of items to be returned
     * @return List with sorted values
     */
    default List<T> getFilteredList(MultiValueMap<String, Object> params, int page, int size) {
        return getAll(page, size);
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
    Optional<T> getById(long id);

    /**
     * finds element by name
     *
     * @param name is required element name
     * @return required element
     */
    Optional<T> getByName(String name);

    /**
     * deletes element by id
     *
     * @param id is required element id
     * @return true when element was deleted
     */
    boolean deleteById(long id);
}

