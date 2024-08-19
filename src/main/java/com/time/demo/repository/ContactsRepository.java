package com.time.demo.repository;

import com.time.demo.entity.Contacts;
import com.time.demo.entity.enums.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Long> {
    List<Contacts> findAllByCreatedBy(Long ownerId);
    List<Contacts> findAllByCreatedByOrContactIdOrderByCreatedAtDesc(Long ownerId, Long ownerId1);

    boolean existsByContactUsername1AndCreatedBy(String username, Long ownerId);
}

// https://www.geeksforgeeks.org/spring-boot-jpa-native-query-with-example/