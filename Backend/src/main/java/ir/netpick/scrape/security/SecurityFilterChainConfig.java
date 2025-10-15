package ir.netpick.scrape.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ir.netpick.scrape.exception.DelegatedAuthEntryPoint;
import ir.netpick.scrape.jwt.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final DelegatedAuthEntryPoint delegatedAuthEntryPoint;

    public SecurityFilterChainConfig(AuthenticationProvider authenticationProvider,
            JWTAuthenticationFilter jwtAuthenticationFilter, DelegatedAuthEntryPoint delegatedAuthEntryPoint) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.delegatedAuthEntryPoint = delegatedAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(request -> {
            request
                    .requestMatchers(HttpMethod.POST, "/auth/sign-up", "/auth/sign-in")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/actuator/**")
                    .permitAll()
                    .anyRequest().authenticated();
        });
        http.sessionManagement(request -> {
            request.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(request -> {
            request.authenticationEntryPoint(delegatedAuthEntryPoint);
        });
        return http.build();
    }

}
