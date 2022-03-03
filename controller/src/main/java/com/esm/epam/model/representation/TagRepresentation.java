package com.esm.epam.model.representation;

import com.esm.epam.entity.audit.ModificationInformation;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class TagRepresentation extends RepresentationModel<TagRepresentation> {
    private final Long id;
    private final String name;
    private final ModificationInformation modificationInformation;
}