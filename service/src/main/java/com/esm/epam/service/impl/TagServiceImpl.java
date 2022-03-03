package com.esm.epam.service.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.repository.CRDDao;
import com.esm.epam.service.CRDService;
import com.esm.epam.validator.ServiceValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TagServiceImpl implements CRDService<Tag> {
    private final CRDDao<Tag> tagDao;
    private final ServiceValidator<Tag> validator;

    @Override
    public List<Tag> getAll(int page, int size) {
        return tagDao.getAll((page - 1) * size, size);
    }

    @Override
    public Tag add(Tag tag) {
        return tagDao.add(tag);
    }

    @Override
    public Tag getById(long id) {
        Optional<Tag> tag = tagDao.getById(id);
        validator.validateEntity(tag, id);
        return tag.get();
    }

    @Override
    public boolean deleteById(long id) {
        return tagDao.deleteById(id);
    }
}
