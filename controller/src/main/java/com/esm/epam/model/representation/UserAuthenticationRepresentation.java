package com.esm.epam.model.representation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserAuthenticationRepresentation {
    private final String token;
    private final UserRepresentation userRepresentation;
}
