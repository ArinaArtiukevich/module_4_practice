package com.esm.epam.controller;

import com.esm.epam.entity.User;
import com.esm.epam.exception.InvalidCredentialsException;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.jwt.provider.JwtProvider;
import com.esm.epam.mapper.Mapper;
import com.esm.epam.model.authentication.AuthenticationRequest;
import com.esm.epam.model.authentication.AuthenticationResponse;
import com.esm.epam.model.dto.UserDTO;
import com.esm.epam.model.representation.UserRepresentation;
import com.esm.epam.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@Validated
@AllArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final HateoasBuilder<UserRepresentation> hateoasBuilder;
    private final RepresentationModelAssembler<User, UserRepresentation> userRepresentationModelAssembler;
    private final Mapper<UserDTO, User> userMapper;
    private JwtProvider jwtProvider;

    @PostMapping("/signup")
    @ResponseStatus(CREATED)
    public RepresentationModel<UserRepresentation> addUser(@RequestBody UserDTO userDTO) {
        UserRepresentation addedUserRepresentation = userRepresentationModelAssembler.toModel(userService.add(userMapper.mapEntity(userDTO)));
        hateoasBuilder.buildFullHateoas(addedUserRepresentation);
        return addedUserRepresentation;
    }

    @PostMapping("/signin")
    @ResponseStatus(OK)
    public AuthenticationResponse signInUser(@RequestBody AuthenticationRequest authenticationRequest) {
        Optional<User> user = userService.getByLogin(authenticationRequest.getLogin());
        AuthenticationResponse authenticationResponse;
        if (user.isPresent() && BCrypt.checkpw(authenticationRequest.getPassword(), user.get().getPassword())) {
            authenticationResponse = new AuthenticationResponse(jwtProvider.generateToken(authenticationRequest.getLogin()));
        } else {
            throw new InvalidCredentialsException("Invalid password or login");
        }
        return authenticationResponse;
    }
}
