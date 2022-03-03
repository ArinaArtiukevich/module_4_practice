package com.esm.epam.service.impl;

import com.esm.epam.builder.Builder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.repository.impl.CertificateDaoImpl;
import com.esm.epam.service.impl.CertificateServiceImpl;
import com.esm.epam.validator.impl.ServiceCertificateValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class CertificateServiceImplTest {

    @Mock
    private CertificateDaoImpl certificateDao = Mockito.mock(CertificateDaoImpl.class);

    @Mock
    private ServiceCertificateValidatorImpl validator = Mockito.mock(ServiceCertificateValidatorImpl.class);

    @Mock
    private Builder<Certificate> certificateBuilder;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    private final Tag tag = Tag.builder().id(10L).name("indoors").build();
    private final Certificate certificateWithFieldsToBeUpdated = Certificate.builder()
            .name("football")
            .description("playing football")
            .build();
    private final Certificate newCertificate = Certificate.builder()
            .id(5L)
            .name("tennis")
            .description("playing tennis")
            .price(204)
            .duration(30)
            .build();
    private final Certificate certificate = Certificate.builder()
            .id(1L)
            .name("skiing")
            .description("skiing in alps")
            .price(200)
            .duration(100)
            .tags(Collections.singletonList(tag))
            .build();

    private final List<Certificate> certificates = Arrays.asList(Certificate.builder()
                    .id(2L)
                    .name("snowboarding")
                    .description("snowboarding school")
                    .price(1440)
                    .duration(12)
                    .build(),
            Certificate.builder()
                    .id(3L)
                    .name("sneakers")
                    .description("clothing and presents")
                    .price(200)
                    .duration(1)
                    .tags(Collections.singletonList(Tag.builder().id(1L).name("tag_paper").build()))
                    .build(),
            Certificate.builder()
                    .id(4L)
                    .name("hockey")
                    .description("sport")
                    .price(120)
                    .duration(62)
                    .tags(Collections.singletonList(Tag.builder().id(2L).name("tag_name").build()))
                    .build()
    );

    @Test
    void testUpdate_positive() {
        Certificate expectedCertificate = Certificate.builder().id(1L)
                .name("football")
                .description("playing football")
                .price(200)
                .duration(100)
                .tags(Collections.singletonList(tag))
                .build();
        when(certificateDao.update(expectedCertificate)).thenReturn(expectedCertificate);
        when(certificateDao.getById(1L)).thenReturn(Optional.ofNullable(certificate));
        when(certificateBuilder.buildObject(certificate, certificateWithFieldsToBeUpdated)).thenReturn(expectedCertificate);
        when(certificateDao.getAll(0, 1000)).thenReturn(Collections.singletonList(certificate));

        certificateService.update(certificateWithFieldsToBeUpdated, 1L);
        Certificate actualCertificate = certificateService.update(certificateWithFieldsToBeUpdated, 1L);

        assertEquals(expectedCertificate, actualCertificate);
    }

    @Test
    void testGetAll_positive() {
        when(certificateDao.getAll(0, 1000)).thenReturn(certificates);
        List<Certificate> actualCertificates = certificateService.getAll(0, 1000);
        assertEquals(certificates, actualCertificates);
    }


    @Test
     void testAdd_positive() {
        long newId = 5L;
        Certificate addedCertificate = Certificate.builder()
                .id(newId)
                .name("tennis")
                .description("playing tennis")
                .price(204)
                .duration(30)
                .build();
        when(certificateDao.add(newCertificate)).thenReturn(addedCertificate);
        Optional<Certificate> actualCertificate = Optional.ofNullable(certificateService.add(newCertificate));
        assertEquals(addedCertificate, actualCertificate.get());
    }


    @Test
     void testGetById_positive() {
        when(certificateDao.getById(1L)).thenReturn(Optional.ofNullable(certificate));
        Certificate actualCertificate = certificateService.getById(1L);
        assertEquals(certificate, actualCertificate);
    }

    @Test
     void testDeleteById_positive() {
        when(certificateDao.deleteById(2L)).thenReturn(true);
        certificateService.deleteById(certificates.get(0).getId());
        Mockito.verify(certificateDao).deleteById(certificates.get(0).getId());
    }

    @Test
    void testDeleteById() {
        boolean expectedResult = false;
        long invalidId = -1L;
        when(certificateDao.deleteById(invalidId)).thenReturn(false);
        Boolean actualResult = certificateService.deleteById(invalidId);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void testGetFilteredList_filterByPartName() {
        String partName = "ing";
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("name", partName);
        List<Certificate> list = new LinkedList<>(certificates);

        doAnswer(new Answer<List<Certificate>>() {
            public List<Certificate> answer(InvocationOnMock invocation) {
                list.removeIf(e -> !e.getName().contains(partName));
                return list;
            }
        }).when(certificateDao).getFilteredList(params, 0, 1000);

        List<Certificate> actualCertificates = certificateService.getFilteredList(params, 0, 1000);
        assertEquals(list, actualCertificates);
    }

    @Test
    void testDeleteTag_positive() {
        Certificate certificateBefore = Certificate.builder()
                .id(1L)
                .name("skiing")
                .description("skiing in alps")
                .price(200)
                .duration(100)
                .build();
        certificateBefore.setTags(new LinkedList<>(Arrays.asList(Tag.builder().id(1L).name("tag_concert").build(), tag)));
        when(certificateDao.getById(1L)).thenReturn(Optional.of(certificateBefore));
        doAnswer(new Answer<Optional<Certificate>>() {
            public Optional<Certificate> answer(InvocationOnMock invocation) {
                certificateBefore.getTags().remove(0);
                return Optional.of(certificateBefore);
            }
        }).when(certificateDao).deleteTag(1L, 1L);

        Optional<Certificate> actualCertificate = certificateService.deleteTag(1L, 1L);
        assertEquals(certificate, actualCertificate.get());
    }
}