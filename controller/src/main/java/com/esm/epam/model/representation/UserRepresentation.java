package com.esm.epam.model.representation;

import com.esm.epam.entity.audit.ModificationInformation;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserRepresentation extends RepresentationModel<UserRepresentation> {
    private final Long id;
    private final String login;
    private final int budget;
    private final List<CertificateRepresentation> certificates;
    private final ModificationInformation modificationInformation;
}