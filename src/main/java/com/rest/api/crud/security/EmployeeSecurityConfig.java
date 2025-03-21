package com.rest.api.crud.security;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

// !!!"Overrides" user/pass from application.properties file!!!
@Configuration
public class EmployeeSecurityConfig {
    // Load environment variables
    private final Dotenv dotenv = Dotenv.load();

    // Add support for JDBC - configuring Spring Security to use JDBC authentication
    // instead of in-memory users:

    /*@Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        // Tell Spring Security to use JDBC authentication with datasource from sql script
        return new JdbcUserDetailsManager(dataSource);
    }*/

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // define query to retrieve a user by username (how to find users)
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, pw, active from members where user_id=?");

        // define query to retrieve the authorities/roles be username (how to find roles)
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/members")
                        .hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/members/**")
                        .hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/members")
                        .hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/members/**")
                        .hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/members/**")
                        .hasRole("ADMIN")

        );
        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSRF)
        // in general, not required for stateless REST APIs that use POST, PUT, DELETE
        // and/or PATCH
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    /*// Hard-coded user accounts(((
    @Bean
    // InMemoryUserDetailsManager is a class in Spring Security that stores
    // user details in memory (RAM) instead of a database. Used for testing or
    // simple applications where we don't need a real database for authentication.
    public InMemoryUserDetailsManager userDetailsManager() {
        // User.builder() is a static method from
        // org.springframework.security.core.userdetails.User. It provides a fluent
        // API (Builder Pattern) for creating a UserDetails object.
        //💡 Instead of using multiple constructor parameters, builder() allows
        // to chain methods to make the code cleaner and more readable.
        UserDetails john = User.builder()
                .username(dotenv.get("USER_JOHN"))
                // {noop} means no encoding
                .password("{noop}" + dotenv.get("PASSWORD_JOHN"))
                .roles("EMPLOYEE")
                .build();

        UserDetails mary = User.builder()
                .username(dotenv.get("USER_MARY"))
                .password("{noop}" + dotenv.get("PASSWORD_MARY"))
                .roles("EMPLOYEE", "MANAGER")
                .build();

        UserDetails susan = User.builder()
                .username(dotenv.get("USER_SUSAN"))
                .password("{noop}" + dotenv.get("PASSWORD_SUSAN"))
                .roles("EMPLOYEE", "MANAGER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(john, mary, susan);
    }*/
}
