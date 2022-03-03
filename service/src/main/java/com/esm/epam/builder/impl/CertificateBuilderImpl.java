package com.esm.epam.builder.impl;

import com.esm.epam.builder.Builder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.repository.CRDDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CertificateBuilderImpl implements Builder<Certificate> {
    private final CRDDao<Tag> tagDao;

    @Override
    public Certificate buildObject(Certificate currentObject, Certificate objectToBeUpdated) {
        Certificate newCertificate = new Certificate();

        newCertificate.setId(currentObject.getId());
        newCertificate.setLastUpdateDate(LocalDateTime.now().toString());
        newCertificate.setCreateDate(currentObject.getCreateDate());
        newCertificate.getModificationInformation().setCreatedEntityBy(currentObject.getModificationInformation().getCreatedEntityBy());
        newCertificate.getModificationInformation().setCreationEntityDate(currentObject.getModificationInformation().getCreationEntityDate());

        if (objectToBeUpdated.getName() != null) {
            newCertificate.setName(objectToBeUpdated.getName());
        } else {
            newCertificate.setName(currentObject.getName());
        }

        if (objectToBeUpdated.getDescription() != null) {
            newCertificate.setDescription(objectToBeUpdated.getDescription());
        } else {
            newCertificate.setDescription(currentObject.getDescription());
        }
        if (objectToBeUpdated.getPrice() != 0) {
            newCertificate.setPrice(objectToBeUpdated.getPrice());
        } else {
            newCertificate.setPrice(currentObject.getPrice());
        }
        if (objectToBeUpdated.getDuration() != 0) {
            newCertificate.setDuration(objectToBeUpdated.getDuration());
        } else {
            newCertificate.setDuration(currentObject.getDuration());
        }
        List<Tag> tags = new ArrayList<>();
        if (objectToBeUpdated.getTags() != null) {
            addNewTags(objectToBeUpdated, tags);
        }
        if (currentObject.getTags() != null) {
            addExistedTags(tags, currentObject.getTags());
        }
        newCertificate.setTags(tags);
        return newCertificate;
    }

    private void addExistedTags(List<Tag> tags, List<Tag> currentTags) {
        currentTags.stream().filter(tag -> !tags.contains(tag)).forEach(tags::add);
    }

    private void addNewTags(Certificate objectToBeUpdated, List<Tag> tags) {
        List<Tag> tagsToBeAdded = objectToBeUpdated.getTags();
        tagsToBeAdded.stream().filter(tag -> (tag.getId() == null && tag.getName() != null)).forEach(tag -> addTagToList(tagDao.getByName(tag.getName()), tags));
        tagsToBeAdded.stream().filter(tag -> (tag.getId() != null && tag.getName() == null)).forEach(tag -> addTagToList(tagDao.getById(tag.getId()), tags));
        tagsToBeAdded.stream().filter(tag -> (tag.getId() != null && tag.getName() != null)).forEach(tags::add);

    }

    private void addTagToList(Optional<Tag> tagToBeAdded, List<Tag> tags) {
        if (tagToBeAdded.isPresent() && !tags.contains(tagToBeAdded.get())) {
            tags.add(tagToBeAdded.get());
        }
    }
}
