package com.time.demo.repository;

import com.time.demo.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByNameAndCreatedBy(String name, Long id);
    boolean existsByIdAndCreatedBy(Long groupId, Long ownerId);
    List<Group> findAllByCreatedBy(Long ownerId);
}
