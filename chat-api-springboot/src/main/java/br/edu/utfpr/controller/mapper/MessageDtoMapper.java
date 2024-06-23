package br.edu.utfpr.controller.mapper;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dto.ChatDto;
import br.edu.utfpr.dto.MessageDto;
import br.edu.utfpr.dto.UserDto;
import br.edu.utfpr.modal.Message;

public class MessageDtoMapper {

	public static MessageDto toMessageDto(Message message) {

		ChatDto chatDto = ChatDtoMapper.toChatDto(message.getChat());
		UserDto userDto = UserDtoMapper.toUserDTO(message.getUser());

		MessageDto messageDto = new MessageDto();
		messageDto.setId(message.getId());
		messageDto.setChat(chatDto);
		messageDto.setContent(message.getContent());
		messageDto.setIs_read(message.getIs_read());
		messageDto.setTimeStamp(message.getTimeStamp());
		messageDto.setUser(userDto);

		return messageDto;
	}

	public static List<MessageDto> toMessageDtos(List<Message> messages) {

		List<MessageDto> messageDtos = new ArrayList<>();

		for (Message message : messages) {
			MessageDto messageDto = toMessageDto(message);
			messageDtos.add(messageDto);
		}

		return messageDtos;
	}

}
