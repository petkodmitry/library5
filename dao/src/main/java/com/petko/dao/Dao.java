package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.Entity;

import java.util.List;
import java.util.Map;

public interface Dao<T extends Entity> {
    /**
     * adds entity in database
     * @param entity - new entity
     */
    void save(T entity) throws DaoException;

    /**
     * updates entity in database
     * @param entity - entity
     */
    void update(T entity) throws DaoException;

    /**
     * gives a list of all elements in the DB
     * @return List of all elements
     */
    List<T> getAll(int first, int max, String sortBy, String orderType, Map<String, String> filters) throws DaoException;

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
