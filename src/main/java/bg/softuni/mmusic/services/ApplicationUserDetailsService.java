package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByUsername(username)
                .map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found !"));
    }

    private UserDetails map(bg.softuni.mmusic.model.entities.User user) {
        return new User(
                user.getUsername(),
                user.getPassword(),
                extractAuthorities(user)
        );
    }
    private List<GrantedAuthority> extractAuthorities(bg.softuni.mmusic.model.entities.User user) {
        return user.getRoles()
                .stream()
                .map(this::mapRole)
                .collect(Collectors.toList());
    }
    private GrantedAuthority mapRole(UserRole userRole) {
        return new SimpleGrantedAuthority("ROLE_" + userRole.getRole().name());
    }
}
