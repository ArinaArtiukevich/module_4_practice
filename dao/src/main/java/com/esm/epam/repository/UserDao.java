package com.esm.epam.repository;

import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    /**
     * gets all users
     *
     * @param page is started element
     * @param size the number of items to be returned
     * @return List with users
     */
    List<User> getAll(int page, int size);

    /**
     * finds user by id
     *
     * @param id is required element id
     * @return required element
     */
    Optional<User> getById(long id);

    /**
     * updates user budget
     *
     * @param user is entity with new budget
     * @return updated user
     */
    User updateBudget(User user);

    /**
     * gets the most widely used tag of a user with the highest cost of all orders
     *
     * @return required tag
     */
    Optional<Tag> getMostWidelyUsedTag();
}
