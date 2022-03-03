package com.esm.epam.service;

import com.esm.epam.entity.Order;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
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
    User getById(long id);

    /**
     * updates user budget
     *
     * @param user   is entity with new budget
     * @param idUser is id of user to be updated
     * @return updated user
     */
    User update(User user, long idUser);

    /**
     * finds orders by user id
     *
     * @param id   is id of user
     * @param page is started element
     * @param size the number of items to be returned
     * @return required orders
     */
    List<Order> getOrders(long id, int page, int size);

    /**
     * gets the most widely used tag of a user with the highest cost of all orders
     *
     * @return required tag
     */
    Optional<Tag> getMostWidelyUsedTag();
}
