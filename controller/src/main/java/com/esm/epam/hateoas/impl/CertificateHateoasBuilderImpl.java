package com.esm.epam.hateoas.impl;

import com.esm.epam.controller.CertificateController;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.model.dto.CertificateDTO;
import com.esm.epam.model.representation.CertificateRepresentation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.esm.epam.util.ParameterAttribute.DEFAULT_PAGE_NUMBER;
import static com.esm.epam.util.ParameterAttribute.DEFAULT_SIZE;
import static com.esm.epam.util.ParameterAttribute.NAME;
import static com.esm.epam.util.ParameterAttribute.SORT;
import static com.esm.epam.util.ParameterAttribute.TAG;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateHateoasBuilderImpl implements HateoasBuilder<CertificateRepresentation> {
    @Override
    public void buildDefaultHateoas(RepresentationModel model) {
        MultiValueMap<String, Object> localParams = new LinkedMultiValueMap<>();
        localParams.add(SORT, NAME);
        model.add(linkTo(methodOn(CertificateController.class).getCertificateList(localParams, DEFAULT_PAGE_NUMBER, DEFAULT_SIZE)).withSelfRel().withType(HttpMethod.GET.toString()));
        model.add(linkTo(methodOn(CertificateController.class).addCertificate(new CertificateDTO())).withSelfRel().withType(HttpMethod.POST.toString()));
    }

    @Override
    public void buildFullHateoas(CertificateRepresentation certificateRepresentation) {
        buildDefaultHateoas(certificateRepresentation);
        certificateRepresentation.add(linkTo(methodOn(CertificateController.class).getCertificate(certificateRepresentation.getId())).withSelfRel().withType(HttpMethod.GET.toString()));
        certificateRepresentation.add(linkTo(methodOn(CertificateController.class).deleteCertificate(certificateRepresentation.getId())).withSelfRel().withType(HttpMethod.DELETE.toString()));
        certificateRepresentation.add(linkTo(methodOn(CertificateController.class).deleteTagCertificate(certificateRepresentation.getId(), 1L)).withRel(TAG).withType(HttpMethod.DELETE.toString()));
        certificateRepresentation.add(linkTo(methodOn(CertificateController.class).updateCertificate(certificateRepresentation.getId(), new CertificateDTO())).withSelfRel().withType(HttpMethod.PATCH.toString()));
    }
}
