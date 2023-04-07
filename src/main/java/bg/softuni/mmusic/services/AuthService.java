package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.error.UserNotFoundException;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    private final EmailService emailService;

    @Autowired
    public AuthService(UserRepository userRepository,
                       UserMapper userMapper,
                       UserRoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, UserService userService, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
    }


    public void register(UserRegisterDto registerDto) throws MessagingException {

        Set<UserRole> userRoles = registerDto.getRoles().stream().map(roleRepository::findByRole)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        User userToSave = userMapper.userRegisterDtoToUser(registerDto);
        userToSave.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userToSave.setRoles(userRoles);
        userToSave.setCreatedDate(LocalDate.now());
        userToSave.setVerifyCode(RandomStringUtils.randomAlphabetic(6));

        userRepository.save(userToSave);
        emailService.sendActivationEmail(userToSave.getEmail(),
                userToSave.getUsername(), userToSave.getVerifyCode(), userToSave.getUuid());
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

    public void verify(String code, String userUuid) {
        User user = userService.getUserByUuid(userUuid);

        if (user.getVerifyCode().equals(code)) {
            user.setActivated(true);
            userRepository.saveAndFlush(user);
        }
    }
}
