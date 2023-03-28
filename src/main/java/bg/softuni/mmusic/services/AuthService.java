package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.error.UserNotFoundException;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public AuthService(UserRepository userRepository,
                       UserMapper userMapper,
                       UserRoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(UserRegisterDto registerDto) {

        Set<UserRole> userRoles = registerDto.getRoles().stream().map(roleRepository::findByRole)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        User userToSave = userMapper.userRegisterDtoToUser(registerDto);
        userToSave.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userToSave.setRoles(userRoles);

        userRepository.save(userToSave);
    }

    public User getAuthenticatedUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails details) {
            return userRepository.findByUsername(details.getUsername())
                    .orElseThrow(() -> new UserNotFoundException(details.getUsername()));
        }
        return null;
    }

    public boolean checkIfEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean checkIfUsernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
