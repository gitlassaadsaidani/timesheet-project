package tn.esprit.spring.services;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.UserRepository;


//@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class UserServiceImplMockTest {
    @Mock
    UserRepository mockedUserRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void testAddUser() {
        // Mock
        UserRepositoryMock userRepositoryMock = new UserRepositoryMock();
        when(mockedUserRepository.save(any(User.class))).
            thenAnswer(invocation -> userRepositoryMock.save(invocation.getArgument(0)));
        // Arrange
        Date user1DateNaissance = new GregorianCalendar(1980, Calendar.FEBRUARY, 2).getTime();
        User user1 = new User("First name 1", "Last name 1", user1DateNaissance, Role.ADMINISTRATEUR);
        User user2 = new User("First name 2", "Last name 2", new GregorianCalendar(2000, Calendar.AUGUST, 20).getTime(), Role.INGENIEUR);
        // Act
        User returnedUser1 = userService.addUser(user1);
        User returnedUser2 = userService.addUser(user2);
        // Assert
        assertEquals(user1.getLastName(), "Last name 1");
        assertTrue(user1.getDateNaissance().compareTo(user1DateNaissance) == 0);
        assertEquals(user1.getRole(), Role.ADMINISTRATEUR);
        assertFalse(returnedUser1.getId() == 0);
        assertTrue(returnedUser2.getId() - returnedUser1.getId() == 1);
    }

    @Test
    public void testUpdateUser() {
        // Mock
        UserRepositoryMock userRepositoryMock = new UserRepositoryMock();
        when(mockedUserRepository.save(any(User.class))).
            thenAnswer(invocation -> userRepositoryMock.save(invocation.getArgument(0)));
        // Arrange
        User user = new User("First name", "Last name", new GregorianCalendar(1980, Calendar.FEBRUARY, 2).getTime(), Role.ADMINISTRATEUR);
        // Act
        User returnedUser = userService.addUser(user);
        long id = returnedUser.getId();

        returnedUser.setLastName("Last name updated");
        returnedUser = userService.updateUser(returnedUser);
        // Assert
        assertEquals(returnedUser.getLastName(), "Last name updated");
        assertEquals(returnedUser.getId(), id);
    }
    
    @Test
    public void testDeleteUser() {
        UserRepositoryMock userRepositoryMock = new UserRepositoryMock();
        when(mockedUserRepository.save(any(User.class))).
            thenAnswer(invocation -> userRepositoryMock.save(invocation.getArgument(0)));

        doAnswer(invocation -> {
            userRepositoryMock.deleteById(invocation.getArgument(0));
            return null;
        }).when(mockedUserRepository).deleteById(any(long.class));    
        // Arrange
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        User returnedUser1 = userService.addUser(user1);
        User returnedUser2 = userService.addUser(user2);
        // Act 1
        userService.deleteUser(returnedUser2.getId().toString());
        // Assert 1
        assertNull(userService.retrieveUser(returnedUser2.getId().toString()));
        // Act 2
        User returnedUser3 = userService.addUser(user3);
        // Assert 2
        assertEquals(returnedUser3.getId() - returnedUser1.getId(), 2);
    }

    @Test
    public void testRetrieveUser() {
        // Mock
        UserRepositoryMock userRepositoryMock = new UserRepositoryMock();
        when(mockedUserRepository.save(any(User.class))).
            thenAnswer(invocation -> userRepositoryMock.save(invocation.getArgument(0)));
        when(mockedUserRepository.findById(anyLong())).
            thenAnswer(invocation -> userRepositoryMock.findById(invocation.getArgument(0)));
        // Arrange
        User user1 = new User("First name 1", "Last name 1", new GregorianCalendar(1980, Calendar.FEBRUARY, 2).getTime(), Role.ADMINISTRATEUR);
        User user2 = new User("First name 2", "Last name 2", new GregorianCalendar(2000, Calendar.AUGUST, 20).getTime(), Role.INGENIEUR);
        userService.addUser(user1);
        User returnedUser2 = userService.addUser(user2);
        // Act
        User retrieveUser2 = userService.retrieveUser(returnedUser2.getId().toString());
        User retrieveUser3 = userService.retrieveUser(Long.valueOf(returnedUser2.getId() + 20).toString());
        // Assert
        assertEquals(retrieveUser2.getId(), returnedUser2.getId());
        assertNull(retrieveUser3);
    }
}
