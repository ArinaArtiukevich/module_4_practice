package com.esm.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDTO {
    private long id;
    private String name;
    private String description;
    private int price;
    private int duration;
    private MultipartFile file;
    private List<TagDTO> tags;
}