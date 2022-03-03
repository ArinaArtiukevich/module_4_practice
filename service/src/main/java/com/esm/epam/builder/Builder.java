package com.esm.epam.builder;

public interface Builder<T> {
    /**
     * builds new object
     *
     * @param currentObject     is current object
     * @param objectToBeUpdated is object with fields to be updated
     * @return object with updated fields
     */
    T buildObject(T currentObject, T objectToBeUpdated);
}
