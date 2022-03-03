package com.esm.epam.entity;

import com.esm.epam.entity.audit.ModificationInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;

    @Column(name = "user_id")
    @Min(value = 0, message = "Id user should be positive.")
    private long idUser;

    @Column(name = "certificate_id")
    @Min(value = 0, message = "Id certificate should be positive.")
    private long idCertificate;

    @Column(name = "price")
    @Min(value = 0, message = "Price should be positive.")
    private int price;

    @Column(name = "payment_date")
    @NotBlank(message = "Payment date should not be empty.")
    private String paymentDate;

    @Embedded
    private ModificationInformation modificationInformation = new ModificationInformation();

}
