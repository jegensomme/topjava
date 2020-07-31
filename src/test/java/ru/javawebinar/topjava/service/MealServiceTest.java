package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal createdMeal = service.create(newMeal, USER_ID);
        Integer newId = createdMeal.getId();
        newMeal.setId(newId);
        assertMatch(createdMeal, newMeal);
        assertMatch(service.get(newId, USER_ID), createdMeal);
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdated();
        service.update(updatedMeal, USER_ID);
        assertMatch(service.get(updatedMeal.getId(), USER_ID), updatedMeal);
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
    }

    @Test
    public void get() {
        Meal gottenMeal = service.get(LIMIT.getId(), USER_ID);
        assertMatch(gottenMeal, LIMIT);
    }

    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(BREAKFAST2.getId(), ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(LIMIT.getId(), USER_ID);
        assertNull(repository.get(LIMIT.getId(), USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
        assertThrows(NotFoundException.class, () -> service.delete(LUNCH2.getId(), NOT_FOUND));
        assertThrows(NotFoundException.class, () -> service.delete(DINNER2.getId(), ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 29),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        assertMatch(betweenInclusive, BREAKFAST1, LUNCH1, DINNER1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, ADMIN_LUNCH, ADMIN_DINNER);
    }
}