package br.edu.utfpr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.controller.mapper.ChatDtoMapper;
import br.edu.utfpr.dto.ChatDto;
import br.edu.utfpr.exception.ChatException;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.Chat;
import br.edu.utfpr.modal.User;
import br.edu.utfpr.request.GroupChatRequest;
import br.edu.utfpr.request.RenameGroupRequest;
import br.edu.utfpr.request.SingleChatRequest;
import br.edu.utfpr.response.ApiResponse;
import br.edu.utfpr.service.ChatService;
import br.edu.utfpr.service.UserService;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
	@Autowired
	private ChatService chatService;

	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<ChatDto>> getAllGroupChatHandler() throws ChatException {
		List<Chat> chats = chatService.getAllGroupChat();

		List<ChatDto> chatDtos = ChatDtoMapper.toChatDtos(chats);

		return new ResponseEntity<List<ChatDto>>(chatDtos, HttpStatus.OK);
	}

	@PostMapping("/single")
	public ResponseEntity<ChatDto> creatChatHandler(@RequestHeader("Authorization") String jwt,
			@RequestBody SingleChatRequest singleChatRequest) throws UserException {
		User reqUser = userService.findUserProfile(jwt);

		Chat chat = chatService.createChat(reqUser.getId(), singleChatRequest.getUserId(), false);
		ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

		return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);
	}

	@PostMapping("/group")
	public ResponseEntity<ChatDto> createGroupHandler(@RequestBody GroupChatRequest groupChatRequest,
			@RequestHeader("Authorization") String jwt) throws UserException {

		User reqUser = userService.findUserProfile(jwt);

		Chat chat = chatService.createGroup(groupChatRequest, reqUser.getId());
		ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

		return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);

	}

	@GetMapping("/{chatId}")
	public ResponseEntity<ChatDto> findChatByIdHandler(@PathVariable Integer chatId) throws ChatException {

		Chat chat = chatService.findChatById(chatId);

		ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

		return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);

	}

	@GetMapping("/user")
	public ResponseEntity<List<ChatDto>> findAllChatByUserIdHandler(@RequestHeader("Authorization") String jwt)
			throws UserException {

		User user = userService.findUserProfile(jwt);

		List<Chat> chats = chatService.findAllChatByUserId(user.getId());

		List<ChatDto> chatDtos = ChatDtoMapper.toChatDtos(chats);

		return new ResponseEntity<List<ChatDto>>(chatDtos, HttpStatus.ACCEPTED);
	}

	@PutMapping("/{chatId}/add/{userId}")
	public ResponseEntity<ChatDto> addUserToGroupHandler(@RequestHeader("Authorization") String jwt,
			@PathVariable Integer chatId, @PathVariable Integer userId)
			throws UserException, ChatException {
		User reqUser = userService.findUserProfile(jwt);

		Chat chat = chatService.addUserToGroup(userId, chatId, reqUser.getId());

		ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

		return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);
	}

	@PutMapping("/{chatId}/accept/{userId}")
	public ResponseEntity<ChatDto> acceptUserToGroupHandler(@RequestHeader("Authorization") String jwt,
			@PathVariable Integer chatId, @PathVariable Integer userId)
			throws UserException, ChatException {
		User reqUser = userService.findUserProfile(jwt);

		Chat chat = chatService.acceptUserToGroup(userId, chatId, reqUser.getId());

		ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

		return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);
	}

	@PutMapping("/{chatId}/rename")
	public ResponseEntity<ChatDto> renameGroupHandler(@PathVariable Integer chatId,
			@RequestBody RenameGroupRequest renameGoupRequest, @RequestHeader("Authorization") String jwt)
			throws ChatException, UserException {

		User reqUser = userService.findUserProfile(jwt);

		Chat chat = chatService.renameGroup(chatId, renameGoupRequest.getGroupName(), reqUser.getId());

		ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

		return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);
	}

	@PutMapping("/{chatId}/remove/{userId}")
	public ResponseEntity<ApiResponse> removeFromGroupHandler(@RequestHeader("Authorization") String jwt,
			@PathVariable Integer chatId, @PathVariable Integer userId) throws UserException, ChatException {
		User reqUser = userService.findUserProfile(jwt);

		chatService.removeFromGroup(chatId, userId, reqUser.getId());

		ApiResponse res = new ApiResponse("pessoa removida com sucesso", true);

		return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/delete/{chatId}/{userId}")
	public ResponseEntity<ChatDto> deleteChatHandler(@PathVariable Integer chatId, @PathVariable Integer userId)
			throws ChatException, UserException {

		Chat chat = chatService.deleteChat(chatId, userId);
		ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

		return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);
	}
}
