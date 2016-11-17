package com.petko.services;

import com.petko.entities.EntityOLD;

import java.util.List;

public interface Service<T extends EntityOLD> {
    void add(T entity);
    List<T> getAll();
    T getByLogin(String login);
    void update(T entity);
    void delete(int id);
}
