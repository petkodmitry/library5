package com.petko.dao;

import com.petko.entitiesOLD.EntityOLD;

import java.util.List;

public interface DaoOLD<T extends EntityOLD> {
    /**
     * adds entity in database
     * @param entity - entity
     */
    void add(T entity);
    List<T> getAll();
    T getById(int id);
    void delete(int id);
}
