package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.UserLoginDto;
import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ModelAttribute(name = "registerDto")
    public UserRegisterDto userRegisterDro() {
        return new UserRegisterDto();
    }

    @GetMapping("/login")
    public String getLogin() {
        return "auth-login";
    }

    @PostMapping("/login")
    public String postLogin(@Valid UserLoginDto loginDto) {
        authService.login(loginDto);

        return "/index";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "/auth-register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute(name = "registerDto") UserRegisterDto registerDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDto",
                    bindingResult);

            return "redirect:/users/register";
        }

        authService.register(registerDto);

        return "redirect:/login";
    }

}
