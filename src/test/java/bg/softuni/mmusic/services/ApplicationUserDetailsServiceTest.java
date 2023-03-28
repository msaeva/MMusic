package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {
    private final String NOT_EXISTING_EMAIL = "pesho@example.com";


    @Mock
    private UserRepository mockUserRepository;

    private ApplicationUserDetailsService toTest;

    @BeforeEach
    void setUp() {
        toTest = new ApplicationUserDetailsService(
                mockUserRepository
        );
    }

    @Test
    void testUserFound() {
        String existingUsername = "ivan22";
        // ARRANGE
        UserRole testAdminRole = new UserRole(Role.ADMIN);
        UserRole testMusicianRole = new UserRole(Role.MUSICIAN);

        User testUser = User.builder()
                .email("ivan@abv.bg")
                .username(existingUsername)
                .firstName("ivan")
                .lastName("petrov")
                .password("password")
                .createdDate(LocalDate.now())
                .roles(Set.of(testAdminRole, testMusicianRole))
                .build();

        Mockito.when(mockUserRepository.findByUsername(existingUsername))
                .thenReturn(Optional.of(testUser));
        // ARRANGE


        //ACT
        UserDetails adminDetails = toTest.loadUserByUsername(existingUsername);
        //ACT

        //ASSERT
        Assertions.assertNotNull(adminDetails);
        Assertions.assertEquals(existingUsername, adminDetails.getUsername());
        Assertions.assertEquals(testUser.getPassword(), adminDetails.getPassword());
        Assertions.assertEquals(2, adminDetails.getAuthorities().size(),
                "The authorities are supposed to be just two - ADMIN/MUSICIAN.");

        assertRole(adminDetails.getAuthorities(), "ROLE_ADMIN");
        assertRole(adminDetails.getAuthorities(), "ROLE_MUSICIAN");
        //ASSERT
    }

    private void assertRole(Collection<? extends GrantedAuthority> authorities,
                            String role) {
        authorities.
                stream().
                filter(a -> role.equals(a.getAuthority())).
                findAny().
                orElseThrow(() -> new AssertionFailedError("Role " + role + " not found!"));
    }

    @Test
    void testUserNotFound() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername(NOT_EXISTING_EMAIL)
        );
    }


}
