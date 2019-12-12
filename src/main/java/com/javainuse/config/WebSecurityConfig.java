package com.javainuse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity
				.csrf()
				.disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/authenticate").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/employees").permitAll().antMatchers("/create")
				.permitAll().antMatchers("/getAllEmployees").permitAll()
				.antMatchers("/register").permitAll()
				.
				// all other requests need to be authenticated
				anyRequest()
				.authenticated()
				.and()
				.
				// make sure we use stateless session; session won't be used to
				// store user's state.
				// exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().
				sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		// httpSecurity.addFilterBefore(jwtRequestFilter,
		// UsernamePasswordAuthenticationFilter.class);
	}

	// @Override
	// protected void configure(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.inMemoryAuthentication()
	// .passwordEncoder(passwordEncoder)
	// .withUser("user").password(passwordEncoder.encode("123456")).roles("USER")
	// .and()
	// .withUser("admin").password(passwordEncoder.encode("123456")).roles("USER",
	// "ADMIN");
	// }
}
