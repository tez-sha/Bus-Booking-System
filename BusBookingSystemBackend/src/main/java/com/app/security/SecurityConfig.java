package com.app.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private PasswordEncoder enc;

	@Autowired
	private JwtAuthenticationFilter jwtFilter;

	@Autowired
	private CustomAuthenticationEntryPoint authEntry;

	@Bean
	public SecurityFilterChain authorizeRequests(HttpSecurity http) throws Exception {
		http.cors(withDefaults()).csrf(csrf -> csrf.disable())
				.exceptionHandling(handling -> handling.authenticationEntryPoint(authEntry))
				.authorizeRequests(requests -> requests.antMatchers(
						// ui static files
						"/", "/static/**", "/assets/**",
						// user
						"/user/signup", "/user/signin", "/password-reset/reset", "/password-reset/request",
						// station
						"/station/getstations",
						// route
						"/route/allroutes",
						// bus
						"/bus/getbuses", "/bus/getallbuses",
						// other
						"/v*/api-doc*/**", "/swagger-ui/**",
						// backendstatuscheck
						"/check-backend-status").permitAll().antMatchers(
								"/station/deletestation",
								// seats
								"/seats/bus/{busId}",
								"/seat/lock", "/seat/unlock", "/seat/{busid}",
								// bookings
								"/bookings/book", "/bookings/getbookings/{userid}", "/bookings/getbooking/{bookingId}",
								"/bookings/getbookings",
								// seatAllocation
								"/passenger/bus/{busId}/seat-list")
						.hasAnyRole("ROLE_CUSTOMER", "ROLE_USER"))
				.sessionManagement(management -> management
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
