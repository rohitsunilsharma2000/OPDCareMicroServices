package com.example.supportservice.repository;

import com.example.supportservice.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findBySenderAndReceiver( String sender, String receiver);
}
