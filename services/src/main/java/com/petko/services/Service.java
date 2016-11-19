package com.petko.services;

import com.petko.entities.Entity;

import java.util.List;

public interface Service<T extends Entity> {
    void add(T entity);
    List<T> getAll();
    T getByLogin(String login);
//    T getById(int id);
    void update(T entity);
    void delete(int id);
}
