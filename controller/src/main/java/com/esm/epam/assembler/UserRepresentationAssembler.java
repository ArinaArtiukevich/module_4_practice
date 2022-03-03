package com.esm.epam.assembler;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.User;
import com.esm.epam.model.representation.CertificateRepresentation;
import com.esm.epam.model.representation.UserRepresentation;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserRepresentationAssembler implements RepresentationModelAssembler<User, UserRepresentation> {
    private final RepresentationModelAssembler<Certificate, CertificateRepresentation> certificateRepresentationModelAssembler;

    @Override
    public UserRepresentation toModel(User entity) {
        List<CertificateRepresentation> certificateRepresentations = Optional.ofNullable(entity.getCertificates())
                .orElseGet(Collections::emptyList).stream()
                .map(certificateRepresentationModelAssembler::toModel)
                .collect(Collectors.toList());
        return UserRepresentation.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .budget(entity.getBudget())
                .certificates(certificateRepresentations)
                .modificationInformation(entity.getModificationInformation())
                .build();
    }

    @Override
    public CollectionModel<UserRepresentation> toCollectionModel(Iterable<? extends User> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
