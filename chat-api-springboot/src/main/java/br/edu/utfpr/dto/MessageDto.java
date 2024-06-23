package br.edu.utfpr.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageDto {
	private Integer id;
	private String content;
	private LocalDateTime timeStamp;
	private Boolean is_read;
	private UserDto user;
	private ChatDto chat;
}
