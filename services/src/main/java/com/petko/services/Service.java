package com.petko.services;

import com.petko.entities2.Entity;

import java.util.List;

public interface Service<T extends Entity> {
    void add(T entity);
    List<T> getAll();
    T getByLogin(String login);
    void update(T entity);
    void delete(int id);
}
