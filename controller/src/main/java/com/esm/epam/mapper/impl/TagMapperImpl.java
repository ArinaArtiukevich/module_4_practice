package com.esm.epam.mapper.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.entity.audit.ModificationInformation;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.dto.TagDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TagMapperImpl implements Mapper<TagDTO, Tag> {

    @Override
    public Tag mapEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        tag.setModificationInformation(new ModificationInformation());
        return tag;
    }
}
