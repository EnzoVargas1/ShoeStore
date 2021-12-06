package com.ShoeStore.ShoeStore.security.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ShoeStore.ShoeStore.security.AuthenticationRequest;
import com.ShoeStore.ShoeStore.security.filters.JwtRequestFilter;
import com.ShoeStore.ShoeStore.security.models.User;
import com.ShoeStore.ShoeStore.security.services.UserService;
import com.ShoeStore.ShoeStore.security.util.JwtUtil;


@CrossOrigin(origins = "*")
@RestController
class SecurityController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Autowired
	private UserService userService;


	@RequestMapping({ "/hello" })
	public String firstPage() {
		return "Welcome to the store";
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		UserDTO dto = new UserDTO(jwt, userDetails.getAuthorities().toString());	// to include the Role along with jwt in the response
		return ResponseEntity.ok(dto);

		//return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	public static class UserDTO {
		public UserDTO(String jwt, String roles) {
			this.jwt = jwt;
			this.roles = roles;
		}
		String jwt;
		String roles;
		
		public String getJwt() {
			return jwt;
		}
		public void setJwt(String jwt) {
			this.jwt = jwt;
		}
		public String getRoles() {
			return roles;
		}
		public void setRoles(String roles) {
			this.roles = roles;
		}
	}
	@PostMapping(value = "/authenticate/createuser")
	@ResponseStatus(HttpStatus.CREATED)
	public User addUser(@RequestBody User user) {
		return userService.addUser(user);
	}
	
	@PutMapping(value="authenticate/{id}/updateuser")
	public User updateUser( @RequestBody User user,@PathVariable int id) {
		
		return userService.updateUser(user,id);
	}
	
	@GetMapping(value ="/authenticate/{id}/user")
	public User getUser(@PathVariable int id){
		return userService.getUser(id);
	}

}

@Configuration
@EnableWebSecurity
 class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
	
	@Autowired
	private UserDetailsService myUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	  
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.cors().and().csrf().disable()  
				.authorizeRequests()
			    .antMatchers("/authenticate").permitAll()
				.antMatchers("/differentshoes").permitAll()
				.antMatchers("/authenticate/createuser").permitAll()
				.antMatchers("/customers").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/shoes").permitAll()
				.antMatchers("/Me**").hasRole("USER")
				.anyRequest().authenticated()
				.and().exceptionHandling()
				.and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
}


