package com.esm.epam.assembler;

import com.esm.epam.entity.Tag;
import com.esm.epam.model.representation.TagRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static com.esm.epam.util.ParameterAttribute.SLASH;
import static com.esm.epam.util.ParameterAttribute.TAGS_FOLDER_NAME;

@Component
public class TagRepresentationAssembler implements RepresentationModelAssembler<Tag, TagRepresentation> {
    @Override
    public TagRepresentation toModel(Tag entity) {
        return TagRepresentation.builder()
                .id(entity.getId())
                .name(entity.getName())
                .modificationInformation(entity.getModificationInformation())
                .imagePath(getImageAbsolutePath(entity))
                .build();
    }

    @Override
    public CollectionModel<TagRepresentation> toCollectionModel(Iterable<? extends Tag> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    private String getImageAbsolutePath(Tag entity) {
        String image = null;
        if (entity.getTagImage() != null && entity.getName() != null) {
            image = SLASH + TAGS_FOLDER_NAME + SLASH + entity.getName() + SLASH + entity.getTagImage();
        }
        return image;
    }
}
