package ru.javawebinar.topjava.dao;

import java.util.Collection;

public interface MealRepository<T, Id> {

    public T save(T meal);

    public boolean delete(Id id);

    public T get(Id id);

    public Collection<T> getAll();
}
