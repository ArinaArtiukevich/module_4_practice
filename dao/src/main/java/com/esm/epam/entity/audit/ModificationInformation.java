package com.esm.epam.entity.audit;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

import static com.esm.epam.util.ParameterAttribute.USER_NAME;
@Component
@Embeddable
@Data
public class ModificationInformation {
    @Column
    private LocalDateTime creationEntityDate;

    @Column
    private LocalDateTime modificationEntityDate;

    @Column
    private String createdEntityBy;

    @Column
    private String modifiedEntityBy;


    @PrePersist
    public void onPrePersist() {
        if (this.getCreationEntityDate() == null) {
            this.setCreationEntityDate(LocalDateTime.now());
            this.setCreatedEntityBy(USER_NAME);
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setModificationEntityDate(LocalDateTime.now());
        this.setModifiedEntityBy(USER_NAME);
    }

}