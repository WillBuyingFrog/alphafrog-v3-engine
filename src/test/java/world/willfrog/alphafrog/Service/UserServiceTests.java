package world.willfrog.alphafrog.Service;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import world.willfrog.alphafrog.Common.JwtUtils;
import world.willfrog.alphafrog.Dao.Common.UserDao;
import world.willfrog.alphafrog.Entity.Common.User;
import world.willfrog.alphafrog.Service.Common.JwtService;
import world.willfrog.alphafrog.Service.ServiceImpl.Common.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest
public class UserServiceTests {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("hello");
    }




    @Test
    public void testCheckNewUsername() {
        when(userDao.getUserByUsername("testUser")).thenReturn(null);
        int result = userService.checkNewUsername("testUser");
        assertEquals(0, result);

        User user = new User(null, "testUser", "password", "test@example.com", System.currentTimeMillis(), 1, 0, 100);
        when(userDao.getUserByUsername("testUser")).thenReturn(user);
        result = userService.checkNewUsername("testUser");

        assertEquals(1, result);
    }



}
