package in.ashokit.app.config;

import in.ashokit.app.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class ApplicationSecurityConfiguration {

    @Autowired
    DataSource dataSource;
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.DELETE, "/api/delete/{id}").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/save").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/update").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/update/{id}").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/login").permitAll())

                .httpBasic(Customizer.withDefaults());
        http.csrf(CsrfConfigurer::disable);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public UserDetailsService  userDetailsService() {
        UserDetails adminUserDetails = User.builder().username("John")
                .password(passwordEncoder().encode("john@123"))
                .roles("ADMIN").build();
        UserDetails userDetails = User.builder().username("Allen")
                .password(passwordEncoder().encode("allen@123"))
                .roles("USER").build();
        return new InMemoryUserDetailsManager(adminUserDetails, userDetails);
    }*/

    @Bean
    public UserDetailsService userDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return builder.build();
    }
}