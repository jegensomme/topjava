package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository<Meal, Integer> {

    public static Map<Integer, Meal> repository;
    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        initRepository();
        fillRepository(MealsUtil.MEALS);
    }

    private void initRepository() {
        repository = new ConcurrentHashMap<>();
    }

    public void fillRepository(List<Meal> meals) {
        meals.forEach(meal -> this.save(meal));
    }

    public void clearStore() {
        repository.clear();
    }

    @Override
    public Meal save(Meal meal) {
        if(meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        return repository.put(meal.getId(), meal);
    }

    @Override
    public boolean delete(Integer id) {
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(Integer id) {
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }
}
