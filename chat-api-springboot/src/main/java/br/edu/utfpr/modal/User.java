package br.edu.utfpr.modal;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_table")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String full_name;
	private String email;
	private String profile_picture;
	private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<>();

	public User(String full_name, String email, String password) {
		this.full_name = full_name;
		this.email = email;
		this.password = password;
	}
}
