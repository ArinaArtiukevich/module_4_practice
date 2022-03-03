package com.esm.epam.hateoas.impl;

import com.esm.epam.controller.UserController;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.model.dto.UserDTO;
import com.esm.epam.model.representation.UserRepresentation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static com.esm.epam.util.ParameterAttribute.DEFAULT_PAGE_NUMBER;
import static com.esm.epam.util.ParameterAttribute.DEFAULT_SIZE;
import static com.esm.epam.util.ParameterAttribute.MOST_WIDELY_USED_TAG;
import static com.esm.epam.util.ParameterAttribute.ORDER;
import static com.esm.epam.util.ParameterAttribute.TAG;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasBuilderImpl implements HateoasBuilder<UserRepresentation> {
    @Override
    public void buildDefaultHateoas(RepresentationModel model) {
        model.add(linkTo(methodOn(UserController.class).getUserList(DEFAULT_PAGE_NUMBER, DEFAULT_SIZE)).withSelfRel().withType(HttpMethod.GET.toString()));
        model.add(linkTo(methodOn(UserController.class).getMostWidelyUsedTag()).slash(MOST_WIDELY_USED_TAG).withRel(TAG).withType(HttpMethod.GET.toString()));
    }

    @Override
    public void buildFullHateoas(UserRepresentation userRepresentation) {
        buildDefaultHateoas(userRepresentation);
        userRepresentation.add(linkTo(methodOn(UserController.class).getUser(userRepresentation.getId())).withSelfRel().withType(HttpMethod.GET.toString()));
        userRepresentation.add(linkTo(methodOn(UserController.class).getUserOrders(userRepresentation.getId(), DEFAULT_PAGE_NUMBER, DEFAULT_SIZE)).withRel(ORDER).withType(HttpMethod.GET.toString()));
        userRepresentation.add(linkTo(methodOn(UserController.class).updateUser(userRepresentation.getId(), new UserDTO())).withSelfRel().withType(HttpMethod.PATCH.toString()));
    }
}
