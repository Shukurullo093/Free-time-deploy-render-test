package com.time.demo.repository;

import com.time.demo.dto.UserDtoInterface;
import com.time.demo.entity.Users;
import org.springframework.data.domain.Pageable;
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
    Optional<Users> findByUsername1(String username);
    boolean existsByUsername1(String username);
    boolean existsByUsername1AndEmailNot(String username, String email);
    boolean existsByPhone(String phone);

    @Query(value = "SELECT * FROM users u WHERE u.username1 LIKE CONCAT('%', :username, '%') AND u.username1!=:owner ORDER BY u.username1", nativeQuery = true)
    List<Users> findAllByUsernameWithQuery(@Param("username") String username, @Param("owner") String owner, Pageable pageable);

//    List<Users> findAllByUsername1Contains(String username, Pageable pageable);

    @Query(value = "SELECT u.first_name AS firstName, u.last_name AS lastName, u.username1 AS username," +
            "u.email AS email, u.phone AS phone, null, null FROM users u", nativeQuery = true)
    List<UserDtoInterface> findAllUsers();
}
