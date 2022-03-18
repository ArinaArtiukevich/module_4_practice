package com.esm.epam.service.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Order;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.repository.CertificateDao;
import com.esm.epam.repository.OrderDao;
import com.esm.epam.repository.UserDao;
import com.esm.epam.service.UserService;
import com.esm.epam.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esm.epam.entity.Role.ROLE_USER;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserValidator userValidator;
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CertificateDao certificateDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.getByLogin(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(String.format("User with login = %s was not found", username));
        }
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.get().getRole().toString()));
        return new org.springframework.security.core.userdetails.User(user.get().getLogin(), user.get().getPassword(), authorities);
    }

    @Override
    public List<User> getAll(int page, int size) {
        return userDao.getAll((page - 1) * size, size);
    }

    @Override
    public User getById(long id) {
        userValidator.validateUserId(getCurrentUser(), id);
        Optional<User> user = userDao.getById(id);
        userValidator.validateEntity(user, id);
        return user.get();
    }

    @Override
    public User update(User user, long idUser) {
        userValidator.validateUserId(getCurrentUser(), idUser);

        User updatedUser;
        Optional<Certificate> certificate;
        Optional<User> userBeforeUpdate = userDao.getById(idUser);
        userValidator.validateEntity(userBeforeUpdate, idUser);
        userValidator.validateUserToBeUpdated(user);
        certificate = certificateDao.getById(user.getCertificates().get(0).getId());
        if (!certificate.isPresent()) {
            throw new ResourceNotFoundException("No such certificate");
        }
        if (userBeforeUpdate.get().getBudget() >= certificate.get().getPrice()) {
            validateUserHasCertificate(idUser, certificate);
            Order order = getOrder(idUser, certificate);
            orderDao.addOrder(order);
            prepareUserToBeUpdated(user, certificate, userBeforeUpdate);
            updatedUser = userDao.updateBudget(user);
        } else {
            throw new ServiceException("User does not have enough money");
        }
        return updatedUser;
    }

    @Override
    public List<Order> getOrders(long idUser, int page, int size) {
        userValidator.validateUserId(getCurrentUser(), idUser);
        return orderDao.getLimitedOrders(idUser, (page - 1) * size, size);
    }

    @Override
    public Optional<Tag> getMostWidelyUsedTag() {
        return userDao.getMostWidelyUsedTag();
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return userDao.getByLogin(login);
    }

    @Override
    public User add(User user) {
        user.setRole(ROLE_USER);
        return userDao.add(user);
    }

    private void validateUserHasCertificate(long idUser, Optional<Certificate> certificate) {
        List<Long> certificatesId = orderDao.getUserOrders(idUser).stream()
                .map(Order::getIdCertificate)
                .collect(Collectors.toList());
        if (certificatesId.contains(certificate.get().getId())) {
            throw new DaoException(String.format("User has certificate with id = %d ", certificate.get().getId()));
        }
    }

    private Order getOrder(long idUser, Optional<Certificate> certificate) {
        Order order = new Order();
        order.setIdUser(idUser);
        order.setIdCertificate(certificate.get().getId());
        order.setPaymentDate(LocalDateTime.now().toString());
        order.setPrice(certificate.get().getPrice());
        return order;
    }


    private void prepareUserToBeUpdated(User user, Optional<Certificate> certificate, Optional<User> userBeforeUpdate) {
        List<Certificate> userCertificates = userBeforeUpdate.get().getCertificates();
        user.setCertificates(userCertificates);
        user.setBudget(userBeforeUpdate.get().getBudget() - certificate.get().getPrice());
        user.setId(userBeforeUpdate.get().getId());
        user.setLogin(userBeforeUpdate.get().getLogin());
        user.setRole(userBeforeUpdate.get().getRole());
        user.setPassword(userBeforeUpdate.get().getPassword());
        user.getModificationInformation().setCreatedEntityBy(userBeforeUpdate.get().getModificationInformation().getCreatedEntityBy());
        user.getModificationInformation().setCreationEntityDate(userBeforeUpdate.get().getModificationInformation().getCreationEntityDate());

    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userDao.getByLogin(authentication.getName()).get();
    }
}
