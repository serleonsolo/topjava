package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository mealRepository;

    @Autowired
    private CrudUserRepository userRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getOne(userId));
        if (meal.isNew()) {
            mealRepository.save(meal);
            return meal;
        } else if (get(meal.getId(), userId) == null) {
            return null;
        }
        return mealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id,userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealRepository.findById(id).orElse(null);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepository.getByDateTime(userId,startDateTime,endDateTime);
    }
}
