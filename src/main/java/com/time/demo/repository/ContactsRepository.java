package com.time.demo.repository;

import com.time.demo.entity.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Long> {
    List<Contacts> findAllByCreatedByOrContactIdOrderByCreatedAtDesc(Long ownerId, Long ownerId1);
    List<Contacts> findAllByGroupId1OrGroupId2(long groupId1, long groupId2);

    int countAllByGroupId1OrGroupId2(long groupId1, long groupId2);

    Optional<Contacts> findByContactIdAndCreatedBy(long contactId, long ownerId);
}

// https://www.geeksforgeeks.org/spring-boot-jpa-native-query-with-example/