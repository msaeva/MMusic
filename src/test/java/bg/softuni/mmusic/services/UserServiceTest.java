package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.UserProfileDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.model.error.UserNotFoundException;
import bg.softuni.mmusic.model.mapper.PlaylistMapper;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import bg.softuni.mmusic.repositories.SongRepository;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private SongRepository mockSongRepository;

    @Mock
    private PlaylistRepository mockPlaylistRepository;
    @Mock
    private SongMapper mockSongMapper;
    @Mock
    private PlaylistMapper mockPlaylistMapper;
    @Mock
    private UserMapper mockUserMapper;
    @Mock
    private UserRoleRepository mockRoleRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    private UserService toTest;
    User testUser = new User();

    @BeforeEach
    public void setUp() {
        toTest = new UserService(mockUserRepository,
                mockSongRepository,
                mockPlaylistRepository,
                mockSongMapper,
                mockPlaylistMapper,
                mockUserMapper,
                mockRoleRepository);

        String encodedPassword = "encoded_password";
        String email = "test@example.com";
        String firstName = "Test";
        String lastName = "Testov";
        String username = "testov";
        UserRole testAdminRole = new UserRole(Role.ADMIN);
        UserRole testUserRole = new UserRole(Role.USER);

        testUser = User.builder().username(username)
                .lastName(lastName)
                .firstName(firstName)
                .email(email)
                .password(encodedPassword)
                .createdDate(LocalDate.now())
                .roles(Set.of(testAdminRole, testUserRole))
                .build();
    }

    @Test
    void testGetUserByUuid() {
        String testUuid = "test-uuid";
        Mockito.when(mockUserRepository.findByUuid(testUuid))
                .thenReturn(Optional.of(testUser));

        User actualUser = toTest.getUserByUuid(testUuid);

        Assertions.assertEquals(testUser.getUuid(), actualUser.getUuid());

    }

    @Test
    void testGetUserByUuidNotFound() {
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> toTest.getUserByUuid("wrong-uuid")
        );
    }

    @Test
    void testGetAllUsers() {

        Mockito.when(mockUserRepository.findAll())
                .thenReturn(List.of(testUser));

        List<User> testAllUsers = toTest.getAllUsers();
        User actualUser = testAllUsers.get(0);

        Assertions.assertEquals(testUser.getUuid(), actualUser.getUuid());
        Assertions.assertEquals(testUser.getEmail(), actualUser.getEmail());

    }

    @Test
    void testUpdate() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setAbout("Something about me");
        userProfileDto.setFullName("Ivan Ivanov");

        String[] fullName = userProfileDto.getFullName().split("\\s+");

        testUser.setAbout(userProfileDto.getAbout());
        testUser.setFirstName(fullName[0]);
        testUser.setLastName(fullName[1]);

        toTest.update(testUser, userProfileDto);

        Mockito.verify(mockUserRepository).saveAndFlush(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();

        Assertions.assertEquals(testUser.getAbout(), updatedUser.getAbout());
        Assertions.assertEquals(testUser.getFirstName(), updatedUser.getFirstName());
        Assertions.assertEquals(testUser.getLastName(), updatedUser.getLastName());
    }
}
