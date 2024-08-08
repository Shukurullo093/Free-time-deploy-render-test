package com.time.demo.repository;

import com.time.demo.entity.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Long> {
    List<Contacts> findAllByCreatedBy(Long ownerId);
}
