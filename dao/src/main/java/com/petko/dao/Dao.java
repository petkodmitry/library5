package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities2.Entity;

import java.util.List;

public interface Dao<T extends Entity> {
    /**
     * adds entity in database
     * @param entity - entity
     */
    void saveOrUpdate(T entity) throws DaoException;

    /**
     * gives a list of all elements in the DB
     * @return List of all elements
     */
    List<T> getAll(int first, int max) throws DaoException;

    /**
     * gives Entity by id
     * @param id - id of looking Entity
     * @return Entity by id
     */
    T getById(int id) throws DaoException;

    /**
     * deletes Entity
     * @param entity - Entity to be deleted
     */
    void delete(T entity) throws DaoException;
}
