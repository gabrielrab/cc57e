package br.edu.utfpr.service;

import java.util.List;

import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.User;
import br.edu.utfpr.request.UpdateUserRequest;

public interface UserService {

	User findUserProfile(String jwt);

	User updateUser(Integer userId, UpdateUserRequest req) throws UserException;

	User findUserById(Integer userId) throws UserException;

	List<User> searchUser(String query);

	List<User> getAllUsers();
}
