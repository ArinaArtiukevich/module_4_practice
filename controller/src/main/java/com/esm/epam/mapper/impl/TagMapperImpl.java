package com.esm.epam.mapper.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.entity.audit.ModificationInformation;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.mapper.AbstractMapper;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.dto.TagDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import static com.esm.epam.util.ParameterAttribute.EMPTY_STRING;
import static com.esm.epam.util.ParameterAttribute.TAGS_FOLDER_NAME;

@Component
@AllArgsConstructor
public class TagMapperImpl extends AbstractMapper implements Mapper<TagDTO, Tag> {

    @Override
    public Tag mapEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        tag.setModificationInformation(new ModificationInformation());
        if (tagDTO.getFile() != null && tagDTO.getFile().getOriginalFilename() != null && !tagDTO.getFile().getOriginalFilename().equals(EMPTY_STRING)) {
            String fileName = StringUtils.cleanPath(tagDTO.getFile().getOriginalFilename());
            tag.setTagImage(fileName);

            try {
                uploadImage(tagDTO.getFile(), tag.getName(), fileName, TAGS_FOLDER_NAME);
            } catch (IOException e) {
                throw new ControllerException("Could not save uploaded file: " + fileName);
            }
        }
        return tag;
    }
}
