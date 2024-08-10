package com.time.demo.repository;

import com.time.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM users u WHERE u.username LIKE CONCAT('%', :username, '%')", nativeQuery = true)
    List<Users> findAllByUsernameWithQuery(@Param("username") String username);

    List<Users> findAllByUsernameStartsWith(String username);
}
