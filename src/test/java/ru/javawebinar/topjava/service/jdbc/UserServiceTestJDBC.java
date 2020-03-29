package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

@ActiveProfiles({Profiles.POSTGRES_DB, Profiles.JDBC})
public class UserServiceTestJDBC extends AbstractUserServiceTest {
}