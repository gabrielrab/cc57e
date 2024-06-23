package br.edu.utfpr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.utfpr.modal.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("select m from Message m join m.chat c where c.id=:chatId")
    List<Message> findMessageByChatId(@Param("chatId") Integer chatId);

}
