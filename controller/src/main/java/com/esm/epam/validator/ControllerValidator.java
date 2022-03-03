package com.esm.epam.validator;

import com.esm.epam.exception.ControllerException;
import org.springframework.util.MultiValueMap;

import static com.esm.epam.util.ParameterAttribute.SORT_KEYS;

public class ControllerValidator {
    private ControllerValidator() {
    }

    public static void validateSortValues(MultiValueMap<String, Object> params) {
        if (params.keySet().stream().anyMatch(key -> !SORT_KEYS.contains(key))) {
            throw new ControllerException("Invalid filter key.");
        }
    }
}
