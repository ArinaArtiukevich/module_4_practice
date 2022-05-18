package com.esm.epam.mapper.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.audit.ModificationInformation;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.dto.CertificateDTO;
import com.esm.epam.model.dto.TagDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esm.epam.util.ParameterAttribute.EMPTY_STRING;
import static com.esm.epam.util.ParameterAttribute.IMAGES_FOLDER_PATH;

@Component
@AllArgsConstructor
public class CertificateMapperImpl implements Mapper<CertificateDTO, Certificate> {
    private Mapper<TagDTO, Tag> tagMapper;

    @Override
    public Certificate mapEntity(CertificateDTO certificateDTO) {
        Certificate certificate = Certificate.builder()
                .id(certificateDTO.getId())
                .name(certificateDTO.getName())
                .description(certificateDTO.getDescription())
                .price(certificateDTO.getPrice())
                .duration(certificateDTO.getDuration())
                .modificationInformation(new ModificationInformation())
                .build();

        if (certificateDTO.getFile() != null && certificateDTO.getFile().getOriginalFilename() != null && !certificateDTO.getFile().getOriginalFilename().equals(EMPTY_STRING)) {
            String fileName = StringUtils.cleanPath(certificateDTO.getFile().getOriginalFilename());
            certificate.setImage(fileName);

            try {
                uploadImage(certificateDTO, certificate, fileName);
            } catch (IOException e) {
                throw new ControllerException("Could not save uploaded file: " + fileName);
            }
        }
        List<Tag> tags = Optional.ofNullable(certificateDTO.getTags())
                .orElseGet(Collections::emptyList).stream()
                .map(tagMapper::mapEntity)
                .collect(Collectors.toList());

        certificate.setTags(tags);
        return certificate;
    }

    private void uploadImage(CertificateDTO certificateDTO, Certificate certificate, String fileName) throws IOException {
        String uploadDir = IMAGES_FOLDER_PATH + certificate.getName();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        InputStream inputStream = certificateDTO.getFile().getInputStream();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
