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
public class CertificateRepresentation extends RepresentationModel<CertificateRepresentation> {
    private final Long id;
    private final String name;
    private final String description;
    private final int price;
    private final int duration;
    private final List<TagRepresentation> tags;
    private ModificationInformation modificationInformation;
}