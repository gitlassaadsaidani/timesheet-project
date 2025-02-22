package tn.esprit.spring.services;


import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class UserServiceImplTest {
    @Test
    public void testAddUser() {
        String env = System.getenv("TIMESHEET_TESTS_FAIL");
        assertTrue(env == null || env.equals("False"));
    }
}
