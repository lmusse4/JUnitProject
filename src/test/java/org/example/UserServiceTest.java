package org.example;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private Map<String, User> userDatabase;
    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        System.out.println("Starting tests for method with @Before");
    }
    @BeforeClass
    public static void setClass() {
        System.out.println("Setting up @BeforeClass");
    }
    @AfterClass
    public static void tearDownClass() {
        System.out.println("Compiling class after testing with @AfterClass");
    }
    @After
    public void tearDown() {
        System.out.println("Executing clean up operations after each test with @After");
    }

    //test  registerUser()
    @Test
    public void registerUserPositive() {
        User user = new User("Mary", "mary123", "Mary@example.com");
        when(userDatabase.containsKey("Mary")).thenReturn(false);
        boolean result = userService.registerUser(user);
        assertTrue(result);
        verify(userDatabase).put("Mary", user);
    }

    @Test
    public void registerUserNegative() {
        User user = new User("Mary", "mary123", "mary@example.com");
        when(userDatabase.containsKey("Mary")).thenReturn(true);
        boolean result = userService.registerUser(user);
        assertFalse(result);
        verify(userDatabase, never()).put(anyString(), any(User.class));
    }

    @Test
    public void registerUserEdge() {
        User user = new User("", "mary123", "mary@example.com");
        when(userDatabase.containsKey("")).thenReturn(false);
        boolean result = userService.registerUser(user);
        assertTrue(result);
        verify(userDatabase).put("", user);
    }

    //test loginUser()
    @Test
    public void loginUserPositive() {
        User user = new User("Mary", "mary123", "mary@example.com");
        when(userDatabase.get("Mary")).thenReturn(user);
        User loggedInUser = userService.loginUser("Mary", "mary123");
        assertNotNull(loggedInUser);
        assertEquals(user, loggedInUser);
    }

    @Test
    public void loginUserNegative_UserNotFound() {
        when(userDatabase.get("unknownUser")).thenReturn(null);
        User loggedInUser = userService.loginUser("unknownUser", "pw1234");
        assertNull(loggedInUser);
    }

    @Test
    public void loginUserEdge_WrongPassword() {
        User user = new User("Mary", "mary123", "mary@example.com");
        when(userDatabase.get("Mary")).thenReturn(user);
        User loggedInUser = userService.loginUser("Mary", "mary1231");
        assertNull(loggedInUser);
    }

}
