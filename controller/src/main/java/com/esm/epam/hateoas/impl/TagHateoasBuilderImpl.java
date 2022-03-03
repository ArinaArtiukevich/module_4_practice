package com.esm.epam.hateoas.impl;

import com.esm.epam.controller.TagController;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.model.dto.TagDTO;
import com.esm.epam.model.representation.TagRepresentation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static com.esm.epam.util.ParameterAttribute.DEFAULT_PAGE_NUMBER;
import static com.esm.epam.util.ParameterAttribute.DEFAULT_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagHateoasBuilderImpl implements HateoasBuilder<TagRepresentation> {
    @Override
    public void buildDefaultHateoas(RepresentationModel representationModel) {
        representationModel.add(linkTo(methodOn(TagController.class).getTagList(DEFAULT_PAGE_NUMBER, DEFAULT_SIZE)).withSelfRel().withType(HttpMethod.GET.toString()));
        representationModel.add(linkTo(methodOn(TagController.class).addTag(new TagDTO())).withSelfRel().withType(HttpMethod.POST.toString()));
    }

    @Override
    public void buildFullHateoas(TagRepresentation tagRepresentation) {
        buildDefaultHateoas(tagRepresentation);
        tagRepresentation.add(linkTo(methodOn(TagController.class).getTag(tagRepresentation.getId())).withSelfRel().withType(HttpMethod.GET.toString()));
        tagRepresentation.add(linkTo(methodOn(TagController.class).deleteTag(tagRepresentation.getId())).withSelfRel().withType(HttpMethod.DELETE.toString()));
    }
}
