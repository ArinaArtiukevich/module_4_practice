package com.esm.epam.controller;

import com.esm.epam.entity.Tag;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.dto.TagDTO;
import com.esm.epam.model.representation.TagRepresentation;
import com.esm.epam.service.CRDService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/tags")
@Validated
@AllArgsConstructor
public class TagController {
    private final CRDService<Tag> tagService;
    private final HateoasBuilder<TagRepresentation> hateoasBuilder;
    private final RepresentationModelAssembler<Tag, TagRepresentation> tagRepresentationAssembler;
    private final Mapper<TagDTO, Tag> tagMapper;

    @GetMapping
    @ResponseStatus(OK)
    public CollectionModel<TagRepresentation> getTagList(@RequestParam("page") @Min(1) int page, @RequestParam("size") @Min(1) int size) {
        CollectionModel<TagRepresentation> tagsRepresentation = tagRepresentationAssembler.toCollectionModel(tagService.getAll(page, size));
        tagsRepresentation.forEach(hateoasBuilder::buildFullHateoas);
        return tagsRepresentation;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public TagRepresentation getTag(@PathVariable("id") @Min(1L) long id) {
        TagRepresentation tagRepresentation = tagRepresentationAssembler.toModel(tagService.getById(id));
        hateoasBuilder.buildFullHateoas(tagRepresentation);
        return tagRepresentation;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RepresentationModel<TagRepresentation>> deleteTag(@PathVariable("id") @Min(1L) long id) {
        ResponseEntity<RepresentationModel<TagRepresentation>> responseEntity;
        if (tagService.deleteById(id)) {
            RepresentationModel<TagRepresentation> representationModel = new RepresentationModel<>();
            hateoasBuilder.buildDefaultHateoas(representationModel);
            responseEntity = new ResponseEntity<>(representationModel, NO_CONTENT);
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public RepresentationModel<TagRepresentation> addTag(@RequestBody TagDTO tagDTO) {
        TagRepresentation addedTagRepresentation = tagRepresentationAssembler.toModel(tagService.add(tagMapper.mapEntity(tagDTO)));
        hateoasBuilder.buildFullHateoas(addedTagRepresentation);
        return addedTagRepresentation;
    }

}