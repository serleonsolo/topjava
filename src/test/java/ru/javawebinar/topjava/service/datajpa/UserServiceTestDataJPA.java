package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

@ActiveProfiles({Profiles.POSTGRES_DB, Profiles.DATAJPA})
public class UserServiceTestDataJPA extends AbstractUserServiceTest {
}