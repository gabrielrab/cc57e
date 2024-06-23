package br.edu.utfpr.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.config.JwtTokenProvider;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.User;
import br.edu.utfpr.repository.UserRepository;
import br.edu.utfpr.request.LoginRequest;
import br.edu.utfpr.response.AuthResponse;
import br.edu.utfpr.service.CustomUserDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetails;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetails) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.customUserDetails = customUserDetails;
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody User user) throws UserException {
		String email = user.getEmail();
		String password = user.getPassword();
		String full_name = user.getFull_name();

		Optional<User> isEmailExist = userRepository.findByEmail(email);

		if (isEmailExist.isPresent()) {
			throw new UserException("Email já cadastrado.");
		}

		User createdUser = new User(full_name, email, passwordEncoder.encode(password));

		userRepository.save(createdUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateJwtToken(authentication);

		AuthResponse authResponse = new AuthResponse();

		authResponse.setStatus(true);
		authResponse.setJwt(token);

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateJwtToken(authentication);
		AuthResponse authResponse = new AuthResponse();

		authResponse.setStatus(true);
		authResponse.setJwt(token);

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserDetails.loadUserByUsername(username);

		if (userDetails == null) {
			throw new BadCredentialsException("Nome ou senha inválidos");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Nome ou senha inválidos");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
