package com.esm.epam.service.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.entity.audit.ModificationInformation;
import com.esm.epam.repository.impl.CertificateDaoImpl;
import com.esm.epam.repository.impl.OrderDaoImpl;
import com.esm.epam.repository.impl.UserDaoImpl;
import com.esm.epam.service.impl.UserServiceImpl;
import com.esm.epam.validator.impl.ServiceUserValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    private final List<User> users = Arrays.asList(
            User.builder().id(1L).login("arina").budget(100).certificates(Arrays.asList(Certificate.builder()
                                    .id(1L)
                                    .name("sneakers")
                                    .description("clothing and presents")
                                    .price(200)
                                    .duration(1)
                                    .tags(Arrays.asList(Tag.builder().id(1L).name("tag_paper").build(), Tag.builder().id(2L).name("tag_name").build()))
                                    .build(),
                            Certificate.builder()
                                    .id(2L)
                                    .name("hockey")
                                    .description("sport")
                                    .price(120)
                                    .duration(62)
                                    .tags(Collections.singletonList(Tag.builder().id(2L).name("tag_name").build()))
                                    .build()))
                    .build(),
            User.builder().id(2L).login("viktor").budget(1020).certificates(Arrays.asList(
                            Certificate.builder()
                                    .id(2L)
                                    .name("hockey")
                                    .description("sport")
                                    .price(120)
                                    .duration(62)
                                    .tags(Collections.singletonList(Tag.builder().id(2L).name("tag_name").build()))
                                    .build(),
                            Certificate.builder()
                                    .id(3L)
                                    .name("snowboarding")
                                    .description("snowboarding school")
                                    .price(10)
                                    .duration(12)
                                    .build()))
                    .build()
    );

    private final Certificate certificate = Certificate.builder()
            .id(4L)
            .name("skiing")
            .description("skiing in alps")
            .price(200)
            .duration(100)
            .build();

    @Mock
    private UserDaoImpl userDao = Mockito.mock(UserDaoImpl.class);

    @Mock
    private OrderDaoImpl orderDao = Mockito.mock(OrderDaoImpl.class);

    @Mock
    private CertificateDaoImpl certificateDao = Mockito.mock(CertificateDaoImpl.class);

    @Mock
    private ServiceUserValidatorImpl validator = Mockito.mock(ServiceUserValidatorImpl.class);

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAll_positive() {
        when(userDao.getAll(0, 1000)).thenReturn(users);
        List<User> actualUsers = userService.getAll(1, 1000);
        assertEquals(users, actualUsers);
    }

    @Test
    void getById() {
        User expectedUser = users.get(1);
        when(userDao.getById(1L)).thenReturn(Optional.ofNullable(users.get(1)));
        User actualUser = userService.getById(1L);
        assertEquals(expectedUser, actualUser);

    }

    @Test
    void update() {
        ModificationInformation modificationInformation = new ModificationInformation();
        modificationInformation.setCreatedEntityBy("arina");
        modificationInformation.setCreationEntityDate(LocalDateTime.of(2022, 2, 20, 9, 10));
        User expectedUser = User.builder().id(2L).login("viktor").budget(820).modificationInformation(modificationInformation).certificates(Arrays.asList(
                        Certificate.builder()
                                .id(4L)
                                .name("skiing")
                                .description("skiing in alps")
                                .price(200)
                                .duration(100)
                                .build()))
                .build();


        User userToBeUpdated = User.builder().certificates(Arrays.asList(certificate)).build();
        userToBeUpdated.setModificationInformation(modificationInformation);

        when(userDao.getById(2L)).thenReturn(Optional.ofNullable(users.get(1)));
        doAnswer(new Answer<Optional<User>>() {
            public Optional<User> answer(InvocationOnMock invocation) {
                users.get(1).setModificationInformation(modificationInformation);
                return Optional.of(users.get(1));
            }
        }).when(userDao).getById(2L);

        when(userDao.updateBudget(expectedUser)).thenReturn(expectedUser);
        when(certificateDao.getById(4L)).thenReturn(Optional.ofNullable(certificate));
        User actualUser = userService.update(expectedUser, 2L);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getMostWidelyUsedTag() {
        Tag expectedTag = Tag.builder().id(2L).name("tag_name").build();
        doReturn(Optional.of(expectedTag)).when(userDao).getMostWidelyUsedTag();
        Optional<Tag> actualTag = userService.getMostWidelyUsedTag();
        assertEquals(expectedTag, actualTag.get());
    }
}