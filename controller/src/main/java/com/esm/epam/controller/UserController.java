package com.esm.epam.controller;

import com.esm.epam.entity.Order;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.dto.UserDTO;
import com.esm.epam.model.representation.OrderRepresentation;
import com.esm.epam.model.representation.TagRepresentation;
import com.esm.epam.model.representation.UserRepresentation;
import com.esm.epam.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
@Validated
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final HateoasBuilder<UserRepresentation> hateoasBuilder;
    private final RepresentationModelAssembler<User, UserRepresentation> userRepresentationModelAssembler;
    private final RepresentationModelAssembler<Tag, TagRepresentation> tagRepresentationModelAssembler;
    private final RepresentationModelAssembler<Order, OrderRepresentation> orderRepresentationModelAssembler;
    private final Mapper<UserDTO, User> userMapper;

    @GetMapping
    @ResponseStatus(OK)
    public CollectionModel<UserRepresentation> getUserList(@RequestParam("page") @Min(1) int page, @RequestParam("size") @Min(1) int size) {
        CollectionModel<UserRepresentation> usersRepresentation = userRepresentationModelAssembler.toCollectionModel(userService.getAll(page, size));
        usersRepresentation.forEach(hateoasBuilder::buildFullHateoas);
        return usersRepresentation;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public UserRepresentation getUser(@PathVariable("id") @Min(1L) long id) {
        UserRepresentation userRepresentation = userRepresentationModelAssembler.toModel(userService.getById(id));
        hateoasBuilder.buildFullHateoas(userRepresentation);
        return userRepresentation;
    }

    @GetMapping(value = "/{id}/orders")
    @ResponseStatus(OK)
    public CollectionModel<OrderRepresentation> getUserOrders(@PathVariable("id") @Min(1L) long id, @RequestParam("page") @Min(1) int page, @RequestParam("size") @Min(1) int size) {
        CollectionModel<OrderRepresentation> orders = orderRepresentationModelAssembler.toCollectionModel(userService.getOrders(id, page, size));
        orders.forEach(hateoasBuilder::buildDefaultHateoas);
        return orders;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public RepresentationModel<UserRepresentation> updateUser(@PathVariable("id") @Min(1L) long id, @RequestBody UserDTO userDTO) {
        UserRepresentation updatedUserRepresentation = userRepresentationModelAssembler.toModel(userService.update(userMapper.mapEntity(userDTO), id));
        hateoasBuilder.buildFullHateoas(updatedUserRepresentation);
        return updatedUserRepresentation;
    }

    @GetMapping("/mostWidelyUsedTag")
    public ResponseEntity<RepresentationModel<TagRepresentation>> getMostWidelyUsedTag() {
        ResponseEntity<RepresentationModel<TagRepresentation>> responseEntity;
        Optional<Tag> requiredTag = userService.getMostWidelyUsedTag();
        if (requiredTag.isPresent()) {
            TagRepresentation tagRepresentation = tagRepresentationModelAssembler.toModel(requiredTag.get());
            hateoasBuilder.buildDefaultHateoas(tagRepresentation);
            responseEntity = new ResponseEntity<>(tagRepresentation, OK);
        } else {
            responseEntity = ResponseEntity.noContent().build();
        }
        return responseEntity;
    }
}