package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User getBySocialId(String socialId);

    User findBySocialId(String socialId);

    User findById(long id);

    Optional<User> findBySocialIdAndLoginType(String socialId, String loginType);

}
