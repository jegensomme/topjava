package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        if(repository.get(meal.getId()).getUserId() != userId)
            return null;

        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            meal.setUserId(oldMeal.getUserId());
            return meal;
        });
    }

    @Override
    public boolean delete(int userId, int mealId) {
        log.info("delete {}", mealId);
        return repository.get(mealId).getUserId() == userId ?
                repository.remove(mealId) != null : false;
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.info("get {}", mealId);
        Meal meal = repository.get(mealId);
        if(meal == null)
            return null;
        return userId == meal.getUserId() ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().
                stream().
                sorted(Comparator.comparing(Meal::getDate).reversed()).
                collect(Collectors.toList());
    }
}

