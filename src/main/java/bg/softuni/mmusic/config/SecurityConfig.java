package bg.softuni.mmusic.config;

import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.services.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // defines which pages will be authorized
                .authorizeHttpRequests()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers("/", "/users/login", "/users/register", "/users/login-error").permitAll()
                    .requestMatchers("/admin").hasRole(Role.ADMIN.name())
                    .requestMatchers("/song/add", "/playlist/create", "song/{uuid}/update").hasRole(Role.MUSICIAN.name())
                    .requestMatchers("/playlist/{uuid}/add").hasRole(Role.MUSICIAN.name())
                // all any pages are available for logged users
                     .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/users/login")
                // the name in the username field
                    .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                    .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                // where to go when login is successful, use true argument if you
                // always want to go there, otherwise go to previous page
                    .defaultSuccessUrl("/", true)
                    .failureForwardUrl("/users/login-error")
                .and()
                    .logout()
                    .logoutUrl("/users/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .key("W3jV7glU0k")
                .tokenValiditySeconds(604800)
                .userDetailsService(new ApplicationUserDetailsService(userRepository));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new ApplicationUserDetailsService(userRepository);
    }
}
