package com.esm.epam.assembler;

import com.esm.epam.entity.Tag;
import com.esm.epam.model.representation.TagRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TagRepresentationAssembler implements RepresentationModelAssembler<Tag, TagRepresentation> {
    @Override
    public TagRepresentation toModel(Tag entity) {
        return TagRepresentation.builder()
                .id(entity.getId())
                .name(entity.getName())
                .modificationInformation(entity.getModificationInformation())
                .build();
    }

    @Override
    public CollectionModel<TagRepresentation> toCollectionModel(Iterable<? extends Tag> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);

    }
}
