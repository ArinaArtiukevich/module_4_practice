package com.esm.epam.repository;

import com.esm.epam.entity.Certificate;

import java.util.Optional;

public interface CertificateDao extends CRUDDao<Certificate> {
    /**
     * deletes tag by tag's id
     *
     * @param id    is id of element with tags
     * @param idTag is id of tag to be deleted
     * @return element with updated tags
     */
    Optional<Certificate> deleteTag(long id, long idTag);
}
