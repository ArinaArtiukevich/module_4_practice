package com.esm.epam.repository.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Order;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.repository.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_NAME;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_USERS;
import static com.esm.epam.util.ParameterAttribute.ORDER_FIELD_PRICE;
import static com.esm.epam.util.ParameterAttribute.ORDER_FIELD_USER_ID;
import static com.esm.epam.util.ParameterAttribute.TAG_FIELD_CERTIFICATES;
import static com.esm.epam.util.ParameterAttribute.TAG_FIELD_ID;
import static com.esm.epam.util.ParameterAttribute.USER_FIELD_LOGIN;

@Repository
@AllArgsConstructor
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<User> getAll(int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        return query.setFirstResult(page).setMaxResults(size).getResultList();
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    @Transactional
    public User updateBudget(User user) {
        return entityManager.merge(user);
    }

    @Override
    public Optional<Tag> getMostWidelyUsedTag() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        long idUser = getIdUser(criteriaBuilder);

        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        criteriaQuery.select(root);

        criteriaQuery.where(criteriaBuilder.equal(root.join(TAG_FIELD_CERTIFICATES).join(CERTIFICATE_FIELD_USERS), idUser));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.count(root.get(TAG_FIELD_ID))));
        criteriaQuery.groupBy(root.get(TAG_FIELD_ID));

        List<Tag> tags = entityManager.createQuery(criteriaQuery).getResultList();

        return tags.stream().findFirst();
    }

    @Override
    public Optional<User> getByLogin(String login) {
        Optional<User> requiredUser = Optional.empty();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(USER_FIELD_LOGIN), login));
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        List<User> certificateList = query.getResultList();
        if (certificateList.size() == 1) {
            requiredUser = Optional.ofNullable(certificateList.get(0));
        }
        return requiredUser;
    }

    @Override
    @Transactional
    public User add(User user) {
        entityManager.persist(user);
        return user;
    }

    private long getIdUser(CriteriaBuilder criteriaBuilder) {
        long idUser;
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root.get(ORDER_FIELD_USER_ID));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(root.get(ORDER_FIELD_PRICE))));
        criteriaQuery.groupBy(root.get(ORDER_FIELD_USER_ID));
        List<Long> idUsers = entityManager.createQuery(criteriaQuery).getResultList();

        idUser = idUsers.stream().findFirst().orElseThrow(() -> new DaoException("Could not find tag"));
        return idUser;
    }
}
