package com.esm.epam.entity;

import com.esm.epam.entity.audit.ModificationInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "gift_certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    @NotBlank(message = "Certificate name should not be empty.")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Certificate description should not be empty.")
    private String description;

    @Column(name = "price")
    @Min(value = 0, message = "Price should be positive.")
    private int price;

    @Column(name = "duration")
    @Min(value = 0, message = "Duration should be positive.")
    private int duration;

    @Column(name = "creation_date")
    private String createDate;

    @Column(name = "last_update_date")
    private String lastUpdateDate;

    @ManyToMany(cascade = {
            MERGE
    })
    @JoinTable(name = "certificates_tags",
            joinColumns = {@JoinColumn(name = "certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;

    @ManyToMany(mappedBy = "certificates")
    private List<User> userList;

    @Embedded
    private ModificationInformation modificationInformation = new ModificationInformation();
}