package com.time.demo.repository;

import com.time.demo.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByNameAndCreatedBy(String name, Long id);
    boolean existsByIdAndCreatedBy(Long groupId, Long ownerId);
}
