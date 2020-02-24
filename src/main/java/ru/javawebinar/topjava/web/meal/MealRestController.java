package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Collection<MealTo> getAll()
    {
        return convert(service.getAll(authUserId()));
    }

    public Collection<MealTo> getAll(Predicate<Meal> filter)
    {
        List<Meal> meals = service.getAll(authUserId());
        return filteredByStreams(meals, DEFAULT_CALORIES_PER_DAY, filter);
    }

    public MealTo get(int id)
    {
        log.info("get {}", id);
        return convert(service.get(id,authUserId()));
    }

    public MealTo create(Meal meal) {
        log.info("create {}", meal);
        return convert(service.create(meal,authUserId()));
    }

    public void delete(int id)
    {
        log.info("delete {}", id);
        service.delete(id,authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {}", meal);
        assureIdConsistent(meal, id);
        service.update(meal,authUserId());
    }

    private List<MealTo> convert(List<Meal> meals)
    {
        return MealsUtil.getTos(meals,MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    private MealTo convert(Meal meal)
    {
        List<Meal> meals = new ArrayList();
        meals.add(meal);
        return convert(meals).get(0);
    }
}