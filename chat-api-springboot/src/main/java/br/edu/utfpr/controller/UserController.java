package br.edu.utfpr.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.controller.mapper.UserDtoMapper;
import br.edu.utfpr.dto.UserDto;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.User;
import br.edu.utfpr.request.UpdateUserRequest;
import br.edu.utfpr.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public ResponseEntity<HashSet<UserDto>> getAllUsersHandler() {
		List<User> users = userService.getAllUsers();
		HashSet<User> set = new HashSet<>(users);

		HashSet<UserDto> userDtos = UserDtoMapper.toUserDtos(set);

		return new ResponseEntity<HashSet<UserDto>>(userDtos, HttpStatus.ACCEPTED);
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<UserDto> updateUserHandler(@RequestBody UpdateUserRequest req, @PathVariable Integer userId)
			throws UserException {
		User updatedUser = userService.updateUser(userId, req);
		UserDto userDto = UserDtoMapper.toUserDTO(updatedUser);

		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<UserDto> getUserProfileHandler(@RequestHeader("Authorization") String jwt) {
		User user = userService.findUserProfile(jwt);

		UserDto userDto = UserDtoMapper.toUserDTO(user);

		return new ResponseEntity<UserDto>(userDto, HttpStatus.ACCEPTED);
	}

	@GetMapping("/search")
	public ResponseEntity<HashSet<UserDto>> searchUsersByName(@RequestParam String name) {
		List<User> users = userService.searchUser(name);

		HashSet<User> set = new HashSet<>(users);

		HashSet<UserDto> userDtos = UserDtoMapper.toUserDtos(set);

		return new ResponseEntity<>(userDtos, HttpStatus.ACCEPTED);
	}

}
