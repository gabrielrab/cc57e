package br.edu.utfpr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import br.edu.utfpr.config.JwtTokenProvider;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.User;
import br.edu.utfpr.repository.UserRepository;
import br.edu.utfpr.request.UpdateUserRequest;

@Service
public class UserServiceImplementation implements UserService {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
		User user = findUserById(userId);

		if (req.getFull_name() != null) {
			user.setFull_name(req.getFull_name());
		}
		if (req.getProfile_picture() != null) {
			user.setProfile_picture(req.getProfile_picture());
		}

		return userRepo.save(user);
	}

	@Override
	public User findUserById(Integer userId) throws UserException {

		Optional<User> opt = userRepo.findById(userId);

		if (opt.isPresent()) {
			User user = opt.get();

			return user;
		}
		throw new UserException("Usuário não encontrado. Verifique se o ID do usuário está correto e tente novamente.");
	}

	@Override
	public User findUserProfile(String jwt) {
		String email = jwtTokenProvider.getEmailFromToken(jwt);

		Optional<User> opt = userRepo.findByEmail(email);

		if (opt.isPresent()) {
			return opt.get();
		}

		throw new BadCredentialsException(
				"Usuário não encontrado. Verifique se o token JWT está correto e tente novamente.");
	}

	@Override
	public List<User> searchUser(String query) {
		return userRepo.searchUsers(query);
	}
}
