package br.edu.utfpr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.utfpr.modal.Chat;
import br.edu.utfpr.modal.User;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @Query("select c from Chat c join fetch c.users u where u.id=:userId")
    List<Chat> findChatByUserId(Integer userId);

    @Query("select c from Chat c Where c.is_group=false And :user Member of c.users And :reqUser Member of c.users")
    Chat findSingleChatByUsersId(@Param("user") User user, @Param("reqUser") User reqUser);
}
