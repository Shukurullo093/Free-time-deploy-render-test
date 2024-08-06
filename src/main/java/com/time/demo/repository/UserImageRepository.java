package com.time.demo.repository;

import com.time.demo.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByHashId(String hashId);
}
