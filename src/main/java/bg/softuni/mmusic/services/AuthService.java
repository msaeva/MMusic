package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.UserLoginDto;
import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(UserLoginDto loginDto) {

        return true;
    }


    public boolean register(UserRegisterDto registerDto) {

        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()
                || userRepository.findByEmail(registerDto.getUsername()).isPresent()) {
            return false;
        }

        User userToSave = userMapper.userRegisterDtoToUser(registerDto);
        userToSave.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.saveAndFlush(userToSave);

        System.out.println(userToSave.toString());

        return true;
    }
}
