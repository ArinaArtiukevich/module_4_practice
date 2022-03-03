package com.esm.epam.mapper.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.audit.ModificationInformation;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.dto.CertificateDTO;
import com.esm.epam.model.dto.TagDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CertificateMapperImpl implements Mapper<CertificateDTO, Certificate> {
    private Mapper<TagDTO, Tag> tagMapper;

    @Override
    public Certificate mapEntity(CertificateDTO certificateDTO) {

        List<Tag> tags = Optional.ofNullable(certificateDTO.getTags())
                .orElseGet(Collections::emptyList).stream()
                .map(tagMapper::mapEntity)
                .collect(Collectors.toList());

        return Certificate.builder()
                .id(certificateDTO.getId())
                .name(certificateDTO.getName())
                .description(certificateDTO.getDescription())
                .price(certificateDTO.getPrice())
                .duration(certificateDTO.getDuration())
                .tags(tags)
                .modificationInformation(new ModificationInformation())
                .build();
    }
}
