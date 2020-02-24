package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    public Meal save(Meal meal) {
        return save(meal,DEFAULT_USER_ID);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        if(meal.getUserId() != userId)
            return null;
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId)
    {
        Meal meal = repository.get(id);
        if(meal != null && meal.getUserId() == userId)
            return repository.remove(id) != null;
        return false;
    }

    @Override
    public Meal get(int id, int userId)
    {
        Meal meal = repository.get(id);
        if(meal != null && meal.getUserId() == userId)
            return meal;
        return null;
    }

    @Override
    public List<Meal> getAll(int userId)
    {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted((meal1,meal2) -> meal2.getDate().compareTo(meal1.getDate()))
                .collect(Collectors.toList());
    }
}

