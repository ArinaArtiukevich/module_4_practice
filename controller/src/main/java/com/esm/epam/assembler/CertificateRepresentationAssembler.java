package com.esm.epam.assembler;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.model.representation.CertificateRepresentation;
import com.esm.epam.model.representation.TagRepresentation;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esm.epam.util.ParameterAttribute.CERTIFICATES_FOLDER_NAME;
import static com.esm.epam.util.ParameterAttribute.IMAGES_FOLDER_NAME;
import static com.esm.epam.util.ParameterAttribute.SLASH;
import static com.esm.epam.util.ParameterAttribute.TAGS_FOLDER_NAME;

@Component
@AllArgsConstructor
public class CertificateRepresentationAssembler implements RepresentationModelAssembler<Certificate, CertificateRepresentation> {
    private final RepresentationModelAssembler<Tag, TagRepresentation> tagRepresentationAssembler;

    @Override
    public CertificateRepresentation toModel(Certificate entity) {
        List<TagRepresentation> tagsRepresentation = Optional.ofNullable(entity.getTags())
                .orElseGet(Collections::emptyList).stream()
                .map(tagRepresentationAssembler::toModel)
                .collect(Collectors.toList());

        return CertificateRepresentation.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .duration(entity.getDuration())
                .tags(tagsRepresentation)
                .imagePath(getImageAbsolutePath(entity))
                .modificationInformation(entity.getModificationInformation())
                .build();
    }

    @Override
    public CollectionModel<CertificateRepresentation> toCollectionModel(Iterable<? extends Certificate> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    private String getImageAbsolutePath(Certificate entity) {
        String image = null;
        if (entity.getImage() != null && entity.getName() != null) {
            image = SLASH + CERTIFICATES_FOLDER_NAME + SLASH + entity.getName() + SLASH + entity.getImage();
        }
        return image;
    }
}