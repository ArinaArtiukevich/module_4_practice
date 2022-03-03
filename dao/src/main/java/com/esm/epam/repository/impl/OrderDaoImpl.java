package com.esm.epam.repository.impl;

import com.esm.epam.entity.Order;
import com.esm.epam.repository.OrderDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.esm.epam.util.ParameterAttribute.ORDER_FIELD_USER_ID;

@Repository
@AllArgsConstructor
public class OrderDaoImpl implements OrderDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void addOrder(Order order) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(order);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Order> getUserOrders(long idUser) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager
                .createQuery(getQueryToGetUserOrders(idUser, entityManager))
                .getResultList();
    }

    @Override
    public List<Order> getLimitedOrders(long id, int page, int size) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager
                .createQuery(getQueryToGetUserOrders(id, entityManager)).setFirstResult(page).setMaxResults(size)
                .getResultList();
    }

    private CriteriaQuery<Order> getQueryToGetUserOrders(long idUser, EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(ORDER_FIELD_USER_ID), idUser));
        return criteriaQuery;
    }
}
