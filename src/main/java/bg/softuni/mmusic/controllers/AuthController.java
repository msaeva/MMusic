package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.UserLoginDto;
import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.services.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ModelAttribute(name = "registerDto")
    public UserRegisterDto userRegisterDto() {
        return new UserRegisterDto();
    }

    @ModelAttribute(name = "loginDto")
    public UserLoginDto userLoginDto() {
        return new UserLoginDto();
    }

    @GetMapping("/login")
    public String getLogin() {
        return "auth-login";
    }

    @PostMapping("/login-error")
    public String failedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                              String username,
                              RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/users/login";
    }

    @GetMapping("/register")
    public String register() {
        return "/auth-register";
    }


    @PostMapping("/register")
    public String register(@Valid UserRegisterDto registerDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) throws MessagingException {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            bindingResult.addError(new FieldError("differentConfirmPassword",
                    "confirmPassword", "Passwords must be the same!"));
        }

        if (authService.checkIfEmailExist(registerDto.getEmail())) {
            bindingResult
                    .addError(new FieldError("emailExist",
                            "email", "Email is already taken!"));
        }
        if (authService.checkIfUsernameExist(registerDto.getUsername())) {
            bindingResult
                    .addError(new FieldError("usernameExist",
                            "username", "Username is already taken. Choose another one!"));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDto",
                    bindingResult);

            return "redirect:/users/register";
        }
        authService.register(registerDto);
        return "redirect:/users/login";
    }

    @GetMapping("/{userUuid}/verify-code/{code}")
    public String verifyCode(@PathVariable(name = "code") String code,
                             @PathVariable(name = "userUuid") String userUuid) {

        authService.verify(code, userUuid);
        return "redirect:/users/login";
    }
}
