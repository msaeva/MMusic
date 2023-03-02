package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.UserLoginDto;
import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository, UserMapper userMapper, UserRoleRepository roleRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public void login(String username) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(auth);

    }


    public boolean register(UserRegisterDto registerDto) {

        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()
                || userRepository.findByEmail(registerDto.getUsername()).isPresent()) {
            return false;
        }
        Set<UserRole> userRoles = registerDto.getRoles().stream().map(roleRepository::findByRole)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        User userToSave = userMapper.userRegisterDtoToUser(registerDto);
        userToSave.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userToSave.setRoles(userRoles);

        System.out.println(userToSave.toString());

        userRepository.save(userToSave);

        return true;
    }

    public User getAuthenticatedUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails details) {
            return userRepository.findByUsername(details.getUsername()).orElse(null);
        }
        return null;
    }
}
