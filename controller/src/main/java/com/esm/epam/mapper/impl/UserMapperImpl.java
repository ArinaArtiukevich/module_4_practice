package com.esm.epam.mapper.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.User;
import com.esm.epam.entity.audit.ModificationInformation;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.dto.CertificateDTO;
import com.esm.epam.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserMapperImpl implements Mapper<UserDTO, User> {
    private Mapper<CertificateDTO, Certificate> certificateMapper;

    @Override
    public User mapEntity(UserDTO userDTO) {
        List<Certificate> certificates = Optional.ofNullable(userDTO.getCertificates())
                .orElseGet(Collections::emptyList).stream()
                .map(certificateMapper::mapEntity)
                .collect(Collectors.toList());
        return User.builder()
                .id(userDTO.getId())
                .login(userDTO.getLogin())
                .budget(userDTO.getBudget())
                .certificates(certificates)
                .modificationInformation(new ModificationInformation())
                .build();
    }
}
