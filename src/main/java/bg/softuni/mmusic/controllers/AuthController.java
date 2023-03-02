package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.UserLoginDto;
import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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
                              RedirectAttributes redirectAttributes){

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
                               RedirectAttributes redirectAttributes) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            bindingResult.addError(new FieldError("differentConfirmPassword",
                    "confirmPassword", "Passwords must be the same"));
        }


        if (bindingResult.hasErrors() || !authService.register(userRegisterDto())) {
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDto", bindingResult);

            log.info("{}", bindingResult.getErrorCount() );

            return "redirect:register";
        }

        return "redirect:login";
    }
}
