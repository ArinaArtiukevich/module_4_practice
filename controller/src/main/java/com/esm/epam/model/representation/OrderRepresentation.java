package com.esm.epam.model.representation;

import com.esm.epam.entity.audit.ModificationInformation;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class OrderRepresentation extends RepresentationModel<OrderRepresentation> {
    private final Long id;
    private final long idUser;
    private final long idCertificate;
    private final int price;
    private final String paymentDate;
    private ModificationInformation modificationInformation;
}