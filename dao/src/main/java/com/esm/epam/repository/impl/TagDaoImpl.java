package com.esm.epam.repository.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.repository.CRDDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.TAG_FIELD_ID;
import static com.esm.epam.util.ParameterAttribute.TAG_FIELD_NAME;


@Repository
@AllArgsConstructor
public class TagDaoImpl implements CRDDao<Tag> {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<Tag> getAll(int page, int size) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root);
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);
        return query.setFirstResult(page).setMaxResults(size).getResultList();
    }

    @Override
    public Tag add(Tag tag) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(tag);
        entityManager.getTransaction().commit();
        return tag;
    }

    @Override
    public Optional<Tag> getById(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> getByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Optional<Tag> requiredTag = Optional.empty();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(TAG_FIELD_NAME), name));
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);
        List<Tag> tagList = query.getResultList();
        if (tagList.size() == 1) {
            requiredTag = Optional.ofNullable(tagList.get(0));
        }
        return requiredTag;
    }

    @Override
    public boolean deleteById(long id) {
        boolean isDeleted = false;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Tag> criteriaDelete = criteriaBuilder.createCriteriaDelete(Tag.class);
        Root<Tag> root = criteriaDelete.from(Tag.class);
        criteriaDelete.where(criteriaBuilder.equal(root.get(TAG_FIELD_ID), id));
        entityManager.getTransaction().begin();
        if (entityManager.createQuery(criteriaDelete).executeUpdate() > 0) {
            isDeleted = true;
        }
        entityManager.getTransaction().commit();
        return isDeleted;
    }

}
