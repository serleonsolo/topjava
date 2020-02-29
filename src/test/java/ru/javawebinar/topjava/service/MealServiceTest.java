package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.testdata.MealTestData.assertMatch;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.USER_ID;

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

    @Test
    public void create()
    {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "Create", 1000);
        Meal created = service.create(newMeal,USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId,USER_ID), newMeal);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateMealPerDayCreate() throws Exception {
        LocalDateTime ldt = LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0);
        service.create(new Meal(null, ldt, "Duplicate 1", 1000), USER_ID);
        service.create(new Meal(null, ldt, "Duplicate 2", 1100), USER_ID);
    }

    @Test
    public void get()
    {
        LocalDateTime ldt = LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0);
        Meal mealForDelete = new Meal(null, ldt, "Get", 1000);
        service.create(mealForDelete, USER_ID);
        service.get(mealForDelete.getId(),USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound()
    {
        service.get(1,USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getOtherUser()
    {
        LocalDateTime ldt = LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0);
        Meal mealForDelete = new Meal(null, ldt, "Get Other User", 1000);
        service.create(mealForDelete, USER_ID);
        service.get(mealForDelete.getId(),ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        LocalDateTime ldt = LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0);
        Meal mealForDelete = new Meal(null, ldt, "Delete", 1000);
        service.create(mealForDelete, USER_ID);
        service.delete(mealForDelete.getId(),USER_ID);
        service.get(mealForDelete.getId(),USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteOtherUser() {
        LocalDateTime ldt = LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0);
        Meal mealForDelete = new Meal(null, ldt, "Delete Other User", 1000);
        service.create(mealForDelete, USER_ID);
        service.delete(mealForDelete.getId(),ADMIN_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        //TODO test
    }

    @Test
    public void getAll() {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "Gat All", 1000);
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, newMeal);
    }

    @Test
    public void getAllNotFound() {
        List<Meal> all = service.getAll(1);
        assert all.size() == 0;
    }

    @Test
    public void update() {
        Meal mealForUpdate = new Meal(null, LocalDateTime.now(), "Update", 1000);
        service.create(mealForUpdate, USER_ID);
        mealForUpdate.setDescription("Updated");
        service.update(mealForUpdate,USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal mealForUpdate = new Meal(null, LocalDateTime.now(), "Update Not Found", 1000);
        service.create(mealForUpdate, USER_ID);
        mealForUpdate.setDescription("Updated");
        mealForUpdate.setId(1);
        service.update(mealForUpdate,USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateOtherUser() {
        Meal mealForUpdate = new Meal(null, LocalDateTime.now(), "Update Other User", 1000);
        service.create(mealForUpdate, USER_ID);
        mealForUpdate.setDescription("Updated");
        service.update(mealForUpdate,ADMIN_ID);
    }
}