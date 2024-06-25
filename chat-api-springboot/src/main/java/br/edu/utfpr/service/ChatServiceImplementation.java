package br.edu.utfpr.service;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.utfpr.exception.ChatException;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.Chat;
import br.edu.utfpr.modal.LeaveStrategy;
import br.edu.utfpr.modal.User;
import br.edu.utfpr.repository.ChatRepository;
import br.edu.utfpr.request.GroupChatRequest;
import jakarta.persistence.EntityManager;

@Service
public class ChatServiceImplementation implements ChatService {
	@Autowired
	private UserService userService;

	@Autowired
	private ChatRepository chatRepo;

	@Autowired
	EntityManager em;

	@Override
	public Chat createChat(Integer reqUserId, Integer userId2, boolean isGroup) throws UserException {
		User reqUser = userService.findUserById(reqUserId);
		User user2 = userService.findUserById(userId2);

		Chat isChatExist = chatRepo.findSingleChatByUsersId(user2, reqUser);

		if (isChatExist != null) {
			return isChatExist;
		}

		Chat chat = new Chat();

		chat.setCreated_by(reqUser);
		chat.getUsers().add(reqUser);
		chat.getUsers().add(user2);
		chat.setIs_group(false);

		Chat createdChat = chatRepo.save(chat);

		return createdChat;
	}

	@Override
	public Chat findChatById(Integer chatId) throws ChatException {

		Optional<Chat> chat = chatRepo.findById(chatId);

		if (chat.isPresent()) {
			return chat.get();
		}
		throw new ChatException(
				"Chat não encontrado. Por favor, verifique se o chat existe ou se você tem permissão para acessá-lo.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException {

		User user = userService.findUserById(userId);

		List<Chat> chats = chatRepo.findChatByUserId(user.getId());

		chats.forEach(chat -> {
			chat.getMessages().size();
			chat.getUsers().size();

			HashSet<User> users = new HashSet<>();

			List<Object[]> result = em.createNativeQuery(
					"SELECT\n" + //
							"    c1_0.id AS chat_id,\n" + //
							"    c1_0.chat_image,\n" + //
							"    c1_0.chat_name,\n" + //
							"    c1_0.created_by_id,\n" + //
							"    c1_0.is_group,\n" + //
							"    u1_1.id AS user_id,\n" + //
							"    u1_1.email,\n" + //
							"    u1_1.full_name,\n" + //
							"    u1_1.password,\n" + //
							"    u1_1.profile_picture\n" + //
							"FROM\n" + //
							"    \"sistemas-distribuidos-chat\".chats c1_0\n" + //
							"        JOIN\n" + //
							"    \"sistemas-distribuidos-chat\".chat_users u1_0\n" + //
							"    ON c1_0.id = u1_0.chat_id\n" + //
							"        JOIN\n" + //
							"    \"sistemas-distribuidos-chat\".user_table u1_1\n" + //
							"    ON u1_1.id = u1_0.user_id\n" + //
							"WHERE\n" + //
							"    c1_0.id = " + chat.getId() + ";")
					.getResultList();

			for (Object[] row : result) {
				Long id = ((Number) row[5]).longValue();
				String email = (String) row[6];
				String fullName = (String) row[7];
				String password = (String) row[8];
				String profilePicture = (String) row[9];

				User u = new User();
				u.setId(id.intValue());
				u.setEmail(email);
				u.setFull_name(fullName);
				u.setPassword(password);
				u.setProfile_picture(profilePicture);

				users.add(u);
			}

			chat.setUsers(users);
		});

		return chats;
	}

	@Override
	public Chat deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {

		User user = userService.findUserById(userId);
		Chat chat = findChatById(chatId);

		if ((chat.getCreated_by().getId().equals(user.getId())) && !chat.getIs_group()) {
			if (chat.getLeaveStrategy().equals(LeaveStrategy.DELETE_GROUP) || chat.getLeaveStrategy() == null) {
				chatRepo.deleteById(chat.getId());
				return chat;
			} else {
				chat.getUsers().removeIf(u -> u.getId().equals(user.getId()));
				chat.getAdmins().removeIf(u -> u.getId().equals(user.getId()));

				User newAdmin = chat.getUsers().stream()
						.filter(u -> !u.getId().equals(user.getId()))
						.findAny()
						.orElse(null);

				if (newAdmin != null) {
					chat.getAdmins().add(newAdmin);
				}
				chatRepo.save(chat);
				return chat;
			}
		}

		throw new ChatException("Apenas o dono do grupo pode deletar o grupo.");
	}

	@Override
	public Chat createGroup(GroupChatRequest req, Integer reqUserId) throws UserException {

		User reqUser = userService.findUserById(reqUserId);

		Chat chat = new Chat();

		chat.setCreated_by(reqUser);
		chat.getUsers().add(reqUser);

		for (Integer userId : req.getUserIds()) {
			User user = userService.findUserById(userId);
			if (user != null)
				chat.getUsers().add(user);
		}

		chat.setChat_name(req.getChat_name());
		chat.setChat_image(req.getChat_image());
		chat.setIs_group(true);
		chat.getAdmins().add(reqUser);

		return chatRepo.save(chat);

	}

	@Override
	public Chat addUserToGroup(Integer userId, Integer chatId, Integer reqUserId) throws UserException, ChatException {
		Chat chat = findChatById(chatId);
		User user = userService.findUserById(userId);

		if (chat.getCreated_by().getId() != reqUserId) {
			chat.getPendingUsers().add(user);
		} else {
			chat.getUsers().add(user);
		}

		chatRepo.save(chat);

		return chat;
	}

	@Override
	public Chat renameGroup(Integer chatId, String groupName, Integer reqUserId) throws ChatException, UserException {
		Chat chat = findChatById(chatId);
		User user = userService.findUserById(reqUserId);

		if (chat.getUsers().contains(user))
			chat.setChat_name(groupName);

		return chatRepo.save(chat);
	}

	@Override
	public Chat removeFromGroup(Integer chatId, Integer userId, Integer reqUserId) throws UserException, ChatException {
		Chat chat = findChatById(chatId);
		User user = userService.findUserById(userId);

		if (chat.getCreated_by().getId().equals(reqUserId) || user.getId().equals(reqUserId)) {
			chat.getUsers().removeIf(u -> u.getId().equals(userId));
			return chatRepo.save(chat);
		} else {
			throw new ChatException("Apenas o dono do grupo ou o próprio usuário podem remover alguém do grupo.");
		}
	}

	@Override
	public List<Chat> getAllGroupChat() {
		List<Chat> chats = chatRepo.findAll();
		List<Chat> groupChats = new ArrayList<>();

		chats.forEach(chat -> {
			if (chat.getIs_group()) {
				chat.getMessages().size();
				chat.getUsers().size();
				groupChats.add(chat);
			}
		});

		return groupChats;
	}

	@Override
	public Chat acceptUserToGroup(Integer userId, Integer chatId, Integer reqUserId)
			throws UserException, ChatException {
		Chat chat = findChatById(chatId);
		User user = userService.findUserById(userId);

		if (chat.getCreated_by().getId() == reqUserId) {
			chat.getUsers().add(user);
			chat.getPendingUsers().removeIf(u -> u.getId().equals(userId));
			chatRepo.save(chat);
		} else {
			throw new ChatException("Apenas o dono do grupo pode aceitar usuários no grupo.");
		}

		return chat;
	}

}
