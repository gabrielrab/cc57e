package br.edu.utfpr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.utfpr.modal.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.full_name LIKE %:name%")
	List<User> searchUsers(@Param("name") String name);
}
