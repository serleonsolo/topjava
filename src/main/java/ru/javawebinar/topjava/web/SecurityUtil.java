package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_USER_ID;

public class SecurityUtil {

    public static int authUserId() {
        return DEFAULT_USER_ID;
    }

    public static int authUserCaloriesPerDay()
    {
        return DEFAULT_CALORIES_PER_DAY;
    }
}