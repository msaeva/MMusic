package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserMapper mockUserMapper;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @Mock
    private UserService mockUserService;
    @Mock
    private EmailService emailService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    private AuthService toTest;

    @BeforeEach
    public void setUp() {
        toTest = new AuthService(mockUserRepository,
                mockUserMapper,
                mockUserRoleRepository,
                mockPasswordEncoder, mockUserService, emailService);
    }

    @Test
    void testRegistration_SaveInvoked() throws MessagingException {
        String testPassword = "password";
        String encodedPassword = "encoded_password";
        String email = "test@example.com";
        String firstName = "Test";
        String lastName = "Testov";
        String username = "testov";
        UserRole testAdminRole = new UserRole(Role.ADMIN);
        UserRole testUserRole = new UserRole(Role.USER);

        UserRegisterDto testRegisterDto = UserRegisterDto.builder()
                .username(username)
                .lastName(lastName)
                .firstName(firstName)
                .email(email)
                .roles(Set.of(Role.ADMIN, Role.USER))
                .password(testPassword)
                .build();

        Mockito.when(mockPasswordEncoder.encode(testRegisterDto.getPassword()))
                .thenReturn(encodedPassword);

        User userTest = User.builder()
                .username(username)
                .lastName(lastName)
                .firstName(firstName)
                .email(email)
                .roles(Set.of(testAdminRole, testUserRole))
                .password(encodedPassword)
                .build();

        Mockito.when(mockUserMapper.userRegisterDtoToUser(testRegisterDto))
                .thenReturn(userTest);

        toTest.register(testRegisterDto);

        //ASSERT
        Mockito.verify(mockUserRepository).save(userArgumentCaptor.capture());
        User actualSavedUser = userArgumentCaptor.getValue();
        Assertions.assertEquals(testRegisterDto.getUsername(), actualSavedUser.getUsername());
        Assertions.assertEquals(encodedPassword, actualSavedUser.getPassword());

    }
}
