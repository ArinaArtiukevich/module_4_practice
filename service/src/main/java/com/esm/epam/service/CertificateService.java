package com.esm.epam.service;

import com.esm.epam.entity.Certificate;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

public interface CertificateService extends CRUDService<Certificate> {
    /**
     * deletes tag by tag's id
     *
     * @param id    is id of element with tags
     * @param idTag is id of tag to be deleted
     * @return element with updated tags
     */
    Optional<Certificate> deleteTag(long id, long idTag);


    /**
     * gets certificates depending on params
     *
     * @param params collection that contains {@link String} as
     *               key and {@link Object} as value
     * @param page   is started element
     * @param size   the number of items to be returned
     * @return List with values
     */
    List<Certificate> getCertificates(MultiValueMap<String, Object> params, int page, int size);
}
