package com.esm.epam.controller;

import com.esm.epam.entity.Certificate;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.dto.CertificateDTO;
import com.esm.epam.model.representation.CertificateRepresentation;
import com.esm.epam.service.CertificateService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Optional;

import static com.esm.epam.validator.ControllerValidator.validateSortValues;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/certificates")
@Validated
@AllArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;
    private final HateoasBuilder<CertificateRepresentation> hateoasBuilder;
    private final RepresentationModelAssembler<Certificate, CertificateRepresentation> certificateRepresentationAssembler;
    private final Mapper<CertificateDTO, Certificate> certificateMapper;


    @GetMapping
    @ResponseStatus(OK)
    public CollectionModel<CertificateRepresentation> getCertificateList(@RequestParam(required = false) MultiValueMap<String, Object> params, @RequestParam("page") @Min(1) int page, @RequestParam("size") @Min(1) int size) {
        validateSortValues(params);
        CollectionModel<CertificateRepresentation> certificatesRepresentation = certificateRepresentationAssembler.toCollectionModel(certificateService.getCertificates(params, page, size));
        certificatesRepresentation.forEach(hateoasBuilder::buildFullHateoas);
        return certificatesRepresentation;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public CertificateRepresentation getCertificate(@PathVariable("id") @Min(1L) long id) {
        CertificateRepresentation certificateRepresentation = certificateRepresentationAssembler.toModel(certificateService.getById(id));
        hateoasBuilder.buildFullHateoas(certificateRepresentation);
        return certificateRepresentation;
    }


    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<RepresentationModel<CertificateRepresentation>> deleteTagCertificate(@PathVariable("id") @Min(1L) long id, @PathVariable("tagId") @Min(1L) long tagId) {
        ResponseEntity<RepresentationModel<CertificateRepresentation>> responseEntity;
        Optional<Certificate> updatedCertificate = certificateService.deleteTag(id, tagId);
        if (updatedCertificate.isPresent()) {
            CertificateRepresentation updatedCertificateRepresentation = certificateRepresentationAssembler.toModel(updatedCertificate.get());
            hateoasBuilder.buildFullHateoas(updatedCertificateRepresentation);
            responseEntity = new ResponseEntity<>(updatedCertificateRepresentation, NO_CONTENT);
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RepresentationModel<CertificateRepresentation>> deleteCertificate(@PathVariable("id") @Min(1L) long id) {
        ResponseEntity<RepresentationModel<CertificateRepresentation>> responseEntity;
        if (certificateService.deleteById(id)) {
            RepresentationModel<CertificateRepresentation> representationModel = new RepresentationModel<>();
            hateoasBuilder.buildDefaultHateoas(representationModel);
            responseEntity = new ResponseEntity<>(representationModel, NO_CONTENT);
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public RepresentationModel<CertificateRepresentation> addCertificate(@RequestBody CertificateDTO certificateDTO) {
        CertificateRepresentation addedCertificateRepresentation = certificateRepresentationAssembler.toModel(certificateService.add(certificateMapper.mapEntity(certificateDTO)));
        hateoasBuilder.buildFullHateoas(addedCertificateRepresentation);
        return addedCertificateRepresentation;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public RepresentationModel<CertificateRepresentation> updateCertificate(@PathVariable("id") @Min(1L) long id, @RequestBody CertificateDTO certificateDTO) {
        CertificateRepresentation updatedCertificateRepresentation = certificateRepresentationAssembler.toModel(certificateService.update(certificateMapper.mapEntity(certificateDTO), id));
        hateoasBuilder.buildFullHateoas(updatedCertificateRepresentation);
        return updatedCertificateRepresentation;
    }
}
