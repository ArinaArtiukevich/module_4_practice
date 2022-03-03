package com.esm.epam.builder;

import com.esm.epam.entity.Certificate;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface PredicateBuilder {
    /**
     * gets predicates to build query
     *
     * @param params          collection that contains {@link String} as key and {@link Object} as value
     * @param criteriaBuilder used to create predicate
     * @param criteriaQuery   used to specify the ordering expressions
     * @param root            is a root type in the from clause
     * @return required list
     */
    List<Predicate> getPredicates(MultiValueMap<String, Object> params, CriteriaBuilder criteriaBuilder, CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> root);
}
