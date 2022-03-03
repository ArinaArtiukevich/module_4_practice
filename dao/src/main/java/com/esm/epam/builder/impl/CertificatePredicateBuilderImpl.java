package com.esm.epam.builder.impl;

import com.esm.epam.builder.PredicateBuilder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.exception.DaoException;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_DESCRIPTION;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_DATE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_DESCRIPTION;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_NAME;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_TAGS;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_NAME;
import static com.esm.epam.util.ParameterAttribute.DATE_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.DESC_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.DIRECTION_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.NAME_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.PAGE_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.PERCENT_SYMBOL;
import static com.esm.epam.util.ParameterAttribute.SIZE_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.SORT_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.TAG;

@Component
public class CertificatePredicateBuilderImpl implements PredicateBuilder {

    @Override
    public List<Predicate> getPredicates(MultiValueMap<String, Object> params, CriteriaBuilder criteriaBuilder, CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> root) {
        List<Predicate> predicates = new ArrayList<>();

        params.entrySet().forEach(entry -> {
            switch (entry.getKey()) {
                case CERTIFICATE_NAME:
                    addPredicate(params, entry, predicates, criteriaBuilder, root, CERTIFICATE_FIELD_NAME);
                    break;
                case CERTIFICATE_DESCRIPTION:
                    addPredicate(params, entry, predicates, criteriaBuilder, root, CERTIFICATE_FIELD_DESCRIPTION);
                    break;
                case TAG:
                    addTagPredicate(criteriaBuilder, root, predicates, entry);
                    break;
                case SORT_STATEMENT:
                    sortByParameter(params, criteriaBuilder, criteriaQuery, root, entry);
                    break;
                default:
                    if (!Objects.equals(entry.getKey(), DIRECTION_PARAMETER) && !Objects.equals(entry.getKey(), PAGE_PARAMETER) && !Objects.equals(entry.getKey(), SIZE_PARAMETER)) {
                        throw new DaoException("Invalid filter key.");
                    }
                    break;
            }
        });
        return predicates;
    }

    private void addTagPredicate(CriteriaBuilder criteriaBuilder, Root<Certificate> root, List<Predicate> predicates, Map.Entry<String, List<Object>> entry) {
        entry.getValue().forEach(tag -> predicates.add(criteriaBuilder.isMember(tag, root.get(CERTIFICATE_FIELD_TAGS))));
    }

    private void addPredicate(MultiValueMap<String, Object> params, Map.Entry<String, List<Object>> entry, List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Certificate> root, String certificateField) {
        params.get(entry.getKey()).forEach((value) -> {
            predicates.add(
                    criteriaBuilder.like(root.get(certificateField), PERCENT_SYMBOL + value + PERCENT_SYMBOL));
        });
    }

    private void sortByParameter(MultiValueMap<String, Object> params, CriteriaBuilder criteriaBuilder, CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> root, Map.Entry<String, List<Object>> entry) {
        entry.getValue().stream().findFirst().ifPresent(value -> {
            if (NAME_PARAMETER.equals(value)) {
                sortByDirectionAndParameter(params, criteriaBuilder, criteriaQuery, root, CERTIFICATE_FIELD_NAME);
            } else if (DATE_PARAMETER.equals(value)) {
                sortByDirectionAndParameter(params, criteriaBuilder, criteriaQuery, root, CERTIFICATE_FIELD_DATE);
            }
        });
    }

    private void sortByDirectionAndParameter(MultiValueMap<String, Object> params, CriteriaBuilder criteriaBuilder, CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> root, String parameter) {
        if (params.containsKey(DIRECTION_PARAMETER) && ((String) params.get(DIRECTION_PARAMETER).get(0)).equalsIgnoreCase(DESC_STATEMENT)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(parameter)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(parameter)));
        }
    }
}
