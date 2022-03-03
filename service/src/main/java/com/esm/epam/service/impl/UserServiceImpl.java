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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator;
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CertificateDao certificateDao;

    @Override
    public List<User> getAll(int page, int size) {
        return userDao.getAll((page - 1) * size, size);
    }

    @Override
    public User getById(long id) {
        Optional<User> user = userDao.getById(id);
        userValidator.validateEntity(user, id);
        return user.get();
    }

    @Override
    public User update(User user, long idUser) {
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
        return orderDao.getLimitedOrders(idUser, (page - 1) * size, size);
    }

    @Override
    public Optional<Tag> getMostWidelyUsedTag() {
        return userDao.getMostWidelyUsedTag();
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
        user.getModificationInformation().setCreatedEntityBy(userBeforeUpdate.get().getModificationInformation().getCreatedEntityBy());
        user.getModificationInformation().setCreationEntityDate(userBeforeUpdate.get().getModificationInformation().getCreationEntityDate());

    }
}
