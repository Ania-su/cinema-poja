package school.hei.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/register", "/login")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/movies/**", "/projections/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.PUT, "/movies/**", "/projections/**")
                    .hasRole("MANAGER")
                    .requestMatchers(HttpMethod.GET, "/reservations")
                    .hasAnyRole("MANAGER", "EMPLOYEE")
                    .requestMatchers(HttpMethod.POST, "/reservations")
                    .hasAnyRole("CLIENT", "EMPLOYEE", "MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/reservations/**")
                    .hasAnyRole("EMPLOYEE", "MANAGER")
                    .requestMatchers("/rooms/**")
                    .hasAnyRole("MANAGER", "EMPLOYEE")
                    .anyRequest()
                    .authenticated())
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
