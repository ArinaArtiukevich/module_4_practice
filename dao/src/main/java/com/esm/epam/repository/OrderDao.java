package com.esm.epam.repository;

import com.esm.epam.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    /**
     * adds new order
     *
     * @param order is element to be added
     */
    void addOrder(Order order);

    /**
     * finds orders by user id
     *
     * @param idUser is id of user
     * @return required orders
     */
    List<Order> getUserOrders(long idUser);

    /**
     * finds orders by user id
     *
     * @param id   is id of user
     * @param page is started element
     * @param size the number of items to be returned
     * @return required orders
     */
    List<Order> getLimitedOrders(long id, int page, int size);

    /**
     * gets order by certificate id
     *
     * @param idCertificate   is id of certificate
     * @return required orders
     */
    List<Order> getOrderByCertificateId(long idCertificate);
}
