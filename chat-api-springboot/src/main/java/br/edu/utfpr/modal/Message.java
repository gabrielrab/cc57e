package br.edu.utfpr.modal;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String content;
	private LocalDateTime timeStamp;
	private Boolean is_read;

	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@ManyToOne
	@JoinColumn(name = "chat_id")
	private Chat chat;

	public Message(String content, LocalDateTime timeStamp, Boolean is_read, User user, Chat chat) {
		this.content = content;
		this.timeStamp = timeStamp;
		this.is_read = is_read;
		this.user = user;
		this.chat = chat;
	}
}
