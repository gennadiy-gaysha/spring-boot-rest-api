package com.rest.api.crud.security;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// !!!"Overrides" user/pass from application.properties file!!!
@Configuration
public class EmployeeSecurityConfig {
    // Load environment variables
    private final Dotenv dotenv = Dotenv.load();

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
    }
}
