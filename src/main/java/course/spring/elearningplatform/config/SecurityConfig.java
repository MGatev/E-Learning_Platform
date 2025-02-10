package course.spring.elearningplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private static final String LOGIN_PAGE = "/login";
    private static final String LOGOUT_PAGE = "/logout";
    private static final String REGISTER_PAGE = "/register";
    private static final String STATIC_RESOURCES = "/css/**";
    private static final String HOME_PAGE = "/home";
    private static final String LOGOUT_SUCCESS_URL = "/login?logout";
    private static final String IMAGES = "/images/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(REGISTER_PAGE, LOGIN_PAGE, STATIC_RESOURCES, IMAGES).permitAll()
                        .requestMatchers(LOGOUT_PAGE).authenticated()
                        .requestMatchers("/quizzes/submit").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(LOGIN_PAGE)
                        .defaultSuccessUrl(HOME_PAGE, true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_PAGE)
                        .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                        .permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_PAGE, "GET"))
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
