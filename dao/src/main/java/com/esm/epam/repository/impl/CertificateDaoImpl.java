package com.esm.epam.repository.impl;

import com.esm.epam.builder.PredicateBuilder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.exception.DaoException;
import com.esm.epam.repository.CertificateDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_ID;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_NAME;

@Repository
@AllArgsConstructor
public class CertificateDaoImpl implements CertificateDao {
    private final PredicateBuilder predicateBuilder;
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<Certificate> getFilteredList(MultiValueMap<String, Object> params, int page, int size) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        List<Predicate> predicates = predicateBuilder.getPredicates(params, criteriaBuilder, criteriaQuery, root);

        criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager
                .createQuery(criteriaQuery).setFirstResult(page).setMaxResults(size).getResultList();
    }

    @Override
    public List<Certificate> getAll(int page, int size) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        criteriaQuery.select(root);
        TypedQuery<Certificate> query = entityManager.createQuery(criteriaQuery);
        return query.setFirstResult(page).setMaxResults(size).getResultList();
    }

    @Override
    public Certificate update(Certificate certificate) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Certificate updatedCertificate = entityManager.merge(certificate);
        entityManager.getTransaction().commit();
        return updatedCertificate;
    }

    @Override
    public Certificate add(Certificate certificate) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(certificate);
        entityManager.getTransaction().commit();

        Optional<Certificate> addedCertificate = getById(certificate.getId());
        if (!addedCertificate.isPresent()){
            throw new DaoException("Certificate was not added");
        }
        return addedCertificate.get();
    }

    @Override
    public Optional<Certificate> getById(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return Optional.ofNullable(entityManager.find(Certificate.class, id));
    }

    @Override
    public boolean deleteById(long id) {
        boolean isDeleted = false;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Certificate> criteriaDelete = criteriaBuilder.createCriteriaDelete(Certificate.class);
        Root<Certificate> root = criteriaDelete.from(Certificate.class);
        criteriaDelete.where(criteriaBuilder.equal(root.get(CERTIFICATE_FIELD_ID), id));
        entityManager.getTransaction().begin();
        if (entityManager.createQuery(criteriaDelete).executeUpdate() > 0) {
            isDeleted = true;
        }
        entityManager.getTransaction().commit();
        return isDeleted;
    }

    @Override
    public Optional<Certificate> deleteTag(long id, long idTag) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Certificate certificate = entityManager.find(Certificate.class, id);
        certificate.getTags().removeIf(tag -> (tag.getId().equals(idTag)));
        entityManager.getTransaction().commit();
        return getById(id);
    }

    @Override
    public Optional<Certificate> getByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Optional<Certificate> requiredCertificate = Optional.empty();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(CERTIFICATE_FIELD_NAME), name));
        TypedQuery<Certificate> query = entityManager.createQuery(criteriaQuery);
        List<Certificate> certificateList = query.getResultList();
        if (certificateList.size() == 1) {
            requiredCertificate = Optional.ofNullable(certificateList.get(0));
        }
        return requiredCertificate;
    }

}
