package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println(filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(15, 0), 2000));
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(15, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesHistory = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            caloriesHistory.merge(localDate, meal.getCalories(), (a, b) -> a + b);
        }
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsWithExcess.add(new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesHistory.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesHistory = meals.stream()
                .collect(groupingBy(UserMeal::getDate, summingInt(UserMeal::getCalories)));
        List<UserMealWithExcess> mealsWithExcess = meals.stream()
                .filter(x -> TimeUtil.isBetweenInclusive(x.getDateTime().toLocalTime(), startTime, endTime))
                .map(x -> new UserMealWithExcess(x.getDateTime(),
                        x.getDescription(),
                        x.getCalories(),
                        caloriesHistory.get(x.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
        return mealsWithExcess;
    }
}
