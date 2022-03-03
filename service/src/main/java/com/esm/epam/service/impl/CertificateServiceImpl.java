package com.esm.epam.service.impl;

import com.esm.epam.builder.Builder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.repository.CRDDao;
import com.esm.epam.repository.CertificateDao;
import com.esm.epam.service.CertificateService;
import com.esm.epam.validator.ServiceValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.TAG;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;
    private final CRDDao<Tag> tagDao;
    private final ServiceValidator<Certificate> validator;
    private final Builder<Certificate> certificateBuilder;

    @Override
    public Certificate update(Certificate certificate, long idCertificate) {
        Optional<Certificate> certificateBeforeUpdate = certificateDao.getById(idCertificate);
        Certificate updatedCertificate;
        if (certificateBeforeUpdate.isPresent()) {
            Certificate toBeUpdatedCertificate = certificateBuilder.buildObject(certificateBeforeUpdate.get(), certificate);
            updatedCertificate = certificateDao.update(toBeUpdatedCertificate);
        } else {
            throw new ResourceNotFoundException("No such certificate");
        }
        return updatedCertificate;
    }

    @Override
    public List<Certificate> getCertificates(MultiValueMap<String, Object> params, int page, int size) {
        int actualPage = (page - 1) * size;
        List<Certificate> certificates;
        if (params.size() == 2) {
            certificates = getAll(actualPage, size);
        } else {
            certificates = getFilteredList(params, actualPage, size);
        }
        return certificates;
    }

    @Override
    public List<Certificate> getAll(int actualPage, int size) {
        return certificateDao.getAll(actualPage, size);
    }

    @Override
    public Certificate add(Certificate certificate) {
        certificate.setCreateDate(LocalDateTime.now().toString());
        return certificateDao.add(certificate);
    }

    @Override
    public Certificate getById(long id) {
        Optional<Certificate> certificate = certificateDao.getById(id);
        validator.validateEntity(certificate, id);
        return certificate.get();
    }

    @Override
    public boolean deleteById(long id) {
        return certificateDao.deleteById(id);
    }

    @Override
    public List<Certificate> getFilteredList(MultiValueMap<String, Object> params, int actualPage, int size) {
        prepareTagParam(params);
        return certificateDao.getFilteredList(params, actualPage, size);
    }

    @Override
    public Optional<Certificate> deleteTag(long id, long idTag) {
        Optional<Certificate> certificate = certificateDao.getById(id);
        validator.validateEntity(certificate, id);
        certificate.get().getTags().stream()
                .filter(localTag -> idTag == localTag.getId())
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Requested tag resource not found id = %d", idTag)));
        return certificateDao.deleteTag(id, idTag);
    }

    private void prepareTagParam(MultiValueMap<String, Object> params) {
        if (params.containsKey(TAG)) {
            List<Object> nameTags = params.get(TAG);
            List<Tag> tags = new ArrayList<>();

            nameTags.forEach(name -> {
                Optional<Tag> tag = tagDao.getByName((String) name);
                if (!tag.isPresent()) {
                    throw new ServiceException(String.format("Tag with name = %s does not exist", name));
                }
                tags.add(tag.get());
            });
            List<Object> tagsObjectList = new ArrayList<>(tags);
            params.replace(TAG, tagsObjectList);
        }
    }
}
