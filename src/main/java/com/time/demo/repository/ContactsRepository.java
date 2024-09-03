package com.time.demo.repository;

import com.time.demo.entity.Contacts;
import com.time.demo.entity.enums.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Long> {
    List<Contacts> findAllByCreatedBy(Long ownerId);
    List<Contacts> findAllByCreatedByOrContactIdOrderByCreatedAtDesc(Long ownerId, Long ownerId1);
    List<Contacts> findAllByGroupId1AndAndContactType1(long groupId, ContactType type);
    List<Contacts> findAllByGroupId2AndAndContactType2(long groupId, ContactType type);
    int countAllByGroupId1OrGroupId2(long groupId1, long groupId2);

    Optional<Contacts> findByContactIdAndCreatedBy(long contactId, long ownerId);
}

// https://www.geeksforgeeks.org/spring-boot-jpa-native-query-with-example/