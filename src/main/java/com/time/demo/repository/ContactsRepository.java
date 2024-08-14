package com.time.demo.repository;

import com.time.demo.entity.Contacts;
import com.time.demo.entity.enums.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Long> {
    List<Contacts> findAllByCreatedBy(Long ownerId);
    List<Contacts> findAllByCreatedByOrContactIdOrderByCreatedAtDesc(Long ownerId, Long ownerId1);

    boolean existsByContactUsername1AndStatus(String username, InviteStatus status);
}

// https://www.geeksforgeeks.org/spring-boot-jpa-native-query-with-example/