package com.esm.epam.assembler;

import com.esm.epam.entity.Order;
import com.esm.epam.model.representation.OrderRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class OrderRepresentationAssembler implements RepresentationModelAssembler<Order, OrderRepresentation> {
    @Override
    public OrderRepresentation toModel(Order entity) {
        return OrderRepresentation.builder()
                .id(entity.getId())
                .idUser(entity.getIdUser())
                .idCertificate(entity.getIdCertificate())
                .price(entity.getPrice())
                .paymentDate(entity.getPaymentDate())
                .modificationInformation(entity.getModificationInformation())
                .build();
    }

    @Override
    public CollectionModel<OrderRepresentation> toCollectionModel(Iterable<? extends Order> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
