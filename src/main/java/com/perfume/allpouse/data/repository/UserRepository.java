package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getBySocialId(String socialId);

    User findBySocialId(String socialId);

    User findById(long id);

    User findBySocialIdAndLoginType(String socialId, String loginType);

}
