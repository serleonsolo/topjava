package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

@ActiveProfiles({Profiles.POSTGRES_DB, Profiles.DATAJPA})
public class MealServiceTestDataJPA extends AbstractMealServiceTest {
}
