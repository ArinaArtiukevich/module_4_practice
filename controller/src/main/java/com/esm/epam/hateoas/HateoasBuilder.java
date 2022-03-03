package com.esm.epam.hateoas;

import org.springframework.hateoas.RepresentationModel;

/**
 * @param <T> describes type parameter
 */
public interface HateoasBuilder<T extends RepresentationModel<? extends T>> {
    /**
     * builds hateos with default links
     *
     * @param model is entity to collect links
     */
    void buildDefaultHateoas(RepresentationModel model);

    /**
     * builds hateos with extended number of links
     *
     * @param t is entity to collect links
     */
    void buildFullHateoas(T t);
}
